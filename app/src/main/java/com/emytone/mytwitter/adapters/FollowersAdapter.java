package com.emytone.mytwitter.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.emytone.mytwitter.R;

/**
 * Created by emytone on 09/09/2016.
 */

public class FollowersAdapter extends SimpleCursorAdapter {

    /**
     * twitter developer key
     */
    public final static String TWIT_KEY = "your key";//alter
    /**
     * twitter developer secret
     */
    public final static String TWIT_SECRET = "your secret";//alter

    /**
     * strings representing database column names to map to views
     */
    static final String[] from = {"name", "handle",
            "bio", "follower_img"};
    /**
     * view item IDs for mapping database record values to
     */
    static final int[] to = {R.id.name, R.id.handle,
            R.id.bio, R.id.bio};

    private String LOG_TAG = "FollowersAdapter";

    public FollowersAdapter(Context context, Cursor c) {
        super(context, R.layout.follower, c, from, to);
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {
        super.bindView(row, context, cursor);

        try {
           /* //get profile image
            URL profileURL =
                    new URL(cursor.getString(cursor.getColumnIndex("follower_img")));

            //set the image in the view for the current tweet
            ImageView profPic = (ImageView)row.findViewById(R.id.userImg);
            profPic.setImageDrawable(Drawable.createFromStream
                    ((InputStream) profileURL.getContent(), ""));*/
        } catch (Exception te) {
            Log.e(LOG_TAG, te.getMessage());
        }
    }
}

