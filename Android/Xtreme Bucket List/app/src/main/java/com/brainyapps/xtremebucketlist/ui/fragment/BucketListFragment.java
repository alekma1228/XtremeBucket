package com.brainyapps.xtremebucketlist.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppGlobals;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.ui.activity.BucketDetailActivity;
import com.brainyapps.xtremebucketlist.ui.activity.MainActivity;
import com.brainyapps.xtremebucketlist.ui.activity.NewBucketActivity;
import com.brainyapps.xtremebucketlist.ui.adapter.BucketListAdapter;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BucketListFragment extends Fragment {
    private MainActivity mainActivity;
    private View mView = null;
    public static LayoutInflater inflater;

    @BindView(R.id.listView)
    ListView listView;

    private ArrayList<BucketModel> bucketList = new ArrayList<>();
    private BucketListAdapter bucketListAdapter;

    public void setMainActivity(MainActivity activity) { mainActivity = activity; }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bucket_list, container, false);

        this.inflater = inflater;

        mainActivity = (MainActivity) getActivity();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = MainActivity.instance;
    }
    @Override
    public void onResume(){
        super.onResume();

        if (mView == null)
            return;
        if (inflater == null)
            return;

        if (listView == null)
            initUI(mView);

        onRefresh();
    }
    public void onRefresh(){
        CommonUtil.hideKeyboard(mainActivity, listView);

        getBucketList();
    }
    private void initUI(View view){
        ButterKnife.bind(this, mView);
        bucketListAdapter = new BucketListAdapter(mainActivity);

        bucketListAdapter.setData(bucketList);
        listView.setAdapter(bucketListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BucketModel bucketModel = bucketListAdapter.getItem(position);
                Intent intent = new Intent(mainActivity, BucketDetailActivity.class);
                int index = AppGlobals.getIndexOfBucketItem(bucketList, bucketModel);
                intent.putExtra(AppConstant.EK_OBJECTID, index);
                mainActivity.startActivity(intent);
                mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
            }
        });
    }
    @OnClick(R.id.btn_add)
    public void onAdd(){
        Intent intent = new Intent(mainActivity, NewBucketActivity.class);;
        mainActivity.startActivity(intent);
        mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
    }

    private void getBucketList(){
        if (mainActivity.getCurrentPageIndex() != 0){
            return;
        }
        AppPreference appPreference = new AppPreference(mainActivity);
        bucketList = appPreference.getBucketListData();
        bucketListAdapter.setData(bucketList);
        bucketListAdapter.notifyDataSetChanged();

        if (bucketList.isEmpty()){
            MessageUtil.showAlertDialog(mainActivity, MessageUtil.TYPE_WARNING, "There is no item available.");
        }
    }
}
