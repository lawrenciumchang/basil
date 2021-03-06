package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.lawrencium.basil.james.backend.registration.Registration;
import com.lawrencium.basil.james.backend.registration.model.CollectionResponseStringCollection;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Act_BudgetBuddy extends Activity implements GoogleApiClient.OnConnectionFailedListener , GoogleApiClient.ConnectionCallbacks, View.OnClickListener{
    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    private int mId = 0;


    //GOOGLE PLUS----
    private static final String TAG = "SignInTestActivity";

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    private static boolean isConnected;

    /**
     * True if the sign-in button was clicked.  When true, we know to resolve all
     * issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;
    //GOOGLE PLUS----

    private String userName;

    /*Used for device Registration*/
    private static final String PROPERTY_USER_NAME ="user_name";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private String regID;
    private Context context;
    private static final String SENDER_ID = "508206130718";
    /*Used for device Registration*/

    private boolean sampleDataAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_budget_buddy);

        //GOOGLE PLUS----
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(this);
        regID = getRegistrationId(context);
        userName = getUserName(context);
        System.out.println("Current RegID: " + regID);

        System.out.println("Current User: " + userName);
        if(userName.isEmpty()) {
            findViewById(R.id.signIn).setOnClickListener(this);
            findViewById(R.id.signIn).setVisibility(View.VISIBLE);
            findViewById(R.id.signOut).setOnClickListener(this);
            findViewById(R.id.signOut).setVisibility(View.GONE);
            findViewById(R.id.revokeAccess).setOnClickListener(this);
            findViewById(R.id.revokeAccess).setVisibility(View.GONE);
//            findViewById(R.id.friends).setVisibility(View.GONE);
        }
        else{ //userName != null
            findViewById(R.id.signIn).setOnClickListener(this);
            findViewById(R.id.signIn).setVisibility(View.GONE);
            findViewById(R.id.signOut).setOnClickListener(this);
            findViewById(R.id.signOut).setVisibility(View.VISIBLE);
            findViewById(R.id.revokeAccess).setVisibility(View.VISIBLE);
//            findViewById(R.id.friends).setVisibility(View.VISIBLE);
        }
        //GOOGLE PLUS----


        //NOTIFICATIONS----------------
        /*AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(Act_BudgetBuddy.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Act_BudgetBuddy.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alarmStartTime = Calendar.getInstance();

        alarmStartTime.set(Calendar.HOUR_OF_DAY, 21);
        alarmStartTime.set(Calendar.MINUTE, 42);
        alarmStartTime.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), getInterval(), pendingIntent);*/
        //NOTIFICATIONS----------------
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_buddy, menu);
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
            Toast.makeText(this,"What would you like to see in the settings?", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Takes the user to the Budget portion of the app.
     * The user must be logged in in order to continue to the next page.
     * @param view
     */
    public void budgetView(View view){
        if(userName.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please log in before managing your finances.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            //Toast.makeText(getApplicationContext(), "This is Evan and Annie's job!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Act_BudgetManagerMain.class);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

    /**
     * Takes the user to the Tabs portion of the app.
     * The user must be logged in in order to continue to the next page.
     * @param view
     */
    public void tabsView(View view){
        if(userName.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please log in before managing your finances.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Intent intent = new Intent(this, Act_TabsPage.class);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

    //GOOGLE PLUS---------------------------------------- from this point onwards
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        mSignInClicked = false;

        // Hide the sign in button, show the sign out buttons.
        findViewById(R.id.signIn).setVisibility(View.GONE);
        findViewById(R.id.signOut).setVisibility(View.VISIBLE);
        findViewById(R.id.revokeAccess).setVisibility(View.VISIBLE);
//        findViewById(R.id.friends).setVisibility(View.VISIBLE);

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String scopes = "oauth2:server:client_id:508206130718-gqprkkft766leju5tuac9vv3tvjol8ec.apps.googleusercontent.com:api_scope:" + Scopes.PLUS_LOGIN+" email";
                // For James' desktop use oauth2:server:client_id:508206130718-m4jc139av45c9h853padqejam69kpm0q.apps.googleusercontent.com:api_scope
                // For Evan's desktop use oauth2:server:client_id:508206130718-gqprkkft766leju5tuac9vv3tvjol8ec.apps.googleusercontent.com:api_scope
                try {
                    String code = GoogleAuthUtil.getToken(
                            context,                                              // Context context
                            Plus.AccountApi.getAccountName(mGoogleApiClient),  // String accountName
                            scopes                                          // String scope
                    );

                } catch (
                        IOException transientEx
                        )

                {
                    // network or server error, the call is expected to succeed if you try again later.
                    // Don't attempt to call again immediately - the request is likely to
                    // fail, you'll hit quotas or back-off.

                    return null;
                } catch (
                        UserRecoverableAuthException e
                        )

                {
                    // Requesting an authorization code will always throw
                    // UserRecoverableAuthException on the first call to GoogleAuthUtil.getToken
                    // because the user must consent to offline access to their data.  After
                    // consent is granted control is returned to your activity in onActivityResult
                    // and the second call to GoogleAuthUtil.getToken will succeed.
                    //startActivityForResult(e.getIntent(), AUTH_CODE_REQUEST_CODE);
                    return null;
                } catch (
                        GoogleAuthException authEx
                        )

                {
                    // Failure. The call is not expected to ever succeed so it should not be
                    // retried.

                    return null;
                } catch (
                        Exception e
                        )

                {
                    throw new RuntimeException(e);
                }

                return null;
            }
        };
        task.execute((Void) null);


        //GET USERNAME
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            if(userName.isEmpty()) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                storeUserName(context, currentPerson.getDisplayName());
                userName = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                if (regID.isEmpty()) {
                    registerInBackground(userName, email);
                }
            }
        }

        if(!isConnected) {
            isConnected = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Welcome, " + userName + "!");
            builder.setCancelable(true);
            builder.setPositiveButton("Continue", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            getFriends();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signIn && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
        else if (v.getId() == R.id.signOut  && !mGoogleApiClient.isConnecting()) {//
            if (mGoogleApiClient.isConnected()) {
                Log.e(TAG, "User signed out!");
                // Hide the sign out buttons, show the sign in button.
                findViewById(R.id.signIn).setVisibility(View.VISIBLE);
                findViewById(R.id.signOut).setVisibility(View.GONE);
                findViewById(R.id.revokeAccess).setVisibility(View.GONE);
//                findViewById(R.id.friends).setVisibility(View.GONE);
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                unRegisterInBackground(userName, regID);
                userName = "";
                storeUserName(context, userName);
                regID ="";
                storeRegistrationId(context, regID);

                isConnected = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You have been successfully signed out.");
                builder.setCancelable(true);
                builder.setPositiveButton("Okay", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this, "User was not logged in!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.revokeAccess  && !mGoogleApiClient.isConnecting()){
            if (mGoogleApiClient.isConnected()) {

                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.e(TAG, "User access revoked!");
                                mGoogleApiClient.disconnect();
                                mGoogleApiClient.connect();
                                unRegisterInBackground(userName, regID);

                                userName = "";
                                storeUserName(context, userName);
                                regID ="";
                                storeRegistrationId(context, regID);

                                findViewById(R.id.signIn).setVisibility(View.VISIBLE);
                                findViewById(R.id.signOut).setVisibility(View.GONE);
                                findViewById(R.id.revokeAccess).setVisibility(View.GONE);
//                                findViewById(R.id.friends).setVisibility(View.GONE);
                            }
                        });

                isConnected = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You have been successfully signed out of Basil's server.");
                builder.setCancelable(true);
                builder.setPositiveButton("Okay", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this, "User was not logged in!", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            if (mSignInClicked && result.hasResolution()) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                try {
                    result.startResolutionForResult(this, RC_SIGN_IN);
                    mIntentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        }

    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }

    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private String getUserName(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String user_name = prefs.getString(PROPERTY_USER_NAME, "");
        if (user_name.isEmpty()) {
            Log.i(TAG, "userName not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return user_name;
    }

    private void storeUserName(Context context, String username) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving userName on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER_NAME, username);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(Act_BudgetBuddy.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground(final String username, final String email) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                if (regService == null) {
                    Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl("https://eternal-ruler-92119.appspot.com/_ah/api/");
                    regService = builder.build();
                }

                String msg = "";
                try {

                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regId = gcm.register(SENDER_ID);

                    Logger.getLogger("REGISTRATION").log(Level.INFO, "Device registered, registration ID=" + regId+"\n"+"User Name is "+username);
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    regService.register(regId, username, email).execute();
                    storeRegistrationId(context, regId);
                    regID = getRegistrationId(context);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
            }
        }.execute(null, null, null);

    }

    private void unRegisterInBackground(final String username, final String regid) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                if (regService == null) {
                    Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl("https://eternal-ruler-92119.appspot.com/_ah/api/");
                    regService = builder.build();
                }

                String msg = "";
                try {
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    regService.unregister(regid).execute();

                    Logger.getLogger("REGISTRATION").log(Level.INFO, "Device unregistered, registration ID=" + regid+"\n"+"User Name is "+username);

                } catch (IOException ex) {
                    ex.printStackTrace();
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
            }
        }.execute(null, null, null);

    }


    // NOTIFICATION STUFF STARTS HERE -------------------------------------------------------------------

    /**
     * Used to set an interval for how often notifications repeat.
     * @return  Time in ms of repeat interval
     */
    private int getInterval(){
        int days = 1;
        int hours = 24;
        int minutes = 60;
        int seconds = 60;
        int milliseconds = 1000;
//        int repeatMS = days * hours * minutes * seconds * milliseconds;
        int repeatMS = minutes * seconds * milliseconds;
        return repeatMS;
    }

    /**
     * Called when notification button clicked on home page. Used for testing.
     * @param view  Once notification has been clicked, must send user to Act_BudgetManagerMain,
     *              and preserve parent navigation functionality.
     */
    public void notify(View view){

//        int notificationId = new Random().nextInt();

        // Sets up the Snooze and Dismiss action buttons that will appear in the
        // big view of the notification.

        // REMEMBER TO CHANGE CLASS LATER
//        Intent declineIntent = new Intent(this, Act_TabsPage.class);
//        PendingIntent piDecline = PendingIntent.getActivity(this, 0, declineIntent, 0);
        PendingIntent piDecline = NotificationActivity.getDeclineIntent(mId, context);

//        Intent confirmIntent = new Intent(this, Act_TabsPage.class);
//        PendingIntent piConfirm = PendingIntent.getActivity(this, 0, confirmIntent, 0);
        PendingIntent piConfirm = NotificationActivity.getConfirmIntent(mId, context);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.basil_icon)
                        .setContentTitle("Your Budget Buddy")
//                        .setContentText("You have overspent in eating out this week. Better hit the gym!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("You have overspent in eating out this week. Better hit the gym!"))
                        .addAction(R.mipmap.basil_icon, getString(R.string.decline), piDecline)
                        .addAction(R.mipmap.basil_icon, getString(R.string.confirm), piConfirm)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Act_BudgetBuddy.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Act_BudgetManagerMain.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        mNotificationManager.notify(mId, mBuilder.build());

        mId++;
    }


    // TESTING FOR FRIENDS STARTS HERE -------------------------------------------------------------------

    /**
     * Sends user to list of friends page.
     * @param view
     */
    public void friends(View view) {

        Intent intent = new Intent(this, Act_FriendsPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);

    }

    void getFriends(){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                if (regService == null) {
                    Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl("https://eternal-ruler-92119.appspot.com/_ah/api/");
                    regService = builder.build();
                }

                String msg = "";
                try {
                    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(getApplicationContext());
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    CollectionResponseStringCollection registeredUsers = regService.listFriends().execute();
                    List<List<String>> test = registeredUsers.getItems();

                    ListIterator<List<String>> userIterator = test.listIterator();

//                    friendsList = new ArrayList<ArrayList<String>>();
//
//                    ArrayList<String> nextFriend;
                    List<String> nextUser;
//
//                    while (userIterator.hasNext()) {
//                        tempArr = new ArrayList<String>(userIterator.next());
//                        friendsList.add(tempArr);
//                    }

                    ContentValues values = new ContentValues();
                    while(userIterator.hasNext()) {
                        nextUser = userIterator.next();
                        try {
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, nextUser.get(0));
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL, nextUser.get(1));
                            long newRowId = db.insertOrThrow(
                                    FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                                    FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                                    values);
                            Log.i(nextUser.get(0), nextUser.get(1));
                        } catch (SQLiteConstraintException e) {
                            // insert will throw an exception if one of the users retrieved is already in the friends list.
                            // When this happens do nothing, the loop will continue on its own.
                            Log.i("SQLiteConstraint", e.getMessage());
                        }
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    msg = "Error: " + ex.getMessage();
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
            }
        }.execute(null, null, null);

    }

    /**
     * makeSampleData - Fills database with sample data to show off features
     * @param view
     */
    public void makeSampleData(View view) {
        if(!sampleDataAdded) {
            SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Budget.newCategory(db, "$plurge", "600");
            Budget.newCategory(db, "Groceries", "300");
            Budget.newCategory(db, "Recreation", "100");
            Budget.newCategory(db, "Rent and Utilities", "1000");
            Budget.newCategory(db, "Shopping", "400");
            Budget.newCategory(db, "Socializing", "300");
            Budget.newCategory(db, "Uncategorized", "200");

            // Date format: "yyyy/MM/dd k:mm:ss"
            Budget.newTransaction(db, "HEB", "52.73", "Groceries", "2015/04/04 5:55:55");
            Budget.newTransaction(db, "Apple", "3", "Groceries", "2015/04/07 5:55:55");
            Budget.newTransaction(db, "Peanut Butter", "4.50", "Groceries", "2015/04/08 5:55:55");
            Budget.newTransaction(db, "Bread", "2.99", "Groceries", "2015/04/08 5:55:55");
            Budget.newTransaction(db, "Toilet Paper", "6", "Groceries", "2015/04/19 5:55:55");
            Budget.newTransaction(db, "HEB", "65.39", "Groceries", "2015/05/10 5:55:55");
            Budget.newTransaction(db, "Tooth Brush", "10.12", "Groceries", "2015/05/02 5:55:55");
            Budget.newTransaction(db, "Sewing Kit", "1.50", "Groceries", "2015/05/03 5:55:55");
            Budget.newTransaction(db, "Powerade", "3.99", "Groceries", "2015/05/03 5:55:55");
            Budget.newTransaction(db, "AA Batteries", "8.55", "Groceries", "2015/05/05 5:55:55");

            Budget.newTransaction(db, "April Rent", "850", "Rent and Utilities", "2015/04/01 5:55:55");
            Budget.newTransaction(db, "March Utilities", "53.24", "Rent and Utilities", "2015/04/10 5:55:55");
            Budget.newTransaction(db, "March Cable", "76.10", "Rent and Utilities", "2015/04/10 5:55:55");
            Budget.newTransaction(db, "May Rent", "850", "Rent and Utilities", "2015/05/01 5:55:55");
            Budget.newTransaction(db, "April Utilities", "61.77", "Rent and Utilities", "2015/05/10 5:55:55");
            Budget.newTransaction(db, "April Cable", "76.10", "Rent and Utilities", "2015/05/10 5:55:55");

            Budget.newTransaction(db, "Gym Membership", "30", "Recreation", "2015/04/09 5:55:55");
            Budget.newTransaction(db, "Ping Pong Paddle", "37.21", "Recreation", "2015/04/21 5:55:55");
            Budget.newTransaction(db, "Ping Pong Balls", "12.78", "Recreation", "2015/04/21 5:55:55");
            Budget.newTransaction(db, "Slackline", "10.36", "Recreation", "2015/05/03 5:55:55");
            Budget.newTransaction(db, "Gym Membership", "30", "Recreation", "2015/05/09 5:55:55");

            Budget.newTransaction(db, "Boots", "250", "Shopping", "2015/04/11 5:55:55");
            Budget.newTransaction(db, "Banana Republic", "70", "Shopping", "2015/04/25 5:55:55");
            Budget.newTransaction(db, "Nordstrom's Rack", "400", "Shopping", "2015/05/01 5:55:55");
            Budget.newTransaction(db, "Dillard's", "150", "Shopping", "2015/05/08 5:55:55");

            Budget.newTransaction(db, "Top Golf", "15", "Socializing", "2015/04/09 5:55:55");
            Budget.newTransaction(db, "Dancing", "15", "Socializing", "2015/04/16 5:55:55");
            Budget.newTransaction(db, "Bowling", "12", "Socializing", "2015/05/01 5:55:55");
            Budget.newTransaction(db, "Bubblies", "50", "Socializing", "2015/05/09 5:55:55");
            Budget.newTransaction(db, "Mixers", "30", "Socializing", "2015/05/09 5:55:55");

            Budget.newTransaction(db, "Alaska", "599.99", "$plurge", "2015/05/09 5:55:55");
            sampleDataAdded = true;

            CharSequence text = "Sample data populated!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            ((Button)findViewById(R.id.sampleData)).setVisibility(View.GONE);
        }
        else {
            CharSequence text = "Sample data already added!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            ((Button)findViewById(R.id.sampleData)).setVisibility(View.GONE);
        }
    }
}
