package com.example.konsultacjeplusv6.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.model.Przedmiot;

import java.util.List;

public class PrzedmiotAdapter extends RecyclerView.Adapter<PrzedmiotHolder> {

    private List<Przedmiot> przedmiotList;

    public PrzedmiotAdapter(List<Przedmiot> przedmiotList) {
        this.przedmiotList = przedmiotList;
    }

    @NonNull
    @Override
    public PrzedmiotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_przedmiot, parent, false);
        return new PrzedmiotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrzedmiotHolder holder, int position) {
        Przedmiot przedmiot = przedmiotList.get(position);

        holder.Nazwa_przedmiotu.setText(przedmiot.getNazwa_przedmiotu());
        holder.Skrot_przedmiotu.setText(przedmiot.getSkrot_przedmiotu());
        holder.ECTS.setText(String.valueOf(przedmiot.getECTS()));
    }

    @Override
    public int getItemCount() {
        return przedmiotList.size();
    }
}
