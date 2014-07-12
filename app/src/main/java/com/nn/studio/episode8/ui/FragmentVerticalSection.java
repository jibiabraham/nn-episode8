package com.nn.studio.episode8.ui;

/**
 * Created by jibi on 12/7/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nn.studio.episode8.model.Discussion;

/**
 * Created by jibi on 12/7/14.
 */
public class FragmentVerticalSection extends Fragment {
    private static final String KEY_DISCUSSION = "discussion";
    public FragmentVerticalSection() {}

    public static FragmentVerticalSection newInstance(Discussion discussion){
        FragmentVerticalSection fragment = new FragmentVerticalSection();
        Bundle args = new Bundle();
        args.putParcelable(KEY_DISCUSSION, discussion);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
