package com.example.projektinzynierski;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import static android.content.Context.ALARM_SERVICE;


public class CalendarFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private TextView szczepienieTextView, odrobaczanieTextView, textViewUstawSzczepienie, textViewUstawOdrobaczanie;
    private Button szczepienieButton, odrobaczanieButton, dzisiajSzczepienie, dzisiajOdrobaczanie, zapiszDaty;
    private int daySzczepienie, monthSzczepienie, yearSzczepienie,
            dayOdrobaczanie, mothOdrobaczanie, yearOdrobaczanie;
    private DatePickerDialog.OnDateSetListener setListenerSzczepienie, setListenerOdrobaczanie;
    private Spinner spinner;
    private DatabaseHelper dogsDB;
    private ArrayList<String> ID, names, szczepienia, odrobaczenia, name;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<AlarmManager> alarmManager;
    private boolean isDbEmpty = false;
    int globalPosition = 0;
    private ArrayList<PendingIntent> intentArrayList, intentArrayListO;


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
        intentArrayList = new ArrayList<>();
        alarmManager = new ArrayList<>();
        dzisiajOdrobaczanie = getActivity().findViewById(R.id.dzisiajOdrobaczanie);
        dogsDB = new DatabaseHelper(getActivity());
        ID = new ArrayList<>();
        zapiszDaty = getActivity().findViewById(R.id.zapiszDaty);
        names = new ArrayList<>();
        szczepienia = new ArrayList<>();
        odrobaczenia = new ArrayList<>();
        name = new ArrayList<>();

        spinner.setOnItemSelectedListener(this);
        Calendar c = Calendar.getInstance();
        yearSzczepienie = c.get(Calendar.YEAR);
        monthSzczepienie = c.get(Calendar.MONTH);
        daySzczepienie = c.get(Calendar.DAY_OF_MONTH);

        dayOdrobaczanie = c.get(Calendar.DAY_OF_MONTH);
        mothOdrobaczanie = c.get(Calendar.MONTH);
        yearOdrobaczanie = c.get(Calendar.YEAR);


        try {
            loadListeners();
            loadData(getView());
            loadCalendar(globalPosition);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("tag", parent.getItemAtPosition(position).toString());
        globalPosition = position;

        loadCalendar(Integer.parseInt(ID.get(globalPosition)));
        Log.e("idglobal", ID.get(globalPosition));


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadCalendar(int id) {
        Log.e("błąd", ID.get(globalPosition));
        Cursor data = dogsDB.showDates(Integer.parseInt(ID.get(globalPosition)));
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
            isDbEmpty = true;
        } else {
            isDbEmpty = false;
            while (data.moveToNext()) {
                szczepienia.add(data.getString(1));
                odrobaczenia.add(data.getString(2));
            }
            Log.e("MISSING DANE", odrobaczenia.get(0));
            Log.e("MISSING DANE", szczepienia.get(0));
            if (odrobaczenia.get(0).equals("0")) {
                odrobaczanieTextView.setText("Brak");
            } else {
                odrobaczanieTextView.setText(odrobaczenia.get(0));
            }
            if (szczepienia.get(0).equals("0")) {
                szczepienieTextView.setText("Brak");
            } else {
                szczepienieTextView.setText(szczepienia.get(0));
            }
        }

        odrobaczenia.clear();
        szczepienia.clear();

    }

    //wypełnienie spinnera
    private void loadData(View v) {
        //wypełnienie tablicy
        ID.clear();
        names.clear();
        name.clear();
        odrobaczenia.clear();
        szczepienia.clear();

        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            isDbEmpty = true;
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        } else {
            isDbEmpty = false;
            while (data.moveToNext()) {
                Log.e("id", data.getString(0));
                ID.add(data.getString(0));
                names.add(data.getString(1));
            }
        }
        Cursor cursor = dogsDB.showDates(Integer.parseInt(ID.get(globalPosition)));
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Log.e("druga tabela", cursor.getString(3));
                szczepienia.add(cursor.getString(1));
                odrobaczenia.add(cursor.getString(2));
            }
        }
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(spinnerAdapter);

    }

    private void loadListeners() {
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

        zapiszDaty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zapiszDaty();
            }
        });
    }

    //tutaj musi się zapisywać i ustawiać alarmy ręcznie
    private void zapiszDaty() {
        cancelAlarm();
        alarmManager.clear();
        intentArrayList.clear();
        boolean flag = true;

        try {
            if (spinnerAdapter.isEmpty()) ;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        if (flag) {//tutaj zapis
            String odrobaczanie = odrobaczanieTextView.getText().toString();
            String szczepienie = szczepienieTextView.getText().toString();
            if (szczepienie.equals("Brak")) {
                szczepienie = "0";
            }
            if (odrobaczanie.equals("Brak")) {
                odrobaczanie = "0";
            }
            dogsDB.addDataOdrobaczanie(odrobaczanie, Integer.parseInt(ID.get(globalPosition)));
            dogsDB.addDataSzczepienie(szczepienie, Integer.parseInt(ID.get(globalPosition)));

            name.clear();
            szczepienia.clear();
            odrobaczenia.clear();
            Cursor data = dogsDB.joinCalendar();
            if (data.getCount() == 0) {
                //baza jest pusta
            } else {
                int ileAlarmow = 0;
                while (data.moveToNext()) {
                    szczepienia.add(data.getString(1));
                    odrobaczenia.add(data.getString(2));
                    name.add(data.getString(3));
                    ileAlarmow++;
                }
                //mamy wypełnione listy, teraz musimy aktywować alarmy w momencie kiedy jest szczepienie czy odrobaczanie
                Intent[] intent = new Intent[ileAlarmow];
                Intent[] intentO = new Intent[ileAlarmow];

                int sd, sm, sy, od, om, oy;
                //alerty na szczepienia
//                for (int k = 0; k < szczepienia.size(); k++) {
//                    if (szczepienia.get(k).equals("0")|| szczepienia.get(k).equals("Brak")) {
//                        ileAlarmow--;
//                        szczepienia.remove(k);
//                        ID.remove(k);
//                    }
//                }
//
//                for (int k = 0; k < odrobaczenia.size(); k++) {
//
//                    if (odrobaczenia.get(k).equals("0") || odrobaczenia.get(k).equals("Brak")) {
//                        ileAlarmow--;
//                        odrobaczenia.remove(k);
//                        ID.remove(k);
//                        name.remove(k);
//                    }
//                }


                Log.e("USTAWIANIE ALARMU", "LICZBA ALARMOW: " + ileAlarmow);
                for (int i = 0; i < ileAlarmow; i++) {
                    intent[i] = new Intent(getContext().getApplicationContext(), AlertsSzczepienie.class);
                    intent[i].addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intent[i].putExtra("name", name.get(i));

                    if (szczepienia.get(i).equals("0")) {
                        alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                        intentArrayList.add(PendingIntent.getBroadcast(getActivity(), i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT));

                    } else {
                        String[] parts = szczepienia.get(i).split("/");
                        sd = Integer.parseInt(parts[0]);
                        sm = Integer.parseInt(parts[1]);
                        sy = Integer.parseInt(parts[2]);

                        Calendar c = Calendar.getInstance();
                        c.getTimeInMillis();
                        c.set(Calendar.HOUR_OF_DAY, 12);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);

                        //własne ustawienia
//                        c.set(Calendar.HOUR_OF_DAY, 18);
//                        c.set(Calendar.MINUTE, 55);
//                        c.set(Calendar.SECOND, 0);

                        c.set(Calendar.DAY_OF_MONTH, sd);
                        c.set(Calendar.MONTH, sm - 1);
                        c.set(Calendar.YEAR, sy);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent[i], 0);
                        if (c.before(Calendar.getInstance())) {
//                            c.add(Calendar.DATE, 1);
                            Log.e("UPDATED INSTANCE SZ", c.getTime().toString());
                        }
                        alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                        alarmManager.get(i).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                                pendingIntent);
                        Log.e("CREATED ALARM SZ", c.getTime().toString());
                        intentArrayList.add(pendingIntent);
                    }
                }
                for (int i = 0; i < ileAlarmow; i++) {
                    intentO[i] = new Intent(getContext().getApplicationContext(), AlertsOdrobaczanie.class);
                    intentO[i].addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intentO[i].putExtra("name", name.get(i));

                    if (odrobaczenia.get(i).equals("0")) {
                        alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                        intentArrayList.add(PendingIntent.getBroadcast(getActivity(), i, intentO[i], PendingIntent.FLAG_UPDATE_CURRENT));

                    } else {
                        String[] parts = odrobaczenia.get(i).split("/");
                        Log.e("ERROR", odrobaczenia.get(i));
                        od = Integer.parseInt(parts[0]);
                        om = Integer.parseInt(parts[1]);
                        oy = Integer.parseInt(parts[2]);

                        Calendar c = Calendar.getInstance();
                        c.getTimeInMillis();
                        c.set(Calendar.HOUR_OF_DAY, 12);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);

                        //własne ustawienia
//                        c.set(Calendar.HOUR_OF_DAY, 18);
//                        c.set(Calendar.MINUTE, 57);
//                        c.set(Calendar.SECOND, 0);

                        c.set(Calendar.DAY_OF_MONTH, od);
                        c.set(Calendar.MONTH, om - 1);
                        c.set(Calendar.YEAR, oy);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intentO[i], 0);
                        if (c.before(Calendar.getInstance())) {
//                            c.add(Calendar.DATE, 1);
                            Log.e("UPDATED INSTANCE OD", c.getTime().toString());
                        }
                        alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                        alarmManager.get(i).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                                pendingIntent);
                        Log.e("CREATED ALARM OD", c.getTime().toString());
                        intentArrayList.add(pendingIntent);
                    }
                }

                Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();


            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        }

    }

    private void saveDatesSz() {
        cancelAlarm();

        name.clear();
        szczepienia.clear();
        alarmManager.clear();
        intentArrayList.clear();
        Cursor data = dogsDB.joinCalendar();
        if (data.getCount() == 0) {
            //baza jest pusta
        } else {
            int ileAlarmow = 0;
            while (data.moveToNext()) {
                szczepienia.add(data.getString(1));
                name.add(data.getString(3));
                ileAlarmow++;
            }
            //mamy wypełnione listy, teraz musimy aktywować alarmy w momencie kiedy jest szczepienie czy odrobaczanie
            Intent[] intent = new Intent[ileAlarmow];

            int sd, sm, sy;
            //alerty na szczepienia
//            for (int k = 0; k < szczepienia.size(); k++) {
//
//                if (szczepienia.get(k).equals("0")) {
//                    ileAlarmow--;
////                        szczepienia.remove(k);
//                }
//            }
            Log.e("USTAWIANIE ALARMU", "LICZBA ALARMOW: " + ileAlarmow);
            for (int i = 0; i < ileAlarmow; i++) {
                intent[i] = new Intent(getContext().getApplicationContext(), AlertsSzczepienie.class);
                intent[i].addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                intent[i].putExtra("name", name.get(i));

                if (szczepienia.get(i).equals("0")) {
                    alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                    intentArrayList.add(PendingIntent.getBroadcast(getActivity(), i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT));
                } else {
                    String[] parts = szczepienia.get(i).split("/");
                    sd = Integer.parseInt(parts[0]);
                    sm = Integer.parseInt(parts[1]);
                    sy = Integer.parseInt(parts[2]);

                    Calendar c = Calendar.getInstance();
                    c.getTimeInMillis();
                    c.set(Calendar.HOUR_OF_DAY, 12);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_MONTH, sd);
                    c.set(Calendar.MONTH, sm - 1);
                    c.set(Calendar.YEAR, sy);

                    //własne ustawienia
//                    c.set(Calendar.HOUR_OF_DAY, 19);
//                    c.set(Calendar.MINUTE, 26);
//                    c.set(Calendar.SECOND, 0);
//                    c.set(Calendar.DAY_OF_MONTH, 23);
//                    c.set(Calendar.MONTH, 9);
//                    c.set(Calendar.YEAR, 2020);


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT);
                    if (c.before(Calendar.getInstance())) {
                        c.add(Calendar.DATE, 1);
                        Log.e("UPDATED INSTANCE SZ", c.getTime().toString());
                    }
                    alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                    alarmManager.get(i).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                            pendingIntent);
                    Log.e("CREATED ALARM SZ", c.getTime().toString());
                    intentArrayList.add(pendingIntent);
                }
            }


        }
    }

    private void saveDatesOd() {
        cancelAlarm();

        name.clear();
        odrobaczenia.clear();
        alarmManager.clear();
        intentArrayList.clear();
        Cursor data = dogsDB.joinCalendar();
        if (data.getCount() == 0) {
            //baza jest pusta
        } else {
            int ileAlarmow = 0;
            while (data.moveToNext()) {
                odrobaczenia.add(data.getString(2));
                name.add(data.getString(3));
                ileAlarmow++;
            }
            //mamy wypełnione listy, teraz musimy aktywować alarmy w momencie kiedy jest szczepienie czy odrobaczanie
            Intent[] intentO = new Intent[ileAlarmow];

            int od, om, oy;
            //alerty na odrobaczania
//            for (int k = 0; k < odrobaczenia.size(); k++) {
//
//                if (odrobaczenia.get(k).equals("0")) {
//                    ileAlarmow--;
////                        szczepienia.remove(k);
//                }
//            }
            Log.e("USTAWIANIE ALARMU", "LICZBA ALARMOW: " + ileAlarmow);
            for (int i = 0; i < ileAlarmow; i++) {
                intentO[i] = new Intent(getContext().getApplicationContext(), AlertsOdrobaczanie.class);
                intentO[i].addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                intentO[i].putExtra("name", name.get(i));
                if (odrobaczenia.get(i).equals("0")) {
                    alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                    intentArrayList.add(PendingIntent.getBroadcast(getActivity(), i, intentO[i], PendingIntent.FLAG_UPDATE_CURRENT));

                } else {
                    String[] parts = odrobaczenia.get(i).split("/");
                    od = Integer.parseInt(parts[0]);
                    om = Integer.parseInt(parts[1]);
                    oy = Integer.parseInt(parts[2]);

                    Calendar c = Calendar.getInstance();
                    c.getTimeInMillis();
                    c.set(Calendar.HOUR_OF_DAY, 12);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.DAY_OF_MONTH, od);
                    c.set(Calendar.MONTH, om - 1);
                    c.set(Calendar.YEAR, oy);

                    //własne ustawienia
//                    c.set(Calendar.HOUR_OF_DAY, 19);
//                    c.set(Calendar.MINUTE, 27);
//                    c.set(Calendar.SECOND, 0);
//                    c.set(Calendar.DAY_OF_MONTH, 23);
//                    c.set(Calendar.MONTH, 9);
//                    c.set(Calendar.YEAR, 2020);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intentO[i], 0);
                    if (c.before(Calendar.getInstance())) {
                        c.add(Calendar.DATE, 1);
                        Log.e("UPDATED INSTANCE OD", c.getTime().toString());
                    }
                    alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                    alarmManager.get(i).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                            pendingIntent);
                    Log.e("CREATED ALARM OD", c.getTime().toString());
                    intentArrayList.add(pendingIntent);
                }
            }
        }
    }

    private void cancelAlarm() {
        Log.e("CO JEST NULL", Integer.toString(intentArrayList.size()));
        Log.e("CO JEST NULL", Integer.toString(alarmManager.size()));

        for (int i = 0; i < intentArrayList.size(); i++) {
            // index out of bound tutaj
            alarmManager.get(i).cancel(intentArrayList.get(i));
        }


    }

    //tutaj musi się wczytywać alarm po kliknięciu
    private void setSzczepienie() {
        if (isDbEmpty) {

        } else {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(System.currentTimeMillis());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR, 1);
            Date nextDate = c.getTime();
            szczepienieTextView.setText(formatter.format(nextDate));
            Log.e("data", formatter.format(nextDate));
            String dane = formatter.format(nextDate);
            dogsDB.addDataSzczepienie(dane, Integer.parseInt(ID.get(globalPosition)));
            names.clear();
            name.clear();


            spinner.setAdapter(null);
            saveDatesSz();
            loadCalendar(globalPosition);
            ID.clear();
            loadData(getView());
            spinner.setSelection(globalPosition);

        }


    }

    //tutaj musi się wczytywać alarm po kliknięciu
    private void setOdrobaczanie() {
        if (isDbEmpty) {

        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(System.currentTimeMillis());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, 3);
            Date nextDate = c.getTime();
            odrobaczanieTextView.setText(formatter.format(nextDate));
            Log.e("data", formatter.format(nextDate));
            String dane = formatter.format(nextDate);
            dogsDB.addDataOdrobaczanie(dane, Integer.parseInt(ID.get(globalPosition)));
            names.clear();
            name.clear();
            
            spinner.setAdapter(null);
            saveDatesOd();
            loadCalendar(globalPosition);
            ID.clear();
            loadData(getView());
            spinner.setSelection(globalPosition);
        }
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
