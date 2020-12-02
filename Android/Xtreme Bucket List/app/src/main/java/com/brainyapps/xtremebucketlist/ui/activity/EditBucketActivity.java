package com.brainyapps.xtremebucketlist.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppGlobals;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.listener.ObjectListener;
import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.model.FileModel;
import com.brainyapps.xtremebucketlist.model.UserModel;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.brainyapps.xtremebucketlist.utility.MyImageLoader;
import com.brainyapps.xtremebucketlist.utility.ResourceUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBucketActivity extends BaseActivity {
    public static EditBucketActivity instance;

    @BindView(R.id.txt_goal)
    EditText txt_goal;
    @BindView(R.id.img_photo)
    ImageView img_photo;
    @BindView(R.id.progress_file)
    ProgressBar progress_file;
    @BindView(R.id.txt_detail)
    EditText txt_detail;
    @BindView(R.id.txt_note)
    EditText txt_note;

    private int objectId;
    private BucketModel bucketModel;

    private final int PICTURE_PICK = 1000;
    private final int CAMERA_CAPTURE = 1001;
    private final int PICTURE_PICK_ON_WEB = 1002;

    private boolean isPhotoAdded = false;
    private Bitmap mOrgBmp = null;
    private String mAvatarFilename = "";
    private String mLinkUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bucket);
        ButterKnife.bind(this);
        instance = this;

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
    @OnClick(R.id.btn_done)
    public void onDone(){
        CommonUtil.hideKeyboard(instance, txt_goal);
        if (isValid()) {
            save();
        }
    }
    @OnClick(R.id.img_photo)
    public void onImageSelect(){
        final Dialog dialog = new Dialog(instance);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_photo);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        TextView btn_take_photo = (TextView) dialog.findViewById(R.id.btn_take_photo);
        TextView btn_photo_gallery = (TextView) dialog.findViewById(R.id.btn_photo_gallery);
        TextView btn_search_web = (TextView) dialog.findViewById(R.id.btn_search_web);
        TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (CommonUtil.verifyStoragePermissions(CommonUtil.TYPE_CAMERA_PERMISSION, instance)){
                    if (CommonUtil.verifyStoragePermissions(CommonUtil.TYPE_STORAGE_PERMISSION, instance)){
                        chooseTakePhoto(true);
                    } else {
                        MessageUtil.showError(instance, R.string.msg_error_permission_storage);
                    }
                } else {
                    MessageUtil.showError(instance, R.string.msg_error_permission_camera);
                }
            }
        });
        btn_photo_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (CommonUtil.verifyStoragePermissions(CommonUtil.TYPE_STORAGE_PERMISSION, instance))
                    chooseTakePhoto(false);
                else
                    MessageUtil.showError(instance, R.string.msg_error_permission_storage);
            }
        });
        btn_search_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(instance, BrowserActivity.class);
                startActivityForResult(intent, PICTURE_PICK_ON_WEB);
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
        mLinkUrl = bucketModel.link;
        init();
    }

    // choose photo and take
    private void chooseTakePhoto(boolean isTake) {
        if (!isTake) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICTURE_PICK);
        } else {
            try {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(ResourceUtil.getAvatarFilePath());
                if (file.exists())
                    file.delete();
                Uri photoURI = FileProvider.getUriForFile(instance, getApplicationContext().getPackageName() + ".provider", file);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            } catch (ActivityNotFoundException anfe) {
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_PICK_ON_WEB && resultCode == RESULT_OK) {
            mLinkUrl = data.getStringExtra("url");
            String ret = data.getStringExtra("return");
            Uri imageUri = Uri.fromFile(new File(ret));
            startCropImageActivity(imageUri);
        } else if (requestCode == PICTURE_PICK && resultCode == Activity.RESULT_OK) {
            mLinkUrl = "";
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // no permissions required or already grunted, can start crop image activity
            startCropImageActivity(imageUri);
        } else if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
            mLinkUrl = "";
            try {
                File file = new File(ResourceUtil.getAvatarFilePath());
                ExifInterface ei = new ExifInterface(ResourceUtil.getAvatarFilePath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap bitmap = ResourceUtil.decodeSampledBitmapFromFile(ResourceUtil.getAvatarFilePath(), UserModel.AVATAR_SIZE);
                Bitmap rotatedBitmap = null;
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = AppGlobals.rotateImage(bitmap, 90);
                        file.delete();
                        ResourceUtil.saveBitmapToSdcard(rotatedBitmap, ResourceUtil.getAvatarFilePath());
                        file = new File(ResourceUtil.getAvatarFilePath());
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = AppGlobals.rotateImage(bitmap, 180);
                        file.delete();
                        ResourceUtil.saveBitmapToSdcard(rotatedBitmap, ResourceUtil.getAvatarFilePath());
                        file = new File(ResourceUtil.getAvatarFilePath());
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = AppGlobals.rotateImage(bitmap, 270);
                        file.delete();
                        ResourceUtil.saveBitmapToSdcard(rotatedBitmap, ResourceUtil.getAvatarFilePath());
                        file = new File(ResourceUtil.getAvatarFilePath());
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                Uri photoURI = FileProvider.getUriForFile(instance, getApplicationContext().getPackageName() + ".provider", file);
                startCropImageActivity(photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bm = ResourceUtil.decodeUri(instance, result.getUri(), UserModel.AVATAR_SIZE);
                    if (bm != null) {
                        if (mOrgBmp != null)
                            mOrgBmp.recycle();
                        mOrgBmp = bm;
                        img_photo.setImageBitmap(mOrgBmp);
                        mAvatarFilename = ResourceUtil.getAvatarFilePath();
                        ResourceUtil.saveBitmapToSdcard(mOrgBmp, mAvatarFilename);
                        isPhotoAdded = true;
                    } else {
                        Log.i(getString(R.string.app_name), "Bitmap is null");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                MessageUtil.showError(instance, "Cropping failed: " + result.getError());
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        if (CommonUtil.verifyStoragePermissions(CommonUtil.TYPE_STORAGE_PERMISSION, this)) {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setMultiTouchEnabled(true)
                    .start(this);
        } else {
            MessageUtil.showError(instance, R.string.msg_error_permission_storage);
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(txt_goal.getText().toString().trim())) {
            MessageUtil.showError(instance, "Please enter your goal.");
            return false;
        }
        if (txt_goal.getText().toString().trim().length() > 60) {
            MessageUtil.showError(instance, "Entered your goal is very long.");
            return false;
        }
        if (TextUtils.isEmpty(txt_detail.getText().toString().trim())) {
            MessageUtil.showError(instance, "Please enter details.");
            return false;
        }

//        if (txt_detail.getText().toString().trim().length() > 1000) {
//            MessageUtil.showError(instance, "Entered details is very long.");
//            return false;
//        }
        if (TextUtils.isEmpty(txt_note.getText().toString().trim())) {
            MessageUtil.showError(instance, "Please enter your note.");
            return false;
        }
//        if (txt_note.getText().toString().trim().length() > 1000) {
//            MessageUtil.showError(instance, "Entered your notes is very long.");
//            return false;
//        }

        return true;
    }
    private void save() {
        bucketModel.goal = txt_goal.getText().toString().trim();
        bucketModel.detail = txt_detail.getText().toString().trim();
        bucketModel.note = txt_note.getText().toString().trim();
        bucketModel.link = mLinkUrl;
        if (!mAvatarFilename.isEmpty()){
            String pictureFilePath = ResourceUtil.getBucketPhotoFilePath();
            ResourceUtil.saveBitmapToSdcard(mOrgBmp, pictureFilePath);
            bucketModel.pictureFilePath = pictureFilePath;
        }
        AppPreference appPreference = new AppPreference(this);
        ArrayList<BucketModel> arrayList = appPreference.getBucketListData();
        arrayList.remove(objectId);
        arrayList.add(objectId, bucketModel);
        appPreference.setBucketListData(arrayList);

        MessageUtil.showAlertDialog(instance, MessageUtil.TYPE_SUCCESS, "This bucket list item is saved.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBack();
            }
        });
    }
}
