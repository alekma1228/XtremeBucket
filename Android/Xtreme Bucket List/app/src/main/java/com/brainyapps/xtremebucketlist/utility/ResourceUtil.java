package com.brainyapps.xtremebucketlist.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Surface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ResourceUtil {
    public static String RES_DIRECTORY = Environment.getExternalStorageDirectory() + "/brainyapps/XtremeBucketList/";
    /*
     * File
     */
    public static String getImageFilePath(String fileName) {
        String tempDirPath = RES_DIRECTORY;
        String tempFileName = fileName;

        File tempDir = new File(tempDirPath);
        if (!tempDir.exists())
            tempDir.mkdirs();
        File tempFile = new File(tempDirPath + tempFileName);
        if (!tempFile.exists())
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        return tempDirPath + tempFileName;
    }
    public static String getCapturedImageFilePath() {
        return getImageFilePath("camera.png");
    }
    public static String getCapturedVideoFilePath() {
        return getImageFilePath("capturedVideo.mp4");
    }
    public static String getAvatarFilePath() {
        return getImageFilePath("avatar.png");
    }
    public static String getPhotoFilePath() {
        return getImageFilePath("photo.png");
    }
    public static String getBucketPhotoFilePath() {
        Date date = new Date();
        String dateStr = date.toString();
        return getImageFilePath("photo_" + dateStr + ".png");
    }
    public static String getVideoFilePath() {
        return getImageFilePath("video.mp4");
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(path, options);
        int reqHeight = reqWidth * options.outHeight / options.outWidth;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width <= reqWidth && height <= reqHeight)
            return 1;

        float widthRatio = (float)width / reqWidth;
        float heightRatio = (float)height / reqHeight;
        float maxRatio = Math.max(widthRatio, heightRatio);
        inSampleSize = (int)(maxRatio + 0.5);
        return inSampleSize;
    }

    public static Bitmap decodeUri(Context context, Uri selectedImage, int reqSize) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < reqSize
                    || height_tmp / 2 < reqSize) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
    }
    public static Bitmap decodeUri(String path) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeFile(path, options);
    }
    /*
    * Save bitmap on given file path on sdcard
    */
    public static void saveBitmapToSdcard(Bitmap bitmap, String dirPath) {
        File tempFile = new File(dirPath);
        if (tempFile.exists())
            tempFile.delete();

        try {
            FileOutputStream fOut = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat){
            filePath = generateFromKitkat(uri,context);
        }

        if(filePath != null){
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private static String generateFromKitkat(Uri uri, Context context){
        String filePath = null;
        if(DocumentsContract.isDocumentUri(context, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);
            if (wholeID.split(":").length < 2)
                return "";
            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Video.Media.DATA };
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String getResourceDirectory() {
        String tempDirPath = RES_DIRECTORY;
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists())
            tempDir.mkdirs();

        return tempDirPath;
    }
    private static String mVideoFileExtension = "mp4";
    public static String getCaptureVideoFilePath(Context context) {
        return getResourceDirectory() + "post_video." + mVideoFileExtension;
    }
    private static String mPhotoFileExtension = "jpg";
    public static void setPhotoExtension(String fileExtension) {
        mPhotoFileExtension = fileExtension;
    }
    public static String getCaptureImageFilePath(Context context) {
        return getResourceDirectory() + "post_image."+mPhotoFileExtension;
    }
    public static int getRotationAngle(Activity mContext, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


}
