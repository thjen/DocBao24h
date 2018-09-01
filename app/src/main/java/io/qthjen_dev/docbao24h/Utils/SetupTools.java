package io.qthjen_dev.docbao24h.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SetupTools {
    public static void setupToolbarAsMyDefault(Context context, Toolbar toolbar, String nametbar) {
        ((AppCompatActivity) context).getSupportActionBar();
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(nametbar);
    }
    public static boolean isPackageInstalled(String pakageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(pakageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
