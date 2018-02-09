package com.jeongmin.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 뷰를 오른쪽으로 하나 씩 배치 한다.
 * 레이아웃의 가로길이를 넘어가는 경우에는 자동으로 줄바꿈을 한다.
 *
 * Created by jay on 18. 1. 24.
 */

public class FlowLayout extends ViewGroup {
    DisplayMetrics metrics;

    public FlowLayout(Context context) {
        super(context);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int lineTop = getPaddingTop();  // 각 라인의 top
        final int right = getMeasuredWidth() - getPaddingRight();


        int maxLineHeight = 0;
        int top;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams childMargin = (MarginLayoutParams) child.getLayoutParams();
            boolean enoughLineSpace = (left + childMargin.leftMargin + childMargin.rightMargin + child.getMeasuredWidth()) <= right;
            if (enoughLineSpace) {
                left += childMargin.leftMargin;
                top = lineTop + childMargin.topMargin;
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                maxLineHeight = Math.max(maxLineHeight, childMargin.topMargin + childMargin.bottomMargin + child.getMeasuredHeight());
            } else {
                lineTop += maxLineHeight;
                maxLineHeight = 0;

                left = getPaddingLeft() + childMargin.leftMargin;
                top = lineTop + childMargin.topMargin;

                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                maxLineHeight = Math.max(maxLineHeight, childMargin.topMargin + childMargin.bottomMargin + child.getMeasuredHeight());
            }
            Log.d("jm.lee", String.format("onLayout(chaild%d) l:%d, t:%d, r:%d, b:%d, measuredWidth : %d", i, left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight(), child.getMeasuredWidth()));
            left = left + child.getMeasuredWidth() + childMargin.rightMargin;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("jm.lee", String.format("onMeasure(widthMeasureSpec(%s), heightMeasureSpec(%s)", MeasureSpec.toString(widthMeasureSpec), MeasureSpec.toString(heightMeasureSpec)));
        // 자식들
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // GONE 면 크기를 구하지 않고 VISIBLE나 INVISIBLE일 때만 측정
            if (child.getVisibility() == GONE) {
                continue;
            }

            // 자식 뷰의 크기 결정
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        // 나
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void init() {
        metrics = getResources().getDisplayMetrics();
    }
}
