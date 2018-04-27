package com.example.android.myweatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myweatherapp.customViews.MyTextView;
import com.example.android.myweatherapp.pojo.Forecast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<Forecast> forecasts;

    public MyAdapter(Context context, List<Forecast> forecasts) {
        this.context = context;
        this.forecasts = forecasts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        holder.day.setText(forecast.getDay());
        holder.max.setText(forecast.getHigh());
        holder.min.setText(forecast.getLow());
        holder.text.setText(Setter.settxt(Integer.parseInt(forecast.getCode())));
        holder.imageView.setImageResource(Setter.setImg(Integer.parseInt(forecast.getCode())));
        holder.upiv.setImageResource(R.drawable.up);
        holder.downiv.setImageResource(R.drawable.down);


    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MyTextView day;
        private MyTextView max;
        private MyTextView min;
        private MyTextView text;
        private ImageView imageView;
        private ImageView upiv;
        private ImageView downiv;
        public MyViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.datetv);
            max = itemView.findViewById(R.id.max);
            min = itemView.findViewById(R.id.min);
            text = itemView.findViewById(R.id.texttv);
            imageView = itemView.findViewById(R.id.imageView);
            upiv = itemView.findViewById(R.id.up);
            downiv = itemView.findViewById(R.id.down);
        }
    }
}

