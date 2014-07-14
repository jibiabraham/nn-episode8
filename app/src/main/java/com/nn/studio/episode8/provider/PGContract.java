package com.nn.studio.episode8.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jibi on 7/7/14.
 * Contract class for the app DB/ContentProvider
 * References and credist
 *  1. NotePad sample app provided in the Android SDK
 */
public final class PGContract {
    public static final String AUTHORITY = "com.nn.studio.episode8";

    // An empty constructor - must not be initiated
    private PGContract(){}

    // Forums table
    public static final class Forums implements BaseColumns {
        private Forums(){}

        public static final String TABLE_NAME = "forums";
        public static final String SCHEME = "content://";
        public static final String PATH_FORUMS = "/forums";
        public static final String PATH_FORUM_ID = "/forums/";
        public static final int FORUM_ID_PATH_POSITION = 1;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_FORUMS);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_FORUM_ID);
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_FORUM_ID + "/#");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nn.studio.pg.forum";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nn.studio.pg.forum";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_URL_STARTSWITH = "url_startswith";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";

        public static final String[] DEFAULT_PROJECTION = { _ID, COLUMN_NAME_TITLE, COLUMN_NAME_URL_STARTSWITH };
    }

    // Discussions table
    public static final class Discussions implements BaseColumns {
        private Discussions(){}

        public static final String TABLE_NAME = "discussions";
        public static final String SCHEME = "content://";
        public static final String PATH_DISCUSSIONS = "/discussions";
        public static final String PATH_DISCUSSION_ID = "/discussions/";
        public static final int DISCUSSION_ID_PATH_POSITION = 1;
        public static final int DISCUSSION_ID_PATH_POSITION_NESTED = 3;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_DISCUSSIONS);
        public static final Uri CONTENT_NESTED_URI = Uri.parse(SCHEME + AUTHORITY + Forums.PATH_FORUMS + "/#" + PATH_DISCUSSIONS);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_DISCUSSION_ID);
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_DISCUSSION_ID + "/#");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nn.studio.pg.discussion";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nn.studio.pg.discussion";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";
        public static final String COLUMN_NAME_FORUM_ID = "forum_id";

        public static final String[] DEFAULT_PROJECTION = { _ID, COLUMN_NAME_TITLE, COLUMN_NAME_URL };
    }

    // Posts table
    public static final class Posts implements BaseColumns {
        private Posts(){}

        public static final String TABLE_NAME = "posts";
        public static final String SCHEME = "content://";
        public static final String PATH_POSTS = "/posts";
        public static final String PATH_POST_ID = "/posts/";
        public static final int POST_ID_PATH_POSITION = 1;
        public static final int POST_ID_PATH_POSITION_NESTED = 5;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_POSTS);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_POST_ID);
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_POST_ID + "/#");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nn.studio.pg.post";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nn.studio.pg.post";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_LIKES_COUNT = "likes_count";
        public static final String COLUMN_NAME_COMMENTS_COUNT = "comments_count";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";
        public static final String COLUMN_NAME_DISCUSSION_ID = "discussion_id";
        public static final String COLUMN_NAME_FORUM_ID = "forum_id";
        public static final String COLUMN_NAME_AUTHOR_ID = "author_id";

        public static Uri getNestedUrl(Long forumId, Long discussionId){
            String url = String.format(SCHEME + AUTHORITY + Forums.PATH_FORUMS + "/%s" + Discussions.PATH_DISCUSSIONS + "/%s" + PATH_POSTS, Long.toString(forumId), Long.toString(discussionId));
            return Uri.parse(url);
        }
    }

    // Comments table
    public static final class Comments implements BaseColumns {
        private Comments(){}

        public static final String TABLE_NAME = "comments";
        public static final String SCHEME = "content://";
        public static final String PATH_COMMENTS = "/comments";
        public static final String PATH_COMMENT_ID = "/comments/";
        public static final int COMMENT_ID_PATH_POSITION = 1;
        public static final int COMMENT_ID_PATH_POSITION_NESTED = 7;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_COMMENTS);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_COMMENT_ID);
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_COMMENT_ID + "/#");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nn.studio.pg.comment";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nn.studio.pg.comment";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_LIKES_COUNT = "likes_count";
        public static final String COLUMN_NAME_COMMENTS_COUNT = "comments_count";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";
        public static final String COLUMN_NAME_POST_ID = "post_id";
        public static final String COLUMN_NAME_DISCUSSION_ID = "discussion_id";
        public static final String COLUMN_NAME_FORUM_ID = "forum_id";
        public static final String COLUMN_NAME_AUTHOR_ID = "author_id";
    }

    // Users table
    public static final class Users implements BaseColumns {
        private Users(){}

        public static final String TABLE_NAME = "users";
        public static final String SCHEME = "content://";
        public static final String PATH_USERS = "/users";
        public static final String PATH_USER_ID = "/users/";
        public static final int USER_ID_PATH_POSITION = 1;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_USERS);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_USER_ID);
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_USER_ID + "/#");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nn.studio.pg.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nn.studio.pg.user";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String COLUMN_NAME_FULLNAME = "fullname";
        public static final String COLUMN_NAME_NICK = "nick";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";
    }
}
