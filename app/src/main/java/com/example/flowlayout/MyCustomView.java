package com.example.flowlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jay on 18. 2. 11.
 */

public class MyCustomView extends View {
    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("jm.lee", String.format("MyCustomView.onMeasure(widthMeasureSped:%s, heightMeasureSpec:%s)", MeasureSpec.toString(widthMeasureSpec), MeasureSpec.toString(heightMeasureSpec)));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
