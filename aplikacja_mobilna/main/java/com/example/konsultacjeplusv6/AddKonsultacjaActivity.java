package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddKonsultacjaActivity extends AppCompatActivity {

    private EditText dataOdEditText;
    private EditText dataDoEditText;
    private EditText nrPokojuEditText;
    private RadioGroup czyOnlineRadioGroup;
    private RadioGroup czyPubliczneRadioGroup;
    private EditText nrRealizacjiEditText;
    private Button addKonsultacjaButton;
    private int nrProwadzacego;
    private ApiService apiService;
    private Calendar dataOdCalendar;
    private Calendar dataDoCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_konsultacja);

        dataOdEditText = findViewById(R.id.dataOdEditText);
        dataDoEditText = findViewById(R.id.dataDoEditText);
        nrPokojuEditText = findViewById(R.id.nrPokojuEditText);
        czyOnlineRadioGroup = findViewById(R.id.czyOnlineRadioGroup);
        czyPubliczneRadioGroup = findViewById(R.id.czyPubliczneRadioGroup);
        nrRealizacjiEditText = findViewById(R.id.nrRealizacjiEditText);
        addKonsultacjaButton = findViewById(R.id.addKonsultacjaButton);

        RetrofitService retrofitService = new RetrofitService();
        Retrofit retrofit = retrofitService.getRetrofit();
        apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");
        String userJson = sharedPreferences.getString("userJson", "");
        Gson gson = new Gson();
        Prowadzacy prowadzacy = gson.fromJson(userJson, Prowadzacy.class);
        nrProwadzacego = prowadzacy.getNr_prowadzacego();

        dataOdCalendar = Calendar.getInstance();
        dataDoCalendar = Calendar.getInstance();

        dataOdEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(dataOdCalendar, dataOdEditText);
            }
        });

        dataDoEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(dataDoCalendar, dataDoEditText);
            }
        });

        addKonsultacjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveKonsultacja();
            }
        });

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Set<Integer> alwaysTrueItems = new HashSet<>();
        BottomNavigation.setupBottomNavigation(this, BottomNavigation.ITEM_KONSULTACJE, alwaysTrueItems);
    }

    private void saveKonsultacja() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());

        String dataOdText = dataOdEditText.getText().toString().trim();
        String dataDoText = dataDoEditText.getText().toString().trim();

        if (TextUtils.isEmpty(dataOdText) || TextUtils.isEmpty(dataDoText)) {
            Toast.makeText(AddKonsultacjaActivity.this, "Proszę podać wszystkie dane", Toast.LENGTH_SHORT).show();
            return;
        }

        Date dataOd = null;
        Date dataDo = null;
        try {
            dataOd = dateFormat.parse(dataOdText);
            dataDo = dateFormat.parse(dataDoText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Integer nrPokoju = null;
        if (!nrPokojuEditText.getText().toString().isEmpty()) {
            nrPokoju = Integer.parseInt(nrPokojuEditText.getText().toString());
        }

        Integer nrRealizacji = null;
        if (!nrRealizacjiEditText.getText().toString().isEmpty()) {
            nrRealizacji = Integer.parseInt(nrRealizacjiEditText.getText().toString());
        }

        int czyOnlineRadioButtonId = czyOnlineRadioGroup.getCheckedRadioButtonId();
        String czyOnline = "";
        if (czyOnlineRadioButtonId != -1) {
            RadioButton czyOnlineRadioButton = findViewById(czyOnlineRadioButtonId);
            if (czyOnlineRadioButton.getText().toString().equalsIgnoreCase("Tak")) {
                czyOnline = "T";
            } else if (czyOnlineRadioButton.getText().toString().equalsIgnoreCase("Nie")) {
                czyOnline = "N";
            }
        }

        int czyPubliczneRadioButtonId = czyPubliczneRadioGroup.getCheckedRadioButtonId();
        String czyPubliczne = "";
        if (czyPubliczneRadioButtonId != -1) {
            RadioButton czyPubliczneRadioButton = findViewById(czyPubliczneRadioButtonId);
            if (czyPubliczneRadioButton.getText().toString().equalsIgnoreCase("Tak")) {
                czyPubliczne = "T";
            } else if (czyPubliczneRadioButton.getText().toString().equalsIgnoreCase("Nie")) {
                czyPubliczne = "N";
            }
        }

        Konsultacja konsultacja = new Konsultacja();
        konsultacja.setDataOd(dataOd);
        konsultacja.setDataDo(dataDo);
        konsultacja.setNr_pokoju(nrPokoju);
        konsultacja.setCzy_online(czyOnline);
        konsultacja.setCzy_publiczne(czyPubliczne);
        konsultacja.setNr_realizacji(nrRealizacji);
        konsultacja.setNr_prowadzacego(nrProwadzacego);

        Call<Konsultacja> call = apiService.save(konsultacja);
        call.enqueue(new Callback<Konsultacja>() {
            @Override
            public void onResponse(Call<Konsultacja> call, Response<Konsultacja> response) {
                if (response.isSuccessful()) {
                    Konsultacja savedKonsultacja = response.body();
                    finish();
                } else {
                    Toast.makeText(AddKonsultacjaActivity.this, "Nieudany zapis konsultacji", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Konsultacja> call, Throwable t) {
                Toast.makeText(AddKonsultacjaActivity.this, "Błąd połączenia", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateTimePicker(final Calendar calendar, final EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        dateFormat.setTimeZone(TimeZone.getDefault());
                        String formattedDateTime = dateFormat.format(calendar.getTime());

                        // Remove seconds from the formattedDateTime
                        formattedDateTime = formattedDateTime.substring(0, formattedDateTime.length() - 3);

                        editText.setText(formattedDateTime);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddKonsultacjaActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        };

        Locale locale = new Locale("pl", "PL");
        Locale.setDefault(locale);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddKonsultacjaActivity.this, R.style.DatePickerDialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Set minimum date to current date
        datePickerDialog.show();
    }

}