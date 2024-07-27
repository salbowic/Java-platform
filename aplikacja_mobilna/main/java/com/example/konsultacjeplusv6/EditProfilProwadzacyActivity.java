package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilProwadzacyActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextNrTel;
    private TextView prowadzacyNameTextView;
    private TextView prowadzacySurnameTextView;
    private TextView prowadzacyTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_prowadzacy);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Prowadzacy prowadzacy = gson.fromJson(userJson, Prowadzacy.class);

        prowadzacyNameTextView = findViewById(R.id.prowadzacyNameTextView);
        prowadzacySurnameTextView = findViewById(R.id.prowadzacySurnameTextView);
        prowadzacyTitleTextView = findViewById(R.id.prowadzacyTitleTextView);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNrTel = findViewById(R.id.editTextNrTel);

        String prowadzacyName = prowadzacy.getImie();
        String prowadzacySurname = prowadzacy.getNazwisko();
        String prowadzacyEmail = prowadzacy.getEmail();
        String prowadzacyTitle = prowadzacy.getFullTytul();
        String prowadzacyNr_telefonu = prowadzacy.getNr_telefonu();
        int nrProwadzacego = prowadzacy.getNr_prowadzacego();

        // Ustawienie wartości dla elementów graficznych
        prowadzacyNameTextView.setText(prowadzacyName);
        prowadzacySurnameTextView.setText(prowadzacySurname);
        prowadzacyTitleTextView.setText(prowadzacyTitle);
        editTextEmail.setText(prowadzacyEmail);
        editTextNrTel.setText(prowadzacyNr_telefonu);

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

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedEmail = editTextEmail.getText().toString().trim();
                String updatedNrTel = editTextNrTel.getText().toString().trim();
                if (!updatedEmail.equals(prowadzacyEmail) || !updatedNrTel.equals(prowadzacyNr_telefonu)) {
                    RetrofitService retrofitService = new RetrofitService();
                    ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                    Map<String, String> data = new HashMap<>();
                    data.put("email", updatedEmail);
                    data.put("nrTel", updatedNrTel);
                    Call<Void> call = apiService.updateProwadzacy(nrProwadzacego, data);
                    Log.d("API_CALL", "Zaktualizowano nrTel: " + updatedNrTel);
                    Log.d("API_CALL", "Zaktualizowano Email: " + updatedEmail);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                prowadzacy.setEmail(updatedEmail);
                                prowadzacy.setNr_telefonu(updatedNrTel);
                                String updatedUserJson = gson.toJson(prowadzacy);
                                sharedPreferences.edit().putString("userJson", updatedUserJson).apply();
                                Toast.makeText(EditProfilProwadzacyActivity.this, "Zapisano zmiany", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfilProwadzacyActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(EditProfilProwadzacyActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(EditProfilProwadzacyActivity.this, "Fail 2", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditProfilProwadzacyActivity.this, "Brak dokonanych zmian", Toast.LENGTH_SHORT).show();
                }
            }

        });

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_PROFILE, alwaysTrueItems);
    }
}
