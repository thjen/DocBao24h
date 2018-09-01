package io.qthjen_dev.docbao24h.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sdsmdg.tastytoast.TastyToast;

import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.NetworkUtils.NetworkTools;
import io.qthjen_dev.docbao24h.Utils.SetupTools;

public class FeedbackActivity extends AppCompatActivity {

    private Button mFeedbackEmail, mFeedbackOrther;
    private Toolbar mTbar;
    private RelativeLayout mBackground;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mFeedbackEmail = findViewById(R.id.sendFeedBackByEmail);
        mFeedbackOrther = findViewById(R.id.sendFeedBackByOther);
        mTbar = findViewById(R.id.tbar_feedback);
        mBackground = findViewById(R.id.feedback_background);

        SetupTools.setupToolbarAsMyDefault(this, mTbar, getResources().getString(R.string.feedback));
        mTbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
            }
        });

        mFeedbackEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FeedbackActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.dialog_send_feedback_by_email);

                final TextInputLayout tilEmail = dialog.findViewById(R.id.til_email);
                final TextInputLayout tilSubject = dialog.findViewById(R.id.til_subject);
                final TextInputLayout tilBody = dialog.findViewById(R.id.til_body);
                Button btBack = dialog.findViewById(R.id.backDialogSendFeedbackEmail);
                Button btDone = dialog.findViewById(R.id.bt_doneDialog);

                btBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                btDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tilEmail.getEditText().getText().toString().trim().equals("") && !tilSubject.getEditText()
                                .getText().toString().trim().equals("") && !tilBody.getEditText().getText().toString().trim().equals("")) {
                            PackageManager pm = getPackageManager();
                            boolean isInstall = SetupTools.isPackageInstalled("com.google.android.gm", pm);
                            if (isInstall) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{tilEmail.getEditText().getText().toString().trim()});
                                intent.putExtra(Intent.EXTRA_SUBJECT, tilSubject.getEditText().getText().toString().trim());
                                intent.putExtra(Intent.EXTRA_TEXT, tilBody.getEditText().getText().toString().trim());
                                try {
                                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.sending)));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    TastyToast.makeText(FeedbackActivity.this, getResources().getString(R.string.errorCheckinfo), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            } else {
                                TastyToast.makeText(FeedbackActivity.this, getResources().getString(R.string.emailNoInstalled), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        } else {
                            TastyToast.makeText(FeedbackActivity.this, getResources().getString(R.string.pleaseInputInfo), TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        }
                    }
                });

                dialog.show();
            }
        });

        mFeedbackOrther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FeedbackActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.dialog_send_feedback_by_orther);
                Button btDone = dialog.findViewById(R.id.bt_doneDialogOrther);
                Button btClose = dialog.findViewById(R.id.backDialogSendFeedbackOrther);
                final TextInputLayout tilName = dialog.findViewById(R.id.til_nameFeedBack);
                final TextInputLayout tilSdt = dialog.findViewById(R.id.til_sdt);
                final TextInputLayout tilDescription = dialog.findViewById(R.id.til_description);

                btClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                btDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (NetworkTools.CheckNetwork(FeedbackActivity.this)) {
                            if (!tilName.getEditText().getText().toString().trim().equals("") && !tilSdt.getEditText()
                                    .getText().toString().trim().equals("") && !tilDescription.getEditText().getText().toString().trim().equals("")) {
                                TastyToast.makeText(FeedbackActivity.this, getResources().getString(R.string.sendfinished), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            } else {
                                TastyToast.makeText(FeedbackActivity.this, getResources().getString(R.string.pleaseInputInfo), TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        } else {
                            TastyToast.makeText(FeedbackActivity.this, "Không có kết nối mạng!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_x, R.anim.slide_left_to_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPreferences = getSharedPreferences(FinalUtils.themeName, MODE_PRIVATE);
        boolean state = mSharedPreferences.getBoolean(FinalUtils.nightModeState, false);
        boolean deepsea = mSharedPreferences.getBoolean(FinalUtils.whiteModernState, false);
        if (state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_dark));
        }
        if (deepsea) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_two));
        }
        if (!deepsea && !state) {
            mBackground.setBackground(getResources().getDrawable(R.drawable.background_drawable_one));
        }
    }
}
