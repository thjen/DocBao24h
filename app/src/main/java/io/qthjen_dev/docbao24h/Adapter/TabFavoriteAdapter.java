package io.qthjen_dev.docbao24h.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.qthjen_dev.docbao24h.Fragment.FragmentFavorite;
import io.qthjen_dev.docbao24h.Fragment.FragmentRecently;

public class TabFavoriteAdapter extends FragmentPagerAdapter {

    private String favorite, recently;

    public TabFavoriteAdapter(FragmentManager fm, String favorite, String recently) {
        super(fm);
        this.favorite = favorite;
        this.recently = recently;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentFavorite favorite = new FragmentFavorite();
                return favorite;
            case 1:
                FragmentRecently fragmentRecently = new FragmentRecently();
                return fragmentRecently;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return favorite;
            case 1: return recently;
            default: return null;
        }
    }
}
