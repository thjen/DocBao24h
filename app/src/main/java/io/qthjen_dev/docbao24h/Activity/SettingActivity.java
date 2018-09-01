package io.qthjen_dev.docbao24h.Activity;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.SetupTools;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mTbarSetting;
    private Switch mSwitchNightMode, mSwitchWhiteModern;
    private RelativeLayout mBackground;
    private TextView tvNightMode, tvWhiteModern;
    private ImageView ivNightMode, ivWhiteModern;

    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        SetupTools.setupToolbarAsMyDefault(this, mTbarSetting, getResources().getString(R.string.setting));
        mTbarSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
            }
        });
        setupNightMode();
        setupWhiteModern();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
    }

    private void setupNightMode() {
        mShared = getSharedPreferences(FinalUtils.themeName, MODE_PRIVATE);
        mEditor = mShared.edit();
        mSwitchNightMode.setChecked(false);
        mSwitchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEditor.putBoolean(FinalUtils.nightModeState, true);
                    mEditor.putBoolean(FinalUtils.whiteModernState, false);
                    mEditor.commit();
                    mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
                    ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.night_white));
                    mSwitchWhiteModern.setChecked(false);
                } else if (!mSwitchWhiteModern.isChecked()) {
                    mEditor.putBoolean(FinalUtils.nightModeState, false);
                    mEditor.putBoolean(FinalUtils.whiteModernState, false);
                    mEditor.commit();
                    mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
                    ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                }
            }
        });
    }

    private void setupWhiteModern() {
        mShared = getSharedPreferences(FinalUtils.themeName, MODE_PRIVATE);
        mEditor = mShared.edit();
        mSwitchWhiteModern.setChecked(false);
        mSwitchWhiteModern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEditor.putBoolean(FinalUtils.whiteModernState, true);
                    mEditor.putBoolean(FinalUtils.nightModeState, false);
                    mEditor.commit();
                    mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
                    ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                    mSwitchNightMode.setChecked(false);
                } else if (!mSwitchNightMode.isChecked()) {
                    mEditor.putBoolean(FinalUtils.whiteModernState, false);
                    mEditor.putBoolean(FinalUtils.nightModeState, false);
                    mEditor.commit();
                    mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
                    ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        boolean state = mShared.getBoolean(FinalUtils.nightModeState, false);
        boolean stateWhiteModern = mShared.getBoolean(FinalUtils.whiteModernState, false);
        mSwitchNightMode.setChecked(state);
        mSwitchWhiteModern.setChecked(stateWhiteModern);
        if (state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
            ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.night_white));
        }
        if (stateWhiteModern) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
            ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        }
        if (!state && !stateWhiteModern) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
            ivNightMode.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        }
    }

    private void initView() {
        mTbarSetting = findViewById(R.id.tbar_setting);
        mSwitchNightMode = findViewById(R.id.switchNightmode);
        mBackground = findViewById(R.id.setting_background);
        tvNightMode = findViewById(R.id.tv_nightmodeSt);
        ivNightMode = findViewById(R.id.iv_nightmode);
        mSwitchWhiteModern = findViewById(R.id.whiteModern);
        tvWhiteModern = findViewById(R.id.tv_whiteModern);
        ivWhiteModern = findViewById(R.id.iv_whiteModern);
    }
}
