package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konsultacjeplusv6.model.Student;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class ProfilStudentActivity extends AppCompatActivity {

    private TextView studentNameTextView;
    private TextView studentSurnameTextView;
    private TextView studentEmailTextView;
    private TextView studentIndexTextView;
    private TextView studentNrTelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_student);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Student student = gson.fromJson(userJson, Student.class);

        // Zainicjowanie elementów UI
        studentNameTextView = findViewById(R.id.studentNameTextView);
        studentSurnameTextView = findViewById(R.id.studentSurnameTextView);
        studentEmailTextView = findViewById(R.id.studentEmailTextView);
        studentIndexTextView = findViewById(R.id.studentIndexTextView);
        studentNrTelTextView = findViewById(R.id.studentNrTelTextView);

        String studentName = student.getImie();
        String studentSurname = student.getNazwisko();
        String studentEmail = student.getEmail();
        String studentIndex = student.getNr_indeksu();
        String studentNr_telefonu = student.getNr_telefonu();
        String studentPlec = student.getPlec();

        // Ustawienie wartości elementów UI
        studentNameTextView.setText(studentName);
        studentSurnameTextView.setText(studentSurname);
        studentEmailTextView.setText(studentEmail);
        studentIndexTextView.setText(studentIndex);
        studentNrTelTextView.setText(studentNr_telefonu);

        ImageView studentImageView = findViewById(R.id.studentImageView);
        String base64Image = student.getZdjecie64();

        if (base64Image != null && !base64Image.isEmpty()) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            studentImageView.setImageBitmap(decodedBitmap);
        } else {
            if (student.getPlec().equals("M")) {
                studentImageView.setImageResource(R.drawable.default_man);
            } else {
                studentImageView.setImageResource(R.drawable.default_woman);
            }
        }

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(ProfilStudentActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilStudentActivity.this, EditProfilStudentActivity.class);
                startActivity(intent);
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_PROFILE);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_PROFILE, alwaysTrueItems);
    }
}