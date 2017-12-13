package com.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

//import android.widget.Toast;
//import android.widget.Toast;

public class InCall extends AppCompatActivity {

    private boolean isAboveLolipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    private BroadcastReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("FINISH_ACTIVITY");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isAboveLolipop) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        super.onCreate(savedInstanceState);
        Log.d("IncomingCallActivity", "onCreate: ");

        setContentView(R.layout.incall_screen);

        String number = getIntent().getStringExtra("incomingnumber");
        String name = getIntent().getStringExtra("incomingname");
        TextView nameText = (TextView)findViewById(R.id.nameText);
        nameText.setText(name);

        TextView numberText = (TextView)findViewById(R.id.numberText);
        numberText.setText(number);

        ImageButton btnMute = (ImageButton) findViewById(R.id.btnMute);
        ImageButton btnDecline = (ImageButton) findViewById(R.id.btnDecline);
        ImageButton btnSpeaker = (ImageButton) findViewById(R.id.btnSpeaker);

        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                // get original mode
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                int originalMode = audioManager.getMode();
                // change mute
                boolean state = !audioManager.isMicrophoneMute();
                audioManager.setMicrophoneMute(state);
                // set mode back
                audioManager.setMode(originalMode);

                ImageButton btnMute = (ImageButton) findViewById(R.id.btnMute);
                GradientDrawable bgShape = (GradientDrawable) btnMute.getBackground();
                if (state) {
                    bgShape.setColor(Color.parseColor("#7A7A7A"));
                } else {
                    bgShape.setColor(Color.parseColor("#00000000"));
                }
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dismissReceive = new Intent();
                dismissReceive.setAction(InCallManager.DISMISS);
                sendBroadcast(dismissReceive);
            }
        });

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                // get original mode
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                int originalMode = audioManager.getMode();
                // change mute
                boolean state = !audioManager.isSpeakerphoneOn();
                audioManager.setSpeakerphoneOn(state);
                // set mode back
                audioManager.setMode(originalMode);

                ImageButton btnMute = (ImageButton) findViewById(R.id.btnSpeaker);
                GradientDrawable bgShape = (GradientDrawable) btnMute.getBackground();
                if (state) {
                    bgShape.setColor(Color.parseColor("#7A7A7A"));
                } else {
                    bgShape.setColor(Color.parseColor("#00000000"));
                }
            }
        });

    }

}
