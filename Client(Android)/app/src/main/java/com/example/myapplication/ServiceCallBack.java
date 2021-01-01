package com.example.myapplication;

import java.util.LinkedList;

public interface ServiceCallBack {
    void updateChat(LinkedList<String> newChat);
    void alert(String title, String msg);
}
