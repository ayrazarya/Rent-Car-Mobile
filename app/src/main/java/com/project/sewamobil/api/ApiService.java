package com.project.sewamobil.api;

import com.project.sewamobil.model.Mobil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/v1/cars")
    Call<List<Mobil>> getCars();

    // Mendapatkan mobil berdasarkan ID
    @GET("cars/{id}")
    Call<Mobil> getCarById(@Path("id") int id);

    static ApiService getInstance() {
        return ApiClient.getRetrofitInstance().create(ApiService.class);
    }
}
