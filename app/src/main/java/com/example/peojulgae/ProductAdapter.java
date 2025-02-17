package com.example.peojulgae;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("가격: " + product.getPrice());
        holder.productDescription.setText( product.getDescription());
        holder.productDiscount.setText( "할인율:" + product.getDiscount()+"%");




        // 이미지 로드
        if (product.getPhotoUrls() != null && !product.getPhotoUrls().isEmpty()) {
            Glide.with(context).load(product.getPhotoUrls().get(0)).into(holder.productImage);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, productDiscount, productDescription;
        public ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productDescription= itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscount = itemView.findViewById(R.id.product_discount); // 할인율 표시를 위한 TextView
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
