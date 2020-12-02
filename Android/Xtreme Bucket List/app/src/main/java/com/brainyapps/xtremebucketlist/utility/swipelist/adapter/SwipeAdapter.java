package com.brainyapps.xtremebucketlist.utility.swipelist.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainyapps.xtremebucketlist.AppGlobals;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.ui.activity.BaseActivity;
import com.brainyapps.xtremebucketlist.ui.fragment.CompletedFragment;
import com.brainyapps.xtremebucketlist.utility.MyImageLoader;
import com.brainyapps.xtremebucketlist.utility.ResourceUtil;
import com.brainyapps.xtremebucketlist.utility.swipelist.widget.SwipeListView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.DeleteCallback;
import com.parse.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SwipeAdapter extends ArrayAdapter<BucketModel> {
    private BaseActivity mContext = null;
    private int mRightWidth = 0;
    private SwipeListView mListView;
    private CompletedFragment messageFragment;

    private ArrayList<BucketModel> mData;

    public void setData(ArrayList<BucketModel> data){
        mData = data;
    }
    public ArrayList<BucketModel> getData(){
        return mData;
    }

    public SwipeAdapter(BaseActivity ctx, int rightWidth, SwipeListView listView, CompletedFragment messageFragment) {
        super(ctx, 0);
        mContext = ctx;
        mRightWidth = rightWidth;
        mListView = listView;
        this.messageFragment = messageFragment;
    }

    public BucketModel getItem(int position) {
        return mData.get(position);
    }
    @Override
    public int getCount() {
        if (mData == null){
            return 0;
        }
        return mData.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder item;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_completedlist, parent, false);
            item = new ViewHolder();
            item.item_left = (View)convertView.findViewById(R.id.item_left);
            item.item_right = (View)convertView.findViewById(R.id.item_right);
            item.item_right_del = (ImageView) convertView.findViewById(R.id.img_recycler);
            item.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
            item.loadingBar = (ProgressBar) convertView.findViewById(R.id.progress_file);
            item.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            item.txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);

            convertView.setTag(item);
        } else {
            item = (ViewHolder)convertView.getTag();
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        item.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        item.item_right.setLayoutParams(lp2);
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        item.item_right_del.setLayoutParams(lp3);

        final BucketModel model = getItem(position);
        File pictureFile = new File(model.pictureFilePath);
        item.loadingBar.setVisibility(View.GONE);
        if (pictureFile.exists()){
            try {
                Bitmap bitmap = ResourceUtil.decodeUri(model.pictureFilePath);
                item.img_photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                item.img_photo.setImageResource(R.drawable.default_image_bg);
            }
        } else {
            item.img_photo.setImageResource(R.drawable.default_image_bg);
        }

        item.txt_name.setText(model.goal);
        item.txt_desc.setText(model.detail);

        item.item_right_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                TextView title = (TextView) dialog.findViewById(R.id.txt_title);
                TextView message = (TextView) dialog.findViewById(R.id.txt_message);
                TextView left = (TextView) dialog.findViewById(R.id.btn_left);
                TextView right = (TextView) dialog.findViewById(R.id.btn_right);
                title.setText("Delete");
                message.setText("Are you sure you want to delete?");
                left.setText("Delete");
                right.setText("No");
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppPreference appPreference = new AppPreference(mContext);
                        ArrayList<BucketModel> arrayList = appPreference.getBucketListData();
                        int index = AppGlobals.getIndexOfBucketItem(arrayList, model);
                        BucketModel bucketModel = arrayList.get(index);
                        bucketModel.isCompleted = false;
                        arrayList.remove(index);
                        arrayList.add(index, bucketModel);
                        appPreference.setBucketListData(arrayList);

                        mData.remove(position);
                        notifyDataSetChanged();

                        dialog.dismiss();
                        if (mListView != null){
                            mListView.anyHiddenRight();
                        }
                    }
                });

                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListView != null){
                            mListView.anyHiddenRight();
                        }

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        View item_left;
        View item_right;
        ImageView item_right_del;
        ImageView img_photo;
        ProgressBar loadingBar;
        TextView txt_name;
        TextView txt_desc;
    }
}
