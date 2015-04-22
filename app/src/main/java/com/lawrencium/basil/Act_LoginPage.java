package com.lawrencium.basil;




// *****************************************************************
// this class is for Google+ login
// currently not implemented
// *****************************************************************

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class Act_LoginPage extends Activity implements GoogleApiClient.OnConnectionFailedListener , GoogleApiClient.ConnectionCallbacks, View.OnClickListener, ResultCallback<People.LoadPeopleResult> {

    private static final String TAG = "SignInTestActivity";

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    /**
     * True if the sign-in button was clicked.  When true, we know to resolve all
     * issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login_page);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
//        mIntentInProgress = false;
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    }

    protected void onStart() {
        Toast.makeText(this, "START!", Toast.LENGTH_SHORT).show();
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "User is connected!", Toast.LENGTH_SHORT).show();
//        Bundle appActivities = new Bundle();
//        appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES,
//                "<APP-ACTIVITY1> <APP-ACTIVITY2>");
        final Context context = this.getApplicationContext();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String scopes = "oauth2:server:client_id:508206130718-m4jc139av45c9h853padqejam69kpm0q.apps.googleusercontent.com:api_scope:" + Scopes.PLUS_LOGIN;

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

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            //String personPhoto = currentPerson.getImage();
            String personGooglePlusProfile = currentPerson.getUrl();
            Toast.makeText(this, "User is: "+personName, Toast.LENGTH_SHORT).show();
        }
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
        if (v.getId() == R.id.sign_out_button) {
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                Toast.makeText(this, "User is signed out!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "User was not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOutClick(View v) {
//        if (v.getId() == R.id.sign_out_button) {
//            if (mGoogleApiClient.isConnected()) {
//                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                mGoogleApiClient.disconnect();
//                mGoogleApiClient.connect();
//                Toast.makeText(this, "User is signed out!", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();
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
//        if (!mIntentInProgress && connectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
//                        RC_SIGN_IN, null, 0, 0, 0);
//            } catch (IntentSender.SendIntentException e) {
//                // The intent was canceled before it was sent.  Return to the default
//                // state and attempt to connect to get an updated ConnectionResult.
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
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
//        if (requestCode == RC_SIGN_IN) {
//            mIntentInProgress = false;
//
//            if (!mGoogleApiClient.isConnecting()) {
//                mGoogleApiClient.connect();
//            }
//        }
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