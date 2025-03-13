package com.example.praktikum3_1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProductAdapter productAdapter;
    private MaterialToolbar toolbar;
    private MaterialTextView tvWelcome;
    private ShimmerFrameLayout shimmerFrameLayout;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        loadProducts();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        tvWelcome = findViewById(R.id.tvWelcome);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);

        // Set welcome text
        String nama = getIntent().getStringExtra("nama");
        tvWelcome.setText("Welcome, " + nama);
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.accent);
        swipeRefresh.setOnRefreshListener(() -> {
            productList.clear();
            productAdapter.notifyDataSetChanged();
            loadProducts();
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProducts() {
        shimmerFrameLayout.startShimmer();

        HomeAPI api = ServerAPI.getClient().create(HomeAPI.class);
        api.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    showToast("Failed to load products");
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(android.view.View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                showToast("Network error: " + t.getMessage());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(android.view.View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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