package com.lawrencium.basil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by Lawrencium on 4/27/15.
 */
public class AlarmService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    private int mId = 0;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        //banana testing
//        Context context = this.getApplicationContext();
//        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
//        Intent mIntent = new Intent(this, Act_BudgetBuddy.class);
//        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle("Bananas");
//        builder.setContentText("get your bananas");
//        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setContentIntent(pendingIntent);
//
//        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.basil_icon)
                        .setContentTitle("Your Budget Buddy")
//                        .setContentText("You have overspent in eating out this week. Better hit the gym!")
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("You have overspent in eating out this week. Better hit the gym!"));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Act_BudgetManagerMain.class);

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

        stopSelf();
    }
}
