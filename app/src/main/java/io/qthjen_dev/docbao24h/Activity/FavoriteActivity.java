package io.qthjen_dev.docbao24h.Activity;

import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.qthjen_dev.docbao24h.Adapter.TabFavoriteAdapter;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.SetupTools;

public class FavoriteActivity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private RelativeLayout mBackground;
    private AppBarLayout mAppbar;

    private TabFavoriteAdapter mAdapter;

    private String tabTitle;

    private SharedPreferences mShareStateNightMode;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        initView();
        setupTabLayoutAdapter();
        tabTitle = getResources().getString(R.string.favorite);
        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabTitle = tab.getText() + "";
                onResume();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        prepareAd();
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        prepareAd();
                    }
                });
            }
        },1200, 1200, TimeUnit.SECONDS);
    }

    private void  prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_banner));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetupTools.setupToolbarAsMyDefault(FavoriteActivity.this, mToolbar, tabTitle);
        mShareStateNightMode = getSharedPreferences(FinalUtils.themeName, MODE_PRIVATE);
        boolean state = mShareStateNightMode.getBoolean(FinalUtils.nightModeState, false);
        boolean deepsea = mShareStateNightMode.getBoolean(FinalUtils.whiteModernState, false);
        if (state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
            mViewPager.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
        }
        if (deepsea) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
            mViewPager.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
        }
        if (!deepsea && !state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
            mViewPager.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
        }

//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
//            }
//        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
//    }

    private void setupTabLayoutAdapter() {
        mAdapter = new TabFavoriteAdapter(getSupportFragmentManager(), getResources().getString(R.string.favorite), getResources().getString(R.string.readneartime));
        mViewPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mTab = findViewById(R.id.tab_favorite);
        mViewPager = findViewById(R.id.viewpagerFavorite);
        mToolbar = findViewById(R.id.tbar_favorite);
        mBackground = findViewById(R.id.favoriteBackground);
        mAppbar = findViewById(R.id.appbar_favoriteActivity);
    }
}
