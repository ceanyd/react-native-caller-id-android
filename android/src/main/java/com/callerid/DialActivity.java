package com.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class DialActivity extends AppCompatActivity {

    private boolean isAboveLolipop = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
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

    public class CustomOnClickListener implements View.OnClickListener {
        public String number;
        public String name;
        @Override
        public void onClick(View v) {
            Intent answerReceive = new Intent();
            answerReceive.setAction(InCallManager.ANSWER);
            sendBroadcast(answerReceive);
        }

    }

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dial);

        final String number = getIntent().getStringExtra("incomingnumber");
        String name = getIntent().getStringExtra("incomingname");

        TextView nameText = (TextView)findViewById(R.id.nameText);
        nameText.setText(name);

        TextView numberText = (TextView)findViewById(R.id.numberText);
        numberText.setText(number);

        ImageButton btnAnswer = (ImageButton) findViewById(R.id.btnAnswer);
        ImageButton btnDecline = (ImageButton) findViewById(R.id.btnDecline);
        CustomOnClickListener onClickListener = new CustomOnClickListener();
        onClickListener.name = name;
        onClickListener.number = number;

        btnAnswer.setOnClickListener(onClickListener);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent answerReceive = new Intent();
                answerReceive.setAction(InCallManager.DISMISS);
                sendBroadcast(answerReceive);
            }
        });


    }
}
