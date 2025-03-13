package com.example.praktikum3_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword, etNama;
    private MaterialButton btnSubmit;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNama = findViewById(R.id.etNama);
        btnSubmit = findViewById(R.id.imgbtnSubmit);
        btnLogin = findViewById(R.id.tvLogin);

        // Set click listeners
        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String nama = etNama.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || nama.isEmpty()) {
                displayError("Please fill in all fields");
                return;
            }

            prosesSubmit(email, nama, password);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void prosesSubmit(String email, String nama, String password) {
        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);
        api.register(email, password, nama).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        displayError("Registration failed: " + response.code());
                        return;
                    }

                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("status").equals("1")) {
                        new AlertDialog.Builder(Register.this)
                                .setTitle("Registration Successful")
                                .setMessage("Please login with your account")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .show();
                    } else {
                        displayError("Registration failed: " + json.getString("message"));
                    }
                } catch (JSONException | IOException e) {
                    displayError("Error: " + e.getMessage());
                    Log.e("Register", "Error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new AlertDialog.Builder(Register.this)
                        .setTitle("Network Error")
                        .setMessage("Please check your internet connection: " + t.getMessage())
                        .setNegativeButton("Retry", null)
                        .show();
            }
        });
    }

    private void displayError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}