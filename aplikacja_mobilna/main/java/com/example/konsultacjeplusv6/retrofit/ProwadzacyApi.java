package com.example.konsultacjeplusv6.retrofit;

import com.example.konsultacjeplusv6.model.Prowadzacy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProwadzacyApi {

    @GET("prowadzacy/json")
    Call<List<Prowadzacy>> listAllProwadzacy();

}
