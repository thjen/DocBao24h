package io.qthjen_dev.docbao24h.Fragment;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.qthjen_dev.docbao24h.Adapter.FavoriteAdapter;
import io.qthjen_dev.docbao24h.Model.ItemFavorited;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.DatabaseUtils.FavoriteSQLite;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.ItemFavoriteAdapterListener;
import io.qthjen_dev.docbao24h.Utils.SetupTools;

public class FragmentFavorite extends Fragment implements ItemFavoriteAdapterListener {

    private View mView;
    private RecyclerView mRecycler;
    private List<ItemFavorited> mList;
    private FavoriteAdapter mAdapter;

    private FavoriteSQLite mSQLite;
    private SharedPreferences mShareFavorite;
    private SharedPreferences.Editor mEditor;

    private SearchView mSearchView;

    private CustomTabsClient mClient;

    public FragmentFavorite() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mView = inflater.inflate(R.layout.fragment_fragment_bookmark, container, false);
        mRecycler = mView.findViewById(R.id.recyclerFavorite);

        mSQLite = new FavoriteSQLite(getContext());

        mList = new ArrayList<>();
        mList.clear();
        mList = mSQLite.getAllFavoriteItem();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FavoriteAdapter(mList, getContext(), this);

        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_favorite, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.mn_search_favorite).getActionView();

        EditText editText = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.WHITE);
        editText.setHintTextColor(Color.WHITE);

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mn_search_favorite) {
            return true;
        } else if (item.getItemId() == R.id.mn_removeAllFavorite) {
            /** dialog remove all data of favorite include sqlite and share preferences (favorite state) **/
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(getResources().getString(R.string.remove));
            alertDialog.setMessage(getResources().getString(R.string.areyouremoveallFavorite));
            alertDialog.setCancelable(false);
            alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSQLite.deleteAllFavorite();
                    /** delete all data of sharedpreferences favorite **/
                    mShareFavorite = getContext().getSharedPreferences("checkedFavorite", Context.MODE_PRIVATE);
                    mEditor = mShareFavorite.edit();
                    mEditor.clear();
                    mEditor.commit();
                    mAdapter.clearAll();
                }
            });
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemFavoriteAdapterListener(ItemFavorited item) {
        openChromeCustomTab(item.getLinkFav().trim());
        warmUpChrome();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openChromeCustomTab(String link) {
        Uri uri = Uri.parse(link);
        if (uri == null) {
            return;
        }

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setData(uri);
        customTabsIntent.intent.putExtra(FinalUtils.EXTRA_CUSTOM_TABS_TOOLBAR_COLOR, getResources().getColor(R.color.colorPrimary));

        PackageManager packageManager = getContext().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, FinalUtils.PACKAGE_NAME))
                customTabsIntent.intent.setPackage(FinalUtils.PACKAGE_NAME);
        }

        customTabsIntent.launchUrl(getActivity(), uri);
    }

    private void warmUpChrome() {
        CustomTabsServiceConnection service = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
                mClient.warmup(0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(getActivity(), FinalUtils.PACKAGE_NAME, service);
    }
}
