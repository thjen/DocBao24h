package io.qthjen_dev.docbao24h.Adapter;

import android.content.Context;
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
import io.qthjen_dev.docbao24h.Utils.ItemRecentlyAdapterListener;

public class RecentlyAdapter extends RecyclerView.Adapter<RecentlyAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<ItemFavorited> list;
    private List<ItemFavorited> listFilter;

    private ItemRecentlyAdapterListener listener;

    public RecentlyAdapter(Context context, List<ItemFavorited> list, ItemRecentlyAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.listFilter = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_item_recently, parent, false);

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

        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemRecentlyAdapterListener(list.get(position));
            }
        });
    }

    public void clearAll() {
        final int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++)
                list.remove(0);
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
                String charSequence = constraint.toString();
                if (charSequence.isEmpty()) {
                    list = listFilter;
                } else {
                    List<ItemFavorited> filterList = new ArrayList<>();
                    for (ItemFavorited row:listFilter) {
                        if (row.getTitleFav().toLowerCase().contains(charSequence.toLowerCase())) {
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

        TextView tvTitle, tvDate;
        ImageView image;
        View myView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_titleRecently);
            tvDate = itemView.findViewById(R.id.tv_dateRecently);
            image = itemView.findViewById(R.id.iv_recentlyItem);

            myView = itemView;
        }
    }
}
