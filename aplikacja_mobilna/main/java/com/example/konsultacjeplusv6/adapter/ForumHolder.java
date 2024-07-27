package com.example.konsultacjeplusv6.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.model.Forum;

import java.util.List;

public class ForumHolder extends RecyclerView.ViewHolder {

    TextView Rodzaj, NazwaSpecjalizacji, Semestr, NazwaPrzedmiotu;
    ForumAdapter.OnItemClickListener clickListener;
    List<Forum> forumList;

    public ForumHolder(@NonNull View itemView, List<Forum> forumList, ForumAdapter.OnItemClickListener clickListener) {
        super(itemView);
        this.forumList = forumList;
        this.clickListener = clickListener;
        Rodzaj = itemView.findViewById(R.id.forumListItem_Rodzaj);
        NazwaSpecjalizacji = itemView.findViewById(R.id.forumListItem_NazwaSpecjalizacji);
        NazwaPrzedmiotu = itemView.findViewById(R.id.forumListItem_NazwaPrzedmiotu);
        Semestr = itemView.findViewById(R.id.forumListItem_Semestr);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    Forum forum = forumList.get(position);
                    clickListener.onItemClick(forum);
                }
            }
        });
        Log.d("ForumHolder", "Views bound successfully.");
    }
}



