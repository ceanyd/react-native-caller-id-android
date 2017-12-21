package com.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import com.callerid.db.DataBase;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            DataBase dataBase = DataBase.getDatabase(context, null); // for first call
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            String name;
            if(stringId == 0) name = applicationInfo.nonLocalizedLabel.toString();
            else name = context.getString(stringId);
            if(null == dataBase) Toast.makeText(context, "Open app for the Caller-ID to work with " + name, Toast.LENGTH_LONG).show();
            else Toast.makeText(context, "Caller ID ready to work with " +  name, Toast.LENGTH_SHORT).show();
        }
    }
}
