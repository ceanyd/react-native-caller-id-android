package com.callerid;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.io.IOException;
import java.lang.reflect.Method;

public class InCallManager extends BroadcastReceiver {

    public static final String ANSWER = "ANSWER";
    public static final String DISMISS = "DISMISS";

    private final boolean isAboveLOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    private final boolean isAboveO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (ANSWER.equals(action)) {
            try {
                if (isAboveO) {
                    TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                    tm.acceptRingingCall();
                } else if (isAboveLOLLIPOP) {
                    for (MediaController mediaController : ((MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE)).getActiveSessions(new ComponentName(context, NotificationReceiverService.class))) {
                        if ("com.android.server.telecom".equals(mediaController.getPackageName())) {
                            mediaController.dispatchMediaButtonEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                            return;
                        }
                    }
                } else {
                    try {
                        Runtime.getRuntime().exec("input keyevent " +
                                Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

                    } catch (IOException e) {
                        // Runtime.exec(String) had an I/O problem, try to fall back
                        String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                        Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                                Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                        KeyEvent.KEYCODE_HEADSETHOOK));
                        Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                                Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                        KeyEvent.KEYCODE_HEADSETHOOK));

                        context.sendOrderedBroadcast(btnDown, enforcedPerm);
                        context.sendOrderedBroadcast(btnUp, enforcedPerm);
                    }
                }
            } catch (SecurityException e2) {
                e2.printStackTrace();
            }
        } else if (DISMISS.equals(action)) {
            try {
//                CallReceiver.setIsNotMissedCall();

                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Class c = Class.forName(manager.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                ITelephony telephony = (ITelephony) m.invoke(manager);
                telephony.endCall();
            } catch(Exception e){
                Log.d(DISMISS,e.getMessage());
            }
        }
    }
}