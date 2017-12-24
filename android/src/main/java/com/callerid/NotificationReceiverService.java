package com.callerid;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

//import android.widget.Toast;

public class NotificationReceiverService extends NotificationListenerService {

    Context context;
    public static boolean isCallIn;
    static boolean isMissed;
    static String name = "Undefined";
    static String number;
    private boolean dissableNotificationWithVersion = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP;

//    private NLServiceReceiver nrec;
//    private StatusBarNotification incall_sbn;

    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();

//        nrec = new NLServiceReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.gggg.LIST_NOTE");
//        registerReceiver(nrec, filter);
    }

    static public void update(String type, String n, String num) {
        switch(type) {
            case "received":
                isCallIn = true;
                isMissed = true;
                name = n;
                number = num;
                break;
            case "answered":
                if (isCallIn) isMissed = false;
                isCallIn = false;
                break;
            case "ended":
                isCallIn = false;
                break;
            case "missed":
                isCallIn = false;
                break;
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        super.onNotificationRemoved(sbn);
//        try {
//            String packageName = sbn.getPackageName();
//
//            if ("com.android.incallui".equals(packageName) || "com.android.dialer".equals(packageName) || "com.google.android.dialer".equals(packageName)) {
//
//                Bundle extras = sbn.getNotification().extras;
//
//                if ("Ongoing call".equals(extras.getString(Notification.EXTRA_TEXT)) || "Текущий вызов".equals(extras.getString(Notification.EXTRA_TEXT))) {
//
////                Toast.makeText(getApplicationContext(), "Ongoing call", Toast.LENGTH_LONG).show();
//
//                } else if (isCallIn && ("Incoming call".equals(extras.getString(Notification.EXTRA_TEXT)) || "Входящий вызов".equals(extras.getString(Notification.EXTRA_TEXT)))) {
//                    onCallInEnded();
////                CustomNotification notification = new CustomNotification(context, name, number);
////                notification.cancel();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    private String locIncomingName (String packageName) {
        if (packageName.indexOf("dialer") > 0) {
            return "type_incoming";
        }
        return "card_title_incoming_call";
    }

    private String locOngoingName (String packageName) {
        if (packageName.indexOf("dialer") > 0) {
            return "notification_ongoing_call";
        }
        return "notification_ongoing_call";
    }

    private String locMissingName (String packageName) {
        if (packageName.indexOf("dialer") > 0) {
            return "notification_missedCallTitle";
        }
        return "notification_missedCallTitle";
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        super.onNotificationPosted(sbn);
        if (dissableNotificationWithVersion) return;
        try {
            String packageName = sbn.getPackageName();

            if (packageName.indexOf("incallui") > 0 || packageName.indexOf("dialer") > 0) {

                Bundle extras = sbn.getNotification().extras;
                Resources res = context.getPackageManager().getResourcesForApplication(packageName);

                int incomingResId = res.getIdentifier(locIncomingName(packageName), "string", packageName);
                String incomingExtraText = res.getString(incomingResId);

                int missingResId = res.getIdentifier(locMissingName(packageName), "string", packageName);
                String missingExtraText = res.getString(missingResId);

                if (incomingExtraText.equals(extras.getString(Notification.EXTRA_TEXT))) {
                    CallReceiver.setIsAlreadyShownNotification();
                    if(isCallIn) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                CustomNotification notification = new CustomNotification(context, name, number);
                                notification.showInc();
                            }
                        });
                    }
                } else if (missingExtraText.equals(extras.getString(Notification.EXTRA_TEXT)) | missingExtraText.equals(extras.getString(Notification.EXTRA_TITLE))) {

                    if(isMissed) {
                        isMissed = false;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                CustomNotification notification = new CustomNotification(context, name, number);
                                notification.showMiss();
                            }
                        });
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    class NLServiceReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getStringExtra("action").equals("CLEAR_NOTIFICATIONS")) {
//                Toast.makeText(context, "CLEARING NOTIFICATIONS", Toast.LENGTH_SHORT).show();
//                cancelNotification(incall_sbn.getKey());
//            }
//        }
//
//    }
}