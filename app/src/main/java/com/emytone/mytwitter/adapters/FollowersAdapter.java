package com.emytone.mytwitter.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.emytone.mytwitter.R;

import java.io.InputStream;
import java.net.URL;

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
            R.id.bio, R.id.userImg};

    private String LOG_TAG = "FollowersAdapter";

    public FollowersAdapter(Context context, Cursor c) {
        super(context, R.layout.follower, c, from, to, 0);
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xfffff0f0;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 80, 80);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {
        super.bindView(row, context, cursor);

        String imagePath = cursor.getString(cursor.getColumnIndex("follower_img"));
        String bio = cursor.getString(cursor.getColumnIndex("bio"));
        TextView bioTextView = (TextView) row.findViewById(R.id.bio);
        TextView handleTextView = (TextView) row.findViewById(R.id.handle);
        handleTextView.setText("@" + cursor.getString(cursor.getColumnIndex("handle")));
        if (bio == null)
            bioTextView.setVisibility(View.GONE);
        else//for scrolling and view reuse
            bioTextView.setVisibility(View.VISIBLE);
        ImageView profPic = (ImageView) row.findViewById(R.id.userImg);
        // The Very Basic
        new AsyncTask<Object, Void, Bitmap>() {
            ImageView profPic;

            protected void onPreExecute() {
                // Pre Code
            }

            protected Bitmap doInBackground(Object... param) {
                // Background Code
                try {
                    URL profileURL =
                            new URL((String) param[0]);

                    profPic = (ImageView) param[1];


                    Bitmap bm = BitmapFactory.decodeStream((InputStream) profileURL.getContent());

                    return bm;
                    /*return Drawable.createFromStream
                            ((InputStream) profileURL.getContent(), "");*/
                } catch (Exception te) {
                    Log.e(LOG_TAG, te.getMessage());
                }
                return null;
            }

            protected void onPostExecute(Bitmap img) {
                // Post Code
                if (img != null) {
                    // Bitmap resized = Bitmap.createScaledBitmap(img, 100, 100, true);
                    //Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
                    profPic.setImageBitmap(img);
                }
            }
        }.execute(imagePath, profPic);
        //get profile image

    }
}

