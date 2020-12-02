package com.brainyapps.xtremebucketlist.ui.activity.onboarding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingSecondFragment extends Fragment {
    private View mView = null;
    private AppPreference appPreference;
    private OnboardingActivity onboardingActivity;

    public void setOnboardingActivity(OnboardingActivity activity){
        onboardingActivity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_onboarding_second, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }
    @OnClick(R.id.btn_next)
    public void onNext(){
        onboardingActivity.onNext();
    }

    @OnClick(R.id.btn_skip)
    public void onSkip(){
        onboardingActivity.onSkip();
    }
}
