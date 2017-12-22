package com.callerid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

public class CustomNotification {
    private Context ctx;
    private String n;
    private String num;

    private static boolean isFirstInc;

    public CustomNotification(Context context, String name, String number) {
        ctx = context;
        n = name;
        num = number;
    }

    private void createChannel(NotificationManager notificationManager, String id, String name, int importance) {
       try {
           NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);

           // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
           notificationChannel.enableLights(true);
           notificationChannel.setLightColor(Color.GREEN);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.enableVibration(true);
           notificationManager.createNotificationChannel(notificationChannel);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    public void showInc() {
        if(isFirstInc) return;
        Intent dialIntent = new Intent(ctx, DialActivity.class);
        dialIntent.putExtra("incomingnumber", num);
        dialIntent.putExtra("incomingname", n);
//                intentPhoneCall.putExtra("state", TelephonyManager.EXTRA_STATE_RINGING);
        dialIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent1 = PendingIntent.getActivity(ctx, 0, dialIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent answerReceive = new Intent();

        answerReceive.setAction(InCallManager.ANSWER);
        PendingIntent intentAnswer = PendingIntent.getBroadcast(ctx, 0, answerReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action answerAction = new NotificationCompat.Action.Builder(R.drawable.ic_call_black_24dp, "ANSWER", intentAnswer).build();

        Intent dismissReceive = new Intent();
        dismissReceive.setAction(InCallManager.DISMISS);
        PendingIntent intentDismiss = PendingIntent.getBroadcast(ctx, 0, dismissReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(R.drawable.ic_call_end_black_24dp, "DISMISS", intentDismiss).build();


        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        String id = "caller_id_incoming_channel";
        String name = "caller_id_incoming_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager, id, name, NotificationManager.IMPORTANCE_HIGH);
        }
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(ctx, name);
        notifBuilder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // need NotificationManager.IMPORTANCE_HIGH for 26 API
//                .setCategory(Notification.CATEGORY_CALL)
                .setFullScreenIntent(intent1, true)

                .addAction(dismissAction)
                .addAction(answerAction)

                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(n)
                .setContentText("Incoming call " + num)
                .setSmallIcon(R.drawable.ic_phone_in_talk_black_24dp)
                .setContentIntent(intent1)

                .setChannelId(id)

                .setOngoing(true)
                .setShowWhen(true)
                .setAutoCancel(true);

//        notifBuilder.flags = android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.FLAG_AUTO_CANCEL;

        isFirstInc = true;
        notificationManager.notify(766, notifBuilder.build());
    }

    public void showOng() {
        Intent dialIntent = new Intent(ctx, InCall.class);
        dialIntent.putExtra("incomingnumber", num);
        dialIntent.putExtra("incomingname", n);
        PendingIntent intent1 = PendingIntent.getActivity(ctx, 0, dialIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissReceive = new Intent();
        dismissReceive.setAction(InCallManager.DISMISS);
        PendingIntent intentDismiss = PendingIntent.getBroadcast(ctx, 0, dismissReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(R.drawable.ic_call_end_black_24dp, "HANG UP", intentDismiss).build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        String id = "caller_id_ongoing_channel";
        String name = "caller_id_ongoing_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager, id, name, NotificationManager.IMPORTANCE_DEFAULT);
        }

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(ctx, name);
        notifBuilder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setCategory(Notification.CATEGORY_CALL)
//                .setFullScreenIntent(intent1, true)

                .addAction(dismissAction)

                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(n)
                .setContentText("Ongoing call " + num)
                .setSmallIcon(R.drawable.ic_call_black_24dp)
                .setContentIntent(intent1)

                .setChannelId(id)

                .setOngoing(true)
                .setShowWhen(true)
                .setAutoCancel(false);

//        notifBuilder.flags = android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(766, notifBuilder.build());
    }



    public void showMiss() {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_VIEW);
        dialIntent.setType(CallLog.Calls.CONTENT_TYPE);

        PendingIntent intent1 = PendingIntent.getActivity(ctx, 787, dialIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissReceive = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
        PendingIntent intentDismiss = PendingIntent.getActivity(ctx, 78, dismissReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(R.drawable.ic_phone_missed_black_24dp, "CALL BACK", intentDismiss).build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

//        String group = "group_test1";
        String id = "caller_id_missing_channel";
        String name = "caller_id_missing_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager, id, name, NotificationManager.IMPORTANCE_DEFAULT);
        }

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(ctx, name);
        notifBuilder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setCategory(Notification.CATEGORY_CALL)

                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(n)
                .setContentText("Missing call " + num)
                .setSmallIcon(R.drawable.ic_phone_missed_black_24dp)
                .setContentIntent(intent1)

                .addAction(dismissAction)

                .setChannelId(id)

                .setOngoing(false)
                .setShowWhen(true)
                .setAutoCancel(true);

//        notifBuilder.flags = android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.FLAG_AUTO_CANCEL;
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(num + m, 790, notifBuilder.build());
    }


    static public void cancel(Context ctx) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(766);
        isFirstInc = false;
    }
}
