package com.brainyapps.xtremebucketlist.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.ImageResult;
import com.brainyapps.xtremebucketlist.utility.MyImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultArrayAdapter (Context context, List<ImageResult> images){
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        ivImage.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        final ProgressBar progress_file = (ProgressBar) convertView.findViewById(R.id.progress_file);

        tvTitle.setText(Html.fromHtml(imageResult.getTitle()));

//        Picasso.with(getContext()).load(imageResult.getThumbUrl()).into(ivImage);
        progress_file.setVisibility(View.VISIBLE);
        MyImageLoader.showImage(ivImage, imageResult.getThumbUrl(), new SimpleImageLoadingListener(){
            public void onLoadingStarted(String imageUri, View view) {
            }
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progress_file.setVisibility(View.GONE);
            }
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progress_file.setVisibility(View.GONE);
            }
            public void onLoadingCancelled(String imageUri, View view) {
                progress_file.setVisibility(View.GONE);
            }
        });

        return convertView;
    }
}
