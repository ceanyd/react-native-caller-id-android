package com.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.net.Uri;
import java.io.File;

import android.content.SharedPreferences;

public class UpgradeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri packageName = intent.getData();
        if (packageName.toString().equals("package:" + context.getPackageName())) {
            SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            prefs.edit().putBoolean("first", true).commit();
            Toast.makeText(context, "Open app for the Caller-ID to work with " + context.getPackageName(), Toast.LENGTH_LONG).show();
        }
    }
}