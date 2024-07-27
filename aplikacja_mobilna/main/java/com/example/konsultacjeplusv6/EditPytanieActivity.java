package com.example.konsultacjeplusv6;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.konsultacjeplusv6.adapter.ForumAdapter;
import com.example.konsultacjeplusv6.adapter.KonsultacjaStudentAdapter;
import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Przedmiot;
import com.example.konsultacjeplusv6.model.Realizacja;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPytanieActivity extends AppCompatActivity {

    private ApiService apiService;
    private EditText questionEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pytanie);

        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(ApiService.class);

        questionEditText = findViewById(R.id.questionEditText);
        saveButton = findViewById(R.id.saveButton);

        Konsultacja konsultacja = getIntent().getParcelableExtra("konsultacja");



        if (konsultacja != null) {
            questionEditText.setText(konsultacja.getPytanie());
            TextView dataOdTextView = findViewById(R.id.konsultacjaListItemDataOd);
            TextView dataDoTextView = findViewById(R.id.konsultacjaListItemDataDo);
            TextView nrPokojuTextView = findViewById(R.id.konsultacjaListItemNr_pokoju);
            TextView czyOnlineTextView = findViewById(R.id.konsultacjaListItemCzy_online);
            TextView czyPubliczneTextView = findViewById(R.id.konsultacjaListItemCzy_publiczne);
            TextView fullTitleTextView = findViewById(R.id.konsultacjaListItemFullTitle);
            ImageView prowadzacyImageView = findViewById(R.id.konsultacjaListItemImage);
            TextView nazwaPrzedmiotuSemestrTextView = findViewById(R.id.konsultacjaListItemNazwaPrzedmiotuSemestr);
            dataOdTextView.setText(konsultacja.getFormattedDataOd());
            dataDoTextView.setText(konsultacja.getFormattedDataDo());
            nrPokojuTextView.setText(String.valueOf(konsultacja.getNr_pokoju()));
            String czyOnline = konsultacja.getCzy_online();
            if (czyOnline.equals("N")) {
                czyOnlineTextView.setText("Offline");
            } else if (czyOnline.equals("T")) {
                czyOnlineTextView.setText("Online");
            } else {
                czyOnlineTextView.setText("Unknown");
            }
            String czyPubliczne = konsultacja.getCzy_publiczne();
            if (czyPubliczne.equals("N")) {
                czyPubliczneTextView.setText("Prywatne");
            } else if (czyPubliczne.equals("T")) {
                czyPubliczneTextView.setText("Publiczne");
            } else {
                czyPubliczneTextView.setText("Unknown");
            }

            apiService.getProwadzacyById(konsultacja.getNr_prowadzacego())
                    .enqueue(new Callback<Prowadzacy>() {
                        @Override
                        public void onResponse(Call<Prowadzacy> call, Response<Prowadzacy> response) {
                            if (response.isSuccessful()) {
                                Prowadzacy prowadzacy = response.body();
                                if (prowadzacy != null) {
                                    // Set the full title of Prowadzacy
                                    String fullTitle = prowadzacy.getFullTytul() +
                                            " " + prowadzacy.getImie() +
                                            " " + prowadzacy.getNazwisko();
                                    fullTitleTextView.setText(fullTitle);
                                    loadProwadzacyImage(prowadzacyImageView, prowadzacy.getNr_prowadzacego());
                                }
                            } else {
                                Log.d("EditPytanieActivity", "Błąd załadowania prowadzącego");
                            }
                        }

                        @Override
                        public void onFailure(Call<Prowadzacy> call, Throwable t) {
                            Log.e("EditPytanieActivity", "API Call Fail: " + t.getMessage());
                            t.printStackTrace();
                        }
                    });

            fetchPrzedmiotData(konsultacja.getNr_realizacji(), new ForumAdapter.PrzedmiotCallback() {
                @Override
                public void onPrzedmiotFetched(String nazwaPrzedmiotu) {
                    fetchRealizacjaData(konsultacja.getNr_realizacji(), new KonsultacjaStudentAdapter.RealizacjaCallback() {
                        @Override
                        public void onRealizacjaFetched(String semestr) {
                            String combinedText = nazwaPrzedmiotu + " " + semestr;
                            nazwaPrzedmiotuSemestrTextView.setText(combinedText);
                        }
                    });
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newQuestion = questionEditText.getText().toString().trim();
                    if (!newQuestion.isEmpty()) {
                        int nrKonsultacji = konsultacja.getNr_konsultacji();
                        updateQuestion(nrKonsultacji, newQuestion);
                    } else {
                        Toast.makeText(EditPytanieActivity.this, "Proszę wprowadzić pytanie", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
        }

        Button buttonCancel = findViewById(R.id.cancelButton);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_KONSULTACJE, alwaysTrueItems);
    }

    private void updateQuestion(int nrKonsultacji, String newQuestion) {
        Call<Void> call = apiService.updateQuestion(nrKonsultacji, newQuestion);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPytanieActivity.this, "Pytanie zostało zaktualizowane", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditPytanieActivity.this, "Nieudana aktualizacja pytania", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditPytanieActivity.this, "Błąd połączenia", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProwadzacyImage(ImageView imageView, int nrProwadzacego) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        Call<ResponseBody> call = apiService.getProwadzacyImage(nrProwadzacego);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] decodedString = Base64.decode(response.body().bytes(), Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(decodedBitmap);
                    } catch (IOException e) {
                        imageView.setImageResource(R.drawable.baseline_school_24);
                    }
                } else {
                    imageView.setImageResource(R.drawable.baseline_school_24);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageView.setImageResource(R.drawable.baseline_school_24);
            }
        });
    }

    private void fetchRealizacjaData(int nrRealizacji, final KonsultacjaStudentAdapter.RealizacjaCallback callback) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        apiService.getRealizacjaData(nrRealizacji).enqueue(new Callback<Realizacja>() {
            @Override
            public void onResponse(Call<Realizacja> call, Response<Realizacja> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Realizacja realizacja = response.body();
                    String semestr = String.valueOf(realizacja.getSemestr());
                    callback.onRealizacjaFetched(semestr);
                } else {
                    callback.onRealizacjaFetched("Error");
                }
            }

            @Override
            public void onFailure(Call<Realizacja> call, Throwable t) {
                callback.onRealizacjaFetched("Fail");
            }
        });
    }

    public interface RealizacjaCallback {
        void onRealizacjaFetched(String semestr);
    }

    private void fetchPrzedmiotData(int nrRealizacji, ForumAdapter.PrzedmiotCallback callback) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        Call<Realizacja> call = apiService.getRealizacjaData(nrRealizacji);
        call.enqueue(new Callback<Realizacja>() {
            @Override
            public void onResponse(Call<Realizacja> call, Response<Realizacja> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Realizacja realizacja = response.body();
                    int nrPrzedmiotu = realizacja.getNr_przedmiotu();

                    fetchPrzedmiotDataUsingNrPrzedmiotu(nrPrzedmiotu, callback);
                } else {
                    callback.onPrzedmiotFetched("Error");
                }
            }

            @Override
            public void onFailure(Call<Realizacja> call, Throwable t) {
                callback.onPrzedmiotFetched("Fail");
            }
        });
    }

    private void fetchPrzedmiotDataUsingNrPrzedmiotu(int nrPrzedmiotu, ForumAdapter.PrzedmiotCallback callback) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        Call<Przedmiot> call = apiService.getPrzedmiotData(nrPrzedmiotu);
        call.enqueue(new Callback<Przedmiot>() {
            @Override
            public void onResponse(Call<Przedmiot> call, Response<Przedmiot> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Przedmiot przedmiot = response.body();
                    String nazwaPrzedmiotu = przedmiot.getNazwa_przedmiotu();
                    callback.onPrzedmiotFetched(nazwaPrzedmiotu);
                } else {
                    callback.onPrzedmiotFetched("Błąd ładowania danych");
                }
            }

            @Override
            public void onFailure(Call<Przedmiot> call, Throwable t) {
                callback.onPrzedmiotFetched("Błąd ładowania danych");
            }
        });
    }

}
