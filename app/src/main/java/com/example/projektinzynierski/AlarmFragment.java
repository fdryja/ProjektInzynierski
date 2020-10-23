package com.example.projektinzynierski;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import static android.content.Context.ALARM_SERVICE;
//import static com.example.projektinzynierski.App.CHANNEL_1_ID;
//import static com.example.projektinzynierski.App.CHANNEL_2_ID;

public class AlarmFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L;
    private Spinner spinner;
    private TextView alarmView1, alarmView2, alarmView3, alarmView4;
    private Button setAlarm1, setAlarm2, setAlarm3, setAlarm4, saveAlarm, resetAlarm;
    private DatabaseHelper dogsDB;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> ID, names, alarm, alarmName, dogName, eating, eating_count;
    private ArrayList<Integer> alarmNumber;
    private int alarmCount, globalPosition = 0, alarm1hour, alarm2hour, alarm3hour, alarm4hour, alarm1minute, alarm2minute, alarm3minute, alarm4minute;
    private TimePickerDialog.OnTimeSetListener setListenerAlarm1, setListenerAlarm2, setListenerAlarm3, setListenerAlarm4;
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<AlarmManager> alarmManager;
    //    private ArrayList<Intent> intent;
//    private AlarmManager[] alarmManager;
    private int ileAlarmow;

    ArrayList<PendingIntent> intentArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = getActivity().findViewById(R.id.spinnerAlarm);
        alarmManager = new ArrayList<>();
        alarmView1 = getActivity().findViewById(R.id.alarmView1);
        intentArrayList = new ArrayList<>();
        alarmView2 = getActivity().findViewById(R.id.alarmView2);
        resetAlarm = getActivity().findViewById(R.id.resetAlarm);
        notificationManagerCompat = NotificationManagerCompat.from(getContext());
        spinner.setOnItemSelectedListener(this);
        alarmView3 = getActivity().findViewById(R.id.alarmView3);
        alarmView4 = getActivity().findViewById(R.id.alarmView4);
        setAlarm1 = getActivity().findViewById(R.id.setAlarm1);
        alarmName = new ArrayList<>();
        dogName = new ArrayList<>();
        setAlarm2 = getActivity().findViewById(R.id.setAlarm2);
        setAlarm3 = getActivity().findViewById(R.id.setAlarm3);
        eating = new ArrayList<>();
        eating_count = new ArrayList<>();
        setAlarm4 = getActivity().findViewById(R.id.setAlarm4);
        alarmNumber = new ArrayList<>();
        ID = new ArrayList<>();
        saveAlarm = getActivity().findViewById(R.id.saveAlarm);
        names = new ArrayList<>();
        alarm = new ArrayList<>();
        dogsDB = new DatabaseHelper(getActivity());
        try {
            loadData(getView());
            loadListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest sss", Toast.LENGTH_SHORT).show();
        }

        alarmVisibility(getView());
        Log.e("alarmCount", Integer.toString(alarmCount));

    }

    private void loadListeners() {
        //buttony
        setAlarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarm1();
            }
        });
        setAlarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarm2();
            }
        });
        setAlarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarm3();
            }
        });
        setAlarm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarm4();
            }
        });
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarms();


                saveAllAlarms();


            }
        });
        resetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAlarms();
            }
        });


        //listenery time
        setListenerAlarm1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String godzina = "Brak";
                if (minute < 10) {
                    String minuteString = "0" + minute;
                    godzina = hourOfDay + ":" + minuteString;
                } else {
                    godzina = hourOfDay + ":" + minute;
                }
                alarmView1.setText(godzina);
            }
        };
        setListenerAlarm2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String godzina = "Brak";
                if (minute < 10) {
                    String minuteString = "0" + minute;
                    godzina = hourOfDay + ":" + minuteString;
                } else {
                    godzina = hourOfDay + ":" + minute;
                }
                alarmView2.setText(godzina);
            }
        };
        setListenerAlarm3 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String godzina = "Brak";
                if (minute < 10) {
                    String minuteString = "0" + minute;
                    godzina = hourOfDay + ":" + minuteString;
                } else {
                    godzina = hourOfDay + ":" + minute;
                }
                alarmView3.setText(godzina);
            }
        };
        setListenerAlarm4 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String godzina = "Brak";
                if (minute < 10) {
                    String minuteString = "0" + minute;
                    godzina = hourOfDay + ":" + minuteString;
                } else {
                    godzina = hourOfDay + ":" + minute;
                }
                alarmView4.setText(godzina);
            }
        };
    }

    private void saveAllAlarms() {
        int dogId;
        String name;
        ID.clear();
        names.clear();
        alarm.clear();
        alarmManager.clear();
        intentArrayList.clear();
        Log.e("USTAWIANIE ALARMU", "ODPALONA FUNKCJA");
        //funkcja która ładuje wszystkie rekordy z tabeli alarmów i aktywuje każdy istniejący w niej alarm, jeżeli jest "Brak" to
        //nie aktywuje tego alarmu;
        Cursor data = dogsDB.joinEating();
        ileAlarmow = 0;
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                alarm.add(data.getString(2));
                alarmName.add(data.getString(1));
                dogName.add(data.getString(3));
                eating_count.add(data.getString(4));
                eating.add(data.getString(5));
                Log.e("alarm",data.getString(2));
                Log.e("alarmname",data.getString(1));
                Log.e("dog",data.getString(3));
                ileAlarmow++;
            }
        }

        for (int k = 0; k < alarm.size(); k++) {

            if (alarm.get(k).equals("Brak")) {
                ileAlarmow--;
                alarm.remove(k);
            }


        }

