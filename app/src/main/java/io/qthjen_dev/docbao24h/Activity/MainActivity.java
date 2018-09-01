package io.qthjen_dev.docbao24h.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.qthjen_dev.docbao24h.Adapter.TabLayoutAdapter;
import io.qthjen_dev.docbao24h.Model.NaviPreferences;
import io.qthjen_dev.docbao24h.Model.TabInfo;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.NetworkUtils.NetworkTools;
import io.qthjen_dev.docbao24h.Utils.NetworkUtils.NetworkStateChange;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mViewPager;
    private Toolbar mToolBar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private RecyclerView mRecyclerNavi;
    private RelativeLayout mBackground;
    private AppBarLayout mAppbarLl;

    private TabLayoutAdapter mAdapter;
    private List<TabInfo> mListTab = new ArrayList<>();

    private List<NaviPreferences> mListNaviPref = new ArrayList<>();
    private NavigationAdapter mNaviAdapter;

    private int mTrangbao;

    private SharedPreferences mPreferences;
    private SharedPreferences mTheme;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** check connect internet, if not show dialog reload or no **/
        /*if (!NetworkTools.CheckNetwork(this)) {
            TastyToast.makeText(this, "Không có kết nối mạng!", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Không có kết nối mạng");
            alert.setMessage("Không có kết nối mạng, bạn có muốn tải lại dữ liệu");
            alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = getIntent();
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            alert.show();
        }

        IntentFilter intentFilter = new IntentFilter(NetworkStateChange.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                boolean isConnectNetwork = intent.getBooleanExtra(NetworkStateChange.IS_NETWORK_AVAILABLE, false);
                if ( !isConnectNetwork ) {
                    TastyToast.makeText(MainActivity.this, "Mất kết nối!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    TastyToast.makeText(MainActivity.this, "Kết nối thành công!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                }

            }
        }, intentFilter);*/

        InitView();
        setupDrawer();
        setupNavigationPref();
        //setUpResideMenu();

        mPreferences = getSharedPreferences("myncc", MODE_PRIVATE);
        mTrangbao = mPreferences.getInt("ncc",0);

        switch (mTrangbao) {
            case 0:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "https://vnexpress.net/rss/tin-moi-nhat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "https://vnexpress.net/rss/thoi-su.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "https://vnexpress.net/rss/the-gioi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhdoan), "https://vnexpress.net/rss/kinh-doanh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.startup), "https://vnexpress.net/rss/startup.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "https://vnexpress.net/rss/giai-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "https://vnexpress.net/rss/the-thao.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "https://vnexpress.net/rss/phap-luat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "https://vnexpress.net/rss/giao-duc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "https://vnexpress.net/rss/suc-khoe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giadinh), "https://vnexpress.net/rss/gia-dinh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.dulich), "https://vnexpress.net/rss/du-lich.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.khoahoc), "https://vnexpress.net/rss/khoa-hoc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.sohoa), "https://vnexpress.net/rss/so-hoa.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xe), "https://vnexpress.net/rss/oto-xe-may.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congdong), "https://vnexpress.net/rss/cong-dong.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tamsu), "https://vnexpress.net/rss/tam-su.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
//            case 1:
//                mListTab.clear();
//                //mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://www.24h.com.vn/upload/rss/trangchu24h.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.tinngay), "http://www.24h.com.vn/upload/rss/tintuctrongngay.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.bongda), "http://www.24h.com.vn/upload/rss/bongda.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.euro), "http://www.24h.com.vn/upload/rss/euro2016.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.anninh), "http://www.24h.com.vn/upload/rss/anninhhinhsu.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.thoitrang), "http://www.24h.com.vn/upload/rss/thoitrang.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.thoitranghitech), "http://www.24h.com.vn/upload/rss/thoitranghitech.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.taichinh), "http://www.24h.com.vn/upload/rss/taichinhbatdongsan.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.amthuc), "http://www.24h.com.vn/upload/rss/amthuc.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.lamdep), "http://www.24h.com.vn/upload/rss/lamdep.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.phim), "http://www.24h.com.vn/upload/rss/phim.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "http://www.24h.com.vn/upload/rss/giaoducduhoc.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "http://www.24h.com.vn/upload/rss/bantrecuocsong.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.canhac), "http://www.24h.com.vn/upload/rss/canhacmtv.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "http://www.24h.com.vn/upload/rss/thethao.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.phithuong), "http://www.24h.com.vn/upload/rss/phithuongkyquac.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "http://www.24h.com.vn/upload/rss/congnghethongtin.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.xe), "http://www.24h.com.vn/upload/rss/otoxemay.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.thitruong), "http://www.24h.com.vn/upload/rss/thitruongtieudung.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.dulich), "http://www.24h.com.vn/upload/rss/dulich.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "http://www.24h.com.vn/upload/rss/suckhoedoisong.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "http://www.24h.com.vn/upload/rss/tintucquocte.rss"));
//                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "http://www.24h.com.vn/upload/rss/giaitri.rss"));
//                //mAdapter.notifyDataSetChanged();
//                break;
            case 1:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://dantri.com.vn/trangchu.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "http://dantri.com.vn/suc-khoe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xahoi), "http://dantri.com.vn/xa-hoi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "http://dantri.com.vn/giai-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "http://dantri.com.vn/giao-duc-khuyen-hoc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "http://dantri.com.vn/the-thao.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "http://dantri.com.vn/the-gioi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhdoan), "http://dantri.com.vn/kinh-doanh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xe), "http://dantri.com.vn/o-to-xe-may.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "http://dantri.com.vn/suc-manh-so.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phithuong), "http://dantri.com.vn/chuyen-la.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.vieclam), "http://dantri.com.vn/viec-lam.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "http://dantri.com.vn/nhip-song-tre.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "http://dantri.com.vn/phap-luat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.diendan), "http://dantri.com.vn/dien-dan.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tuyensinh), "http://dantri.com.vn/tuyen-sinh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.vanhoa), "http://dantri.com.vn/van-hoa.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "http://dantri.com.vn/du-hoc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.dulich), "http://dantri.com.vn/du-lich.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.khoahoc), "http://dantri.com.vn/khoa-hoc-cong-nghe.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            case 2:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "http://soha.vn/giai-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "http://soha.vn/the-thao.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "http://soha.vn/thoi-su.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "http://soha.vn/phap-luat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhdoan), "http://soha.vn/kinh-doanh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "http://soha.vn/quoc-te.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "http://soha.vn/song-khoe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.quansu), "http://soha.vn/quan-su.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congdong), "http://soha.vn/cu-dan-mang.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.khampha), "http://soha.vn/kham-pha.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "http://soha.vn/doi-song.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            case 3:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://vietnamnet.vn/rss/home.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "http://vietnamnet.vn/rss/phap-luat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "http://vietnamnet.vn/rss/cong-nghe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhdoan), "http://vietnamnet.vn/rss/kinh-doanh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "http://vietnamnet.vn/rss/giao-duc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "http://vietnamnet.vn/rss/thoi-su.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "http://vietnamnet.vn/rss/giai-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "http://vietnamnet.vn/rss/suc-khoe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "http://vietnamnet.vn/rss/the-thao.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "http://vietnamnet.vn/rss/doi-song.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "http://vietnamnet.vn/rss/the-gioi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.batdongsan), "http://vietnamnet.vn/rss/bat-dong-san.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tinnong), "http://vietnamnet.vn/rss/tin-moi-nong.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tinnoibat), "http://vietnamnet.vn/rss/tin-noi-bat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tuanvietnam), "http://vietnamnet.vn/rss/tuanvietnam.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            /*case 5:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://www.bongda.com.vn/feed.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdanh), "http://www.bongda.com.vn/bong-da-anh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdviet), "http://www.bongda.com.vn/viet-nam.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tinchuyennhuong), "http://www.bongda.com.vn/tin-chuyen-nhuong.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdy), "http://www.bongda.com.vn/bong-da-y.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdtbn), "http://www.bongda.com.vn/bong-da-tbn.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdd), "http://www.bongda.com.vn/bong-da-duc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdp), "http://www.bongda.com.vn/bong-da-phap.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cham), "http://www.bongda.com.vn/champions-league.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.photo), "http://www.bongda.com.vn/thu-vien-anh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaohuu), "http://www.bongda.com.vn/giao-huu.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.wc), "http://www.bongda.com.vn/world-cup.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.aff), "http://www.bongda.com.vn/aff-cup.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdcau), "http://www.bongda.com.vn/bong-da-chau-au.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdca), "http://www.bongda.com.vn/bong-da-chau-a.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdcm), "http://www.bongda.com.vn/bong-da-chau-my.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.olym), "http://www.bongda.com.vn/olympics.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.euro), "http://www.bongda.com.vn/euro-2016.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.sea), "http://www.bongda.com.vn/sea-games.rss"));
                //mAdapter.notifyDataSetChanged();
                break;*/
//            case 6:
//                mListTab.clear();
//                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "https://tinhte.vn/rss"));
//                //mAdapter.notifyDataSetChanged();
//                break;
            case 4:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://bongda24h.vn/RSS/1.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdanh), "http://bongda24h.vn/RSS/172.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdtbn), "http://bongda24h.vn/RSS/180.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdcau), "http://bongda24h.vn/RSS/184.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdviet), "http://bongda24h.vn/RSS/168.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdy), "http://bongda24h.vn/RSS/176.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdd), "http://bongda24h.vn/RSS/193.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.bdp), "http://bongda24h.vn/RSS/197.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tinnong), "http://bongda24h.vn/RSS/279.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.binhluan), "http://bongda24h.vn/RSS/278.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tinchuyennhuong), "http://bongda24h.vn/RSS/187.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            case 5:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://gamek.vn/trang-chu.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.mmo), "http://gamek.vn/game-online.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thitruong), "http://gamek.vn/thi-truong.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.pcconsole), "http://gamek.vn/pc-console.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.esport), "http://gamek.vn/esport.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.mobile), "http://gamek.vn/mobile-social.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.gaminggear), "http://gamek.vn/gaming-gear.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.mangafilm), "http://gamek.vn/manga-film.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            case 6:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.xahoi), "https://www.tienphong.vn/rss/xa-hoi-2.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhte), "https://www.tienphong.vn/rss/kinh-te-3.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.batdongsan), "https://www.tienphong.vn/rss/dia-oc-166.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "https://www.tienphong.vn/rss/the-gioi-5.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "https://www.tienphong.vn/rss/gioi-tre-4.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xe), "https://www.tienphong.vn/rss/xe-113.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "https://www.tienphong.vn/rss/phap-luat-12.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "https://www.tienphong.vn/rss/the-thao-11.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "https://www.tienphong.vn/rss/giai-tri-36.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "https://www.tienphong.vn/rss/giao-duc-71.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.khoahoc), "https://www.tienphong.vn/rss/cong-nghe-45.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.tuyensinh), "https://www.tienphong.vn/rss/tuyen-sinh-155.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "https://www.tienphong.vn/rss/gia-dinh-suc-khoe-210.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            case 7:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "https://thanhnien.vn/rss/viet-nam.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "https://thanhnien.vn/rss/the-gioi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.chinhtri), "https://thanhnien.vn/rss/chinh-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.vanhoa), "https://thanhnien.vn/rss/van-hoa-nghe-thuat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "https://thethao.thanhnien.vn/rss/home.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "https://thanhnien.vn/rss/doi-song.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhte), "https://thanhnien.vn/rss/kinh-te.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "https://thanhnien.vn/rss/giao-duc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "https://thanhnien.vn/rss/cong-nghe-thong-tin.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.game), "https://game.thanhnien.vn/rss/home.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "https://thanhnien.vn/rss/doi-song/suc-khoe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xe), "https://xe.thanhnien.vn/rss/home.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            case 8:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "https://vtc.vn/feed.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xahoi), "https://vtc.vn/xa-hoi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhte), "https://vtc.vn/kinh-te.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "https://vtc.vn/truyenhinh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "https://vtc.vn/phap-luat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "https://vtc.vn/quoc-te.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.batdongsan), "https://vtc.vn/dia-oc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaitri), "https://vtc.vn/giai-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "https://vtc.vn/the-thao.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "https://vtc.vn/gioi-tre.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.doanhnghiep), "https://vtc.vn/doanh-nghiep-doanh-nhan.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.khampha), "https://vtc.vn/phong-su-kham-pha.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "https://vtc.vn/giao-duc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "https://vtc.vn/cong-nghe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xe), "https://vtc.vn/oto-xe-may.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "https://vtc.vn/suc-khoe-doi-song.rss"));
                //mAdapter.notifyDataSetChanged();
                break;
            /*case 11:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "https://laodong.vn/rss/home.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.xahoi), "https://laodong.vn/rss/xa-hoi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "https://laodong.vn/rss/thoi-su.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congdong), "https://laodong.vn/rss/cong-doan.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "https://laodong.vn/rss/the-gioi.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhte), "https://laodong.vn/rss/kinh-te.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "https://laodong.vn/rss/phap-luat.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "https://laodong.vn/rss/suc-khoe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.vanhoa), "https://laodong.vn/rss/van-hoa-giai-tri.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thethao), "https://laodong.vn/rss/the-thao.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.dulich), "https://laodong.vn/rss/du-lich.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "https://laodong.vn/rss/cong-nghe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.diendan), "https://laodong.vn/rss/dien-dan.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.ldcuoituan), "https://laodong.vn/rss/lao-dong-cuoi-tuan.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.ldvadoising), "https://laodong.vn/rss/lao-dong-doi-song.rss"));
                //mAdapter.notifyDataSetChanged();
                break;*/
            /*case 12:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.tinnong), "http://infonet.vn/news.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thegioi), "http://infonet.vn/the-gioi/5.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.cuocsong), "http://infonet.vn/doi-song/3.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.giaoduc), "http://infonet.vn/giao-duc/1064.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.phapluat), "http://infonet.vn/phap-luat/13.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.thoisu), "http://infonet.vn/thoi-su/2.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhdoan), "http://infonet.vn/kinh-doanh/4.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.suckhoe), "http://infonet.vn/suc-khoe/14.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.truyenthong), "http://infonet.vn/truyen-thong/1093.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.khampha), "http://infonet.vn/kham-pha/9.rss"));
                //mAdapter.notifyDataSetChanged();
                break;*/
//            case 14:
//                mListTab.clear();
//                mListTab.add(new TabInfo(getResources().getString(R.string.trangchu), "http://vnreview.vn/feed/-/rss/home"));
//                //mAdapter.notifyDataSetChanged();
//                break;
            /*case 13:
                mListTab.clear();
                mListTab.add(new TabInfo(getResources().getString(R.string.tintuc), "http://www.pcworld.com.vn/articles/tin-tuc.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.sanpham), "http://www.pcworld.com.vn/articles/san-pham.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.congnghe), "http://www.pcworld.com.vn/articles/cong-nghe.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.kinhdoan), "http://www.pcworld.com.vn/articles/kinh-doanh.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.game), "http://www.pcworld.com.vn/articles/game.rss"));
                mListTab.add(new TabInfo(getResources().getString(R.string.testlab), "http://www.pcworld.com.vn/articles/preview.rss"));
                //mAdapter.notifyDataSetChanged();
                break;*/
        }

        mAdapter = new TabLayoutAdapter(getSupportFragmentManager(), mListTab, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTab.setupWithViewPager(mViewPager);

        /** set title toolbar is bao user choose**/
        switch (mTrangbao) {
            case 0:
                mToolBar.setTitle(getResources().getString(R.string.ex));
                break;
            case 1:
                mToolBar.setTitle(getResources().getString(R.string.dan));
                break;
            case 2:
                mToolBar.setTitle(getResources().getString(R.string.soh));
                break;
            case 3:
                mToolBar.setTitle(getResources().getString(R.string.vietnamnet));
                break;
            case 4:
                mToolBar.setTitle(getResources().getString(R.string.bd24));
                break;
            case 5:
                mToolBar.setTitle(getResources().getString(R.string.gk));
                break;
            case 6:
                mToolBar.setTitle(getResources().getString(R.string.tp));
                break;
            case 7:
                mToolBar.setTitle(getResources().getString(R.string.tn));
                break;
            case 8:
                mToolBar.setTitle(getResources().getString(R.string.vtc));
                break;
        }

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
        }, 1200, 1200, TimeUnit.SECONDS);
    }

    private void  prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_banner));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onResume() {
        super.onResume();
        /** setup theme **/
        mTheme = getSharedPreferences(FinalUtils.themeName, MODE_PRIVATE);
        boolean nightmodeState = mTheme.getBoolean(FinalUtils.nightModeState, false);
        boolean deepsea = mTheme.getBoolean(FinalUtils.whiteModernState, false);
        if (nightmodeState) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
            mNavigation.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
            mViewPager.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
        }
        if(deepsea) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
            mNavigation.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
            mViewPager.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
        }
        if (!nightmodeState && !deepsea) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
            mNavigation.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
            mViewPager.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
        }
    }

    private void setupNavigationPref() {
        mNaviAdapter = new NavigationAdapter(mListNaviPref, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerNavi.setLayoutManager(linearLayoutManager);
        mRecyclerNavi.setAdapter(mNaviAdapter);

        mListNaviPref.add(new NaviPreferences(getResources().getString(R.string.trangchu), R.drawable.home));
        //mListNaviPref.add(new NaviPreferences(getResources().getString(R.string.readneartime), R.drawable.timelapse));
        mListNaviPref.add(new NaviPreferences(getResources().getString(R.string.favorite), R.drawable.favoriteblack));
        mListNaviPref.add(new NaviPreferences(getResources().getString(R.string.setting), R.drawable.setting));
        mListNaviPref.add(new NaviPreferences(getResources().getString(R.string.feedback), R.drawable.feedback));
        mListNaviPref.add(new NaviPreferences(getResources().getString(R.string.about), R.drawable.info));
    }

    private void setupDrawer() {
        mToolBar.getContentInsetEnd();
        mToolBar.setNavigationIcon(R.drawable.navigationmenu);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.START);
                //mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    private void InitView() {
        mTab = findViewById(R.id.tbar_layoutMain);
        mViewPager = findViewById(R.id.view_pagerMain);
        mToolBar = findViewById(R.id.tbar_main);
        mDrawer = findViewById(R.id.drawer);
        mNavigation = findViewById(R.id.navigation);
        mRecyclerNavi = findViewById(R.id.recyclerNavi);
        mBackground = findViewById(R.id.homeBackground);
        mAppbarLl = findViewById(R.id.app_barLayoutMain);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {
        List<NaviPreferences> list;
        Context context;

        public NavigationAdapter(List<NaviPreferences> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_item_navigation, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tvName.setText(list.get(position).getName());
            holder.icon.setImageResource(list.get(position).getIcon());

            holder.myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        mDrawer.closeDrawer(GravityCompat.START);
                        overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay_x);
                    } else if (position == 1) {
                        Intent intent = new Intent(context, FavoriteActivity.class);
                        context.startActivity(intent);
                        mDrawer.closeDrawer(GravityCompat.START);
                        overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay_x);
                    } else if (position == 2) {
                        Intent intent = new Intent(context, SettingActivity.class);
                        context.startActivity(intent);
                        mDrawer.closeDrawer(GravityCompat.START);
                        overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay_x);
                    } else if (position == 3) {
                        Intent intent = new Intent(context, FeedbackActivity.class);
                        context.startActivity(intent);
                        mDrawer.closeDrawer(GravityCompat.START);
                        overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay_x);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            ImageView icon;
            View myView;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.navi_title);
                icon = itemView.findViewById(R.id.icon_navigation);
                myView = itemView;
            }
        }
    }

}
