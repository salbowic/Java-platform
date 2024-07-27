package com.example.konsultacjeplusv6.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.model.Forum;
import com.example.konsultacjeplusv6.model.Przedmiot;
import com.example.konsultacjeplusv6.model.Realizacja;
import com.example.konsultacjeplusv6.model.Specjalizacja;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumAdapter extends RecyclerView.Adapter<ForumHolder> {

    private List<Forum> forumList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Forum forum);
    }

    public ForumAdapter(List<Forum> forumList) {
        this.forumList = forumList;
        if (this.forumList == null) {
            this.forumList = new ArrayList<>();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ForumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_forum, parent, false);
        return new ForumHolder(view, forumList, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumHolder holder, int position) {
        Forum forum = forumList.get(position);
        Log.d("ForumAdapter", "Forum data at position " + position + ": " + forum.toString());
        Log.d("ForumAdapter", "Rodzaj forum " + forum.getRodzaj());
        String fullRodzaj = getFullRodzaj(forum.getRodzaj());
        holder.Rodzaj.setText(fullRodzaj);

        fetchSpecjalizacjaData(forum.getNr_specjalizacji(), holder.NazwaSpecjalizacji);

        fetchRealizacjaData(forum.getNr_realizacji(), holder.Semestr);

        fetchPrzedmiotData(forum.getNr_realizacji(), new PrzedmiotCallback() {
            @Override
            public void onPrzedmiotFetched(String nazwaPrzedmiotu) {
                holder.NazwaPrzedmiotu.setText(nazwaPrzedmiotu);
            }
        });

        final ForumHolder finalHolder = holder;

        fetchPrzedmiotData(forum.getNr_realizacji(), new PrzedmiotCallback() {
            @Override
            public void onPrzedmiotFetched(String nazwaPrzedmiotu) {
                finalHolder.NazwaPrzedmiotu.setText(nazwaPrzedmiotu);
            }
        });
    }

    @Override
    public int getItemCount() {
        int forumCount = forumList.size();
        Log.d("Liczba forów", String.valueOf(forumCount));
        return forumCount;
    }

    private void fetchSpecjalizacjaData(int nrSpecjalizacji, TextView textView) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        Call<Specjalizacja> call = apiService.getSpecjalizacjaData(nrSpecjalizacji);
        call.enqueue(new Callback<Specjalizacja>() {
            @Override
            public void onResponse(Call<Specjalizacja> call, Response<Specjalizacja> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Specjalizacja specjalizacja = response.body();
                    String nazwaSpecjalizacji = specjalizacja.getNazwa_specjalizacji();
                    textView.setText(nazwaSpecjalizacji);
                } else {
                    // Handle API error or empty response
                    textView.setText("Error fetching data");
                }
            }

            @Override
            public void onFailure(Call<Specjalizacja> call, Throwable t) {
                // Handle network or other errors
                textView.setText("Error fetching data");
            }
        });
    }

    private void fetchRealizacjaData(int nrRealizacji, final TextView textView) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        apiService.getRealizacjaData(nrRealizacji).enqueue(new Callback<Realizacja>() {
            @Override
            public void onResponse(Call<Realizacja> call, Response<Realizacja> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Realizacja realizacja = response.body();
                    String Semestr = String.valueOf(realizacja.getSemestr());
                    textView.setText(Semestr);
                } else {
                    textView.setText("Error fetching data");
                }
            }

            @Override
            public void onFailure(Call<Realizacja> call, Throwable t) {
                textView.setText("Failed to fetch data");
            }
        });
    }

    private void fetchPrzedmiotData(int nrRealizacji, PrzedmiotCallback callback) {
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

    private void fetchPrzedmiotDataUsingNrPrzedmiotu(int nrPrzedmiotu, PrzedmiotCallback callback) {
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

    public String getFullRodzaj(String rodzaj) {
        switch (rodzaj) {
            case "S":
                return "Dla studentów";
            case "P":
                return "Dla prowadzących";
            case "W":
                return "Dla wszystkich";
            default:
                return "";
        }
    }

    public interface PrzedmiotCallback {
        void onPrzedmiotFetched(String nazwaPrzedmiotu);
    }

}
