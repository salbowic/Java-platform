package com.example.konsultacjeplusv6.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.SignOutFromKonsultacjeStudentActivity;
import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Przedmiot;
import com.example.konsultacjeplusv6.model.Realizacja;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignOutKonsultacjaAdapter extends RecyclerView.Adapter<SignOutKonsultacjaHolder> {

    private List<Konsultacja> konsultacjaList;
    private SignOutFromKonsultacjeStudentActivity activitySignOut;

    public SignOutKonsultacjaAdapter(List<Konsultacja> konsultacjaList, SignOutFromKonsultacjeStudentActivity activitySignOut) {
        this.konsultacjaList = konsultacjaList;
        this.activitySignOut = activitySignOut;
    }

    @NonNull
    @Override
    public SignOutKonsultacjaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_konsultacja, parent, false);
        return new SignOutKonsultacjaHolder(view, activitySignOut, konsultacjaList);
    }

    @Override
    public void onBindViewHolder(@NonNull SignOutKonsultacjaHolder holder, int position) {
        Konsultacja konsultacja = konsultacjaList.get(position);

        fetchPrzedmiotData(konsultacja.getNr_realizacji(), new ForumAdapter.PrzedmiotCallback() {
            @Override
            public void onPrzedmiotFetched(String nazwaPrzedmiotu) {
                fetchRealizacjaData(konsultacja.getNr_realizacji(), new RealizacjaCallback() {
                    @Override
                    public void onRealizacjaFetched(String semestr) {
                        String combinedText = nazwaPrzedmiotu + " " + semestr;
                        holder.NazwaPrzedmiotuSemestr.setText(combinedText);
                    }
                });
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        apiService.getProwadzacyById(konsultacja.getNr_prowadzacego())
                .enqueue(new Callback<Prowadzacy>() {
                    @Override
                    public void onResponse(Call<Prowadzacy> call, Response<Prowadzacy> response) {
                        if (response.isSuccessful()) {
                            Prowadzacy prowadzacy = response.body();
                            if (prowadzacy != null) {
                                String fullTitle = prowadzacy.getFullTytul() +
                                        " " + prowadzacy.getImie() +
                                        " " + prowadzacy.getNazwisko();
                                holder.FullTitle.setText(fullTitle);
                                loadProwadzacyImage(holder.ProwadzacyImage, prowadzacy.getNr_prowadzacego());
                            }
                        } else {
                            Log.d("SignOutKonsultacjaAdapter", "Błąd załadowania prowadzącego");
                        }
                    }

                    @Override
                    public void onFailure(Call<Prowadzacy> call, Throwable t) {
                        Log.e("SignOutKonsultacjaAdapter", "API Call Fail: " + t.getMessage());
                        t.printStackTrace();
                    }
                });

        holder.DataOd.setText(konsultacja.getFormattedDataOd());
        holder.DataDo.setText(konsultacja.getFormattedDataDo());
        holder.NrPokoju.setText(String.valueOf(konsultacja.getNr_pokoju()));
        String czyOnline = konsultacja.getCzy_online();
        if (czyOnline.equals("N")) {
            holder.CzyOnline.setText("Offline");
        } else if (czyOnline.equals("T")) {
            holder.CzyOnline.setText("Online");
        } else {
            holder.CzyOnline.setText("Unknown");
        }
        String czyPubliczne = konsultacja.getCzy_publiczne();
        if (czyPubliczne.equals("N")) {
            holder.CzyPubliczne.setText("Prywatne");
        } else if (czyPubliczne.equals("T")) {
            holder.CzyPubliczne.setText("Publiczne");
        } else {
            holder.CzyPubliczne.setText("Unknown");
        }
        String pytanieValue = konsultacja.getPytanie();
        if (pytanieValue == null) {
            holder.Pytanie.setText("Brak");
        } else {
            holder.Pytanie.setText(pytanieValue);
        }

    }

    @Override
    public int getItemCount() {
        return konsultacjaList.size();
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

    private void fetchRealizacjaData(int nrRealizacji, final RealizacjaCallback callback) {
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
                    callback.onPrzedmiotFetched("Error fetching data");
                }
            }

            @Override
            public void onFailure(Call<Realizacja> call, Throwable t) {
                callback.onPrzedmiotFetched("Error fetching data");
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
                    Log.d("ForumAdapter", "Fetched nazwaPrzedmiotu: " + nazwaPrzedmiotu);
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

    public interface PrzedmiotCallback {
        void onPrzedmiotFetched(String nazwaPrzedmiotu);
    }
}

