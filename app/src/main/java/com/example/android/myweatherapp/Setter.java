package com.example.android.myweatherapp;

import java.util.Calendar;
import java.util.Date;

public class Setter {
    private static int imgId;
    private static int descId;

    public static int setImg(int code) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        switch (code) {
            case 4: imgId = R.drawable.storm; break;
            case 9: imgId = R.drawable.rain; break;
            case 11:
            case 12: imgId = R.drawable.rrr; break;
            case 14:
            case 16:
            case 18:
            case 43: imgId = R.drawable.hs; break;
            case 20:
            case 21:
            case 22: imgId = R.drawable.foggy; break;
            case 23:
            case 24: imgId = R.drawable.wind; break;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30: imgId = R.drawable.clouds; break;
            case 44: imgId = R.drawable.cloudy; break;
            case 31:
            case 32:  if(hour <= 19 && hour >= 7){
                imgId = R.drawable.sun;
            }else {imgId = R.drawable.clear;} break;
            default: imgId = R.drawable.cloudy; break;

        }
        return imgId;
    }

    public static int settxt(int code){
        switch (code) {
            case 4: descId = R.string.tonder; break;
            case 9: descId = R.string.rain; break;
            case 11:
            case 12: descId = R.string.f; break;
            case 14: descId = R.string.k; break;
            case 16: descId = R.string.g; break;
            case 18: descId = R.string.j; break;
            case 20:
            case 21:
            case 22: descId = R.string.o; break;
            case 23: descId = R.string.n; break;
            case 24: descId = R.string.m; break;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30: descId = R.string.abri; break;
            case 31: descId = R.string.c; break;
            case 32: descId = R.string.w; break;
            case 44: descId = R.string.d; break;
            case 41:
            case 43: descId = R.string.barf; break;
            default: descId = R.string.abri; break;
        }
        return descId;
    }
}
