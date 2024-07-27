package com.example.konsultacjeplusv6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.adapter.ForumAdapter;
import com.example.konsultacjeplusv6.model.Forum;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fora);

        recyclerView = findViewById(R.id.forumList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFora();

        Set<Integer> alwaysTrueItems = new HashSet<>();
        alwaysTrueItems.add(BottomNavigation.ITEM_CHATS);
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_CHATS, alwaysTrueItems);

    }

    private void loadFora() {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        apiService.listAllFora()
                .enqueue(new Callback<List<Forum>>() {
                    @Override
                    public void onResponse(Call<List<Forum>> call, Response<List<Forum>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Forum>> call, Throwable t) {
                        Toast.makeText(ForaActivity.this, "Błąd załadowania forów", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Forum> forumList) {
        ForumAdapter forumAdapter = new ForumAdapter(forumList);
        recyclerView.setAdapter(forumAdapter);
        forumAdapter.setOnItemClickListener(new ForumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Forum forum) {
                Intent intent = new Intent(ForaActivity.this, ChatActivity.class);
                intent.putExtra("forumId", forum.getNr_forum());
                startActivity(intent);
            }
        });
    }
}