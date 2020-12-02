package com.brainyapps.xtremebucketlist.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainyapps.xtremebucketlist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsConditionsActivity extends BaseActivity {
    @BindView(R.id.txt_content)
    TextView txt_content;

    @BindView(R.id.btn_back)
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        ButterKnife.bind(this);
        txt_content.setText(Html.fromHtml(getString(R.string.terms)));
    }
    @Override
    public void onBackPressed(){
        onBack();
    }
    @OnClick(R.id.btn_back)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.exit_to_left, R.anim.exit_to_right);
    }
}
