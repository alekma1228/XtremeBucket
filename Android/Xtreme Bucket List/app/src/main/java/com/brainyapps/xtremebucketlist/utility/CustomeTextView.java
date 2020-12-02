package com.brainyapps.xtremebucketlist.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CustomeTextView extends TextView {
    public CustomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomeTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/newtown.ttf");
        setTypeface(tf ,1);

    }
}
