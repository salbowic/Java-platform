package com.example.konsultacjeplusv6.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.SignOutFromKonsultacjeStudentActivity;
import com.example.konsultacjeplusv6.model.Konsultacja;

import java.util.List;

public class SignOutKonsultacjaHolder extends RecyclerView.ViewHolder {

    TextView DataOd, DataDo, NrPokoju, CzyOnline, CzyPubliczne, Pytanie, FullTitle, NazwaPrzedmiotuSemestr;;
    ImageView ProwadzacyImage;

    private SignOutFromKonsultacjeStudentActivity activitySignOut;
    private List<Konsultacja> konsultacje;

    public SignOutKonsultacjaHolder(@NonNull View itemView, SignOutFromKonsultacjeStudentActivity activitySignOut, List<Konsultacja> konsultacje) {
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Konsultacja clickedKonsultacja = konsultacje.get(position);
                    activitySignOut.showSignOutConfirmationDialog(clickedKonsultacja);
                }
            }
        });
    }
}
