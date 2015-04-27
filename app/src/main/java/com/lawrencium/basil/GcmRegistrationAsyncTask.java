package com.lawrencium.basil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.lawrencium.basil.james.backend.registration.Registration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by James on 4/22/2015.
 */
class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private String registrationID;
    private Context context;
    private String username;
    private String email;


    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "508206130718";

    public GcmRegistrationAsyncTask(Context context, String username, String email) {
        this.username = username;
        this.context = context;
        this.email = email;
    }

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
//            if(regService.)
//            msg = "Device registered, registration ID=" + regId+"/n"+"User Name is "+username;

            Logger.getLogger("REGISTRATION").log(Level.INFO, "Device registered, registration ID=" + regId+"/n"+"User Name is "+username);
            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            regService.register(regId, username, email).execute();
            registrationID = regId;


        } catch (IOException ex) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegistrationID() {
        return registrationID;
    }
}
