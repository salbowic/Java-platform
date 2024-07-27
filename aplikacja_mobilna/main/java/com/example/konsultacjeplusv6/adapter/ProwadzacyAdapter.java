package com.example.konsultacjeplusv6.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.model.Prowadzacy;

import java.util.List;

public class ProwadzacyAdapter extends RecyclerView.Adapter<ProwadzacyHolder> {

    private List<Prowadzacy> prowadzacyList;

    public ProwadzacyAdapter(List<Prowadzacy> prowadzacyList) {
        this.prowadzacyList = prowadzacyList;
    }

    @NonNull
    @Override
    public ProwadzacyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_prowadzacy, parent, false);
        return new ProwadzacyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProwadzacyHolder holder, int position) {
        Prowadzacy prowadzacy = prowadzacyList.get(position);

        holder.Imie.setText(prowadzacy.getImie());
        holder.Nazwisko.setText(prowadzacy.getNazwisko());
        holder.Tytul.setText(prowadzacy.getFullTytul());
        holder.Email.setText(prowadzacy.getEmail());
        holder.Nr_telefonu.setText(prowadzacy.getNr_telefonu());

        if (prowadzacy.getZdjecie64() != null) {
            byte[] decodedString = Base64.decode(prowadzacy.getZdjecie64(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.Zdjecie.setImageBitmap(decodedBitmap);
        } else {
            if (prowadzacy.getPlec().equals("M")) {
                holder.Zdjecie.setImageResource(R.drawable.default_man);
            } else {
                holder.Zdjecie.setImageResource(R.drawable.default_woman);
            }
        }
    }

    @Override
    public int getItemCount() {
        return prowadzacyList.size();
    }

}
