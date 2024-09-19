package com.example.peojulgae;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FoodManageAdapter extends RecyclerView.Adapter<FoodManageAdapter.FoodViewHolder> {

    private List<Food> foodList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public FoodManageAdapter(List<Food> foodList, Context context, OnItemClickListener onItemClickListener) {
        this.foodList = foodList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_manage, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        // 음식 이름 설정
        holder.foodName.setText(food.getName());
        holder.foodDescription.setText(food.getDescription());
        holder.foodPrice.setText("가격: " + food.getPrice());
        holder.foodDiscount.setText("할인율: " + food.getDiscount() + "%");
        // Firebase에서 가져온 메인 이미지 URL을 ImageView에 로드
        Glide.with(context).load(food.getMainImageUrl()).into(holder.foodImage);

        // 수정, 삭제 버튼 클릭 이벤트 설정
        holder.editButton.setOnClickListener(v -> onItemClickListener.onEditClick(food)); // Food 객체 전달
        holder.deleteButton.setOnClickListener(v -> onItemClickListener.onDeleteClick(food)); // Food 객체 전달
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // ViewHolder 클래스 정의
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodDescription;
        TextView foodPrice;
        TextView foodDiscount;
        ImageView foodImage;
        Button editButton, deleteButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodDiscount = itemView.findViewById(R.id.food_discount);
            foodImage = itemView.findViewById(R.id.food_image);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    // 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onEditClick(Food food); // Food 객체를 전달하도록 수정
        void onDeleteClick(Food food); // Food 객체를 전달하도록 수정
    }
}
