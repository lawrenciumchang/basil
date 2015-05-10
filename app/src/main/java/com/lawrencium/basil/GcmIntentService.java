package com.lawrencium.basil;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by James on 4/22/2015.
 */
public class GcmIntentService extends IntentService {

    private int notID = 0;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                showToast(extras.getString("message"));

                String message = extras.getString("message");
                String [] tokens = message.split("[**]+");

                String m;
                if(tokens[0].equals(tokens[1])) {
                    m = tokens[1] + " paid you $" + tokens[5] + " for " + tokens[3] + ".";
                }
                else {
                    m = "You owe " + tokens[2] + " $" + tokens[5] + " for " + tokens[3] + ".";
                }
                System.out.println(m);

                Bundle b = new Bundle();
                b.putString("USER_OWED", tokens[2]);
                b.putString("TITLE", tokens[3]);
                b.putString("AMOUNT", tokens[5]);
                b.putString("TAB_ID", tokens[7]);

                b.putBoolean("IOU", true);

                notify(m, b);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    void notify(String m, Bundle b){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.basil_icon)
                        .setContentTitle("Your Budget Buddy")
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(m));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Act_IouPage.class);
        resultIntent.putExtras(b);


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Act_IouPage.class);
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

        mNotificationManager.notify(notID, mBuilder.build());

        notID++;
    }
}
