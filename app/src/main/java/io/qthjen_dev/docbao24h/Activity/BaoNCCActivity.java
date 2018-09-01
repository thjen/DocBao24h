package io.qthjen_dev.docbao24h.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.ArrayList;
import java.util.List;

import io.qthjen_dev.docbao24h.Model.BaoNCC;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.NetworkUtils.NetworkTools;

public class BaoNCCActivity extends AppCompatActivity {

    private List<BaoNCC> mListNCC = new ArrayList<>();
    private ChooseBaoAdapter mAdapterNCC;

    private Toolbar mTbar;
    private RecyclerView recyclerView;
    private RelativeLayout mBackground;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_ncc);

        recyclerView = findViewById(R.id.recycler_chooseBao);
        mTbar = findViewById(R.id.tbar_baoncc);
        mBackground = findViewById(R.id.baoNccBackground);

        setSupportActionBar(mTbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.trangbao));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
            }
        });

        mSharedPreferences = getSharedPreferences("myncc", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        /** setup adatpter and add trang bao **/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaoNCCActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);

        mListNCC.add(new BaoNCC(getResources().getString(R.string.ex) ,"https://vnexpress.net/rss", getResources().getDrawable(R.drawable.ex)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string._24h) ,"http://www.24h.com.vn/guest/RSS/", getResources().getDrawable(R.drawable.hth)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.dan) ,"http://dantri.com.vn/rss.htm", getResources().getDrawable(R.drawable.dan)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.soh) ,"http://soha.vn/rss.htm", getResources().getDrawable(R.drawable.soha)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.vietnamnet) ,"http://vietnamnet.vn/vn/rss/", getResources().getDrawable(R.drawable.vn)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string.bd365) ,"http://www.bongda.com.vn/main-rss.html", getResources().getDrawable(R.drawable.bb)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string.tt) ,"https://tinhte.vn/rss", getResources().getDrawable(R.drawable.tt)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.bd24) ,"http://bongda24h.vn/RSS.html", getResources().getDrawable(R.drawable.bd)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.gk) ,"http://gamek.vn/rss.chn", getResources().getDrawable(R.drawable.gk)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.tp) ,"https://www.tienphong.vn/rss.aspx", getResources().getDrawable(R.drawable.tp)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.tn) ,"https://thanhnien.vn/rss.html", getResources().getDrawable(R.drawable.tnp)));
        mListNCC.add(new BaoNCC(getResources().getString(R.string.vtc) ,"https://vtc.vn/main-rss.html", getResources().getDrawable(R.drawable.vtc)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string.ld) ,"https://laodong.vn/rss", getResources().getDrawable(R.drawable.ld)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string.info) ,"http://infonet.vn/rss/", getResources().getDrawable(R.drawable.info)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string.vnre) ,"http://vnreview.vn/feed", getResources().getDrawable(R.drawable.vnrev)));
        //mListNCC.add(new BaoNCC(getResources().getString(R.string.pcworld) ,"http://www.pcworld.com.vn/rss.aspx", getResources().getDrawable(R.drawable.pcw)));

        mAdapterNCC = new ChooseBaoAdapter(mListNCC, BaoNCCActivity.this, R.layout.layout_choose_bao);
        recyclerView.setAdapter(mAdapterNCC);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        mSharedNightMode = getSharedPreferences(FinalUtils.themeName, MODE_PRIVATE);
        boolean state = mSharedNightMode.getBoolean(FinalUtils.nightModeState, false);
        boolean deepsea = mSharedNightMode.getBoolean(FinalUtils.whiteModernState, false);
        if (state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
        }
        if (deepsea) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
        }
        if (!deepsea && !state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
        }

        /** set title toolbar is bao user choose**/
        int bao = mSharedPreferences.getInt("ncc", 0);
        switch (bao) {
            case 0:
                mTbar.setTitle(getResources().getString(R.string.ex));
                break;
            case 1:
                mTbar.setTitle(getResources().getString(R.string.dan));
                break;
            case 2:
                mTbar.setTitle(getResources().getString(R.string.soh));
                break;
            case 3:
                mTbar.setTitle(getResources().getString(R.string.vietnamnet));
                break;
            case 4:
                mTbar.setTitle(getResources().getString(R.string.bd24));
                break;
            case 5:
                mTbar.setTitle(getResources().getString(R.string.gk));
                break;
            case 6:
                mTbar.setTitle(getResources().getString(R.string.tp));
                break;
            case 7:
                mTbar.setTitle(getResources().getString(R.string.tn));
                break;
            case 8:
                mTbar.setTitle(getResources().getString(R.string.vtc));
                break;
        }
    }

    class ChooseBaoAdapter extends RecyclerView.Adapter<ChooseBaoAdapter.ViewHolder> {

        List<BaoNCC> list;
        Context context;
        int layout;

        public ChooseBaoAdapter(List<BaoNCC> list, Context context, int layout) {
            this.list = list;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.textView.setText(list.get(position).getName());
            holder.imageView.setImageDrawable(list.get(position).getImage());
            holder.myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkTools.CheckNetwork(context)) {
                        Intent intent = new Intent();
                        switch (position) {
                            /** add id trang bao into shared preferences and send to main activity **/
                            case 0:
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 0);
                                mEditor.commit();
                                break;
//                        case 1: //intent.putExtra("trangbao", 1);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 1);
//                                mEditor.commit();break;
                            case 1: //intent.putExtra("trangbao", 2);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 1);
                                mEditor.commit();
                                break;
                            case 2: //intent.putExtra("trangbao", 3);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 2);
                                mEditor.commit();
                                break;
                            case 3: //intent.putExtra("trangbao", 4);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 3);
                                mEditor.commit();
                                break;
//                        case 5: //intent.putExtra("trangbao", 5);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 5);
//                                mEditor.commit();break;
//                        case 6: //intent.putExtra("trangbao", 6);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 6);
//                                mEditor.commit();break;
                            case 4: //intent.putExtra("trangbao", 7);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 4);
                                mEditor.commit();
                                break;
                            case 5: //intent.putExtra("trangbao", 8);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 5);
                                mEditor.commit();
                                break;
                            case 6: //intent.putExtra("trangbao", 9);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 6);
                                mEditor.commit();
                                break;
                            case 7: //intent.putExtra("trangbao", 10);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 7);
                                mEditor.commit();
                                break;
                            case 8: //intent.putExtra("trangbao", 11);
                                intent.putExtra("trangbao", true);
                                mEditor.putInt("ncc", 8);
                                mEditor.commit();
                                break;
//                        case 11: //intent.putExtra("trangbao", 12);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 11);
//                                mEditor.commit();break;
//                        case 12: //intent.putExtra("trangbao", 13);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 12);
//                                mEditor.commit();break;
//                        case 14: //intent.putExtra("trangbao", 14);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 14);
//                                mEditor.commit();break;
//                        case 13: //intent.putExtra("trangbao", 15);
//                                intent.putExtra("trangbao", true);
//                                mEditor.putInt("ncc", 13);
//                                mEditor.commit();break;

                        }

                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        TastyToast.makeText(context, "Không có kết nối mạng!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView textView;
            View myView;

            public ViewHolder(View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.iv_imagebao);
                textView = itemView.findViewById(R.id.tv_chooseBao);

                myView = itemView;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
        super.onBackPressed();
    }

}
