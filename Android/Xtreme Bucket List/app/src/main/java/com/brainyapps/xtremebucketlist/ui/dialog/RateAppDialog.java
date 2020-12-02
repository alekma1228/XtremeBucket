package com.brainyapps.xtremebucketlist.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.XtremeBucketListApp;
import com.brainyapps.xtremebucketlist.service.RateService;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;

public class RateAppDialog extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate_app);
        findViewById(R.id.txt_ratenow).setOnClickListener(this);
        findViewById(R.id.txt_maybelater).setOnClickListener(this);
        findViewById(R.id.txt_nothanks).setOnClickListener(this);
    }

    public static void OpenMe() {
        Intent startMain = new Intent(XtremeBucketListApp.getContext(), RateAppDialog.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        XtremeBucketListApp.getContext().startActivity(startMain);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.exit_to_left, R.anim.exit_to_right);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_ratenow:
                if (RateService.isRunning()) {
                    Intent intent = new Intent(XtremeBucketListApp.getContext(), RateService.class);
                    stopService(intent);
                }
                CommonUtil.launchMarket();
                finish();
                break;
            case R.id.txt_maybelater:
                if (!RateService.isRunning()) {
                    RateService.isFirst = false;
                    Intent intent = new Intent(XtremeBucketListApp.getContext(), RateService.class);
                    startService(intent);
                }
                finish();
                break;
            case R.id.txt_nothanks:
                finish();
                break;
            default:
                break;
        }
    }
}

