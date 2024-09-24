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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foodList;
    private Context context;

    public FoodAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.foodName.setText(food.getName());
        holder.foodDescription.setText(food.getDescription());
        holder.foodPrice.setText("가격: " + food.getPrice());
        holder.foodDiscount.setText("할인율: " + food.getDiscount() + "%");
        holder.foodQuantity.setText("수량: " + food.getQuantity());  // 수량 표시

        // 이미지 로드
        Glide.with(context).load(food.getMainImageUrl()).into(holder.foodImage);

        // 클릭 리스너 추가: 아이템을 클릭하면 FoodListActivity로 이동하면서 데이터를 전달함
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodListActivity.class);
            intent.putExtra("image_url", food.getMainImageUrl()); // URL 전달
            intent.putExtra("food_name", food.getName());
            intent.putExtra("description", food.getDescription());
            intent.putExtra("food_price", food.getPrice());
            intent.putExtra("discount", food.getDiscount());
            intent.putExtra("discounted_price", calculateDiscountedPrice(food.getPrice(), food.getDiscount()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // 할인된 가격 계산
    private String calculateDiscountedPrice(String price, int discount) {
        try {
            int originalPrice = Integer.parseInt(price.replace(",", "").replace("원", "").trim());
            int discountedPrice = originalPrice - (originalPrice * discount / 100);
            return discountedPrice + "원";
        } catch (NumberFormatException e) {
            return price; // 오류가 발생하면 원래 가격을 반환
        }
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodDescription, foodDiscount, foodQuantity;
        ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodDiscount = itemView.findViewById(R.id.food_discount);
            foodQuantity = itemView.findViewById(R.id.food_quantity);
            foodImage = itemView.findViewById(R.id.food_image);
        }
    }
}