package com.nn.studio.episode8.ui;

/**
 * Created by jibi on 12/7/14.
 */

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nn.studio.episode8.R;
import com.nn.studio.episode8.model.Discussion;
import com.nn.studio.episode8.provider.PGContract;
import com.nn.studio.episode8.ui.adapters.FragmentDiscussionPostsAdapter;
import com.nn.studio.episode8.ui.views.SmartViewPager;

/**
 * Created by jibi on 12/7/14.
 */
public class FragmentVerticalSection extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "FragmentVerticalSection";
    private static final String KEY_DISCUSSION = "discussion";
    private static final String KEY_POSTURI = "post_uri";
    private static final Integer URL_LOADER = 100;
    private FragmentDiscussionPostsAdapter mAdapter;
    private Discussion discussion;
    private Bundle discussionLoaderArgs;

    public FragmentVerticalSection() {}

    public static FragmentVerticalSection newInstance(Discussion discussion){
        FragmentVerticalSection fragment = new FragmentVerticalSection();
        Log.w(TAG, discussion.title);
        Log.w(TAG, discussion.toContentValues().toString());
        Bundle args = new Bundle();
        args.putParcelable(KEY_DISCUSSION, discussion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            discussion = getArguments().getParcelable(KEY_DISCUSSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_preview, container, false);

        TextView discussionTitle = (TextView) view.findViewById(R.id.discussion_title);
        discussionTitle.setText(discussion.title);

        SmartViewPager postsPreview = (SmartViewPager) view.findViewById(R.id.discussion_posts);
        if(mAdapter == null){
            mAdapter = new FragmentDiscussionPostsAdapter(view.getContext(), getChildFragmentManager(), null);
        }
        postsPreview.setAdapter(mAdapter);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(discussion != null){
            Uri postsInDiscussion = PGContract.Posts.getNestedQuestionUrl(discussion.forum_id, discussion._id);
            return new CursorLoader(getActivity().getApplicationContext(), postsInDiscussion, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
