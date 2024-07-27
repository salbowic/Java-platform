package com.example.konsultacjeplusv6.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.model.Wiadomosc;
import java.util.List;

public class WiadomoscAdapter extends RecyclerView.Adapter<WiadomoscHolder> {

    private String loggedInUserNadawca;
    private List<Wiadomosc> wiadomoscList;

    public WiadomoscAdapter(List<Wiadomosc> wiadomoscList, String loggedInUserNadawca) {
        this.wiadomoscList = wiadomoscList;
        this.loggedInUserNadawca = loggedInUserNadawca;
    }

    @NonNull
    @Override
    public WiadomoscHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        return new WiadomoscHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WiadomoscHolder holder, int position) {
        Wiadomosc wiadomosc = wiadomoscList.get(position);
        holder.bindMessage(wiadomosc, loggedInUserNadawca);
    }

    @Override
    public int getItemCount() {
        return wiadomoscList.size();
    }
}



