package com.brainyapps.xtremebucketlist.ui.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.brainyapps.xtremebucketlist.utility.MyImageLoader;
import com.brainyapps.xtremebucketlist.utility.ResourceUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BucketListAdapter extends ArrayAdapter<BucketModel> {
    private LayoutInflater mInflater;
    private BaseActivity mActivity;
    private ArrayList<BucketModel> data;

    public BucketListAdapter(@NonNull BaseActivity context) {
        super(context, R.layout.list_item_bucketlist);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActivity = context;
    }
    public void setData(ArrayList<BucketModel> data) {
        this.data = data;
    }
    public ArrayList<BucketModel> getData() {
        return this.data;
    }
    @Override
    public BucketModel getItem(int position) {
        return data.get(position);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        convertView = mInflater.inflate(R.layout.list_item_bucketlist, parent, false);
        holder = new Holder();
        holder.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
        holder.progress_file = (ProgressBar) convertView.findViewById(R.id.progress_file);
        holder.btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);
        holder.txt_name= (TextView) convertView.findViewById(R.id.txt_name);
        holder.checkbox = (ImageView) convertView.findViewById(R.id.checkbox);
        holder.txt_desc= (TextView) convertView.findViewById(R.id.txt_desc);
        convertView.setTag(holder);

        final BucketModel model = getItem(position);

        File pictureFile = new File(model.pictureFilePath);
        holder.progress_file.setVisibility(View.GONE);
        if (pictureFile.exists()){
            try {
                Bitmap bitmap = ResourceUtil.decodeUri(model.pictureFilePath);
                holder.img_photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                holder.img_photo.setImageResource(R.drawable.default_image_bg);
            }
        } else {
            holder.img_photo.setImageResource(R.drawable.default_image_bg);
        }

        if (model.isCompleted)
            holder.checkbox.setImageResource(R.drawable.ic_checked);
        else
            holder.checkbox.setImageResource(R.drawable.ic_checked_no);

        holder.txt_name.setText(model.goal);
        holder.txt_desc.setText(model.detail);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mActivity);
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
                        AppPreference appPreference = new AppPreference(mActivity);
                        ArrayList<BucketModel> arrayList = appPreference.getBucketListData();
                        int index = AppGlobals.getIndexOfBucketItem(arrayList, model);
                        arrayList.remove(index);
                        appPreference.setBucketListData(arrayList);

                        data.remove(position);
                        notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreference appPreference = new AppPreference(mActivity);
                ArrayList<BucketModel> arrayList = appPreference.getBucketListData();
                int index = AppGlobals.getIndexOfBucketItem(arrayList, model);

                model.isCompleted = !model.isCompleted;
                if (model.isCompleted){
                    holder.checkbox.setImageResource(R.drawable.ic_checked);

                    new AlertDialog.Builder(mActivity)
                            .setTitle("")
                            .setNegativeButton("Date of completion", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Tell me about it..", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                } else {
                    holder.checkbox.setImageResource(R.drawable.ic_checked_no);
                }

                BucketModel bucketModel = arrayList.get(index);
                bucketModel.isCompleted = model.isCompleted;
                appPreference.setBucketListData(arrayList);

                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private static class Holder {
        ImageView img_photo;
        ProgressBar progress_file;
        ImageView btn_delete;
        TextView txt_name;
        ImageView checkbox;
        TextView txt_desc;
    }
}
