package com.example.praktikum3_1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("post_register.php")
    Call<ResponseBody> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("nama") String name
    );

}