package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.konsultacjeplusv6.adapter.KonsultacjaProwadzacyAdapter;
import com.example.konsultacjeplusv6.adapter.KonsultacjaStudentAdapter;
import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KonsultacjeProwadzacyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultacje_prowadzacy);

        recyclerView = findViewById(R.id.konsultacjaList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Prowadzacy prowadzacy = gson.fromJson(userJson, Prowadzacy.class);

        retrieveKonsultacjeByNrProwadzacego(prowadzacy.getNr_prowadzacego());

        ImageButton addKonsultacjaButton = findViewById(R.id.addKonsultacjaButton);
        addKonsultacjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KonsultacjeProwadzacyActivity.this, AddKonsultacjaActivity.class);
                startActivity(intent);
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_KONSULTACJE);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_KONSULTACJE, alwaysTrueItems);
    }


    private void retrieveKonsultacjeByNrProwadzacego(int nrProwadzacego) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        apiService.getKonsultacjeByNrProwadzacego(nrProwadzacego).enqueue(new Callback<List<Konsultacja>>() {
            @Override
            public void onResponse(Call<List<Konsultacja>> call, Response<List<Konsultacja>> response) {
                if (response.isSuccessful()) {
                    List<Konsultacja> konsultacje = response.body();
                    if (konsultacje != null) {
                        populateListView(konsultacje);
                    } else {
                        Log.d("KonsultacjeProwadzacyActivity", "Brak konsultacji na liście");
                    }
                } else {
                    Log.d("KonsultacjeProwadzacyActivity", "Brak odpowiedzi, kod błędu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Konsultacja>> call, Throwable t) {
                Log.d("KonsultacjeProwadzacyActivity", "Fail: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void populateListView(List<Konsultacja> konsultacje) {
        KonsultacjaProwadzacyAdapter konsultacjaProwadzacyAdapter = new KonsultacjaProwadzacyAdapter(this, konsultacje);
        recyclerView.setAdapter(konsultacjaProwadzacyAdapter);
    }
}