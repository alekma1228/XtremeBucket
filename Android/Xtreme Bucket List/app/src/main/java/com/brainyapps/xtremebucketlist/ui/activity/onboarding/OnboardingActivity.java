package com.brainyapps.xtremebucketlist.ui.activity.onboarding;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.listener.UserListener;
import com.brainyapps.xtremebucketlist.model.UserModel;
import com.brainyapps.xtremebucketlist.ui.activity.BaseActivity;
import com.brainyapps.xtremebucketlist.ui.activity.MainActivity;
import com.brainyapps.xtremebucketlist.ui.adapter.MainPagerAdapter;
import com.brainyapps.xtremebucketlist.utility.BaseTask;
import com.brainyapps.xtremebucketlist.utility.CustomViewPager;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnboardingActivity extends BaseActivity {
    @BindView(R.id.content)
    CustomViewPager contentPager;

    private OnboardingFirstFragment onboardinFirstFragment;
    private OnboardingSecondFragment onboardinSecondFragment;
    private OnboardingThirdFragment onboardinThirdFragment;
    private MainPagerAdapter pagerAdapter;
    private int curPageNumber = 0;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ButterKnife.bind(this);

        appPreference = new AppPreference(this);
        appPreference.saveFirstStep();

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        onboardinFirstFragment = new OnboardingFirstFragment();
        onboardinFirstFragment.setOnboardingActivity(this);
        pagerAdapter.addFragment(onboardinFirstFragment);
        onboardinSecondFragment = new OnboardingSecondFragment();
        onboardinSecondFragment.setOnboardingActivity(this);
        pagerAdapter.addFragment(onboardinSecondFragment);
        onboardinThirdFragment = new OnboardingThirdFragment();
        onboardinThirdFragment.setOnboardingActivity(this);
        pagerAdapter.addFragment(onboardinThirdFragment);

        contentPager.setAdapter(pagerAdapter);
        contentPager.setOffscreenPageLimit(3);

        contentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        curPageNumber = 0;
                        break;
                    case 1:
                        curPageNumber = 1;
                        break;
                    case 2:
                        curPageNumber = 2;
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onBackPressed(){
        onSkip();
    }
    public void onSkip(){
        startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
    }
    public void onNext(){
        switch (curPageNumber){
            case 0:
                contentPager.setCurrentItem(1, true);
                break;
            case 1:
                contentPager.setCurrentItem(2, true);
                break;
            case 2:
                onSkip();
                break;
        }
    }
}
