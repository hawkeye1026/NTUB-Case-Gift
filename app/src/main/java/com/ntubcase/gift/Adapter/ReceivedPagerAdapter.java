package com.ntubcase.gift.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ReceivedPagerAdapter extends PagerAdapter {

    private ArrayList<View> mViews;

    public ReceivedPagerAdapter(ArrayList<View> mViews){
        this.mViews = mViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mViews.get(position)!=null) {
            container.removeView(mViews.get(position));
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o==view;
    }
}
