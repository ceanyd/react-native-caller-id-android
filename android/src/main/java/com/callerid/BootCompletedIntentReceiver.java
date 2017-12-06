package com.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Toast.makeText(context, "Caller ID ready to work!", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(context, NotificationReceiverService.class);
//            context.startService(i);
        }
    }
}
