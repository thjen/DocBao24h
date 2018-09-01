package io.qthjen_dev.docbao24h.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import io.qthjen_dev.docbao24h.Fragment.FragmentBottomSheetShare;
import io.qthjen_dev.docbao24h.Model.ItemFavorited;
import io.qthjen_dev.docbao24h.Model.MyReader;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.CopyLinkListener;
import io.qthjen_dev.docbao24h.Utils.DatabaseUtils.FavoriteSQLite;
import io.qthjen_dev.docbao24h.Utils.DatabaseUtils.RecentlyTimeSQLite;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.ItemAdapterListener;
import io.qthjen_dev.docbao24h.Utils.SignInAndShareFacebook;
import io.qthjen_dev.docbao24h.Utils.SignInAndShareGooglePlus;
import io.qthjen_dev.docbao24h.Utils.SignInAndShareTwitter;

public class AdapterReader extends RecyclerView.Adapter<AdapterReader.ViewHolder> implements Filterable {

    private List<MyReader> list;
    private List<MyReader> listFilter;
    private Context context;
    private View myView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences mSharedBaoNCC;
    private SharedPreferences.Editor mEditor;

    private FavoriteSQLite mFavoriteSQLite;
    private int trangbao;

    public static final String sharedPreferencesFavorite = "checkedFavorite";
    public static final String key = "myFavorite";

    private List<ItemFavorited> listRecently;
    private RecentlyTimeSQLite recentlySQL;

    private ItemAdapterListener mListener;

    private SignInAndShareFacebook mListenerShare;
    private SignInAndShareGooglePlus mListenerShareGoogle;
    private SignInAndShareTwitter mListenerShareTwitter;
    private CopyLinkListener mListenerCopyClipboard;

    private SharedPreferences mSharedTheme;

    public AdapterReader(List<MyReader> list, Context context, ItemAdapterListener mListener,
                         SignInAndShareFacebook mListenerShare,
                         SignInAndShareGooglePlus mListenerShareGoogle,
                         SignInAndShareTwitter mListenerShareTwitter,
                         CopyLinkListener mListenerCopyClipboard) {
        this.list = list;
        this.context = context;
        this.listFilter = list;
        this.mListener = mListener;
        this.mListenerShare = mListenerShare;
        this.mListenerShareGoogle = mListenerShareGoogle;
        this.mListenerShareTwitter = mListenerShareTwitter;
        this.mListenerCopyClipboard = mListenerCopyClipboard;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_item_reader, parent, false);

        mSharedPreferences = context.getSharedPreferences(sharedPreferencesFavorite, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mSharedBaoNCC = context.getSharedPreferences("myncc", Context.MODE_PRIVATE);
        trangbao = mSharedBaoNCC.getInt("ncc", 0);

        mFavoriteSQLite = new FavoriteSQLite(context);
        recentlySQL = new RecentlyTimeSQLite(context);

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_list);
        view.startAnimation(anim);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvTitle.setText(list.get(position).getTitle().trim());

        /** cut string date**/
        int index1 = list.get(position).getDate().trim().indexOf(":");
        int index2 = list.get(position).getDate().trim().indexOf("(");

        if (list.get(position).getDate().trim().contains(":")) {
            holder.tvDate.setText(list.get(position).getDate().substring(0, index1 - 3));
        } else {
            holder.tvDate.setText(list.get(position).getDate().substring(0, index2 - 1));
        }

        if (!list.get(position).getImage().equals("")) {
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            layoutParams.gravity = Gravity.CENTER_VERTICAL;
//            holder.image.setLayoutParams(layoutParams);
            Picasso.with(context).load(list.get(position).getImage())
                    .into(holder.image);
        }

        /** get count list recently to add **/
        listRecently = new ArrayList<>();
        listRecently.clear();
        listRecently = recentlySQL.getAllItemRecently();

        /** set data for item favorite to data base**/
        final ItemFavorited itemFavorited = new ItemFavorited();
        itemFavorited.setTitleFav(list.get(position).getTitle());
        itemFavorited.setDateFav(list.get(position).getDate());
        itemFavorited.setLinkFav(list.get(position).getLink());
        itemFavorited.setImageFav(list.get(position).getImage());

