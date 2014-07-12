package com.nn.studio.episode8.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nn.studio.episode8.R;

/**
 * Created by jibi on 12/7/14.
 */
public class DiscussionPreviewFragment extends Fragment {
    public DiscussionPreviewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_preview, container, false);
        return view;
    }
}
