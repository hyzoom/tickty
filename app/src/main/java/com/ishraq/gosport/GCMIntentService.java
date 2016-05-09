package com.ishraq.gosport;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import static com.ishraq.gosport.CommonUtilities.SENDER_ID;
import static com.ishraq.gosport.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {
    String sentMessage = "";
    NotificationManager manager = null;
    Notification myNotication;
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.d(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        ServerUtilities.register(context, FeedListActivity.name, FeedListActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        com.ishraq.gosport.ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("go");

        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message) {
        if (message == null) {
            message = "Welcome to tickety";
        }

//        String sentRegId = message.substring(message.indexOf(getString(R.string.concate)) + 6, message.length());

        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        // HAZEM
        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, FeedListActivity.class);
        notificationIntent.putExtra("complete_msg", message);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(false);
        builder.setTicker("Welcome to tickety");
        builder.setContentTitle("Tickety");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
//        builder.setNumber(100);
        builder.build();

        myNotication = builder.getNotification();
        myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
        myNotication.defaults |= Notification.DEFAULT_SOUND;
        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, myNotication);


    }

}
