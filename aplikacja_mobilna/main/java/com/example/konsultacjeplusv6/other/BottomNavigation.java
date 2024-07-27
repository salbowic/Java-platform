package com.example.konsultacjeplusv6.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.konsultacjeplusv6.ForaActivity;
import com.example.konsultacjeplusv6.KonsultacjeActivity;
import com.example.konsultacjeplusv6.MainActivity;
import com.example.konsultacjeplusv6.ProwadzacyListActivity;
import com.example.konsultacjeplusv6.PrzedmiotyListActivity;
import com.example.konsultacjeplusv6.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;

public class BottomNavigation {

    public static final int ITEM_SUBJECTS = R.id.navigation_subjects;
    public static final int ITEM_TEACHERS = R.id.navigation_teachers;
    public static final int ITEM_PROFILE = R.id.navigation_profil;
    public static final int ITEM_KONSULTACJE = R.id.navigation_konsultacje;
    public static final int ITEM_CHATS = R.id.navigation_chats;

    public static void setupBottomNavigation(Activity activity, int selectedItem, Set<Integer> alwaysTrueItems) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(selectedItem);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (alwaysTrueItems.contains(Integer.valueOf(itemId))) {
                    return true;
                }

                if (itemId == ITEM_SUBJECTS) {
                    activity.startActivity(new Intent(activity, PrzedmiotyListActivity.class));
                    activity.overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == ITEM_TEACHERS) {
                    activity.startActivity(new Intent(activity, ProwadzacyListActivity.class));
                    activity.overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == ITEM_PROFILE) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == ITEM_KONSULTACJE) {
                    activity.startActivity(new Intent(activity, KonsultacjeActivity.class));
                    activity.overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == ITEM_CHATS) {
                    activity.startActivity(new Intent(activity, ForaActivity.class));
                    activity.overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }
}
