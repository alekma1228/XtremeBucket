package com.brainyapps.xtremebucketlist.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.ui.adapter.MainPagerAdapter;
import com.brainyapps.xtremebucketlist.ui.fragment.BrowserFragment;
import com.brainyapps.xtremebucketlist.ui.fragment.BucketListFragment;
import com.brainyapps.xtremebucketlist.ui.fragment.CompletedFragment;
import com.brainyapps.xtremebucketlist.ui.fragment.SettingsFragment;
import com.brainyapps.xtremebucketlist.utility.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static MainActivity instance;

    @BindView(R.id.img_bucket_list)
    ImageView img_bucket_list;
    @BindView(R.id.img_completed_list)
    ImageView img_completed_list;
    @BindView(R.id.img_browser)
    ImageView img_browser;
    @BindView(R.id.img_settings)
    ImageView img_settings;
    @BindView(R.id.content)
    CustomViewPager contentPager;

    MainPagerAdapter pagerAdapter;

    public BucketListFragment bucketListFragment;
    public CompletedFragment completedFragment;
    public BrowserFragment browserFragment;
    public SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        instance = this;

        initUI();
    }
    @Override
    public void onResume(){
        super.onResume();
        instance = this;
        bucketListFragment.setMainActivity(instance);
        completedFragment.setMainActivity(instance);
        browserFragment.setMainActivity(instance);
        settingsFragment.setMainActivity(instance);
    }
    private void initUI() {
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        bucketListFragment = new BucketListFragment();
        pagerAdapter.addFragment(bucketListFragment);
        completedFragment = new CompletedFragment();
        pagerAdapter.addFragment(completedFragment);
        browserFragment = new BrowserFragment();
        pagerAdapter.addFragment(browserFragment);
        settingsFragment = new SettingsFragment();
        pagerAdapter.addFragment(settingsFragment);
        contentPager.setAdapter(pagerAdapter);
        contentPager.setOffscreenPageLimit(4);
        contentPager.setPagingEnabled(false);

        contentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                resetBottomAndToolBarIcons();
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mainTabPressed();
    }
    private void resetBottomAndToolBarIcons(){
        img_bucket_list.setImageResource(R.drawable.ic_tab_bucket);
        img_completed_list.setImageResource(R.drawable.ic_tab_completed);
        img_browser.setImageResource(R.drawable.ic_tab_browser);
        img_settings.setImageResource(R.drawable.ic_tab_settings);

        contentPager.setPagingEnabled(false);
    }
    public void mainTabPressed(){
        bucketListTabPressed();
    }
    @OnClick(R.id.img_bucket_list)
    public void bucketListTabPressed(){
        if (getCurrentPageIndex() == 0)
            return;
        contentPager.setCurrentItem(0, true);
        bucketListFragment.onResume();
        resetBottomAndToolBarIcons();
        img_bucket_list.setImageResource(R.drawable.ic_tab_bucket_selected);
    }
    @OnClick(R.id.img_completed_list)
    public void completedTabPressed(){
        if (getCurrentPageIndex() == 1)
            return;
        contentPager.setCurrentItem(1, true);
        completedFragment.onResume();
        resetBottomAndToolBarIcons();
        img_completed_list.setImageResource(R.drawable.ic_tab_completed_selected);
    }
    @OnClick(R.id.img_browser)
    public void browserTabPressed(){
        if (getCurrentPageIndex() == 2)
            return;
        contentPager.setCurrentItem(2, true);
        browserFragment.onResume();
        browserFragment.onRefresh();
        resetBottomAndToolBarIcons();
        img_browser.setImageResource(R.drawable.ic_tab_browser_selected);
    }
    @OnClick(R.id.img_settings)
    public void settingsTabPressed(){
        if (getCurrentPageIndex() == 3)
            return;
        contentPager.setCurrentItem(3, true);
        settingsFragment.onResume();
        resetBottomAndToolBarIcons();
        img_settings.setImageResource(R.drawable.ic_tab_settings_selected);
    }
    public int getCurrentPageIndex(){
        if (contentPager != null) {
            return contentPager.getCurrentItem();
        } else {
            return 4;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (contentPager.getCurrentItem()){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
    @Override
    public void onBackPressed(){
        final Dialog dialog = new Dialog(instance);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        TextView title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView left = (TextView) dialog.findViewById(R.id.btn_left);
        TextView right = (TextView) dialog.findViewById(R.id.btn_right);
        title.setText(getResources().getString(R.string.app_name));
        message.setText(getResources().getString(R.string.want_exit));
        left.setText(getResources().getString(R.string.Okay));
        right.setText(getResources().getString(R.string.cancel));
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
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
}
