package com.emytone.mytwitter;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.emytone.mytwitter.db.myTwitterDataHelper;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class LoginActivity extends Activity implements OnClickListener {

    /**
     * developer account key for this app
     */
    public final static String TWIT_KEY = "1LWoemQutOLN90r27174DxAwa";
    /**
     * developer secret for the app
     */
    public final static String TWIT_SECRET = "GJA7QpZufuMi2hqgdz444Mn6sUab90oo4dHrVMKAep66kZ9qO4";
    /**
     * app url
     */
    public final static String TWIT_URL = "emytone-android:///";

    /**
     * Twitter instance
     */
    private Twitter emyTwitter;
    /**
     * request token for accessing user account
     */
    private RequestToken emyRequestToken;
    /**
     * shared preferences to store user details
     */
    private SharedPreferences emyPrefs;

    private myTwitterDataHelper dbHelper;
    //for error logging
    private String LOG_TAG = "LoginActivity";//alter for your Activity name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the preferences for the app
        emyPrefs = getSharedPreferences("emyPrefs", 0);

        //find out if the user preferences are set
        if (emyPrefs.getString("user_token", null) == null) {
            new RetrieveFeedTask().execute();
            //no user preferences so prompt to sign in
            setContentView(R.layout.activity_login);
            //setup button for click listener
            Button signIn = (Button) findViewById(R.id.signin);
            signIn.setOnClickListener(LoginActivity.this);


        } else {
            //user preferences are set - get timeline
            //status load

            //setupTimeline();
            startFollowersActivity();
        }
    }

    public void setupTimeline() {
        Log.e(LOG_TAG, "setting up timeline");

        dbHelper = myTwitterDataHelper.getInstance(this);

        try {
            //  emyTwitter = new TwitterFactory().getInstance();
            // final JSONArray twitterFriendsIDsJsonArray = new JSONArray();
            IDs ids = emyTwitter.getFollowersIDs(-1);// ids
            // for (long id : ids.getIDs()) {
            do {
                for (long id : ids.getIDs()) {
                    Log.e(LOG_TAG, "setting up timeline");
                    dbHelper.setValues(emyTwitter.showUser(id));

                }
            } while (ids.hasNext());

        } catch (Exception e) {
            e.printStackTrace();
        }
        startFollowersActivity();
    }

    private void startFollowersActivity() {
        Intent userfollower = new Intent(this, UserFollowers.class);

        startActivity(userfollower);
    }

    /*
 * onNewIntent fires when user returns from Twitter authentication Web page
 */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //get the retrieved data
        Uri twitURI = intent.getData();
        //make sure the url is correct
        if (twitURI != null && twitURI.toString().startsWith(TWIT_URL)) {
            //is verifcation - get the returned data
            String oaVerifier = twitURI.getQueryParameter("oauth_verifier");
            new RetrieveAuthData().execute(oaVerifier);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        return true;
    }

    /**
     * Click listener handles sign in and tweet button presses
     */
    public void onClick(View v) {
        //find view
        Log.e(LOG_TAG, "sign in");
        switch (v.getId()) {
            //sign in button pressed
            case R.id.signin:
                Log.e(LOG_TAG, "sign in");
                //take user to twitter authentication web page to allow app access to their twitter account
                String authURL = emyRequestToken.getAuthenticationURL();
                Log.v(LOG_TAG, authURL);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL)));
                break;
            //other listeners here

            default:
                break;
        }
    }

    class RetrieveAuthData extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... verifier) {


            Log.e(LOG_TAG, "verifier " + verifier[0]);
            //attempt to retrieve access token
            try {
                //try to get an access token using the returned data from the verification page
                AccessToken accToken = emyTwitter.getOAuthAccessToken(emyRequestToken, verifier[0]);
                Log.e(LOG_TAG, "acctoken ");
                //add the token and secret to shared prefs for future reference
                emyPrefs.edit()
                        .putString("user_token", accToken.getToken())
                        .putString("user_secret", accToken.getTokenSecret())
                        .apply();
                Log.e(LOG_TAG, "after token ");
                //display the timeline
                setupTimeline();
            } catch (TwitterException te) {
                Log.e(LOG_TAG, "Failed to get access token: " + te.getMessage());
            }
            return true;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

        private Exception exception;

        protected Boolean doInBackground(String... urls) {


            //get a twitter instance for authentication
            emyTwitter = new TwitterFactory().getInstance();

            //pass developer key and secret
            emyTwitter.setOAuthConsumer(TWIT_KEY, TWIT_SECRET);

            //try to get request token
            try {
                //get authentication request token
                emyRequestToken = emyTwitter.getOAuthRequestToken(TWIT_URL);
                Log.e(LOG_TAG, "TE " + "request succ");
            } catch (TwitterException te) {
                Log.e(LOG_TAG, "TE " + te.getMessage());
            }
            return true;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}
