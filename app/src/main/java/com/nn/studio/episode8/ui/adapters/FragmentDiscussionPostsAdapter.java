package com.nn.studio.episode8.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nn.studio.episode8.model.Post;
import com.nn.studio.episode8.ui.FragmentDiscussionPostPreview;

/**
 * Created by jibi on 14/7/14.
 */
public class FragmentDiscussionPostsAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "FragmentDiscussionPostsAdapter";
    private Cursor mCursor;
    private Context mContext;

    public FragmentDiscussionPostsAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentDiscussionPostsAdapter(Context mContext, FragmentManager fm, Cursor mCursor) {
        super(fm);
        this.mCursor = mCursor;
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        if(mCursor != null){
            mCursor.moveToPosition(position);
            Post post = new Post(mCursor);
            FragmentDiscussionPostPreview fragment = FragmentDiscussionPostPreview.newInstance(post);
            return fragment;
        }
        return null;
    }

    public void swapCursor(Cursor c){
        if(mCursor == c){
            return;
        }
        this.mCursor = c;
        notifyDataSetChanged();
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
