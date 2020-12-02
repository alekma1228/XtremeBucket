package com.brainyapps.xtremebucketlist.ui.activity.onboarding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.brainyapps.xtremebucketlist.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingThirdFragment extends Fragment {
    private View mView = null;
    private OnboardingActivity onboardingActivity;

    private float mFirstX;
    private float mFirstY;

    public void setOnboardingActivity(OnboardingActivity activity){
        onboardingActivity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_onboarding_third, container, false);
        ButterKnife.bind(this, mView);

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                float lastX = ev.getX();
                float lastY = ev.getY();
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstX = lastX;
                        mFirstY = lastY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = lastX - mFirstX;
                        float dy = lastY - mFirstY;
                        if (Math.abs(dy) < Math.abs(dx)) {
                            if (dx < 0 && Math.abs(dx) > 3) {
                                onboardingActivity.onSkip();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mFirstX = 0;
                        mFirstY = 0;
                        break;
                }

                return true;
            }
        });

        return mView;
    }
    @OnClick(R.id.btn_next)
    public void onNext(){
        onboardingActivity.onSkip();
    }
}
