package com.lawrencium.basil;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.lawrencium.basil.james.backend.messaging.Messaging;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by James on 4/22/2015.
 */
class GcmSendAsyncTask extends AsyncTask<Void, Void, String> {
    private static Messaging msg = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    private String username;
    private String email;


    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "508206130718";

    public GcmSendAsyncTask(Context context, String username) {
        this.username = username;
        this.context = context;
        this.email = email;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (msg == null) {
            //Used for local host
//            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
//                    new AndroidJsonFactory(), null)
//                    // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
//                    // otherwise they can be skipped
//                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                        @Override
//                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
//                                throws IOException {
//                            abstractGoogleClientRequest.setDisableGZipContent(true);
//                        }
//                    });
            // end of optional local run code

            Messaging.Builder builderMsg = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://eternal-ruler-92119.appspot.com/_ah/api/");
            msg = builderMsg.build();

        }

        String message = "";
        try {

//            if (gcm == null) {
//                gcm = GoogleCloudMessaging.getInstance(context);
//            }
//            String regId = gcm.register(SENDER_ID);
//            msg = "Device registered, registration ID=" + regId+"/n"+"User Name is "+username;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
//            regService.listDevices()
            msg.messagingEndpoint().sendMessage("Hi there. I am Working "+username).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            message = "Error: " + ex.getMessage();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Logger.getLogger("SENTOUT").log(Level.INFO, msg);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
