package com.example.praktikum3_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Home extends AppCompatActivity {
    private MaterialTextView tvWelcome;
    private MaterialButton btnLogout, btnEditProfile, btnProduct;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnProduct = findViewById(R.id.btnProduct);

        // Get data from intent
        String nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");
        String welcomeText = "Welcome, " + nama + "\n" + email;
        tvWelcome.setText(welcomeText);

        // Set click listeners
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, MainEditProfile.class);
            intent.putExtra("nama", tvWelcome.getText().toString());
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });

        btnProduct.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, ProductActivity.class);
            intent.putExtra("nama", nama);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}