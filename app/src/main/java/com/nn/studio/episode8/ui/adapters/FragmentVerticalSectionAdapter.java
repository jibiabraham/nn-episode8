package com.nn.studio.episode8.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.nn.studio.episode8.model.Discussion;
import com.nn.studio.episode8.provider.PGContract;
import com.nn.studio.episode8.ui.FragmentVerticalSection;

/**
 * Created by jibi on 12/7/14.
 */
public class FragmentVerticalSectionAdapter extends VerticalPagerAdapter {
    private static final String TAG = "FragmentVerticalSectionAdapter";
    private Cursor mCursor;
    private Context mContext;
    private String[] DEFAULT_PROJECTION = PGContract.Discussions.DEFAULT_PROJECTION;

    public FragmentVerticalSectionAdapter(Context context, FragmentManager fm, Cursor mCursor) {
        super(fm);
        mContext = context;
        mCursor = mCursor;
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    public Cursor swapCursor(Cursor newCursor){
        if(newCursor == mCursor){
            return null;
        }
        mCursor = newCursor;
        notifyDataSetChanged();
        return mCursor;
    }

    @Override
    public Fragment getItem(int position) {
        if(mCursor != null){
            mCursor.moveToPosition(position);
            Discussion d = new Discussion(mCursor);
            return FragmentVerticalSection.newInstance(d);
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
