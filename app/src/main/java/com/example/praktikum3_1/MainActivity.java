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

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnSubmit;
    private MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.imgbtnSubmit);
        btnRegister = findViewById(R.id.tvRegister);

        // Set click listeners
        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                displayError("Please fill in all fields");
                return;
            }

            prosesLogin(email, password);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
            finish();
        });
    }

    private void prosesLogin(String email, String password) {
        LoginAPI api = ServerAPI.getClient().create(LoginAPI.class);
        api.login(email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        displayError("Login failed: " + response.code());
                        return;
                    }

                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("status").equals("1")) {
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.putExtra("nama", json.getString("nama"));
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        displayError("Invalid email or password");
                    }
                } catch (JSONException | IOException e) {
                    displayError("Error: " + e.getMessage());
                    Log.e("Login", "Error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                displayError("Network error: " + t.getMessage());
                Log.e("Login", "Network error: " + t.getMessage());
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
}