package com.example.konsultacjeplusv6.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProwadzacyHolder extends RecyclerView.ViewHolder {

    TextView Imie, Nazwisko, Tytul, Email, Nr_telefonu;
    CircleImageView Zdjecie;

    public ProwadzacyHolder(@NonNull View itemView) {
        super(itemView);
        Imie = itemView.findViewById(R.id.prowadzacyListItem_name);
        Nazwisko = itemView.findViewById(R.id.prowadzacyListItem_surname);
        Tytul = itemView.findViewById(R.id.prowadzacyListItem_title);
        Email = itemView.findViewById(R.id.prowadzacyListItem_email);
        Nr_telefonu = itemView.findViewById(R.id.prowadzacyListItem_nrtel);
        Zdjecie = itemView.findViewById(R.id.prowadzacyListItem_image);
    }
}