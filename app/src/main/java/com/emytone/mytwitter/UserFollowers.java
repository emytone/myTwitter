package com.emytone.mytwitter;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.emytone.mytwitter.adapters.FollowersAdapter;
import com.emytone.mytwitter.db.myTwitterDataHelper;

public class UserFollowers extends Activity {

    /**
     * main view for the home timeline
     */
    private ListView followers;
    /**
     * database helper for update data
     */
    private myTwitterDataHelper dbHelper;
    /**
     * update database
     */
    private SQLiteDatabase db;
    /**
     * cursor for handling data
     */
    private Cursor followersCursor;
    /**
     * adapter for mapping data
     */
    private FollowersAdapter followersAdapter;

    private String LOG_TAG = "UserFollowers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_followers);

        try {

            followers = (ListView) findViewById(R.id.followersList);
            //get the database
            dbHelper = myTwitterDataHelper.getInstance(this);
            //query the database, most recent tweets first
            followersCursor = dbHelper.queryFollowers();
            //instantiate adapter
            followersAdapter = new FollowersAdapter(this, followersCursor);
            followers.setAdapter(followersAdapter);
        } catch (Exception te) {
            Log.e(LOG_TAG, "Failed to fetch followers: " + te.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_followers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
