package com.brainyapps.xtremebucketlist;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.CamcorderProfile;
import android.support.annotation.NonNull;
import android.util.Log;

import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.model.UserModel;
import com.brainyapps.xtremebucketlist.push.PushNoti;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AppGlobals {
    // Screen size
    public static float SCREEN_WIDTH = 480;
    public static float SCREEN_HEIGHT = 800;
    public static UserModel mUserModel;
    public static String TAG = "AppGlobal";

    public static ArrayList<String> QUALITY_LABELS = new ArrayList<String>();
    public static ArrayList<Integer> QUALITY_LEVELS = new ArrayList<Integer>();
    public static ArrayList<String> RESOLUTION_LABELS = new ArrayList<String>();
    public static ArrayList<String> RESOLUTION_SIZES = new ArrayList<String>();

    @SuppressLint("InlinedApi")
    public static void init() {

    }
    public static void sendNotification(int type, ArrayList<ParseUser> userList, String objectId, String message, String senderId) {
        ParseUser curUser = ParseUser.getCurrentUser();
        for (int i=0; i<userList.size(); i++){
            if (!userList.get(i).getObjectId().equals(curUser.getObjectId())){
                PushNoti.sendPush(type, objectId, userList.get(i), message, senderId, null);
            }
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    @NonNull
    public static Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    public static int getIndexOfBucketItem(ArrayList<BucketModel> arrayList, BucketModel bucketModel){
        int ret = 0;

        for (int i = 0; i< arrayList.size(); i++){
            BucketModel model = arrayList.get(i);
            if (model.goal.equals(bucketModel.goal)
                    && model.detail.equals(bucketModel.detail)
                    && model.note.equals(bucketModel.note)
                    && model.link.equals(bucketModel.link)
                    && model.pictureFilePath.equals(bucketModel.pictureFilePath)
                    && model.isCompleted == bucketModel.isCompleted){
                return i;
            }
        }
        return ret;
    }
    public static ArrayList<BucketModel> getCompletedBuckets(ArrayList<BucketModel> arrayList){
        ArrayList<BucketModel> ret = new ArrayList<>();
        for (int i = 0; i< arrayList.size(); i++){
            BucketModel model = arrayList.get(i);
            if (model.isCompleted == true){
                ret.add(model);
            }
        }
        return ret;
    }
}
