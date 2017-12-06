package com.callerid.notif;

import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

//import android.widget.Toast;

public class AddPermissions extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "AddPermissions";
    }

    public AddPermissions(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void notificationsAccess() {

        if (NotificationManagerCompat.getEnabledListenerPackages(getReactApplicationContext()).contains(getReactApplicationContext().getPackageName())) {

        } else {
            Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getReactApplicationContext().startActivity(i);
        }
    }

    @ReactMethod
    public void getNotificationsAccess(Promise promise) {
        promise.resolve(NotificationManagerCompat.getEnabledListenerPackages(getReactApplicationContext()).contains(getReactApplicationContext().getPackageName()));
    }
}