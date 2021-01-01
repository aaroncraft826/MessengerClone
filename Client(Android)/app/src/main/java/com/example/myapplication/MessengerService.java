package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Binder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;
import java.lang.StringBuilder;
import java.net.SocketException;
import java.util.LinkedList;

public class MessengerService extends Service {

    private Socket sock;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ServiceCallBack callBack;

    private IBinder binder = new MyBinder();

    private LinkedList<String> chat = new LinkedList<String>();

    @Override
    public IBinder onBind(Intent intent) {
        final String name = intent.getStringExtra("name");
        final String address = intent.getStringExtra("address");
        final int port = intent.getIntExtra("port", -1);

        new Thread(){
            @Override
            public void run(){
                try{
                    sock = new Socket(address, port);
                    dis = new DataInputStream((sock.getInputStream()));
                    dos = new DataOutputStream((sock.getOutputStream()));

                    dos.writeUTF("NAM|" + name + "|");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        return binder;
    }

    public void listenForMessages(){

        new Thread(){
            @Override
            public void run() {
                //LinkedList<String> newChat = new LinkedList<String>();
                while(true){
                   try{
                       String temp = dis.readUTF();
                       if(!temp.equals("END|")){
                           chat.addFirst(temp);
                           Log.d("MESSAGE Added HERE", temp);
                       }else{
                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                               @Override
                               public void run() {
                                   callBack.updateChat(chat);
                               }
                           });
                           //chat.clear();
                       }
                   }catch(SocketException e){
                       new Handler(Looper.getMainLooper()).post(new Runnable() {
                           @Override
                           public void run() {
                               callBack.alert("Connection Lost", "ERROR: Lost connection to server.");
                           }
                       });
                       return;
                   } catch(Exception e){
                       Log.d("ERROR READ HERE", e.toString());
                   }
                }
            }
        }.start();
    }

    public LinkedList<String> getChat(){
        return chat;
    }

    public void sendMessage(String msg){
        StringBuilder sb = new StringBuilder("MSG|");
        sb.append(msg);
        sb.append("|");

        final String finalMessage = sb.toString();

        new Thread(){
            @Override
            public void run(){
                try{
                    dos.writeUTF(finalMessage);
                }catch(Exception e){
                }
            }
        }.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //close socket and streams
        try{ sock.close(); }catch(Exception e){ }
        try{ dis.close(); }catch(Exception e){ }
        try{ dos.close(); }catch(Exception e){ }
    }

    public class MyBinder extends Binder{
        MessengerService getService(){
            return MessengerService.this;
        }
    }

    public void setCallBack(ServiceCallBack cb){
        callBack = cb;
    }
}
