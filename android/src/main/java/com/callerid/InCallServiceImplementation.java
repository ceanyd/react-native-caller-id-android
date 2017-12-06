package com.callerid;

import android.content.Intent;
import android.net.Uri;
import android.telecom.InCallService;

public class InCallServiceImplementation extends InCallService {

    @Override
    public void onCreate() {
        System.out.println("FIRST CREATED SERVICE!!!");
    }

    @Override
    public void onDestroy() {
        System.out.println("DESTROY SERVICE!!!");
    }

    private void stopService() {
        System.out.println("STOP SERVICE!!!");
    }

    private void performDial(String numberString) {
        if (!numberString.equals("")) {
            Uri number = Uri.parse("tel:" + numberString);
            Intent dial = new Intent(Intent.ACTION_CALL, number);
            startActivity(dial);
        }
    }

//    @Override
//    public void onCallAdded(Call call) {
//        System.out.println("***************");
//        System.out.println(call);
//        System.out.println("***************");
//    }

//    @Override
//    public void onCallRemoved(Call call) {
//        System.out.println("********onCallRemoved*******");
//        System.out.println(call);
//        System.out.println("********onCallRemoved*******");
//    }

//    @Override
//    public void onBringToForeground(boolean state) {
//        System.out.println("********onBringToForeground*******");
//        System.out.println(state);
//        System.out.println("********onBringToForeground*******");
//    }

}
