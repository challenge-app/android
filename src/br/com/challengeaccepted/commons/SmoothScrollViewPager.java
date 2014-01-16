package br.com.challengeaccepted.commons;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

public class SmoothScrollViewPager extends ViewPager {
    public SmoothScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.enabled = true;
        postInitViewPager();
    }

    private ScrollerCustomDuration mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
             Class<?> viewpager = ViewPager.class;
                Field scroller = viewpager.getDeclaredField("mScroller");
                scroller.setAccessible(true);
                Field interpolator = viewpager.getDeclaredField("sInterpolator");
                interpolator.setAccessible(true);

                mScroller = new ScrollerCustomDuration(getContext(),
                        (Interpolator) interpolator.get(null));
                scroller.set(this, mScroller);
        } catch (Exception e) {
            Log.e("MyPager", e.getMessage());
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
}