package com.example.konsultacjeplusv6;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.konsultacjeplusv6.model.LoginResponse;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Student;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Zainicjowanie elementów UI
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Zainicjowanie API poprzez Retrofit
        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(ApiService.class);

        // Sprawdzenie czy użytkownik jest już zalogowany
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");

        if (!userType.isEmpty() && !userJson.isEmpty()) {
            // Użytkownik jest zalogowany, przekierowanie do profilu
            if (userType.equals("student")) {
                Intent intent = new Intent(LoginActivity.this, ProfilStudentActivity.class);
                startActivity(intent);
                finish();
            } else if (userType.equals("prowadzacy")) {
                Intent intent = new Intent(LoginActivity.this, ProfilProwadzacyActivity.class);
                startActivity(intent);
                finish();
            }
        }

        // Zainicjowanie przycisku logowania
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Log.d("LoginActivity", "Email: " + email);
                Log.d("LoginActivity", "Password: " + password);

                Map<String, String> credentials = new HashMap<>();
                credentials.put("email", email);
                credentials.put("password", password);

                // Wykorzystanie API do potwierdzenia danych logowania użytkownika
                Call<LoginResponse> call = apiService.loginMobile(credentials);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            Log.d("LoginActivity", "Odpowiedź logowania: " + loginResponse);
                            if (loginResponse != null) {
                                Log.d("LoginActivity", "Student: " + loginResponse.getStudent());
                                Log.d("LoginActivity", "Prowadzacy: " + loginResponse.getProwadzacy());
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                if (loginResponse.getStudent() != null) {
                                    Student student = loginResponse.getStudent();
                                    Gson gson = new Gson();
                                    String studentJson = gson.toJson(student);
                                    editor.putString("userType", "student");
                                    editor.putString("userJson", studentJson);
                                    String nadawca = "Stud. " + student.getImie() + " " + student.getNazwisko();
                                    editor.putString("nadawca", nadawca);
                                    editor.apply();
                                    Intent intent = new Intent(LoginActivity.this, ProfilStudentActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (loginResponse.getProwadzacy() != null) {
                                    Prowadzacy prowadzacy = loginResponse.getProwadzacy();
                                    Gson gson = new Gson();
                                    String prowadzacyJson = gson.toJson(prowadzacy);
                                    editor.putString("userType", "prowadzacy");
                                    editor.putString("userJson", prowadzacyJson);
                                    String nadawca = prowadzacy.getFullTytul() + " " + prowadzacy.getImie() + " " + prowadzacy.getNazwisko();
                                    editor.putString("nadawca", nadawca);
                                    editor.apply();
                                    Intent intent = new Intent(LoginActivity.this, ProfilProwadzacyActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showToast("Niespodziewana odpowiedź lub brak danych");
                                    Log.e("LoginActivity", "Niespodziewana odpowiedź lub brak danych");
                                }
                            } else {
                                showToast("Niespodziewana odpowiedź lub brak danych");
                                Log.e("LoginActivity", "Niespodziewana odpowiedź lub brak danych");
                            }
                        } else {
                            showToast("Niepoprawny email lub hasło");
                            Log.e("LoginActivity", "Error: " + response.code() + " " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        showToast("Błąd połączenia" + t.getMessage());
                        Log.e("LoginActivity", "Network error: " + t.getMessage());
                    }
                });
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
