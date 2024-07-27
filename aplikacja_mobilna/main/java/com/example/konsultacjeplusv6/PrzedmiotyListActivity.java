package com.example.konsultacjeplusv6;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.adapter.PrzedmiotAdapter;
import com.example.konsultacjeplusv6.model.Przedmiot;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.PrzedmiotyApi;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrzedmiotyListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Przedmiot> allPrzedmiotyList;
    private List<Przedmiot> filteredPrzedmiotyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_przedmioty_list);

        recyclerView = findViewById(R.id.przedmiotyList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allPrzedmiotyList = new ArrayList<>();
        filteredPrzedmiotyList = new ArrayList<>();

        loadPrzedmioty();
        setupSearchBar();

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_SUBJECTS);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_SUBJECTS, alwaysTrueItems);
    }
    private void loadPrzedmioty() {
        RetrofitService retrofitService = new RetrofitService();
        PrzedmiotyApi przedmiotyApi = retrofitService.getRetrofit().create(PrzedmiotyApi.class);
        przedmiotyApi.listAllPrzedmiot()
                .enqueue(new Callback<List<Przedmiot>>() {
                    @Override
                    public void onResponse(Call<List<Przedmiot>> call, Response<List<Przedmiot>> response) {
                        allPrzedmiotyList = response.body();
                        filteredPrzedmiotyList = new ArrayList<>(allPrzedmiotyList);
                        populateListView(filteredPrzedmiotyList);
                    }

                    @Override
                    public void onFailure(Call<List<Przedmiot>> call, Throwable t) {
                        Toast.makeText(PrzedmiotyListActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Przedmiot> przedmiotList) {
        PrzedmiotAdapter przedmiotAdapter = new PrzedmiotAdapter(przedmiotList);
        recyclerView.setAdapter(przedmiotAdapter);
    }

    private void setupSearchBar() {
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterPrzedmioty(searchEditText.getText().toString());
                return true;
            }
            return false;
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPrzedmioty(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterPrzedmioty(String query) {
        filteredPrzedmiotyList.clear();
        for (Przedmiot przedmiot : allPrzedmiotyList) {
            if (przedmiot.getNazwa_przedmiotu().toLowerCase().contains(query.toLowerCase())) {
                filteredPrzedmiotyList.add(przedmiot);
            }
        }
        populateListView(filteredPrzedmiotyList);
    }
}
