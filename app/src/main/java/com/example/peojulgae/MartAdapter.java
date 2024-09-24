// MartAdapter.java
package com.example.peojulgae;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MartAdapter extends RecyclerView.Adapter<MartAdapter.MartViewHolder> {

    private List<Mart> martList;
    private Context context;

    public MartAdapter(Context context, List<Mart> martList) {
        this.martList = martList;
        this.context = context;
    }

    @NonNull
    @Override
    public MartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mart, parent, false);
        return new MartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MartViewHolder holder, int position) {
        Mart mart = martList.get(position);
        holder.martName.setText(mart.getName());
        holder.martAddress.setText(mart.getAddress());
        holder.martHours.setText(mart.getHours());

        // 이미지 로드
        if (mart.getPhotoUrls() != null && !mart.getPhotoUrls().isEmpty()) {
            Glide.with(context).load(mart.getPhotoUrls().get(0)).into(holder.martImage);
        }
        // 마트 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MplistActivity.class);
            intent.putExtra("martId", mart.getMartId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return martList.size();
    }

    public static class MartViewHolder extends RecyclerView.ViewHolder {

        public TextView martName, martAddress, martHours;
        public ImageView martImage;

        public MartViewHolder(@NonNull View itemView) {
            super(itemView);
            martName = itemView.findViewById(R.id.mart_name);
            martAddress = itemView.findViewById(R.id.mart_address);
            martHours = itemView.findViewById(R.id.mart_hours);
            martImage = itemView.findViewById(R.id.mart_image);
        }
    }
}
