package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.IOException;

public class Act_BudgetBuddy extends Activity implements GoogleApiClient.OnConnectionFailedListener , GoogleApiClient.ConnectionCallbacks, View.OnClickListener, ResultCallback<People.LoadPeopleResult> {
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

//    SharedPreferences pref = getApplicationContext().getSharedPreferences("RegInfo", MODE_PRIVATE);
//    SharedPreferences.Editor editor = pref.edit();
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

    private static String userName;

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

        System.out.println("Current User: " + userName);
        if(userName == null) {
            findViewById(R.id.signIn).setOnClickListener(this);
//          findViewById(R.id.signIn).setVisibility(View.VISIBLE);
            findViewById(R.id.signOut).setOnClickListener(this);
            findViewById(R.id.signOut).setVisibility(View.INVISIBLE);
        }
        else{ //userName != null
            findViewById(R.id.signIn).setOnClickListener(this);
            findViewById(R.id.signIn).setVisibility(View.INVISIBLE);
            findViewById(R.id.signOut).setOnClickListener(this);
            findViewById(R.id.signOut).setVisibility(View.VISIBLE);

        }
        //GOOGLE PLUS----






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

    @Override
    public void onBackPressed() {
        //leave empty
    }



    public void budgetView(View view){
        if(userName == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please log in before managing your finances.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            Toast.makeText(getApplicationContext(), "This is Evan and Annie's job!", Toast.LENGTH_SHORT).show();
        }
    }

    public void tabsView(View view){
        if(userName == null){
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
//        Toast.makeText(this, "START!", Toast.LENGTH_SHORT).show();
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    protected void onStop() {
//        Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
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
        findViewById(R.id.signIn).setVisibility(View.INVISIBLE);
        findViewById(R.id.signOut).setVisibility(View.VISIBLE);

        final Context context = this.getApplicationContext();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String scopes = "oauth2:server:client_id:508206130718-m4jc139av45c9h853padqejam69kpm0q.apps.googleusercontent.com:api_scope:" + Scopes.PLUS_LOGIN+" email";

                try {
                    String code = GoogleAuthUtil.getToken(
                            context,                                              // Context context
                            Plus.AccountApi.getAccountName(mGoogleApiClient),  // String accountName
                            scopes//,                                            // String scope
//                    appActivities                                      // Bundle bundle
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
            if(userName == null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                userName = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

//            String personPhoto = currentPerson.getImage();
//            String personGooglePlusProfile = currentPerson.getUrl();


                new GcmRegistrationAsyncTask(this, userName, email).execute();
            }
        }

        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
        if(!isConnected) {
            isConnected = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Welcome, " + userName + "!");
            builder.setCancelable(true);
            builder.setPositiveButton("Continue", null);
            AlertDialog dialog = builder.create();
            dialog.show();
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
        else if (v.getId() == R.id.signOut && !mGoogleApiClient.isConnecting()) {
            if (mGoogleApiClient.isConnected()) {
                // Hide the sign out buttons, show the sign in button.
                findViewById(R.id.signIn).setVisibility(View.VISIBLE);
                findViewById(R.id.signOut).setVisibility(View.INVISIBLE);
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
//                Toast.makeText(this, "User is signed out!", Toast.LENGTH_SHORT).show();
                userName = null;


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
    }



    @Override
    public void onConnectionFailed(ConnectionResult result) {
//        Toast.makeText(this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }
    }
}
