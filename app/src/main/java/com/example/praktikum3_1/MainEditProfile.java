package com.example.praktikum3_1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.appbar.MaterialToolbar;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.target.Target;
import androidx.annotation.Nullable;

public class MainEditProfile extends AppCompatActivity {
    private TextInputEditText etProfileName, etProfileAddress, etProfileCity;
    private TextInputEditText etProfileProvince, etProfilePhone, etProfilePostal;
    private MaterialButton btnSubmit, btnBack;
    private MaterialToolbar toolbar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit_profile);

        initializeViews();
        setupToolbar();
        loadProfileData();
        setupListeners();
    }

    private void initializeViews() {
        // Initialize TextInputEditText fields
        etProfileName = findViewById(R.id.etProfileName);
        etProfileAddress = findViewById(R.id.etProfileAddress);
        etProfileCity = findViewById(R.id.etProfileCity);
        etProfileProvince = findViewById(R.id.etProfileProvince);
        etProfilePhone = findViewById(R.id.etProfilePhone);
        etProfilePostal = findViewById(R.id.etProfilePostal);

        // Initialize buttons
        btnSubmit = findViewById(R.id.imgbtnprofilesubmit);
        btnBack = findViewById(R.id.tvProfileBack);
        toolbar = findViewById(R.id.toolbar);

        // Get email from intent
        email = getIntent().getStringExtra("email");
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set the back icon
        }

        // Handle navigation icon clicks
        toolbar.setNavigationOnClickListener(v -> {
            navigateBack();
        });
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(v -> validateAndUpdateProfile());
        btnBack.setOnClickListener(v -> navigateBack());
    }

    private void loadProfileData() {
        if (email == null || email.isEmpty()) {
            showToast("Email not provided");
            navigateBack();
            return;
        }

        EditProfileAPI api = ServerAPI.getClient().create(EditProfileAPI.class);
        api.getProfile(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        showToast("Failed to load profile: " + response.code());
                        return;
                    }

                    JSONObject json = new JSONObject(response.body().string());

                    // Set values to fields
                    etProfileName.setText(json.optString("nama", ""));
                    etProfileAddress.setText(json.optString("alamat", ""));
                    etProfileCity.setText(json.optString("kota", ""));
                    etProfileProvince.setText(json.optString("provinsi", ""));
                    etProfilePhone.setText(json.optString("telp", ""));
                    etProfilePostal.setText(json.optString("kodepos", ""));

                } catch (Exception e) {
                    showToast("Error loading profile: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("Network error: " + t.getMessage());
            }
        });
    }

    private void validateAndUpdateProfile() {
        // Get values from fields
        String name = etProfileName.getText().toString().trim();
        String address = etProfileAddress.getText().toString().trim();
        String city = etProfileCity.getText().toString().trim();
        String province = etProfileProvince.getText().toString().trim();
        String phone = etProfilePhone.getText().toString().trim();
        String postal = etProfilePostal.getText().toString().trim();

        // Validate fields
        if (name.isEmpty() || address.isEmpty() || city.isEmpty() ||
                province.isEmpty() || phone.isEmpty() || postal.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        // Update profile
        EditProfileAPI api = ServerAPI.getClient().create(EditProfileAPI.class);
        api.updateProfile(name, address, city, province, phone, postal, email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            showToast("Profile updated successfully");
                            navigateToHome();
                        } else {
                            showToast("Failed to update profile");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("Network error: " + t.getMessage());
                    }
                });
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainEditProfile.this, Home.class);
        String welcomeText = etProfileName.getText().toString();
        intent.putExtra("nama", welcomeText);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void navigateBack() {
        Intent intent = new Intent(MainEditProfile.this, Home.class);
        String welcomeText =  etProfileName.getText().toString();
        intent.putExtra("nama", welcomeText);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    private void setProfileImage() {
        ImageView ivProfile = findViewById(R.id.ivProfile);

        Glide.with(this)
                .load(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .circleCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        // Handle failed load
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        // Handle successful load
                        return false;
                    }
                })
                .into(ivProfile);
    }
}