package com.example.locationtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtracker.db.MyLocation;

import java.util.List;

public class AdaptorMyLocationList extends RecyclerView.Adapter {
    Context context;
    List<MyLocation> myLocationList;

    public AdaptorMyLocationList(Context context, List<MyLocation> myLocationList) {
        this.context = context;
        this.myLocationList = myLocationList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_location_list_design, parent, false);
        return new MyLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyLocationViewHolder myLocationViewHolder = (MyLocationViewHolder) holder;
        MyLocation myLocation = myLocationList.get(position);
        myLocationViewHolder.tvRvLat.setText(myLocation.getLatitude());
        myLocationViewHolder.tvRvLon.setText(myLocation.getLongitude());
        myLocationViewHolder.tvRvAddress.setText(myLocation.getAddress());
        myLocationViewHolder.tvRvTime.setText(myLocation.getTime());
    }

    @Override
    public int getItemCount() {
        return myLocationList.size();
    }

    public class MyLocationViewHolder extends RecyclerView.ViewHolder{
        TextView tvRvLat, tvRvLon, tvRvAddress, tvRvTime;
        public MyLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRvLat = itemView.findViewById(R.id.tvRvLat);
            tvRvLon = itemView.findViewById(R.id.tvRvLon);
            tvRvAddress = itemView.findViewById(R.id.tvRvAddress);
            tvRvTime = itemView.findViewById(R.id.tvRvTime);
        }
    }

}
