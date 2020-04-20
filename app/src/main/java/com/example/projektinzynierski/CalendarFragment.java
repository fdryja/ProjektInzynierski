package com.example.projektinzynierski;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private TextView szczepienieTextView, odrobaczanieTextView, textViewUstawSzczepienie, textViewUstawOdrobaczanie;
    private Button szczepienieButton, odrobaczanieButton, dzisiajSzczepienie, dzisiajOdrobaczanie;
    private int daySzczepienie, monthSzczepienie, yearSzczepienie,
            dayOdrobaczanie, mothOdrobaczanie, yearOdrobaczanie;
    private DatePickerDialog.OnDateSetListener setListenerSzczepienie, setListenerOdrobaczanie;
    private Spinner spinner;
    private DatabaseHelper dogsDB;
    private ArrayList<String> ID, names;
    private ArrayAdapter<String> spinnerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = getActivity().findViewById(R.id.spinnerDate);
        textViewUstawSzczepienie = getActivity().findViewById(R.id.textViewUstawSzczepienie);
        textViewUstawOdrobaczanie = getActivity().findViewById(R.id.textViewUstawOdrobaczanie);
        szczepienieTextView = getActivity().findViewById(R.id.szczepienieTextView);
        odrobaczanieTextView = getActivity().findViewById(R.id.odrobaczanieTextView);
        szczepienieButton = getActivity().findViewById(R.id.szczepienieButton);
        odrobaczanieButton = getActivity().findViewById(R.id.odrobaczanieButton);
        dzisiajSzczepienie = getActivity().findViewById(R.id.dzisiajSzczepienie);
        dzisiajOdrobaczanie = getActivity().findViewById(R.id.dzisiajOdrobaczanie);
        dogsDB = new DatabaseHelper(getActivity());
        ID = new ArrayList<>();
        names = new ArrayList<>();

        spinner.setOnItemSelectedListener(this);
        Calendar c = Calendar.getInstance();
        yearSzczepienie = c.get(Calendar.YEAR);
        monthSzczepienie = c.get(Calendar.MONTH);
        daySzczepienie = c.get(Calendar.DAY_OF_MONTH);

        dayOdrobaczanie = c.get(Calendar.DAY_OF_MONTH);
        mothOdrobaczanie = c.get(Calendar.MONTH);
        yearOdrobaczanie = c.get(Calendar.YEAR);

        loadListeners();
        loadData(getView());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("tag", parent.getItemAtPosition(position).toString());
//        selectedDog = parent.getItemAtPosition(position).toString();
//        globalPosition = position;
//        loadEating();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadData(View v) {
        //wypełnienie tablicy
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        } else {
            while (data.moveToNext()) {
                Log.e("tag", data.getString(0));
                ID.add(data.getString(0));
                names.add(data.getString(1));


            }
        }
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(spinnerAdapter);

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
