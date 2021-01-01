package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;

import java.net.InetAddress;
import java.util.LinkedList;

import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ServerActivity extends AppCompatActivity implements ServiceCallBack{

    MessengerService ms;
    private boolean isBind;
    LinearLayout chatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        int port = getIntent().getIntExtra("port", -1);

        final EditText textText = (EditText)findViewById(R.id.textText);
        final Button sendButton = (Button)findViewById(R.id.sendButton);

        chatLayout = (LinearLayout)findViewById(R.id.chatLayout);
        textText.setHint("Type Message Here");
        textText.setText("");

        Intent serviceIntent = new Intent(this, MessengerService.class);
        serviceIntent.putExtra("name", name);
        serviceIntent.putExtra("address", address);
        serviceIntent.putExtra("port", port);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ms.sendMessage(textText.getText().toString());
                textText.setText("");
            }
        });

    }

    @Override
    public void alert(String title, String msg){
        new AlertDialog.Builder(ServerActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .show();
    }

    private void addToChat(String msg){
        TextView tv = new TextView(this);
        tv.setText(msg);
        chatLayout.addView(tv);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessengerService.MyBinder binder = (MessengerService.MyBinder) service;
            ms = binder.getService();
            isBind = true;
            ms.setCallBack(ServerActivity.this);

            ms.listenForMessages();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    public void updateChat(LinkedList<String> newChat) {
        chatLayout.removeAllViews();
        for(String str : newChat){
            addToChat(str);
        }
        newChat.clear();
        final ScrollView scrollView2 = (ScrollView)findViewById(R.id.scrollView2);
        scrollView2.post(new Runnable() {
            @Override
            public void run() {
                scrollView2.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

}
