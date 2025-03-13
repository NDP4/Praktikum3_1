package com.example.praktikum3_1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;
import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView ivProductImage;
    private MaterialTextView tvProductName, tvPrice, tvOriginalPrice;
    private MaterialTextView tvStock, tvCategory, tvDescription;
    private Chip chipDiscount;
    private MaterialToolbar toolbar;
    private NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews();
        setupToolbar();
        setupFormatter();
        displayProductDetails();
    }

    private void initializeViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        tvStock = findViewById(R.id.tvStock);
        tvCategory = findViewById(R.id.tvCategory);
        tvDescription = findViewById(R.id.tvDescription);
        chipDiscount = findViewById(R.id.chipDiscount);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupFormatter() {
        formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
    }

    private void displayProductDetails() {
        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            showToast("Product data not found");
            finish();
            return;
        }

        // Load product image
        Glide.with(this)
                .load(product.getFoto())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(ivProductImage);

        // Set product name
        tvProductName.setText(product.getMerk());

        // Handle pricing and discounts
        double diskon = product.getDiskonjual();
        double hargaAsli = product.getHargajual();

        if (diskon > 0) {
            chipDiscount.setVisibility(android.view.View.VISIBLE);
            tvOriginalPrice.setVisibility(android.view.View.VISIBLE);

            chipDiscount.setText(String.format("%.0f%%", diskon));
            tvOriginalPrice.setText(formatter.format(hargaAsli));
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);

            double hargaDiskon = product.getDiscountedPrice();
            tvPrice.setText(formatter.format(hargaDiskon));
        } else {
            chipDiscount.setVisibility(android.view.View.GONE);
            tvOriginalPrice.setVisibility(android.view.View.GONE);
            tvPrice.setText(formatter.format(hargaAsli));
        }

        // Set other product details
        tvStock.setText(product.getFormattedStock());
        tvCategory.setText(product.getFormattedCategory());
        tvDescription.setText(product.getDeskripsi());

        // Set up add to cart button
        findViewById(R.id.btnAddToCart).setOnClickListener(v ->
                showToast("Added to cart: " + product.getMerk())
        );
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}