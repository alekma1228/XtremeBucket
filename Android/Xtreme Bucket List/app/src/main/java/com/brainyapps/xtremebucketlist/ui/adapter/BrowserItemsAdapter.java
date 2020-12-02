package com.brainyapps.xtremebucketlist.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.SearchResult;
import com.brainyapps.xtremebucketlist.ui.activity.BaseActivity;

import java.util.ArrayList;

public class BrowserItemsAdapter extends ArrayAdapter<SearchResult> {
    private LayoutInflater mInflater;
    private BaseActivity mActivity;
    private ArrayList<SearchResult> data;

    public BrowserItemsAdapter(@NonNull BaseActivity context) {
        super(context, R.layout.list_item_browser);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActivity = context;
    }
    public void setData(ArrayList<SearchResult> data) {
        this.data = data;
    }
    public ArrayList<SearchResult> getData() {
        return this.data;
    }
    @Override
    public SearchResult getItem(int position) {
        return data.get(position);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        convertView = mInflater.inflate(R.layout.list_item_browser, parent, false);
        holder = new Holder();
        holder.txt_title= (TextView) convertView.findViewById(R.id.txt_title);
        holder.txt_link= (TextView) convertView.findViewById(R.id.txt_link);
        holder.txt_desc= (TextView) convertView.findViewById(R.id.txt_desc);
        convertView.setTag(holder);

        SearchResult result = getItem(position);
        holder.txt_title.setText(result.getTitle());
        holder.txt_link.setText(result.getLink());
        holder.txt_desc.setText(result.getSnippet());

        return convertView;
    }

    private static class Holder {
        TextView txt_title;
        TextView txt_link;
        TextView txt_desc;
    }
}
