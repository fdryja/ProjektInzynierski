package com.example.projektinzynierski;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {
    Calendar calendar;
    TextView szczepienieTextView, odrobaczanieTextView, textViewUstawSzczepienie, textViewUstawOdrobaczanie;
    Button szczepienieButton, odrobaczanieButton, dzisiajSzczepienie, dzisiajOdrobaczanie;
    int daySzczepienie, monthSzczepienie, yearSzczepienie,
            dayOdrobaczanie, mothOdrobaczanie, yearOdrobaczanie;
    DatePickerDialog.OnDateSetListener setListenerSzczepienie, setListenerOdrobaczanie;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewUstawSzczepienie = getActivity().findViewById(R.id.textViewUstawSzczepienie);
        textViewUstawOdrobaczanie = getActivity().findViewById(R.id.textViewUstawOdrobaczanie);
        szczepienieTextView = getActivity().findViewById(R.id.szczepienieTextView);
        odrobaczanieTextView = getActivity().findViewById(R.id.odrobaczanieTextView);
        szczepienieButton = getActivity().findViewById(R.id.szczepienieButton);
        odrobaczanieButton = getActivity().findViewById(R.id.odrobaczanieButton);
        dzisiajSzczepienie = getActivity().findViewById(R.id.dzisiajSzczepienie);
        dzisiajOdrobaczanie = getActivity().findViewById(R.id.dzisiajOdrobaczanie);

        Calendar c = Calendar.getInstance();
        yearSzczepienie = c.get(Calendar.YEAR);
        monthSzczepienie = c.get(Calendar.MONTH);
        daySzczepienie = c.get(Calendar.DAY_OF_MONTH);

        dayOdrobaczanie = c.get(Calendar.DAY_OF_MONTH);
        mothOdrobaczanie = c.get(Calendar.MONTH);
        yearOdrobaczanie = c.get(Calendar.YEAR);

        loadListeners();

    }

    private void loadListeners(){
        szczepienieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateSzczepienie();
            }
        });
        odrobaczanieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateOdrobaczanie();
            }
        });
        setListenerSzczepienie = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                szczepienieTextView.setText(date);
            }
        };
        setListenerOdrobaczanie = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                odrobaczanieTextView.setText(date);
            }
        };

        dzisiajSzczepienie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSzczepienie();
            }
        });
        dzisiajOdrobaczanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOdrobaczanie();
            }
        });
    }

    private void setSzczepienie(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 3);
        Date nextDate = c.getTime();
        szczepienieTextView.setText(formatter.format(nextDate));
    }
    private void setOdrobaczanie(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, 1);
        Date nextDate = c.getTime();
        odrobaczanieTextView.setText(formatter.format(nextDate));
    }

    private void getDateSzczepienie() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListenerSzczepienie, yearSzczepienie,
                monthSzczepienie, daySzczepienie);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getDateOdrobaczanie() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerOdrobaczanie, yearOdrobaczanie,
                mothOdrobaczanie, dayOdrobaczanie);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
