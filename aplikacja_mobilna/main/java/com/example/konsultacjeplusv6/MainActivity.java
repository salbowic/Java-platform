package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.konsultacjeplusv6.databinding.ActivityMainBinding;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Student;
import com.google.gson.Gson;


import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");

        //Przekierowanie do odpowiedniego profilu w zależności od typu zalogowanego użytkownika
        if (userType.equals("student")) {
            Gson gson = new Gson();
            Student student = gson.fromJson(userJson, Student.class);

            if (student != null) {
                Intent intent = new Intent(MainActivity.this, ProfilStudentActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (userType.equals("prowadzacy")) {
            Gson gson = new Gson();
            Prowadzacy prowadzacy = gson.fromJson(userJson, Prowadzacy.class);

            if (prowadzacy != null) {
                Intent intent = new Intent(MainActivity.this, ProfilProwadzacyActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}