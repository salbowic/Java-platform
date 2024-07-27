package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.konsultacjeplusv6.adapter.KonsultacjaStudentAdapter;
import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.Student;
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

public class KonsultacjeStudentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultacje_student);

        recyclerView = findViewById(R.id.konsultacjaList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Student student = gson.fromJson(userJson, Student.class);

        retrieveKonsultacjeByNrStudenta(student.getNr_studenta());

        ImageButton signUpButton = findViewById(R.id.SignUpForKonsultacjaButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KonsultacjeStudentActivity.this, SignUpKonsultacjaActivity.class);
                startActivity(intent);
            }


        });

        ImageButton signOutButton = findViewById(R.id.SignOutFromKonsultacjaButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KonsultacjeStudentActivity.this, SignOutFromKonsultacjeStudentActivity.class);
                startActivity(intent);
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_KONSULTACJE);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_KONSULTACJE, alwaysTrueItems);

    }

    private void retrieveKonsultacjeByNrStudenta(int nrStudenta) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        apiService.getKonsultacjeByNrStudenta(nrStudenta).enqueue(new Callback<List<Konsultacja>>() {
            @Override
            public void onResponse(Call<List<Konsultacja>> call, Response<List<Konsultacja>> response) {
                if (response.isSuccessful()) {
                    List<Konsultacja> konsultacje = response.body();
                    if (konsultacje != null) {
                        populateListView(konsultacje);
                    } else {
                        Toast.makeText(KonsultacjeStudentActivity.this, "Brak konsultacji", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KonsultacjeStudentActivity.this, "Błąd odpowiedzi serwera", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Konsultacja>> call, Throwable t) {
                Toast.makeText(KonsultacjeStudentActivity.this, "Błąd ładowania konsultacji", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateListView(List<Konsultacja> konsultacje) {
        KonsultacjaStudentAdapter konsultacjaStudentAdapter = new KonsultacjaStudentAdapter(this, konsultacje);
        recyclerView.setAdapter(konsultacjaStudentAdapter);
    }
}