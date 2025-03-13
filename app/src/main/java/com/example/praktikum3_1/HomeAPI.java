package com.example.praktikum3_1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeAPI {
    @GET("get_product.php")
    Call<List<Product>> getProducts();
}