//        alarmManager = new AlarmManager[ileAlarmow];
        Intent[] intent = new Intent[ileAlarmow];

        int ileAlarmowPo = 0;


        Log.e("USTAWIANIE ALARMU", "LICZBA ALARMOW: " + ileAlarmow);
        for (int i = 0; i < ileAlarmow; i++) {

            intent[i] = new Intent(getContext().getApplicationContext(), AlertReceiver.class);
            intent[i].addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent[i].putExtra("name",dogName.get(i));
            intent[i].putExtra("eating_count",eating_count.get(i));
            intent[i].putExtra("eating",eating.get(i));
            String h = "", m = "";
            if (alarm.get(i).equals("Brak")) {
                alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));
                intentArrayList.add(PendingIntent.getBroadcast(getActivity(), i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT));
            } else {
                String[] parts = alarm.get(i).split(":");
                h = parts[0];
                m = parts[1];

//            dogId = Integer.parseInt(alarmName.get(i)) ;
                Calendar c = Calendar.getInstance();
                c.getTimeInMillis();
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h));
                c.set(Calendar.MINUTE, Integer.parseInt(m));
                c.set(Calendar.SECOND, 0);
//            Log.e("PRZESZLO DALEJ", "W TELEFONIE: " + new Date());
                Log.e("PRZESZLO DALEJ", "DATA: " + c.getTime());
//                startAlarm(c,i);


                if (c.before(Calendar.getInstance())) {
                    c.add(Calendar.DATE, 1);
                    Log.e("UPDATED INSTANCE", c.getTime().toString());
                }

                alarmManager.add((AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE));

//                Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID).setContentTitle("tytuł")
//                        .setContentText("treść")
//                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setCategory(NotificationCompat.CATEGORY_ALARM)
//                        .build();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT);
                //wywołanie na czas
                //tutaj wywala     java.lang.IndexOutOfBoundsException: Index: 1, Size: 1
                alarmManager.get(i).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                ileAlarmowPo++;
                Log.e("CREATED ALARM", c.getTime().toString());

                intentArrayList.add(pendingIntent);

//            Cursor nameCursor = dogsDB.showName(dogId);
//            while (nameCursor.moveToNext()) {
//                dogName.add(nameCursor.getString(0));
//            }

            }



        }
        Log.e("USTAWIANIE ALARMU", "LICZBA ALARMOW PO: " + ileAlarmowPo);
        Cursor cursor = dogsDB.showData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                ID.add(cursor.getString(0));
                names.add(cursor.getString(1));

            }
        }
