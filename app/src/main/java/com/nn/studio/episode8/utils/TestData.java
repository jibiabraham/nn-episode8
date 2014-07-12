package com.nn.studio.episode8.utils;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nn.studio.episode8.model.Forum;
import com.nn.studio.episode8.provider.PGContract;

import java.util.HashMap;

/**
 * Created by jibi on 8/7/14.
 */
public final class TestData {
    private static final String TAG = "TestData";
    private TestData(){}

    public static void insertTestData(SQLiteDatabase db){
        final String[] FORUMS = { "CAT", "XAT,SNAP,CMAT", "Bank PO", "GMAT", "GRE,GATE", "Jobs & Careers", "Lounge" };
        final HashMap<String, String> urlMap = new HashMap<String, String>(){{
            put(FORUMS[0], "cat");
            put(FORUMS[1], "xat-snap-cmat-others");
            put(FORUMS[2], "bank-po");
            put(FORUMS[3], "gmat");
            put(FORUMS[4], "gre-gate-other-exams");
            put(FORUMS[5], "jobs-careers");
            put(FORUMS[6], "lounge");
        }};
        for (String title: FORUMS){
            long id = db.insert(PGContract.Forums.TABLE_NAME, null, new Forum(title, urlMap.get(title),null, null).toContentValues());
            Log.w(TAG, title + "::" + Long.toString(id));
        }
    }
}
