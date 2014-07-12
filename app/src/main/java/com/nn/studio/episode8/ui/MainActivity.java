package com.nn.studio.episode8.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.nn.studio.episode8.R;
import com.nn.studio.episode8.provider.PGContract;
import com.nn.studio.episode8.ui.adapters.FragmentVerticalSectionAdapter;
import com.nn.studio.episode8.ui.adapters.VerticalPagerAdapter;
import com.nn.studio.episode8.ui.views.VerticalViewPager;

/**
 * Created by jibi on 12/7/14.
 */
public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int URL_LOADER = 0;
    private FragmentVerticalSectionAdapter mAdapter;
    private final String DISCUSSION_URI = "discussion_uri";
    private VerticalViewPager vvp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle args = new Bundle();
        args.putString(DISCUSSION_URI, PGContract.Discussions.CONTENT_URI.toString());
        getSupportLoaderManager().initLoader(URL_LOADER, args, this);

        Account dummyAccount = new Account("jibi.pg", "com.pagalguy");
        AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(dummyAccount, "pwd", Bundle.EMPTY);

        ContentResolver.setIsSyncable(dummyAccount, PGContract.AUTHORITY, 1);
        ContentResolver.requestSync(dummyAccount, PGContract.AUTHORITY, Bundle.EMPTY);

        vvp = (VerticalViewPager) findViewById(R.id.vertical_section);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(args != null){
            String dUrl = args.containsKey(DISCUSSION_URI) ? args.getString(DISCUSSION_URI) : null;
            if(dUrl != null){
                return new CursorLoader(getApplicationContext(), Uri.parse(dUrl), null, null, null, null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new FragmentVerticalSectionAdapter(getApplicationContext(), getSupportFragmentManager(), data);
        vvp.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
