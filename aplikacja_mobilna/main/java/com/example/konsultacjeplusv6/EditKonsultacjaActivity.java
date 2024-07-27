package com.example.konsultacjeplusv6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.example.konsultacjeplusv6.other.BottomNavigation;
import com.example.konsultacjeplusv6.retrofit.ApiService;
import com.example.konsultacjeplusv6.retrofit.RetrofitService;

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

public class EditKonsultacjaActivity extends AppCompatActivity {

    private EditText dataOdEditText;
    private EditText dataDoEditText;
    private EditText nrPokojuEditText;
    private RadioGroup czyOnlineRadioGroup;
    private RadioGroup czyPubliczneRadioGroup;
    private EditText nrRealizacjiEditText;
    private Button saveKonsultacjaButton;
    private Konsultacja selectedKonsultacja;
    private ApiService apiService;
    private Calendar dataOdCalendar;
    private Calendar dataDoCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_konsultacja);

        RetrofitService retrofitService = new RetrofitService();
        Retrofit retrofit = retrofitService.getRetrofit();
        apiService = retrofit.create(ApiService.class);

        dataOdEditText = findViewById(R.id.dataOdEditText);
        dataDoEditText = findViewById(R.id.dataDoEditText);
        nrPokojuEditText = findViewById(R.id.nrPokojuEditText);
        czyOnlineRadioGroup = findViewById(R.id.czyOnlineRadioGroup);
        czyPubliczneRadioGroup = findViewById(R.id.czyPubliczneRadioGroup);
        nrRealizacjiEditText = findViewById(R.id.nrRealizacjiEditText);
        saveKonsultacjaButton = findViewById(R.id.saveKonsultacjaButton);

        selectedKonsultacja = getIntent().getParcelableExtra("konsultacja");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dataOdText = dateFormat.format(selectedKonsultacja.getDataOd());
        String dataDoText = dateFormat.format(selectedKonsultacja.getDataDo());

        dataOdEditText.setText(dataOdText);
        dataDoEditText.setText(dataDoText);
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

        saveKonsultacjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKonsultacja();
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

    private void updateKonsultacja() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());

        String dataOdText = dataOdEditText.getText().toString().trim();
        String dataDoText = dataDoEditText.getText().toString().trim();

        if (TextUtils.isEmpty(dataOdText) || TextUtils.isEmpty(dataDoText)) {
            Toast.makeText(EditKonsultacjaActivity.this, "Proszę podać wszystkie dane", Toast.LENGTH_SHORT).show();
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


        // Uzupełnienie danych wybranej konsultacji
        selectedKonsultacja.setDataOd(dataOd);
        selectedKonsultacja.setDataDo(dataDo);
        selectedKonsultacja.setNr_pokoju(nrPokoju);
        selectedKonsultacja.setCzy_online(czyOnline);
        selectedKonsultacja.setCzy_publiczne(czyPubliczne);
        selectedKonsultacja.setNr_realizacji(nrRealizacji);

        // Wywołanie API do aktualizacji konsultacji
        Call<Konsultacja> call = apiService.updateKonsultacja(selectedKonsultacja);
        call.enqueue(new Callback<Konsultacja>() {
            @Override
            public void onResponse(Call<Konsultacja> call, Response<Konsultacja> response) {
                if (response.isSuccessful()) {
                    Konsultacja updatedKonsultacja = response.body();
                    finish();
                } else {
                    Toast.makeText(EditKonsultacjaActivity.this, "Nieudana aktualizacja konsultacji", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Konsultacja> call, Throwable t) {
                Toast.makeText(EditKonsultacjaActivity.this, "Błąd połączenia", Toast.LENGTH_SHORT).show();
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        dateFormat.setTimeZone(TimeZone.getDefault());
                        String formattedDateTime = dateFormat.format(calendar.getTime());

                        editText.setText(formattedDateTime);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditKonsultacjaActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        };

        Locale locale = new Locale("pl", "PL");
        Locale.setDefault(locale);
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditKonsultacjaActivity.this, R.style.DatePickerDialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

}
