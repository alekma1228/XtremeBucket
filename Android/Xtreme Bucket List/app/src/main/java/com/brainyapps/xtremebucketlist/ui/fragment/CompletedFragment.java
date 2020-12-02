package com.brainyapps.xtremebucketlist.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppGlobals;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.BucketModel;
import com.brainyapps.xtremebucketlist.ui.activity.CompletedDetailActivity;
import com.brainyapps.xtremebucketlist.ui.activity.MainActivity;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.brainyapps.xtremebucketlist.utility.swipelist.adapter.SwipeAdapter;
import com.brainyapps.xtremebucketlist.utility.swipelist.widget.SwipeListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedFragment extends Fragment {
    private MainActivity mainActivity;
    private View mView = null;
    public static LayoutInflater inflater;

    @BindView(R.id.listView)
    SwipeListView listView;

    private SwipeAdapter mAdapter;
    private ArrayList<BucketModel> mCompletedDatas = new ArrayList<>();
    private ArrayList<BucketModel> bucketModels = new ArrayList<>();

    public void setMainActivity(MainActivity activity) { mainActivity = activity; }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_completed_list, container, false);
        ButterKnife.bind(this, mView);
        this.inflater = inflater;

        mainActivity = (MainActivity) getActivity();
        mAdapter = new SwipeAdapter(mainActivity, listView.getRightViewWidth(), listView, this);

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

        initUI(mView);
        listView.anyHiddenRight();
        onRefresh();
        listView.anyHiddenRight();
    }
    public void onRefresh(){
        getBucketList();
    }
    private void initUI(View view){
        if (mAdapter == null)
            mAdapter = new SwipeAdapter(mainActivity, listView.getRightViewWidth(), listView, this);

        mAdapter.setData(mCompletedDatas);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BucketModel bucketModel = mAdapter.getItem(position);
                Intent intent = new Intent(mainActivity, CompletedDetailActivity.class);
//                intent.putExtra(AppConstant.EK_OBJECTID, bucketModel.getObjectId());
                int index = AppGlobals.getIndexOfBucketItem(bucketModels, bucketModel);
                intent.putExtra(AppConstant.EK_OBJECTID, index);
                startActivity(intent);
                mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
            }
        });

        CommonUtil.hideKeyboard(mainActivity, mView);
    }
    private void getBucketList(){
        if (mainActivity.getCurrentPageIndex() != 1){
            return;
        }

        AppPreference appPreference = new AppPreference(mainActivity);
        bucketModels = appPreference.getBucketListData();
        mCompletedDatas = AppGlobals.getCompletedBuckets(bucketModels);
        mAdapter.setData(mCompletedDatas);
        mAdapter.notifyDataSetChanged();

        if (mCompletedDatas.isEmpty()){
            MessageUtil.showAlertDialog(mainActivity, MessageUtil.TYPE_WARNING, "There is no item available.");
        }
    }

}
