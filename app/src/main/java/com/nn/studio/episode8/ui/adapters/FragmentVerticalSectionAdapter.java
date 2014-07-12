package com.nn.studio.episode8.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by jibi on 12/7/14.
 */
public class FragmentVerticalSectionAdapter extends FragmentStatePagerAdapter {
    private Cursor mCursor;
    private Context mContext;

    public FragmentVerticalSectionAdapter(Context context, FragmentManager fm, Uri queryUri) {
        super(fm);
        mContext = context;
        mCursor = mContext.getContentResolver().query(queryUri, null, null, null, null);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public Fragment getItem(int position) {
        if(mCursor != null){
            mCursor.moveToPosition(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if(mCursor != null){
            return mCursor.getCount();
        }
        return 0;
    }

    public Cursor getCursor(){
        return mCursor;
    }
}
