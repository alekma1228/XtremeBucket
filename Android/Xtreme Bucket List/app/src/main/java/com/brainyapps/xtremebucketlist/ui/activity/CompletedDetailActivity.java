package com.brainyapps.xtremebucketlist.ui.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.utility.BaseTask;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.brainyapps.xtremebucketlist.utility.MyImageLoader;
import com.brainyapps.xtremebucketlist.utility.ResourceUtil;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.ads.AdRequest.LOGTAG;

public class CompletedDetailActivity extends BaseActivity {
    public static CompletedDetailActivity instance;

    @BindView(R.id.txt_goal)
    TextView txt_goal;
    @BindView(R.id.img_photo)
    ImageView img_photo;
    @BindView(R.id.progress_file)
    ProgressBar progress_file;
    @BindView(R.id.txt_detail)
    TextView txt_detail;
    @BindView(R.id.txt_note)
    TextView txt_note;

    private int objectId;
    private BucketModel bucketModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_detail);

        ButterKnife.bind(this);
        instance = this;
    }
    @Override
    public void onResume(){
        super.onResume();
        objectId = getIntent().getIntExtra(AppConstant.EK_OBJECTID, 0);
        getBucketObject(objectId);
    }
    @Override
    public void onBackPressed(){
        onBack();
    }

    @OnClick(R.id.btn_back)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.exit_to_left, R.anim.exit_to_right);
    }
    @OnClick(R.id.btn_share)
    public void onShare(){
        customShare();
    }

    private static String facebook_packageName = "com.facebook.katana";
    private static String instagram_packageName = "com.instagram.android";
    private static String twitter_packageName = "com.twitter.android";

    private void customShare(){
        final Dialog dialog = new Dialog(instance);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_share);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        LinearLayout btn_facebook = (LinearLayout) dialog.findViewById(R.id.btn_facebook);
        LinearLayout btn_instagram = (LinearLayout) dialog.findViewById(R.id.btn_instagram);
        LinearLayout btn_twitter = (LinearLayout) dialog.findViewById(R.id.btn_twitter);
        TextView btn_copy_link = (TextView) dialog.findViewById(R.id.btn_copy_link);
        TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage( facebook_packageName);
                if (intent != null)
                {
                    try {
                        Bitmap bitmap = ResourceUtil.decodeUri(bucketModel.pictureFilePath);
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(bitmap)
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();
                        ShareDialog shareDialog = new ShareDialog(instance);
                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    MessageUtil.showError(instance, "Facebook is not installed.");
                }
                dialog.dismiss();
            }
        });
        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage( instagram_packageName);
                if (intent != null) {
                    // The application exists
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.setPackage(instagram_packageName);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, bucketModel.goal);
                    try {
                        Bitmap bitmap = ResourceUtil.decodeUri(bucketModel.pictureFilePath);
                        String imgBitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap, "bucket",null);
                        Uri imgBitmapUri=Uri.parse(imgBitmapPath);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);

                        instance.startActivity(shareIntent);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    MessageUtil.showError(instance, "Instagram is not installed.");
                }
                dialog.dismiss();
            }
        });
        btn_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage( twitter_packageName);
                if (intent != null) {
                    // The application exists
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.setPackage(twitter_packageName);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, bucketModel.goal);
                    try {
                        Bitmap bitmap = ResourceUtil.decodeUri(bucketModel.pictureFilePath);
                        String imgBitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap, "bucket",null);
                        Uri imgBitmapUri=Uri.parse(imgBitmapPath);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);

                        instance.startActivity(shareIntent);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    MessageUtil.showError(instance, "Twitter is not installed.");
                }
                dialog.dismiss();
            }
        });
        btn_copy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(bucketModel.link)){
                    MessageUtil.showAlertDialog(instance, MessageUtil.TYPE_WARNING, "There is not a link.");
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Bucket", bucketModel.link);
                    clipboard.setPrimaryClip(clip);
                }
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void init(){
        File pictureFile = new File(bucketModel.pictureFilePath);
        progress_file.setVisibility(View.GONE);
        if (pictureFile.exists()){
            try {
                Bitmap bitmap = ResourceUtil.decodeUri(bucketModel.pictureFilePath);
                img_photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                img_photo.setImageResource(R.drawable.default_image_bg);
            }
        } else {
            img_photo.setImageResource(R.drawable.default_image_bg);
        }
        txt_goal.setText(bucketModel.goal);
        txt_detail.setText(bucketModel.detail);
        txt_note.setText(bucketModel.note);
    }
    private void getBucketObject(final int objectId){
        AppPreference appPreference = new AppPreference(this);
        ArrayList<BucketModel> bucketList = appPreference.getBucketListData();
        bucketModel = bucketList.get(objectId);
        init();
    }
}