//        passName();

        AlarmReactivation.schedule(getContext(),60000 );
    }


    public ArrayList<String> passName() {
        return dogName;
    }
//    public String passname(){
//
//    }
    public int getGlobalPosition(){
        return globalPosition;
    }

    private void cancelAlarm() {
        Log.e("CO JEST NULL", Integer.toString(intentArrayList.size()));
        Log.e("CO JEST NULL",Integer.toString(alarmManager.size()));

        for (int i = 0; i < intentArrayList.size(); i++) {
            alarmManager.get(i).cancel(intentArrayList.get(i));
        }
    }


    private void resetAlarms() {
        cancelAlarm();
        alarmView1.setText("Brak");
        alarmView2.setText("Brak");
        alarmView3.setText("Brak");
        alarmView4.setText("Brak");
        saveAlarms();
        loadAlarms(Integer.parseInt(ID.get(globalPosition)));
    }

    private void saveAlarms() {

        cancelAlarm();

        if (alarmCount == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else if (alarmCount == 1) {
            dogsDB.updateAlarm(alarmView1.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 1);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();

        } else if (alarmCount == 2) {
            dogsDB.updateAlarm(alarmView1.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 1);
            dogsDB.updateAlarm(alarmView2.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 2);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
        } else if (alarmCount == 3) {
            dogsDB.updateAlarm(alarmView1.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 1);
            dogsDB.updateAlarm(alarmView2.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 2);
            dogsDB.updateAlarm(alarmView3.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 3);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();

        } else if (alarmCount == 4) {
            dogsDB.updateAlarm(alarmView1.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 1);
            dogsDB.updateAlarm(alarmView2.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 2);
            dogsDB.updateAlarm(alarmView3.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 3);
            dogsDB.updateAlarm(alarmView4.getText().toString(), Integer.parseInt(ID.get(globalPosition)), 4);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAlarm1() {
        String text = alarmView1.getText().toString();
        int hour, minute;
        if (text.equals("Brak")) {
            alarm1hour = 0;
            alarm1minute = 0;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = formatter.parse(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour = date.getHours();
            minute = date.getMinutes();
            alarm1hour = hour;
            alarm1minute = minute;
        }
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm1, alarm1hour, alarm1minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getAlarm2() {
        String text = alarmView2.getText().toString();
        int hour, minute;
        if (text.equals("Brak")) {
            alarm2hour = 0;
            alarm2minute = 0;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = formatter.parse(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour = date.getHours();
            minute = date.getMinutes();
            alarm2hour = hour;
            alarm2minute = minute;
        }
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm2, alarm2hour, alarm2minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getAlarm3() {
        String text = alarmView3.getText().toString();
        int hour, minute;
        if (text.equals("Brak")) {
            alarm3hour = 0;
            alarm3minute = 0;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = formatter.parse(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour = date.getHours();
            minute = date.getMinutes();
            alarm3hour = hour;
            alarm3minute = minute;
        }
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm3, alarm3hour, alarm3minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getAlarm4() {
        String text = alarmView4.getText().toString();
        int hour, minute;
        if (text.equals("Brak")) {
            alarm4hour = 0;
            alarm4minute = 0;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = formatter.parse(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour = date.getHours();
            minute = date.getMinutes();
            alarm4hour = hour;
            alarm4minute = minute;
        }
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm4, alarm4hour, alarm4minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void alarmVisibility(View v) {
        if (alarmCount == 1) {
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            saveAlarm.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.INVISIBLE);
            setAlarm2.setVisibility(View.INVISIBLE);
            alarmView3.setVisibility(View.INVISIBLE);
            setAlarm3.setVisibility(View.INVISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
            resetAlarm.setVisibility(View.VISIBLE);
        } else if (alarmCount == 2) {
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.VISIBLE);
            setAlarm2.setVisibility(View.VISIBLE);
            alarmView3.setVisibility(View.INVISIBLE);
            setAlarm3.setVisibility(View.INVISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
            saveAlarm.setVisibility(View.VISIBLE);
            resetAlarm.setVisibility(View.VISIBLE);
        } else if (alarmCount == 3) {
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.VISIBLE);
            setAlarm2.setVisibility(View.VISIBLE);
            alarmView3.setVisibility(View.VISIBLE);
            setAlarm3.setVisibility(View.VISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            saveAlarm.setVisibility(View.VISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
            resetAlarm.setVisibility(View.VISIBLE);
        } else if (alarmCount == 4) {
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.VISIBLE);
            setAlarm2.setVisibility(View.VISIBLE);
            alarmView3.setVisibility(View.VISIBLE);
            setAlarm3.setVisibility(View.VISIBLE);
            alarmView4.setVisibility(View.VISIBLE);
            setAlarm4.setVisibility(View.VISIBLE);
            saveAlarm.setVisibility(View.VISIBLE);
            resetAlarm.setVisibility(View.VISIBLE);
        } else {
            alarmView1.setVisibility(View.INVISIBLE);
            setAlarm1.setVisibility(View.INVISIBLE);
            alarmView2.setVisibility(View.INVISIBLE);
            setAlarm2.setVisibility(View.INVISIBLE);
            alarmView3.setVisibility(View.INVISIBLE);
            setAlarm3.setVisibility(View.INVISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
            saveAlarm.setVisibility(View.INVISIBLE);
            resetAlarm.setVisibility(View.INVISIBLE);
        }
    }

    private void loadAlarms(int id) {
        alarm.clear();
        alarmNumber.clear();
        Cursor data = dogsDB.showAlarm(id);
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "Brak wyznaczonej ilości jedzenia\nDodaj ją w zakładce \"Opcje karmienia\"", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                alarmNumber.add(data.getInt(1));
                alarm.add(data.getString(2));
            }
            //tutaj wpisywanie alarmów z bazy danych do pól tekstowych
//             Log.e("długość tablicy z alarmami", Integer.toString( alarm.size()));
            if (alarm.size() == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
            } else if (alarm.size() == 1) {
                alarmView1.setText(alarm.get(0));

            } else if (alarm.size() == 2) {
                alarmView1.setText(alarm.get(0));
                alarmView2.setText(alarm.get(1));

            } else if (alarm.size() == 3) {
                alarmView1.setText(alarm.get(0));
                alarmView2.setText(alarm.get(1));
                alarmView3.setText(alarm.get(2));

            } else if (alarm.size() == 4) {
                alarmView1.setText(alarm.get(0));
                alarmView2.setText(alarm.get(1));
                alarmView3.setText(alarm.get(2));
                alarmView4.setText(alarm.get(3));
            }
        }
        alarmVisibility(getView());
    }

    private void loadData(View view) {
        ID.clear();
        names.clear();
        alarmCount = 0;
        alarm.clear();

        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
//                Log.e("id", data.getString(0));
                ID.add(data.getString(0));
                names.add(data.getString(1));
            }
            Log.e("id teraz", ID.get(globalPosition));
            Cursor count = dogsDB.showCount(Integer.parseInt(ID.get(globalPosition)));
            while (count.moveToNext()) {
                alarmCount = count.getInt(0);
            }
        }

        Log.e("alarmCount", Integer.toString(alarmCount));

        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getAlarmCount(int id) {
        Cursor data = dogsDB.showCount(Integer.parseInt(ID.get(globalPosition)));
        while (data.moveToNext()) {

            alarmCount = data.getInt(0);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        loadData(getView());
        globalPosition = position;
        Log.e("id teraz", ID.get(globalPosition));
        getAlarmCount(Integer.parseInt(ID.get(globalPosition)));
        Log.e("alarmCount", Integer.toString(alarmCount));
        loadAlarms(Integer.parseInt(ID.get(globalPosition)));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
