package com.nn.studio.episode8.ui;

/**
 * Created by jibi on 12/7/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nn.studio.episode8.R;
import com.nn.studio.episode8.model.Discussion;

/**
 * Created by jibi on 12/7/14.
 */
public class FragmentVerticalSection extends Fragment {
    private static final String TAG = "FragmentVerticalSection";
    private static final String KEY_DISCUSSION = "discussion";
    private Discussion discussion;

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
        return view;
    }
}