        /** click item start chrome custom tabs if have chrome browser app else start tabs browser app default **/
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(list.get(position));
                /** Check the list of recently viewed 20 items if not then add items **/
                if (listRecently.size() < 20 && !equalsDataTitleDatabase(position)) {
                    recentlySQL.insertRecentlyTimeItemData(itemFavorited);
                } else {

                }
            }
        });

        /** checked favorite event **/
        holder.tb_favorite.setChecked(false);
        holder.tb_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.tb_favorite.setBackgroundResource(R.drawable.favoritefill);
                    mEditor.putBoolean(trangbao + "_" + key + "_" + list.get(position).getTitle(), true);
                    mEditor.commit();
                } else {
                    holder.tb_favorite.setBackgroundResource(R.drawable.favorite);
                    mEditor.putBoolean(trangbao + "_" + key + "_" + list.get(position).getTitle(), false);
                    mEditor.commit();
                }
            }
        });
        mSharedPreferences = context.getSharedPreferences(sharedPreferencesFavorite, Context.MODE_PRIVATE);
        boolean fav = mSharedPreferences.getBoolean(trangbao + "_" + key + "_" + list.get(position).getTitle(), false);
        /** add item is checked to database and delete item unchecked **/
        holder.tb_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tb_favorite.isChecked()) {
                    mFavoriteSQLite.insertFavoriteItem(itemFavorited);
                }
                if (!holder.tb_favorite.isChecked()) {
                    mFavoriteSQLite.deleteFavoriteItemWithTitle(itemFavorited.getTitleFav());
                }
            }
        });
        holder.tb_favorite.setChecked(fav);

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDiaglo(position);
            }
        });
    }

    /** show bottom sheet dialog and event **/
    private void showBottomSheetDiaglo(final int position) {
        mSharedTheme = context.getSharedPreferences(FinalUtils.themeName, Context.MODE_PRIVATE);
        boolean nightState = mSharedTheme.getBoolean(FinalUtils.nightModeState, false);
        boolean deepsea = mSharedTheme.getBoolean(FinalUtils.whiteModernState, false);
        View view = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.fragment_fragment_bottom_sheet_share, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        RelativeLayout shareWithFacebook = dialog.findViewById(R.id.shareWithFacebook);
        RelativeLayout shareWithTwitter = dialog.findViewById(R.id.shareWithTwitter);
        RelativeLayout shareWithGoogle = dialog.findViewById(R.id.shareWithGoogle);
        RelativeLayout copyClipboard = dialog.findViewById(R.id.copyClipboard);
        RelativeLayout close = dialog.findViewById(R.id.close);
        LinearLayout background = dialog.findViewById(R.id.bottomSheetBackground);

        if (nightState) {
            background.setBackground(context.getResources().getDrawable(R.drawable.background_drawable_dark));
        }
        if (deepsea) {
            background.setBackground(context.getResources().getDrawable(R.drawable.background_drawable_two));
        }
        if (!deepsea && !nightState) {
            background.setBackground(context.getResources().getDrawable(R.drawable.background_drawable_one));
        }
        shareWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerShare.onSignInAndShare(list.get(position));
                dialog.cancel();
            }
        });

        shareWithTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerShareTwitter.onSignInAndShareTwitter(list.get(position));
                dialog.cancel();
            }
        });

        shareWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerShareGoogle.onSignInAndShareGoogleP(list.get(position));
                dialog.cancel();
            }
        });

        copyClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerCopyClipboard.onCopyLinkListener(list.get(position));
                dialog.cancel();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void showBottomSheetDialogFragment() {
        FragmentBottomSheetShare fragmentBottomSheetShare = new FragmentBottomSheetShare();
        fragmentBottomSheetShare.show(((AppCompatActivity) context).getSupportFragmentManager(), fragmentBottomSheetShare.getTag());
    }

    /** so sánh title vừa click và title trong database nếu giống nhau thì không thêm vào recently time **/
    private boolean equalsDataTitleDatabase(int positon) {
        for (int i = 0; i < listRecently.size(); i++)
            if (listRecently.get(i).getTitleFav().equals(list.get(positon).getTitle()))
                return true;
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /** setup search **/
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    list = listFilter;
                } else {
                    List<MyReader> filteredList = new ArrayList<>();
                    for (MyReader row:listFilter) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<MyReader>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvTitle;
        ImageView image, ivShare;
        TextView tvDate;
        ToggleButton tb_favorite;

        public ViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardViewReader);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            image = itemView.findViewById(R.id.iv_reader);
            tvDate = itemView.findViewById(R.id.tv_date);
            tb_favorite = itemView.findViewById(R.id.tb_favorite);
            ivShare = itemView.findViewById(R.id.iv_share);

            myView = itemView;
            /** setIsRecyclable to lose state when click toggle button favorite **/
            setIsRecyclable(false);
        }
    }
}