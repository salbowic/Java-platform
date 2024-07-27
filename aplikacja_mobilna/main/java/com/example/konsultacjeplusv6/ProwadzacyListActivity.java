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

import com.example.konsultacjeplusv6.adapter.ProwadzacyAdapter;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ProwadzacyApi;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProwadzacyListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Prowadzacy> allProwadzacyList;
    private List<Prowadzacy> filteredProwadzacyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prowadzacy_list);

        recyclerView = findViewById(R.id.prowadzacyList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allProwadzacyList = new ArrayList<>();
        filteredProwadzacyList = new ArrayList<>();

        loadProwadzacy();
        setupSearchBar();

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_TEACHERS);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_TEACHERS, alwaysTrueItems);
    }

    private void loadProwadzacy() {
        RetrofitService retrofitService = new RetrofitService();
        ProwadzacyApi prowadzacyApi = retrofitService.getRetrofit().create(ProwadzacyApi.class);
        prowadzacyApi.listAllProwadzacy()
                .enqueue(new Callback<List<Prowadzacy>>() {
                    @Override
                    public void onResponse(Call<List<Prowadzacy>> call, Response<List<Prowadzacy>> response) {
                        allProwadzacyList = response.body();
                        filteredProwadzacyList = new ArrayList<>(allProwadzacyList);
                        populateListView(filteredProwadzacyList);
                    }

                    @Override
                    public void onFailure(Call<List<Prowadzacy>> call, Throwable t) {
                        Toast.makeText(ProwadzacyListActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Prowadzacy> prowadzacyList) {
        ProwadzacyAdapter prowadzacyAdapter = new ProwadzacyAdapter(prowadzacyList);
        recyclerView.setAdapter(prowadzacyAdapter);
    }

    private void setupSearchBar() {
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterProwadzacy(searchEditText.getText().toString());
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
                filterProwadzacy(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterProwadzacy(String query) {
        filteredProwadzacyList.clear();
        for (Prowadzacy prowadzacy : allProwadzacyList) {
            if (prowadzacy.getImie().toLowerCase().contains(query.toLowerCase())
                    || prowadzacy.getNazwisko().toLowerCase().contains(query.toLowerCase())) {
                filteredProwadzacyList.add(prowadzacy);
            }
        }
        populateListView(filteredProwadzacyList);
    }
}