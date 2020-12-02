package com.brainyapps.xtremebucketlist.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.ui.activity.AboutActivity;
import com.brainyapps.xtremebucketlist.ui.activity.MainActivity;
import com.brainyapps.xtremebucketlist.ui.activity.PrivacyPolicyActivity;
import com.brainyapps.xtremebucketlist.ui.activity.TermsConditionsActivity;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {
    private MainActivity mainActivity;
    private View mView = null;
    public static LayoutInflater inflater;

    public void setMainActivity(MainActivity activity) { mainActivity = activity; }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);

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

        initUI(mView);

        onRefresh();
    }
    public void onRefresh(){

    }
    private void initUI(View view){
        ButterKnife.bind(this, mView);
        CommonUtil.hideKeyboard(mainActivity, mView);
    }

    @OnClick(R.id.btn_about)
    public void btn_about(){
        Intent intent = new Intent(mainActivity, AboutActivity.class);
        startActivity(intent);
        mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
    }
    @OnClick(R.id.btn_privacy)
    public void btn_privacy(){
        Intent intent = new Intent(mainActivity, PrivacyPolicyActivity.class);
        startActivity(intent);
        mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
    }
    @OnClick(R.id.btn_terms)
    public void btn_terms(){
        Intent intent = new Intent(mainActivity, TermsConditionsActivity.class);
        intent.putExtra("fromSetting", true);
        startActivity(intent);
        mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
    }
    @OnClick(R.id.btn_report)
    public void btn_report(){
        ArrayList<String> toArr = new ArrayList<String>();
        toArr.add(AppConstant.REPORT_EMAIL);
        CommonUtil.SendEmail(mainActivity, AppConstant.REPORT_EMAIL, "Report a Problem",	"", null);
    }
}
