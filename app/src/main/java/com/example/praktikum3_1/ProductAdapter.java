package com.example.praktikum3_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private NumberFormat formatter;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        this.formatter.setMaximumFractionDigits(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product name
        holder.tvMerk.setText(product.getMerk());

        // Set category and unit
        holder.tvCategory.setText(product.getFormattedCategory());
//        holder.tvUnit.setText(String.format("Unit: %s", product.getSatuan()));

        // Handle pricing and discounts
        double diskon = product.getDiskonjual();
        double hargaAsli = product.getHargajual();

        if (diskon > 0) {
            holder.chipDiscount.setVisibility(View.VISIBLE);
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);

            holder.chipDiscount.setText(String.format("%.0f%%", diskon));
            holder.tvOriginalPrice.setText(formatter.format(hargaAsli));
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            double hargaDiskon = product.getDiscountedPrice();
            holder.tvHarga.setText(formatter.format(hargaDiskon));
        } else {
            holder.chipDiscount.setVisibility(View.GONE);
            holder.tvOriginalPrice.setVisibility(View.GONE);
            holder.tvHarga.setText(formatter.format(hargaAsli));
        }

        // Set stock with unit
        holder.tvStok.setText(String.format("Stock: %d %s", product.getStok(), product.getSatuan()));

        // Load product image
        Glide.with(context)
                .load(product.getFoto())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivFoto);

        // Button click listeners
        holder.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(context, "Added to cart: " + product.getMerk(), Toast.LENGTH_SHORT).show();
            // TODO: Implement cart functionality
        });

        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        MaterialTextView tvMerk, tvHarga, tvStok, tvOriginalPrice, tvCategory;
        Chip chipDiscount;
        MaterialButton btnAddToCart, btnDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            tvMerk = itemView.findViewById(R.id.tvMerk);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvStok = itemView.findViewById(R.id.tvStok);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            chipDiscount = itemView.findViewById(R.id.chipDiscount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
//            tvUnit = itemView.findViewById(R.id.tvUnit);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}