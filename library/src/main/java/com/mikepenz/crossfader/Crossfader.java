package com.mikepenz.crossfader;

import android.os.Build;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout;

/**
 * Created by mikepenz on 15.07.15.
 */
public class Crossfader {

    private CrossFadeSlidingPaneLayout mCrossFadeSlidingPaneLayout;

    public Crossfader() {

    }

    private View mContent = null;

    public Crossfader withContent(View content) {
        this.mContent = content;
        return this;
    }

    private View mFirst = null;
    private int mFirstWidth = -1;

    public Crossfader withFirst(View first, int width) {
        this.mFirst = first;
        this.mFirstWidth = width;
        return this;
    }

    private View mSecond = null;
    private int mSecondWidth = -1;

    public Crossfader withSecond(View first, int width) {
        this.mSecond = first;
        this.mSecondWidth = width;
        return this;
    }

    public Crossfader withStructure(View first, int firstWidth, View second, int secondWidth) {
        withFirst(first, firstWidth);
        withSecond(second, secondWidth);
        return this;
    }

    public Crossfader build() {
        if (mFirstWidth < mSecondWidth) {
            throw new RuntimeException("the first layout has to be the layout with the greater width");
        }

        //get the layout which should be replaced by the CrossFadeSlidingPaneLayout
        ViewGroup container = ((ViewGroup) mContent.getParent());

        //remove the content from it's parent
        container.removeView(mContent);

        //create the cross fader container
        mCrossFadeSlidingPaneLayout = (CrossFadeSlidingPaneLayout) LayoutInflater.from(mContent.getContext()).inflate(R.layout.crossfader_base, container, false);
        container.addView(mCrossFadeSlidingPaneLayout);
        FrameLayout mCrossFadePanel = (FrameLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.panel);
        LinearLayout mCrossFadeFirst = (LinearLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.first);
        LinearLayout mCrossFadeSecond = (LinearLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.second);
        LinearLayout mCrossFadeContainer = (LinearLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.content);

        //
        setWidth(mCrossFadePanel, mFirstWidth);
        setWidth(mCrossFadeFirst, mFirstWidth);
        setWidth(mCrossFadeSecond, mSecondWidth);
        setLeftMargin(mCrossFadeContainer, mSecondWidth);

        //add content to the panel
        mCrossFadeFirst.addView(mFirst, mFirstWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        mCrossFadeSecond.addView(mSecond, mSecondWidth, ViewGroup.LayoutParams.MATCH_PARENT);

        //add back main content
        mCrossFadeContainer.addView(mContent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCrossFadeFirst.setAlpha(0);
        }

        return this;
    }

    public boolean isCrossFaded() {
        return mCrossFadeSlidingPaneLayout.isOpen();
    }

    public void crossFade() {
        if (mCrossFadeSlidingPaneLayout.isOpen()) {
            mCrossFadeSlidingPaneLayout.closePane();
        } else {
            mCrossFadeSlidingPaneLayout.openPane();
        }
    }

    public void setWidth(View view, int width) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        view.setLayoutParams(lp);
    }

    public void setLeftMargin(View view, int leftMargin) {
        SlidingPaneLayout.LayoutParams lp = (SlidingPaneLayout.LayoutParams) view.getLayoutParams();
        lp.leftMargin = leftMargin;
        view.setLayoutParams(lp);
    }
}
