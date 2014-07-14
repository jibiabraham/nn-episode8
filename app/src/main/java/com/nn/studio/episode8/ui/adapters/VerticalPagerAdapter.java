package com.nn.studio.episode8.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.*;
import android.view.View;

/**
 * Created by jibi on 12/7/14.
 */
public class VerticalPagerAdapter extends FragmentStatePagerAdapter {
    public VerticalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    public float getPageHeight(int position) {
        return 1.f;
    }
}
