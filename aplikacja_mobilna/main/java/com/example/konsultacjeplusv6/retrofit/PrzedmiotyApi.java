package com.example.konsultacjeplusv6.retrofit;

import com.example.konsultacjeplusv6.model.Przedmiot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PrzedmiotyApi {

    @GET("przedmioty/json")
    Call<List<Przedmiot>> listAllPrzedmiot();

}
