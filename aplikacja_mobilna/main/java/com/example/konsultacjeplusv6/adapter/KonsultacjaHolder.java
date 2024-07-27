package com.example.konsultacjeplusv6.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;

public class KonsultacjaHolder extends RecyclerView.ViewHolder {

    TextView DataOd, DataDo, NrPokoju, CzyOnline, CzyPubliczne, Pytanie, FullTitle, NazwaPrzedmiotuSemestr;;
    ImageView ProwadzacyImage;

    public KonsultacjaHolder(@NonNull View itemView) {
        super(itemView);
        DataOd = itemView.findViewById(R.id.konsultacjaListItemDataOd);
        DataDo = itemView.findViewById(R.id.konsultacjaListItemDataDo);
        NrPokoju = itemView.findViewById(R.id.konsultacjaListItemNr_pokoju);
        CzyOnline = itemView.findViewById(R.id.konsultacjaListItemCzy_online);
        CzyPubliczne = itemView.findViewById(R.id.konsultacjaListItemCzy_publiczne);
        Pytanie = itemView.findViewById(R.id.konsultacjaListItemPytanie);
        FullTitle = itemView.findViewById(R.id.konsultacjaListItemFullTitle);
        ProwadzacyImage = itemView.findViewById(R.id.konsultacjaListItemImage);
        NazwaPrzedmiotuSemestr = itemView.findViewById(R.id.konsultacjaListItemNazwaPrzedmiotuSemestr);
    }
}
