package com.brainyapps.xtremebucketlist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.model.UserModel;
import com.brainyapps.xtremebucketlist.utility.MyImageLoader;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "",
        mailTo = "king.ofmobile@yandex.com",
        customReportContent = {
                ReportField.USER_COMMENT,
                ReportField.ANDROID_VERSION,
                ReportField.APP_VERSION_CODE,
                ReportField.BRAND,
                ReportField.PHONE_MODEL,
                ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class XtremeBucketListApp extends Application{
    public static Context mContext;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = getApplicationContext();
        ACRA.init(this);
        // window size
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        AppGlobals.SCREEN_WIDTH = display.getWidth();
        AppGlobals.SCREEN_HEIGHT = display.getHeight();

        // initialize
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        AppPreference.initialize(pref);

        /*
         * initialize parse
         */
        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("e0dc6fec-8ed4-434a-9e96-4207fbb58248")
                .clientKey("5ab3b7e8-09e3-43d4-b4da-1c29fd512d87")
                .server("https://parseapps.brainyapps.tk:20007/parse")
                .build());

        ParseObject.registerSubclass(UserModel.class);
//        ParseObject.registerSubclass(BucketModel.class);

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Parse Facebook integration
        FacebookSdk.sdkInitialize(mContext);
        ParseFacebookUtils.initialize(this);

        initImageLoader(mContext);
        new MyImageLoader();
        MyImageLoader.init();
    }
    public static Context getContext() {
        return mContext;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
