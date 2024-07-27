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

import com.example.konsultacjeplusv6.model.Student;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.ProwadzacyApi;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilStudentActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextNrTel;
    private TextView studentNameTextView;
    private TextView studentSurnameTextView;
    private TextView studentIndexTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_student);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Student student = gson.fromJson(userJson, Student.class);

        // Zainicjowanie elementów UI
        studentNameTextView = findViewById(R.id.studentNameTextView);
        studentSurnameTextView = findViewById(R.id.studentSurnameTextView);
        studentIndexTextView = findViewById(R.id.studentIndexTextView);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNrTel = findViewById(R.id.editTextNrTel);

        String studentName = student.getImie();
        String studentSurname = student.getNazwisko();
        String studentEmail = student.getEmail();
        String studentIndex = student.getNr_indeksu();
        String studentNr_telefonu = student.getNr_telefonu();
        String studentPlec = student.getPlec();
        int nrStudenta = student.getNr_studenta();

        // Ustawienie wartości elementów UI
        studentNameTextView.setText(studentName);
        studentSurnameTextView.setText(studentSurname);
        studentIndexTextView.setText(studentIndex);
        editTextEmail.setText(studentEmail);
        editTextNrTel.setText(studentNr_telefonu);

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

        // Przycisk do zapisywania zmian
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedEmail = editTextEmail.getText().toString().trim();
                String updatedNrTel = editTextNrTel.getText().toString().trim();
                if (!updatedEmail.equals(studentEmail) || !updatedNrTel.equals(studentNr_telefonu)) {
                    RetrofitService retrofitService = new RetrofitService();
                    ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                    Map<String, String> data = new HashMap<>();
                    data.put("email", updatedEmail);
                    data.put("nrTel", updatedNrTel);
                    Call<Void> call = apiService.updateStudent(nrStudenta, data);
                    Log.d("API_CALL", "Zaktualizowano nrTel: " + updatedNrTel);
                    Log.d("API_CALL", "Zaktualizowano Email: " + updatedEmail);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                student.setEmail(updatedEmail);
                                student.setNr_telefonu(updatedNrTel);
                                String updatedUserJson = gson.toJson(student);
                                sharedPreferences.edit().putString("userJson", updatedUserJson).apply();
                                Toast.makeText(EditProfilStudentActivity.this, "Zapisano zmiany", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfilStudentActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(EditProfilStudentActivity.this, "Fail 1", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(EditProfilStudentActivity.this, "Fail 2", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditProfilStudentActivity.this, "Brak dokonanych zmian", Toast.LENGTH_SHORT).show();
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
