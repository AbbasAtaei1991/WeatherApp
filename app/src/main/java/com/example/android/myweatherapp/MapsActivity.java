package com.example.android.myweatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.android.myweatherapp.customViews.MyTextView;
import com.example.android.myweatherapp.pojo.Forecast;
import com.example.android.myweatherapp.pojo.YahooWeather;
import com.example.android.myweatherapp.utils.PublicMethods;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    RecyclerView recyclerView;
    List<Forecast> forecasts;
    private GoogleMap mMap;
    ProgressDialog dialog;
    Context mContext = this;
    MyTextView loc, day, datetv, temprature, humidity, desc, sunrise, sunset;
    ImageView weatherIcon;
    private static final int LOCATION_REQUEST = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bind();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PersianDate pdate = new PersianDate();
        PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
        String date = pdformater1.format(pdate);
        String da = pdate.dayName();
        datetv.setText(date);
        day.setText(da);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

            SmartLocation.with(mContext).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {

                        @Override
                        public void onLocationUpdated(Location location) {

                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            LatLng myLocation = new LatLng(lat, lng);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 6));
                        }
                    });

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour <= 20 && hour >= 7)
        {
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        }else {

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night));

        }
        LatLng iran = new LatLng(32.4325274,53.4267969);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iran, 5));

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                double lat = mMap.getCameraPosition().target.latitude;
                double lng = mMap.getCameraPosition().target.longitude;

                String q = "select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(" + lat + "," + lng + ")\")and u='c'\n";
                APIInterface apiInterface =APIClient.getClient().create(APIInterface.class);

                Call<YahooWeather> yW = apiInterface.getWeatherCondition(q);

                yW.enqueue(new Callback<YahooWeather>() {
                    @Override
                    public void onResponse(Call<YahooWeather> call, Response<YahooWeather> response) {
                        if (response.isSuccessful()){
                            if (response.body().getQuery().getCount() == 1) {
                                loc.setText(response.body().getQuery().getResults().getChannel().getLocation().getCity());
                                temprature.setText(response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp() + " ºC");
                                humidity.setText(response.body().getQuery().getResults().getChannel().getAtmosphere().getHumidity() + " %");
                                sunrise.setText(response.body().getQuery().getResults().getChannel().getAstronomy().getSunrise());
                                sunset.setText(response.body().getQuery().getResults().getChannel().getAstronomy().getSunset());
                                int code = Integer.parseInt(response.body().getQuery().getResults().getChannel().getItem().getCondition().getCode());
                                String description  = response.body().getQuery().getResults().getChannel().getItem().getCondition().getText();
                                weatherIcon.setImageResource(Setter.setImg(code));
                                desc.setText(Setter.settxt(code));
                                forecasts = response.body().getQuery().getResults().getChannel().getItem().getForecast();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setRecycler(forecasts);
                                    }
                                });


                            }
                        }

                    }


                    @Override
                    public void onFailure(Call<YahooWeather> call, Throwable t) {
                        PublicMethods.showToast(mContext, "Conection Error");
                    }
                });

            }
        });


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    setRecycler(forecasts);
                }
                break;
        }
    }

    void bind() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait");
        dialog.setMessage("Please wait to load response");
        loc = findViewById(R.id.location);
        datetv = findViewById(R.id.date);
        day = findViewById(R.id.day);
        temprature = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidity);
        desc = findViewById(R.id.desc);
        sunrise = findViewById(R.id.sunrisetv);
        sunset = findViewById(R.id.sunsettv);
        weatherIcon = findViewById(R.id.weatherIcon);
    }

    void setRecycler(List<Forecast> forecasts){
        recyclerView = findViewById(R.id.myrecyclerview);
        MyAdapter adapter = new MyAdapter(mContext, forecasts);
        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(lm);


    }

}
