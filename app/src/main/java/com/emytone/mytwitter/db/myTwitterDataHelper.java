package com.emytone.mytwitter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import twitter4j.User;

/**
 * Created by emytone on 09/09/2016.
 */
public class myTwitterDataHelper extends SQLiteOpenHelper {

    /**
     * db version
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * database name
     */
    private static final String DATABASE_NAME = "followers.db";
    private static final String FOLLOWERS_TABLE = "FOLLOWERS";

    /**
     * ID column
     */
    private static final String FOLLOWER_COL = BaseColumns._ID;

    private static final String NAME_COL = "name";

    private static final String HANDLE_COL = "handle";

    private static final String BIO_COL = "bio";

    private static final String FOLLOWER_IMG = "follower_img";

    /**
     * database creation string
     */
    private static final String DATABASE_CREATE = "CREATE TABLE " + FOLLOWERS_TABLE + " (" + FOLLOWER_COL +
            " INTEGER NOT NULL PRIMARY KEY, " + NAME_COL + " TEXT, " + HANDLE_COL +
            " TEXT, " + BIO_COL + " TEXT, " + FOLLOWER_IMG + " TEXT);";
    static myTwitterDataHelper dbHelper;

    private myTwitterDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //static public SQLiteDatabase dataBase;
//singleton pattern
    static public myTwitterDataHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new myTwitterDataHelper(context);
        }
        return dbHelper;
    }

    public ContentValues setValues(User user) {

        //prepare ContentValues to return
        ContentValues followersValues = new ContentValues();

        //get the values
        try {
            //get each value from the table
            followersValues.put(FOLLOWER_COL, user.getId());
            followersValues.put(NAME_COL, user.getName());
            followersValues.put(HANDLE_COL, user.getScreenName());
            followersValues.put(BIO_COL, user.getDescription());
            followersValues.put(FOLLOWER_IMG, user.getProfileImageURL());//instead of save url , should save bitmap byte array for offline load
            //should do service for update followers

            if (dbHelper != null)
                dbHelper.getWritableDatabase().insertOrThrow(FOLLOWERS_TABLE, null, followersValues);
        } catch (Exception te) {
            Log.e("dataHelper", te.getMessage());
        }
        //return the values
        return followersValues;
    }

    public Cursor queryFollowers() {

        return dbHelper.getReadableDatabase().query
                (FOLLOWERS_TABLE, null, null, null, null, null, null);

    }

    /*
 * onCreate executes database creation string
 */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        dbHelper = this;
    }

    /*
 * onUpgrade drops home table and executes creation string
 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FOLLOWERS_TABLE);
        //db.execSQL("VACUUM");
        onCreate(db);
    }
}
