package com.example.android.myweatherapp;

import com.example.android.myweatherapp.pojo.YahooWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("yql?format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<YahooWeather> getWeatherCondition(@Query("q") String q);
}
