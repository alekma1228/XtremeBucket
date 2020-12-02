package com.brainyapps.xtremebucketlist.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class LimitNoListView extends ListView {
    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public LimitNoListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int count = getCount();

        if (count > 0) {
            if (count != oldCount) {
                int height = getChildAt(0).getHeight() + 5;
                oldCount = count;
                params = getLayoutParams();

                int row = count;
                params.height = row * height;
                setLayoutParams(params);
            }
        }
        super.onDraw(canvas);
    }
}