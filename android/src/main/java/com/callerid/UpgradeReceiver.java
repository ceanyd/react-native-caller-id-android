package com.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.net.Uri;
import java.io.File;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

import com.callerid.db.DataBase;

public class UpgradeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri packageName = intent.getData();
        if (packageName.toString().equals("package:" + context.getPackageName())) {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            String name;
            if(stringId == 0) name = applicationInfo.nonLocalizedLabel.toString();
            else name = context.getString(stringId);
            DataBase dataBase = DataBase.getDatabase(context, null); // for first call
            if(null == dataBase) Toast.makeText(context, "Open app for the Caller-ID to work with " + name, Toast.LENGTH_LONG).show();
        }
    }
}