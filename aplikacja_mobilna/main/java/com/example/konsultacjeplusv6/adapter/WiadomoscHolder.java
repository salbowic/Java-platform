package com.example.konsultacjeplusv6.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsultacjeplusv6.R;
import com.example.konsultacjeplusv6.model.Wiadomosc;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WiadomoscHolder extends RecyclerView.ViewHolder {

    private TextView textViewSender;
    private TextView textViewMessage;
    private TextView textViewTimestamp;
    private TextView textViewSenderWhite;
    private TextView textViewMessageWhite;
    private TextView textViewTimestampWhite;
    private LinearLayout blueMessageLayout;
    private LinearLayout whiteMessageLayout;
    private ImageView imageViewSenderBlue;
    private ImageView imageViewSenderWhite;

    public WiadomoscHolder(@NonNull View itemView) {
        super(itemView);
        textViewSender = itemView.findViewById(R.id.textViewSender);
        textViewMessage = itemView.findViewById(R.id.textViewMessage);
        textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        textViewSenderWhite = itemView.findViewById(R.id.textViewSenderWhite);
        textViewMessageWhite = itemView.findViewById(R.id.textViewMessageWhite);
        textViewTimestampWhite = itemView.findViewById(R.id.textViewTimestampWhite);
        blueMessageLayout = itemView.findViewById(R.id.blueMessageLayout);
        whiteMessageLayout = itemView.findViewById(R.id.whiteMessageLayout);
        imageViewSenderBlue = itemView.findViewById(R.id.imageViewSenderBlue);
        imageViewSenderWhite = itemView.findViewById(R.id.imageViewSenderWhite);
    }

    public void bindMessage(Wiadomosc wiadomosc, String loggedInUserNadawca) {
        boolean isSentByLoggedInUser = loggedInUserNadawca.equals(wiadomosc.getNadawca());

        if (wiadomosc.getNr_studenta() != null) {
            fetchStudentImage(wiadomosc.getNr_studenta());
            blueMessageLayout.setVisibility(isSentByLoggedInUser ? View.VISIBLE : View.GONE);
            whiteMessageLayout.setVisibility(isSentByLoggedInUser ? View.GONE : View.VISIBLE);
        } else if (wiadomosc.getNr_prowadzacego() != null) {
            fetchProwadzacyImage(wiadomosc.getNr_prowadzacego());
            blueMessageLayout.setVisibility(isSentByLoggedInUser ? View.VISIBLE : View.GONE);
            whiteMessageLayout.setVisibility(isSentByLoggedInUser ? View.GONE : View.VISIBLE);
        }

        if (isSentByLoggedInUser) {
            textViewSender.setText(loggedInUserNadawca);
            textViewMessage.setText(wiadomosc.getTekst());
            Date messageTime = wiadomosc.getCzas();
            String formattedTime = formatDateTime(messageTime);
            textViewTimestamp.setText(formattedTime);
        } else {
            textViewSenderWhite.setText(wiadomosc.getNadawca());
            textViewMessageWhite.setText(wiadomosc.getTekst());
            Date messageTime = wiadomosc.getCzas();
            String formattedTime = formatDateTime(messageTime);
            textViewTimestampWhite.setText(formattedTime);
        }
    }


    private void fetchStudentImage(int nrStudenta) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        Call<ResponseBody> call = apiService.getStudentImage(nrStudenta);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] decodedString = Base64.decode(response.body().bytes(), Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageViewSenderWhite.setImageBitmap(decodedBitmap);
                        imageViewSenderBlue.setImageBitmap(decodedBitmap);
                    } catch (IOException e) {
                        imageViewSenderWhite.setImageResource(R.drawable.baseline_school_24);
                        imageViewSenderBlue.setImageResource(R.drawable.baseline_school_24);
                    }
                } else {
                    imageViewSenderWhite.setImageResource(R.drawable.baseline_school_24);
                    imageViewSenderBlue.setImageResource(R.drawable.baseline_school_24);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageViewSenderWhite.setImageResource(R.drawable.baseline_school_24);
                imageViewSenderBlue.setImageResource(R.drawable.baseline_school_24);
            }
        });
    }


    private void fetchProwadzacyImage(int nrProwadzacego) {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        Call<ResponseBody> call = apiService.getProwadzacyImage(nrProwadzacego);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] decodedString = Base64.decode(response.body().bytes(), Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageViewSenderWhite.setImageBitmap(decodedBitmap);
                        imageViewSenderBlue.setImageBitmap(decodedBitmap);
                    } catch (IOException e) {
                        imageViewSenderWhite.setImageResource(R.drawable.baseline_school_24);
                        imageViewSenderBlue.setImageResource(R.drawable.baseline_school_24);
                    }
                } else {
                    imageViewSenderWhite.setImageResource(R.drawable.baseline_school_24);
                    imageViewSenderBlue.setImageResource(R.drawable.baseline_school_24);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageViewSenderWhite.setImageResource(R.drawable.baseline_school_24);
                imageViewSenderBlue.setImageResource(R.drawable.baseline_school_24);
            }
        });
    }

    public String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

}


