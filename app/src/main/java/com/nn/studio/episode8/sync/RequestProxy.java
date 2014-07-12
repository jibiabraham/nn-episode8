package com.nn.studio.episode8.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nn.studio.episode8.utils.NetworkRequests;
import com.nn.studio.episode8.provider.PGContract;
import com.nn.studio.episode8.model.Comment;
import com.nn.studio.episode8.model.Discussion;
import com.nn.studio.episode8.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jibi on 9/7/14.
 */
public final class RequestProxy {
    private static String BASEURL = "http://smaug.pagalguy.com";
    private static String TAG = "RequestProxy";

    private RequestProxy(){}

    public static void getDiscussionsInForums(final Context context, final HashMap<String, Long> forumIdMap){
        String BASEURL = "http://smaug.pagalguy.com";
        String[] FORUMURLS = {
                "/cat",
                "/xat-snap-cmat-others",
                "/bank-po",
                "/gmat",
                "/gre-gate-other-exams",
                "/jobs-careers",
                "/lounge"
        };
        for (final String forumUrl: FORUMURLS){
            Log.w(TAG, forumUrl);
            JsonObjectRequest discussionsJsonRequest = new JsonObjectRequest(Request.Method.GET, BASEURL + forumUrl + "?json=1", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject result) {
                            try{
                                JSONArray allDiscussions = result.getJSONObject("payload").getJSONArray("all_threads");
                                for (int i = 0; i < allDiscussions.length(); i++) {
                                    JSONObject discussion = allDiscussions.getJSONArray(i).getJSONObject(0);
                                    Long id = discussion.getLong("id");
                                    String title = discussion.getString("content");
                                    Uri url =  Uri.parse(discussion.getString("url"));
                                    List<String> pathSegments = url.getPathSegments();
                                    String urlStartsWith = pathSegments.get(0);
                                    if(forumIdMap.containsKey(urlStartsWith)){
                                        Uri dUri = context.getContentResolver().insert(
                                                PGContract.Discussions.CONTENT_ID_URI_BASE,
                                                new Discussion(id, title, url.toString(), forumIdMap.get(urlStartsWith)).toContentValues()
                                        );
                                        //Log.w(TAG, dUri.toString());
                                        RequestProxy.getPostsInDiscussion(context, url, id, forumIdMap.get(urlStartsWith));
                                    }
                                }
                            } catch (JSONException ex){
                                ex.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            volleyError.printStackTrace();
                        }
                    }
            );
            NetworkRequests.getInstance(context).addToRequestQueue(discussionsJsonRequest);
        }
    }

    public static void getPostsInDiscussion(final Context context, Uri uri, final Long discussionId, final Long forumId){
        JsonObjectRequest postsJsonRequest = new JsonObjectRequest(Request.Method.GET, BASEURL + uri + "?json=1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        try{
                            JSONArray allPosts = result.getJSONObject("payload").getJSONArray("posts");
                            for (int i = 0; i < allPosts.length(); i++) {
                                JSONObject postJson = allPosts.getJSONObject(i);
                                Post post = Post.fromJson(postJson);
                                if(post != null){
                                    post.discussion_id = discussionId;
                                    post.forum_id = forumId;
                                    Uri pUri = context.getContentResolver().insert(
                                            PGContract.Posts.CONTENT_ID_URI_BASE,
                                            post.toContentValues()
                                    );
                                    String commentsUrl = post.getCommentsUrl();
                                    if(commentsUrl != null){
                                        RequestProxy.getCommentsOnPost(context, Uri.parse(commentsUrl), post._id, discussionId, forumId);
                                    }
                                }
                            }
                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                }
        );
        NetworkRequests.getInstance(context).addToRequestQueue(postsJsonRequest);
    }

    public static void getCommentsOnPost(final Context context, Uri uri, final Long postId, final Long discussionId, final Long forumId){
        JsonObjectRequest commentsJsonRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        try{
                            JSONObject payload = result.getJSONObject("payload");
                            JSONObject pages = payload.getJSONObject("pages");
                            Iterator<String> pageNumbers = pages.keys();
                            while(pageNumbers.hasNext()){
                                String currentPage = pageNumbers.next();
                                JSONArray pageComments = pages.getJSONArray(currentPage);
                                for (int i = 0; i < pageComments.length(); i++) {
                                    JSONObject commentJson = pageComments.getJSONObject(i);
                                    Comment comment = Comment.fromJson(commentJson);
                                    if(comment != null){
                                        comment.post_id = postId;
                                        comment.discussion_id = discussionId;
                                        comment.forum_id = forumId;
                                        Uri cUri = context.getContentResolver().insert(
                                                PGContract.Comments.CONTENT_ID_URI_BASE,
                                                comment.toContentValues()
                                        );
                                    }
                                }
                            }

                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers =  new HashMap<String, String>();
                headers.put("ACCEPT", "application/json, text/javascript, */*; q=0.01");
                return headers;
            }
        };
        NetworkRequests.getInstance(context).addToRequestQueue(commentsJsonRequest);
    }
}
