package com.example.android.myweatherapp.customViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
        this.setTextColor(Color.WHITE);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(tf);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextColor(Color.WHITE);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(tf);
    }


}
