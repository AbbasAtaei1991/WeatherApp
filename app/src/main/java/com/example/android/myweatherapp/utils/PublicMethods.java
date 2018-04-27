package com.example.android.myweatherapp.utils;

import android.content.Context;
import android.widget.Toast;

public class PublicMethods {

    public static void showToast(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
