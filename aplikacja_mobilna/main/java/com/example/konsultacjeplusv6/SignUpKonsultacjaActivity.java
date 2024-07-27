package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.konsultacjeplusv6.adapter.SignUpKonsultacjaAdapter;
import com.example.konsultacjeplusv6.adapter.SignUpKonsultacjaHolder;
import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.Student;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpKonsultacjaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SignUpKonsultacjaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_konsultacja);
        recyclerView = findViewById(R.id.konsultacjaList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SignUpKonsultacjaAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SignUpKonsultacjaHolder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Konsultacja konsultacja = adapter.getItem(position);
                if (konsultacja != null) {
                    Log.d("Konsultacja", "Próba zapisu na konsultacje o nr: " + konsultacja.getNr_konsultacji());
                    signUpForKonsultacja(konsultacja);
                } else {
                    Log.d("Konsultacja", "Błąd zapisu na konsultację z listy: " + position);
                }
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_KONSULTACJE, alwaysTrueItems);
    }

    private void signUpForKonsultacja(Konsultacja konsultacja) {
        int nrKonsultacji = konsultacja.getNr_konsultacji();
        int nrStudenta = getLoggedInStudentNr();

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        Call<Boolean> checkCall = apiService.checkStudentKonsultacja(nrKonsultacji, nrStudenta);
        checkCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isSignedUp = response.body();
                    if (!isSignedUp) {
                        showSignUpConfirmationDialog(nrKonsultacji, nrStudenta);
                    } else {
                        showAlreadySignedUpDialog();
                    }
                } else {
                    Log.d("Konsultacja", "Fail");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Konsultacja", "Error: " + t.getMessage());
            }
        });
    }

    private void showSignUpConfirmationDialog(int nrKonsultacji, int nrStudenta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potwierdzenie");
        builder.setMessage("Na pewno chcesz się zapisać na tę konsultację?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performSignUp(nrKonsultacji, nrStudenta);
            }
        });
        builder.setNegativeButton("Nie", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlreadySignedUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zapisano");
        builder.setMessage("Jesteś już zapisany na tę konsultację.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performSignUp(int nrKonsultacji, int nrStudenta) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        Call<Void> signUpCall = apiService.signUpForKonsultacja(nrKonsultacji, nrStudenta);
        signUpCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SignUpKonsultacjaActivity", "Zapisano na konsultację");
                    startActivity(new Intent(SignUpKonsultacjaActivity.this, KonsultacjeActivity.class));
                } else {
                    Log.d("SignUpKonsultacjaActivity", "Nie zapisano na konsultację");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("SignUpKonsultacjaActivity", "Błąd połączenia przy zapisie");
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