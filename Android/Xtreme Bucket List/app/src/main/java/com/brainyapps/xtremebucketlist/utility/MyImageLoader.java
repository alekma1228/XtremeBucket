package com.brainyapps.xtremebucketlist.utility;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.brainyapps.xtremebucketlist.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class MyImageLoader {
    private static ImageLoader instance = ImageLoader.getInstance();
    public static final int TYPE_USER = 1;
    public static final int TYPE_UPLOAD = 2;
    public static final int TYPE_NORMAL = 3;

    public static DisplayImageOptions upload_options;
    public static DisplayImageOptions profile_options;
    public static DisplayImageOptions normal_options;

    public static void init() {
        profile_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_search_person) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_search_person) // resource or drawable
                .showImageOnFail(R.drawable.ic_search_person) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int size = (width > height ? height : width);
                        return Bitmap.createBitmap(bitmap, 0, 0, size, size);
                    }
                })
                .build();
        upload_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_upload_photo) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_upload_photo) // resource or drawable
                .showImageOnFail(R.drawable.ic_upload_photo) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int size = (width > height ? height : width);
                        return Bitmap.createBitmap(bitmap, 0, 0, size, size);
                    }
                })
                .build();
        normal_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image_bg) // resource or drawable
                .showImageForEmptyUri(R.drawable.default_image_bg) // resource or drawable
                .showImageOnFail(R.drawable.default_image_bg) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int size = (width > height ? height : width);
                        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
                    }
                })
                .build();
    }

    @SuppressWarnings("deprecation")
    public static void clearCache() {
        if (instance != null) {
            instance.clearDiscCache();
            instance.clearMemoryCache();
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.stop();
        }
    }

    public static void showImage(ImageView imgView, String url, SimpleImageLoadingListener listener) {
        if (TextUtils.isEmpty(url))
            return;

        DisplayImageOptions option = normal_options;
        try {
            ImageAware imageAware = new ImageViewAware(imgView, false);
            instance.displayImage(url, imageAware, option, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAvatar(ImageView imgView, String url, int type, SimpleImageLoadingListener listener) {
        if (TextUtils.isEmpty(url))
            return;

        DisplayImageOptions option = profile_options;
        if (type == TYPE_UPLOAD)
            option = upload_options;
        else if (type == TYPE_NORMAL)
            option = normal_options;
        try {
            ImageAware imageAware = new ImageViewAware(imgView, false);
            instance.displayImage(url, imageAware, option, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancel(ImageView imgView) {
        if (imgView != null)
            instance.cancelDisplayTask(imgView);
    }
}
