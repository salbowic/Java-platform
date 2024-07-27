package com.example.konsultacjeplusv6.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;

public class PrzedmiotHolder extends RecyclerView.ViewHolder {

    TextView Nazwa_przedmiotu, Skrot_przedmiotu, ECTS;

    public PrzedmiotHolder(@NonNull View itemView) {
        super(itemView);
        Nazwa_przedmiotu = itemView.findViewById(R.id.przedmiotListItem_name);
        Skrot_przedmiotu = itemView.findViewById(R.id.przedmiotListItem_shortcut);
        ECTS = itemView.findViewById(R.id.przedmiotListItemECTS);
    }
}
