package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.konsultacjeplusv6.adapter.SignOutKonsultacjaAdapter;
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

public class SignOutFromKonsultacjeStudentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out_from_konsultacje_student);

        recyclerView = findViewById(R.id.konsultacjaList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Student student = gson.fromJson(userJson, Student.class);

        retrieveKonsultacjeByNrStudenta(student.getNr_studenta());

        Set<Integer> alwaysTrueItems = new HashSet<>();
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
                        Toast.makeText(SignOutFromKonsultacjeStudentActivity.this, "Brak konsultacji", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignOutFromKonsultacjeStudentActivity.this, "Błąd odpowiedzi serwera", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Konsultacja>> call, Throwable t) {
                Toast.makeText(SignOutFromKonsultacjeStudentActivity.this, "Błąd ładowania konsultacji", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateListView(List<Konsultacja> konsultacje) {
        SignOutKonsultacjaAdapter signOutKonsultacjaAdapter = new SignOutKonsultacjaAdapter(konsultacje, this);
        recyclerView.setAdapter(signOutKonsultacjaAdapter);
    }

    public void showSignOutConfirmationDialog(Konsultacja konsultacja) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potwierdzenie");
        builder.setMessage("Na pewno chcesz się wypisać z tej konsultacji?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOutFromKonsultacja(konsultacja);
            }
        });
        builder.setNegativeButton("Nie", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void signOutFromKonsultacja(Konsultacja konsultacja) {
        int nrKonsultacji = konsultacja.getNr_konsultacji();
        int nrStudenta = getLoggedInStudentNr();

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        Call<Void> signOutCall = apiService.signOutFromKonsultacja(nrKonsultacji, nrStudenta);
        signOutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignOutFromKonsultacjeStudentActivity.this, "Wypisano z konsultacji", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignOutFromKonsultacjeStudentActivity.this, KonsultacjeActivity.class));
                } else {
                    Toast.makeText(SignOutFromKonsultacjeStudentActivity.this, "Błąd wypisania z konsultacji 1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle sign-out failure
                Toast.makeText(SignOutFromKonsultacjeStudentActivity.this, "Błąd wypisania z konsultacji 2", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getLoggedInStudentNr() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        if (userType.equals("student")) {
            String studentJson = sharedPreferences.getString("userJson", "");
            if (!studentJson.isEmpty()) {
                Gson gson = new Gson();
                Student student = gson.fromJson(studentJson, Student.class);
                if (student != null) {
                    return student.getNr_studenta();
                }
            }
        }
        return -1;
    }

}