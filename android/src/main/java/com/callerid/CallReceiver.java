package com.callerid;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.callerid.db.DataBase;
import com.callerid.db.User;

import java.util.Date;

public class CallReceiver extends MainService {

    private Context cont;
    static private boolean isAlreadyShownNotification;
//    static private boolean isMissedCall = true;


    static public void setIsAlreadyShownNotification() {
        isAlreadyShownNotification = true;
    }

//    static public void setIsNotMissedCall() {
//        isMissedCall = false;
//    }

    private class CustomRunnablePhoneActivity implements Runnable {
        public Context ctx;
        public String number;
        public String name;

        @Override
        public void run()
        {

            if (isCallingEnded) { // hack (connected with delay)
                isCallingEnded = false;
            } else {
                Intent intentPhoneCall = new Intent(ctx, DialActivity.class);
                intentPhoneCall.putExtra("incomingnumber", number);
                intentPhoneCall.putExtra("incomingname", name);
//                intentPhoneCall.putExtra("state", TelephonyManager.EXTRA_STATE_RINGING);
                intentPhoneCall.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intentPhoneCall);
            }
        }
    }

    private class RunnableInCall implements Runnable {
        public Context ctx;
        public String number;
        public String name;

        @Override
        public void run()
        {
            Intent i = new Intent(ctx, InCall.class);
            i.putExtra("incomingnumber", number);
            i.putExtra("incomingname", name);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
    }

    static public class CustomRunnableNotification implements Runnable {
        public Context ctx;
        public String number;
        public String name;

        @Override
        public void run()
        {
            if(!isAlreadyShownNotification) {
                CustomNotification notification = new CustomNotification(ctx, name, number);
                notification.showInc();
            }
        }
    }

    private boolean dissableNotificationWithVersion = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP;

    private static boolean isCallingEnded; // hack, cause activity will appear with delay

    private DataBase database;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start, Intent intent)
    {
        isCallingEnded = false;
        database = DataBase.getDatabase(ctx, null);
        User user = database.userDao().getUserByNumber(number);

        if (null != user) {
            NotificationReceiverService.onCallIn(user.name, number);

            KeyguardManager myKM = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
            Boolean lll = myKM.inKeyguardRestrictedInputMode();
            if (lll ||
//                    !NotificationManagerCompat.getEnabledListenerPackages(ctx).contains(ctx.getPackageName()) ||
                    dissableNotificationWithVersion) {
                Handler callActionHandler = new Handler();
                CustomRunnablePhoneActivity runRingingActivity = new CustomRunnablePhoneActivity();
                runRingingActivity.ctx = ctx;
                runRingingActivity.number = number;
                runRingingActivity.name = user.name;
                callActionHandler.postDelayed(runRingingActivity, 950);
                return;
            }

            int delay = 1000;
            if (isAlreadyShownNotification) delay = 0;

            Handler callActionHandler = new Handler();
            CustomRunnableNotification runRingingActivity = new CustomRunnableNotification();
            runRingingActivity.ctx = ctx;
            runRingingActivity.number = number;
            runRingingActivity.name = user.name;
            callActionHandler.postDelayed(runRingingActivity, delay);
        }

    }


    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start, Intent intent)
    {
        isCallingEnded = false;
        NotificationReceiverService.onCallInEnded();

        database = DataBase.getDatabase(ctx, null);
        User user = database.userDao().getUserByNumber(number);
        CustomNotification.cancel(ctx);

        if (null != user) {
            CustomNotification notification = new CustomNotification(ctx, user.name, number);
            notification.showOng();

            Handler callActionHandler = new Handler();
            RunnableInCall runInCallActivity = new RunnableInCall();
            runInCallActivity.ctx = ctx;
            runInCallActivity.number = number;
            runInCallActivity.name = user.name;
            callActionHandler.postDelayed(runInCallActivity, 200);
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        isCallingEnded = true;
        NotificationReceiverService.onCallInEnded();
        CustomNotification.cancel(ctx);
        Intent i = new Intent("FINISH_ACTIVITY");
        ctx.sendBroadcast(i);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        isCallingEnded = true;
        NotificationReceiverService.onCallInEnded();

        database = DataBase.getDatabase(ctx, null);
        User user = database.userDao().getUserByNumber(number);

        CustomNotification.cancel(ctx);

//        if (null != user && isMissedCall) {
//            CustomNotification notification = new CustomNotification(ctx, user.name, number);
//            notification.showMiss();
//        }
//        isMissedCall = true;

        Intent i = new Intent("FINISH_ACTIVITY");
        ctx.sendBroadcast(i);
    }


    public void onTaskRemoved(Intent rootIntent) {

    }

}