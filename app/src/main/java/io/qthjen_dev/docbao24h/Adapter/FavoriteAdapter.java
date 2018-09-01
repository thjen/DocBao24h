package io.qthjen_dev.docbao24h.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.qthjen_dev.docbao24h.Model.ItemFavorited;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.ItemFavoriteAdapterListener;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements Filterable {

    private List<ItemFavorited> list;
    private List<ItemFavorited> listFilter;
    private Context context;

    private ItemFavoriteAdapterListener listener;

    public FavoriteAdapter(List<ItemFavorited> list, Context context, ItemFavoriteAdapterListener listener) {
        this.list = list;
        this.context = context;
        this.listFilter = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_favorite_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getTitleFav());
        /** cut string date**/
        int index1 = list.get(position).getDateFav().trim().indexOf(":");
        int index2 = list.get(position).getDateFav().trim().indexOf("(");

        if (list.get(position).getDateFav().trim().contains(":")) {
            holder.tvDate.setText(list.get(position).getDateFav().substring(0, index1 - 3));
        } else {
            holder.tvDate.setText(list.get(position).getDateFav().substring(0, index2 - 1));
        }
        Picasso.with(context).load(list.get(position).getImageFav()).into(holder.image);

        holder.myItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemFavoriteAdapterListener(list.get(position));
            }
        });
    }

//    public void clearAll() {
//        final int size = list.size();
//        list.clear();
//        notifyItemRangeRemoved(0, size);
//    }

    public void clearAll() {
        final int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    list = listFilter;
                } else {
                    List<ItemFavorited> filterList = new ArrayList<>();
                    for (ItemFavorited row:listFilter) {
                        if (row.getTitleFav().toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(row);
                        }
                    }
                    list = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<ItemFavorited>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View myItem;
        TextView tvTitle, tvDate;
        ImageView image;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_titleFavorite);
            tvDate = itemView.findViewById(R.id.tv_dateFavorite);
            image = itemView.findViewById(R.id.iv_favoriteItem);
            card = itemView.findViewById(R.id.cardViewFavorite);
            myItem = itemView;
        }
    }
}
