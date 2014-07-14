package com.nn.studio.episode8.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nn.studio.episode8.R;
import com.nn.studio.episode8.model.Post;
import com.nn.studio.episode8.utils.HtmlImageParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by jibi on 14/7/14.
 */
public class FragmentDiscussionPostPreview extends Fragment {
    private static final String TAG = "FragmentDiscussionPostPreview";
    private static final String POST_KEY = "post";
    private Post post;

    public FragmentDiscussionPostPreview(){}

    public static FragmentDiscussionPostPreview newInstance(Post post){
        FragmentDiscussionPostPreview fragment = new FragmentDiscussionPostPreview();
        Bundle args = new Bundle();
        args.putParcelable(POST_KEY, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            post = getArguments().getParcelable(POST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_post, container, false);

        TextView content = (TextView) view.findViewById(R.id.content);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView comments = (TextView) view.findViewById(R.id.comments);
        TextView likes = (TextView) view.findViewById(R.id.likes);

        Document htmlContent = Jsoup.parseBodyFragment(post.content);
        HtmlImageParser imgur = new HtmlImageParser(view.getContext(), content);
        content.setText(Html.fromHtml(htmlContent.outerHtml(), imgur, null));

        author.setText(post.getAuthorAsString());
        comments.setText(post.getCommentsCountAsString());
        likes.setText(post.getLikesCountAsString());

        return view;
    }
}
