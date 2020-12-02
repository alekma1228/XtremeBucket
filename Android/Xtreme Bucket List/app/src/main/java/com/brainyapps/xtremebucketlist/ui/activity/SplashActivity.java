package com.brainyapps.xtremebucketlist.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.ui.activity.onboarding.OnboardingActivity;

public class SplashActivity extends BaseActivity {
    public static SplashActivity instance = null;
    public static final int REQUEST_PERMISSION = 1;
    public static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    public static boolean isFirst = true;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        instance = this;
        appPreference = new AppPreference(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (isFirst) {
            isFirst = false;
            verifyStoragePermissions(this);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoNextPage();
                }
            }, 1000);
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission0 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission0 != PackageManager.PERMISSION_GRANTED
                || permission1 != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoNextPage();
                }
            }, 1000);
        }
    }

    private void gotoNextPage() {
        if (!appPreference.getFirstStep()) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(instance, MainActivity.class));
            finish();
        }
    }
}
