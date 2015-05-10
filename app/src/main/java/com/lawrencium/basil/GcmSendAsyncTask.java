package com.lawrencium.basil;

import android.content.Context;
import android.os.AsyncTask;

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
    private Context context;
    private String username;
    private String email;
    private String tabMsg;


    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "508206130718";

    public GcmSendAsyncTask(Context context, String username, String email, String tabMsg) {
        this.username = username;
        this.context = context;
        this.email = email;
        this.tabMsg = tabMsg;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (msg == null) {
            Messaging.Builder builderMsg = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://eternal-ruler-92119.appspot.com/_ah/api/");
            msg = builderMsg.build();
        }

        String message = "";
        try {

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            msg.messagingEndpoint().sendMessage(tabMsg, email).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            message = "Error: " + ex.getMessage();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Logger.getLogger("SENTOUT").log(Level.INFO, msg);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
