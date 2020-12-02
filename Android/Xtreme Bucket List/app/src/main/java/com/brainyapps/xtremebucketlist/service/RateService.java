package com.brainyapps.xtremebucketlist.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.brainyapps.xtremebucketlist.ui.dialog.RateAppDialog;

public class RateService extends Service {
    private static boolean isRunning = false;
    public static boolean isFirst = false;
    private int REFRESH_TIME = 4*3600*1000;
    private Handler mHandler;

    public static RateService instance;

    public RateService() {}

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        isRunning = true;
        startRepeatingTask();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();

        instance = this;
    }

    @Override
    public void onDestroy() {
        stopRepeatingTask();
        isRunning = false;
        super.onDestroy();
    }

    Runnable repeatTask = new Runnable() {
        @Override
        public void run() {
            updateStatus();
            mHandler.postDelayed(repeatTask, REFRESH_TIME);
        }
    };

    public void updateStatus() {
        if (isFirst) {
            RateAppDialog.OpenMe();
        } else {
            isFirst = true;
        }
    }

    void startRepeatingTask() {
        repeatTask.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(repeatTask);
    }
}
