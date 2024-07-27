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

import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class ProfilProwadzacyActivity extends AppCompatActivity {

    private TextView prowadzacyNameTextView;
    private TextView prowadzacySurnameTextView;
    private TextView prowadzacyEmailTextView;
    private TextView prowadzacyTitleTextView;
    private TextView prowadzacyNrTelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_prowadzacy);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Prowadzacy prowadzacy = gson.fromJson(userJson, Prowadzacy.class);

        // Zainicjowanie elementów UI
        prowadzacyTitleTextView = findViewById(R.id.prowadzacyTitleTextView);
        prowadzacyNameTextView = findViewById(R.id.prowadzacyNameTextView);
        prowadzacySurnameTextView = findViewById(R.id.prowadzacySurnameTextView);
        prowadzacyEmailTextView = findViewById(R.id.prowadzacyEmailTextView);
        prowadzacyNrTelTextView = findViewById(R.id.prowadzacyNrTelTextView);

        String prowadzacyTytul = prowadzacy.getFullTytul();
        String prowadzacyName = prowadzacy.getImie();
        String prowadzacySurname = prowadzacy.getNazwisko();
        String prowadzacyEmail = prowadzacy.getEmail();
        String prowadzacyNr_telefonu = prowadzacy.getNr_telefonu();

        // Ustawienie wartości elementów UI
        prowadzacyTitleTextView.setText(prowadzacyTytul);
        prowadzacyNameTextView.setText(prowadzacyName);
        prowadzacySurnameTextView.setText(prowadzacySurname);
        prowadzacyEmailTextView.setText(prowadzacyEmail);
        prowadzacyNrTelTextView.setText(prowadzacyNr_telefonu);

        ImageView prowadzacyImageView = findViewById(R.id.prowadzacyImageView);
        String base64Image = prowadzacy.getZdjecie64();
        if (base64Image != null && !base64Image.isEmpty()) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            prowadzacyImageView.setImageBitmap(decodedBitmap);
        } else {
            if (prowadzacy.getPlec().equals("M")) {
                prowadzacyImageView.setImageResource(R.drawable.default_man);
            } else {
                prowadzacyImageView.setImageResource(R.drawable.default_woman);
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

                Intent intent = new Intent(ProfilProwadzacyActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProwadzacyActivity.this, EditProfilProwadzacyActivity.class);
                startActivity(intent);
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_PROFILE);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_PROFILE, alwaysTrueItems);
    }
}