package com.example.konsultacjeplusv6;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Student;
import com.google.gson.Gson;

public class KonsultacjeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultacje);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");

        //Przekierowanie do odpowiedniej aktywności w zależności od rodzaju użytkownika
        if (userType.equals("student")) {
            Gson gson = new Gson();
            Student student = gson.fromJson(userJson, Student.class);

            if (student != null) {
                Intent intent = new Intent(KonsultacjeActivity.this, KonsultacjeStudentActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (userType.equals("prowadzacy")) {
            Gson gson = new Gson();
            Prowadzacy prowadzacy = gson.fromJson(userJson, Prowadzacy.class);

            if (prowadzacy != null) {
                Intent intent = new Intent(KonsultacjeActivity.this, KonsultacjeProwadzacyActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}