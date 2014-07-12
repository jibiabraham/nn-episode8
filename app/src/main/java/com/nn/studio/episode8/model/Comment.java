package com.nn.studio.episode8.model;

import android.content.ContentValues;

import com.nn.studio.episode8.provider.PGContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jibi on 10/7/14.
 */
public class Comment {
    public Long _id;
    public String content;
    public String author;
    public Integer likesCount;
    public Long created;
    public Long modified;
    public Long post_id;
    public Long discussion_id;
    public Long forum_id;
    public Long author_id;

    public Comment(Long _id, String content, String author, Integer likesCount, Integer commentsCount, Long created, Long modified, Long post_id, Long discussion_id, Long forum_id, Long author_id) {
        this._id = _id;
        this.content = content;
        this.author = author;
        this.likesCount = likesCount;
        this.created = created;
        this.modified = modified;
        this.post_id = post_id;
        this.discussion_id = discussion_id;
        this.forum_id = forum_id;
        this.author_id = author_id;
    }

    public Comment(Long id, String content, String author, Integer likesCount, Long authorId) {
        this._id = id;
        this.content = content;
        this.author = author;
        this.likesCount = likesCount;
        this.author_id = authorId;
    }

    public static Comment fromJson(JSONObject comment){
        try{
            Long id = comment.getLong("id");
            String content = comment.getString("content");
            String author = comment.getJSONObject("author").getString("nick");
            Integer likesCount = comment.has("lkc") ? comment.getInt("lkc") : 0;
            Long authorId = comment.getJSONObject("author").getLong("id");
            return new Comment(id, content, author, likesCount, authorId);
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
        cv.put(PGContract.Comments.COLUMN_NAME_CONTENT, content);
        cv.put(PGContract.Comments.COLUMN_NAME_AUTHOR, author);
        cv.put(PGContract.Comments.COLUMN_NAME_LIKES_COUNT, likesCount);
        cv.put(PGContract.Comments.COLUMN_NAME_CREATE_DATE, created);
        cv.put(PGContract.Comments.COLUMN_NAME_MODIFICATION_DATE, modified);
        cv.put(PGContract.Comments.COLUMN_NAME_POST_ID, post_id);
        cv.put(PGContract.Comments.COLUMN_NAME_DISCUSSION_ID, discussion_id);
        cv.put(PGContract.Comments.COLUMN_NAME_FORUM_ID, forum_id);
        cv.put(PGContract.Comments.COLUMN_NAME_AUTHOR_ID, author_id);
        return cv;
    }
}
