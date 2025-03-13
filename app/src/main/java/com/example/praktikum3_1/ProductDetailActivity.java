package com.example.praktikum3_1;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ImageView ivProductImage;
    private MaterialTextView tvProductName, tvPrice, tvOriginalPrice;
    private MaterialTextView tvStock, tvCategory, tvDescription;
    private Chip chipDiscount;
    private MaterialButton btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews();
        setupToolbar();
        displayProductDetails();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        chipDiscount = findViewById(R.id.chipDiscount);
        tvStock = findViewById(R.id.tvStock);
        tvCategory = findViewById(R.id.tvCategory);
//        tvUnit = findViewById(R.id.tvUnit);
        tvDescription = findViewById(R.id.tvDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void displayProductDetails() {
        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) return;

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);

        // Set product name and image
        tvProductName.setText(product.getMerk());
        Glide.with(this)
                .load(product.getFoto())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(ivProductImage);

        // Set prices and discount
        double discount = product.getDiskonjual();
        if (discount > 0) {
            chipDiscount.setVisibility(View.VISIBLE);
            tvOriginalPrice.setVisibility(View.VISIBLE);

            chipDiscount.setText(String.format("%.0f%%", discount));
            tvOriginalPrice.setText(formatter.format(product.getHargajual()));
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            tvPrice.setText(formatter.format(product.getDiscountedPrice()));
        } else {
            chipDiscount.setVisibility(View.GONE);
            tvOriginalPrice.setVisibility(View.GONE);
            tvPrice.setText(formatter.format(product.getHargajual()));
        }

        // Set other product details
        tvStock.setText(product.getFormattedStock());
        tvCategory.setText(product.getFormattedCategory());
//        tvUnit.setText(product.getFormattedUnit());
        tvDescription.setText(product.getDeskripsi());

        btnAddToCart.setOnClickListener(v -> {
            // TODO: Implement add to cart functionality
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}