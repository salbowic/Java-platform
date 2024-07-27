package com.example.konsultacjeplusv6;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.konsultacjeplusv6.adapter.WiadomoscAdapter;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Student;
import com.example.konsultacjeplusv6.model.Wiadomosc;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import com.google.gson.Gson;

import java.sql.Timestamp;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;

import java.util.Set;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextNewMessage;
    private ImageButton buttonSendMessage;
    private List<Wiadomosc> wiadomoscList;
    private WiadomoscAdapter wiadomoscAdapter;
    private String nadawca;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        int forumId = getIntent().getIntExtra("forumId", -1);

        // Pobranie danych  z SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        String loggedInUserNadawca = sharedPreferences.getString("nadawca", "");

        // Stworzenie string nadawca w zaleznosci od typu uzytkownika
        if ("Student".equals(userType)) {
            Student studentNadawca = parseStudentFromJson(userJson);
            nadawca = "Stud. " + studentNadawca.getImie() + " " + studentNadawca.getNazwisko();
        } else if ("Prowadzacy".equals(userType)) {
            Prowadzacy prowadzacyNadawca = parseProwadzacyFromJson(userJson);
            nadawca = prowadzacyNadawca.getFullTytul() + " " + prowadzacyNadawca.getImie() + " "
                    + prowadzacyNadawca.getNazwisko();
        } else {
            nadawca = "Unknown User";
        }
        // Zainicjalizowanie widokow
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextNewMessage = findViewById(R.id.editTextNewMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        wiadomoscList = new ArrayList<>();
        wiadomoscAdapter = new WiadomoscAdapter(wiadomoscList, loggedInUserNadawca);
        recyclerViewMessages.setAdapter(wiadomoscAdapter);

        wiadomoscAdapter.notifyDataSetChanged();

        // Ustawienie nasłuchiwania przycisku do wysyłania wiadomości
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessageText = editTextNewMessage.getText().toString().trim();
                if (!newMessageText.isEmpty()) {
                    wiadomoscList.add(new Wiadomosc(0, nadawca, newMessageText, getCurrentDateTime(), 0, null, null));
                    wiadomoscAdapter.notifyDataSetChanged();
                    editTextNewMessage.setText("");
                }
            }
        });

        loadMessagesForForum(forumId);

        Set<Integer> alwaysTrueItems = new HashSet<>();
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_CHATS, alwaysTrueItems);
    }

    private void loadMessagesForForum(int forumId) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        apiService.getMessagesForForum(forumId)
                .enqueue(new Callback<List<Wiadomosc>>() {
                    @Override
                    public void onResponse(Call<List<Wiadomosc>> call, Response<List<Wiadomosc>> response) {
                        if (response.isSuccessful()) {
                            List<Wiadomosc> messages = response.body();
                            if (messages != null) {
                                // Clear the existing list and add the new messages
                                wiadomoscList.clear();
                                wiadomoscList.addAll(messages);
                                // Notify the adapter that the data has changed
                                wiadomoscAdapter.notifyDataSetChanged();
                                Log.d("API_RESPONSE", "Response successful. Number of messages received: " + messages.size());

                                // Przewijanie listy wiadomości na dół
                                recyclerViewMessages.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerViewMessages.scrollToPosition(wiadomoscAdapter.getItemCount() - 1);
                                    }
                                });
                            }
                        } else {
                            // obsługa błędów
                            Toast.makeText(ChatActivity.this, "Błąd załadowania wiadomości", Toast.LENGTH_SHORT).show();
                            Log.e("API_RESPONSE", "Error. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Wiadomosc>> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void addNewMessageToForum(int forumId, String messageText) {
        String timestamp = String.valueOf(getCurrentDateTime());
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");

        String nadawca;
        Integer nrStudenta = null;
        Integer nrProwadzacego = null;

        // Stworzenie string nadawca w zaleznosci od typu uzytkownika
        if ("Student".equals(userType)) {
            Student studentNadawca = parseStudentFromJson(userJson);
            nadawca = "Stud. " + studentNadawca.getImie() + " " + studentNadawca.getNazwisko();
            nrStudenta = studentNadawca.getNr_studenta();
        } else if ("Prowadzacy".equals(userType)) {
            Prowadzacy prowadzacyNadawca = parseProwadzacyFromJson(userJson);
            nadawca = prowadzacyNadawca.getFullTytul() + " " + prowadzacyNadawca.getImie() + " "
                    + prowadzacyNadawca.getNazwisko();
            nrProwadzacego = prowadzacyNadawca.getNr_prowadzacego();
        } else {
            nadawca = "Unknown User";
        }

        // Porównanie nadawcy z nadawcą zapisanym w SharedPreferences
        Integer loggedUserId = null;
        if ("Student".equals(userType)) {
            Student studentNadawca = parseStudentFromJson(userJson);
            loggedUserId = studentNadawca.getNr_studenta();
        } else if ("Prowadzacy".equals(userType)) {
            Prowadzacy prowadzacyNadawca = parseProwadzacyFromJson(userJson);
            loggedUserId = prowadzacyNadawca.getNr_prowadzacego();
        }

        Call<Void> call = apiService.addNewMessage(forumId, nadawca, messageText, timestamp, nrStudenta, nrProwadzacego);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadMessagesForForum(forumId);
                    editTextNewMessage.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Nie udało się wysłać wiadomości", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Timestamp getCurrentDateTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return new Timestamp(currentTimeMillis);
    }

    private Student parseStudentFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Student.class);
    }

    private Prowadzacy parseProwadzacyFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Prowadzacy.class);
    }
}
