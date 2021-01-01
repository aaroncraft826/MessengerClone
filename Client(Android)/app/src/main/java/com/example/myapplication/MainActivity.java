package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText nameText = (EditText)findViewById(R.id.nameInput);
        final EditText ipText = (EditText)findViewById(R.id.ipInput);
        final EditText portText = (EditText)findViewById(R.id.portInput);
        final Button connectButton = (Button)findViewById(R.id.connectButton);

        nameText.setHint("YOUR NAME");
        nameText.setText("");

        ipText.setHint("HOST ADDRESS");
        ipText.setText("");

        portText.setHint("PORT NUMBER");
        portText.setText("");

        connectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validate(nameText.getText().toString(), ipText.getText().toString(), portText.getText().toString());
            }
        });
    }

    private void validate(String nameStr, String ipStr, String portStr){

        String name = nameStr;
        String address = ipStr;

        //Determine if port is valid
        int port;
        try{
            port = Integer.parseInt(portStr);
        }catch(NumberFormatException e){
            alert("NumberFormatException", "Error: Port must be number");
            return;
        }

        Intent intent = new Intent(this, ServerActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        intent.putExtra("port", port);
        startActivity(intent);
    }

    private void alert(String title, String msg){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .show();
    }
}
