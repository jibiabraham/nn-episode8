package com.nn.studio.episode8.model;

import android.content.ContentValues;
import android.net.Uri;

import com.nn.studio.episode8.provider.PGContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jibi on 10/7/14.
 */
public class Post {
    public Long _id;
    public String content;
    public String author;
    public Integer likesCount;
    public Integer commentsCount;
    public Long created;
    public Long modified;
    public Long discussion_id;
    public Long forum_id;
    public Long author_id;

    public final String AUTHORITY = "smaug.pagalguy.com";
    public final Integer DEFAULT_COMMENTS_PAGINATION_SIZE = 10;

    public Post(Long _id, String content, String author, Integer likesCount, Integer commentsCount, Long created, Long modified, Long discussion_id, Long forum_id, Long author_id) {
        this._id = _id;
        this.content = content;
        this.author = author;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.created = created;
        this.modified = modified;
        this.discussion_id = discussion_id;
        this.forum_id = forum_id;
        this.author_id = author_id;
    }

    public Post(Long id, String content, String author, Integer commentsCount, Integer likesCount, Long authorId) {
        this._id = id;
        this.content = content;
        this.author = author;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
        this.author_id = authorId;
    }

    public static Post fromJson(JSONObject post){
        try{
            Long id = post.getLong("id");
            String content = post.getString("content");
            String author = post.getJSONObject("author").getString("nick");
            Integer commentsCount = post.has("cmc") ? post.getInt("cmc") : 0;
            Integer likesCount = post.has("lkc") ? post.getInt("lkc") : 0;
            Long authorId = post.getJSONObject("author").getLong("id");
            return new Post(id, content, author, commentsCount, likesCount, authorId);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        Long now = System.currentTimeMillis();
        if(created == null){
            created = now;
        }
        if(modified == null){
            modified = now;
        }
        if(_id != null){
            cv.put(PGContract.Posts._ID, _id);
        }
        cv.put(PGContract.Posts.COLUMN_NAME_CONTENT, content);
        cv.put(PGContract.Posts.COLUMN_NAME_AUTHOR, author);
        cv.put(PGContract.Posts.COLUMN_NAME_LIKES_COUNT, likesCount);
        cv.put(PGContract.Posts.COLUMN_NAME_COMMENTS_COUNT, commentsCount);
        cv.put(PGContract.Posts.COLUMN_NAME_CREATE_DATE, created);
        cv.put(PGContract.Posts.COLUMN_NAME_MODIFICATION_DATE, modified);
        cv.put(PGContract.Posts.COLUMN_NAME_DISCUSSION_ID, discussion_id);
        cv.put(PGContract.Posts.COLUMN_NAME_FORUM_ID, forum_id);
        cv.put(PGContract.Posts.COLUMN_NAME_AUTHOR_ID, author_id);
        return cv;
    }

    public String getCommentsUrl(){
        if(_id == null)
            return null;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority(AUTHORITY).appendPath("comments").appendPath(Long.toString(_id)).appendQueryParameter("pages", "1,2,3,4,5");
        return builder.build().toString();
    }
}
