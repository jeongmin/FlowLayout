package com.jeongmin.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * A ViewGroup that layout its children to next row when there is not enough place to put.
 *
 * Created by jay on 18. 1. 24.
 */

public class FlowLayout extends ViewGroup {
    @IntDef({
            LINE_GRAVITY_NONE,
            LINE_GRAVITY_CENTER,
            LINE_GRAVITY_CENTER_VERTICAL,
            LINE_GRAVITY_CENTER_HORIZONTAL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LineGravity {}

    public static final int LINE_GRAVITY_NONE              = 0;
    public static final int LINE_GRAVITY_CENTER            = 1;
    public static final int LINE_GRAVITY_CENTER_VERTICAL   = 2;
    public static final int LINE_GRAVITY_CENTER_HORIZONTAL = 3;



    DisplayMetrics metrics;

    private ArrayList<Row> mRows;

    @LineGravity
    private int mLineGravity = 0;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        metrics = getResources().getDisplayMetrics();
        mRows = new ArrayList<>();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mLineGravity = typedArray.getInt(R.styleable.FlowLayout_line_gravity, LINE_GRAVITY_NONE);
        typedArray.recycle();
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && p != null;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp == null) {
            return generateDefaultLayoutParams();
        } else {
            return new LayoutParams(lp);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftPos;
        int lineTop;

        for (int lineNum = 0; lineNum < mRows.size(); lineNum++) {
            Row row = mRows.get(lineNum);

            leftPos = getLayoutLeft(row);
            lineTop = getPaddingTop() + row.top;

            for(View child : row.children) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int top = getLayoutTop(lineTop, row.height, child);
                int bottom = top + child.getMeasuredHeight();

                leftPos += lp.leftMargin;
                int right = leftPos + child.getMeasuredWidth();

                child.layout(leftPos, top, right, bottom);
                leftPos = right + lp.rightMargin;
            }
        }
    }


    private int getLayoutLeft(Row row) {
        switch (mLineGravity) {
            case LINE_GRAVITY_CENTER_HORIZONTAL:
            case LINE_GRAVITY_CENTER:
                int centerAvailableWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
                return centerAvailableWidth + getPaddingLeft() - (row.width / 2);
            case LINE_GRAVITY_CENTER_VERTICAL:
            case LINE_GRAVITY_NONE:
            default:
                int leftMargin;
                if (row.children.size() > 0) {
                    leftMargin = ((LayoutParams)row.children.get(0).getLayoutParams()).leftMargin;
                } else {
                    leftMargin = 0;
                }
                return getPaddingLeft() + leftMargin;
        }
    }


    private int getLayoutTop(int lineTop, int lineHeight, View view) {
        LayoutParams lp = (LayoutParams)view.getLayoutParams();
        switch (mLineGravity) {
            case LINE_GRAVITY_CENTER_VERTICAL:
            case LINE_GRAVITY_CENTER:
                return lineTop + (lineHeight / 2) - (view.getMeasuredHeight() / 2) + lp.topMargin - lp.bottomMargin;
            case LINE_GRAVITY_CENTER_HORIZONTAL:
            case LINE_GRAVITY_NONE:
            default:
                return lineTop + lp.topMargin;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRows.clear();
        int childCount = getChildCount();
        int availableWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingTop();


        int maxWidth = 0;

        Row row = new Row(0);
        mRows.add(row);

        int widthIncludingMargin;
        int heightIncludingMargin;
        boolean notEnoughSpace;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            widthIncludingMargin = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            heightIncludingMargin = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            notEnoughSpace = (row.width + widthIncludingMargin > availableWidth);
            if (notEnoughSpace) {
                // break line
                int topOfNewLine = row.top + row.height;
                row = new Row(topOfNewLine);
                mRows.add(row);
            }
            row.addView(child);
            row.width += widthIncludingMargin;
            row.height = Math.max(heightIncludingMargin, row.height);
        }

        int maxHeight = 0;
        for (Row line : mRows) {
            maxWidth = Math.max(maxWidth, line.width);
            maxHeight += line.height;
        }

        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }


    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);

            /*
            final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
            gravity = a.getInt(R.styleable.FrameLayout_Layout_layout_gravity, UNSPECIFIED_GRAVITY);
            a.recycle();
            */
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams lp) {
            super(lp);
        }
    }


    class Row {
        public int width;
        public int height;
        public int top;
        public ArrayList<View> children;

        public Row(int top) {
            this.top = top;
            children = new ArrayList<>();
        }

        public void addView(View child) {
            this.children.add(child);
        }
    }
}
