package io.qthjen_dev.docbao24h.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

import io.qthjen_dev.docbao24h.Fragment.FragmentReader;
import io.qthjen_dev.docbao24h.Model.TabInfo;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private List<TabInfo> list;
    private Context context;

    public TabLayoutAdapter(FragmentManager fm, List<TabInfo> list, Context context) {
        super(fm);
        this.list = list;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentReader fragmentReader = new FragmentReader(position, list); // transmission positon to fragment reader get
        return fragmentReader;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle() + "";
    }

}
