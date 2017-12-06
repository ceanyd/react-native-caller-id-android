package com.callerid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.callerid.db.DataBase;
import com.callerid.db.User;

import java.util.ArrayList;
import java.util.List;

//import ng.max.slideview.SlideView;

public class ServiceActivity extends AppCompatActivity {


    private String TAG = "example";
    private boolean isAboveLolipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    private boolean isAboveO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_LEFT == event.getKeyCode()
                || KeyEvent.KEYCODE_DPAD_DOWN == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_RIGHT == event.getKeyCode()
                || KeyEvent.KEYCODE_DPAD_UP == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_CENTER == event.getKeyCode()
                || KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            return false;
        }
        return true;
    }

    public final static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 11;

    private DataBase database;
    private User user;

    private float dX;
    private float dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        this.overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left);

//        SeekBar sb = (SeekBar) findViewById(R.id.seekbar);
//
//        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                System.out.println("onStartTrackingTouch");
//                if (seekBar.getProgress() > 95) {
//
//                    if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
//                        seekBar.setThumb(getResources().getDrawable(R.drawable.ic_phone_missed_black_24dp));
//                    else
//                        seekBar.setThumb(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_phone_missed_black_24dp));
////                    seekBar.setThumb(getResources().getDrawable(R.drawable.ic_call_black_24dp));
//                } else {
//
//                }
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//System.out.println("onStartTrackingTouch");
////                if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
////                    seekBar.setThumb(getResources().getDrawable(R.drawable.ic_ring_volume_black_24dp));
////                else
////                    seekBar.setThumb(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_ring_volume_black_24dp));
//
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress,
//                                          boolean fromUser) {
//
////                if(progress>50){
////                    if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
////                        seekBar.setThumb(getResources().getDrawable(R.drawable.ic_call_end_black_24dp));
////                    else
////                        seekBar.setThumb(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_call_end_black_24dp));
////                } else {
////
////                    if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
////                        seekBar.setThumb(getResources().getDrawable(R.drawable.ic_call_black_24dp));
////                    else
////                        seekBar.setThumb(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_call_black_24dp));
////                }
//
//            }
//        });


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton readit = (FloatingActionButton) findViewById(R.id.readit);

        final FrameLayout rlayout = (FrameLayout) findViewById(R.id.rlayout);
        readit.setOnTouchListener(new View.OnTouchListener() {
            private float eventX;
            private float end;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println(v.getX());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        dX = v.getX() - event.getRawX();
                        eventX = event.getRawX();
                        end = fab.getLeft();
                        fab.setVisibility(View.GONE);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
                            rlayout.setBackground(getResources().getDrawable(R.drawable.layout_bg_oval_view1));
                        else
                            rlayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_bg_oval_view1));
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if(v.getX() <= 0 && eventX > event.getRawX()) {
                            v.setX(0);
                            return false;
                        }

                        if(v.getX() >= end && eventX + end < event.getRawX()) {
                            v.setX(end);
                            return false;
                        }
                        v.setX(event.getRawX() + dX);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (v.getX() >= end * 0.7) {
                            try {

                        database = DataBase.getDatabase(getApplicationContext());

                        database.userDao().removeAllUsers();


                        List<User> newUsers = new ArrayList<>();
                        newUsers.add(new User("Test5", "555"));
                        newUsers.add(new User("Test6", "666"));
                        newUsers.add(new User("Test4", "444"));
                        newUsers.add(new User("Test2", "222"));
                        newUsers.add(new User("Test3", "333"));

                        List<User> users = database.userDao().getAllUser();
                        if (users.size()==0) {
                            database.userDao().addUsers(newUsers);
                            database.userDao().addUser(new User("Test 1", "111"));
                            Toast.makeText(getApplicationContext(), "DB WAS FILLING", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "EXCEPTION", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                        }
                        v.setX(0);
                        fab.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
                            rlayout.setBackground(getResources().getDrawable(R.drawable.layout_bg_oval_view));
                        else
                            rlayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_bg_oval_view));
                    }
                }
//                if (event.getAction()==MotionEvent.ACTION_MOVE)
//                {
//                    System.out.println(" ACTION MOVE ");
////                    Toast.makeText(getApplicationContext(), "ACTION DOWN", Toast.LENGTH_SHORT).show();
//                } else {
//
////                    Toast.makeText(getApplicationContext(), "ACTION UP", Toast.LENGTH_SHORT).show();
//                    System.out.println(" ACTION UP ");
//
//
//
//
////                    try {
////
////                        database = DataBase.getDatabase(getApplicationContext());
////
////                        database.userDao().removeAllUsers();
////
////
////                        List<User> newUsers = new ArrayList<>();
////                        newUsers.add(new User("Test5", "555"));
////                        newUsers.add(new User("Test6", "666"));
////                        newUsers.add(new User("Test4", "444"));
////                        newUsers.add(new User("Test2", "222"));
////                        newUsers.add(new User("Test3", "333"));
////
////                        List<User> users = database.userDao().getAllUser();
////                        if (users.size()==0) {
////                            database.userDao().addUsers(newUsers);
////                            database.userDao().addUser(new User("Test 1", "111"));
////                            Toast.makeText(getApplicationContext(), "DB WAS FILLING", Toast.LENGTH_SHORT).show();
////                        }
////
////                    } catch (Exception e) {
////                        Toast.makeText(getApplicationContext(), "EXCEPTION", Toast.LENGTH_LONG).show();
////                        e.printStackTrace();
////                    }
//                }
                return true;
            }
        });
//
////        readit.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {

                    CustomNotification notification = new CustomNotification(getApplicationContext(), "HAHAHA", "+12312312");
                    notification.showMiss();

                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "EXCEPTION", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
//                Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:123123123"));
//                startActivity(callIntent);
//                TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
//                PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(new ComponentName(getApplicationContext(), MainService.class), "example");
//                Bundle extras = new Bundle();
//                Uri uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, "999999999", null);
//                extras.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, uri);
//                extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri);
//                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
//                tm.addNewIncomingCall(phoneAccountHandle, extras);
//                tm.placeCall(uri, extras);


//                CustomNotification notification = new CustomNotification(getApplicationContext(), null, null);
//                notification.cancel();
            }
        });
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (NotificationManagerCompat.getEnabledListenerPackages(getApplicationContext()).contains(getPackageName())) {

                    } else {

                        Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            }
        }
    }


    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {

    }

}
