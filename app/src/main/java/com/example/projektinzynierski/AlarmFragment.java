package com.example.projektinzynierski;

import android.app.TimePickerDialog;
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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AlarmFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private TextView alarmView1, alarmView2, alarmView3, alarmView4;
    private Button setAlarm1, setAlarm2, setAlarm3, setAlarm4, saveAlarm, resetAlarm;
    private DatabaseHelper dogsDB;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> ID, names, alarm;
    private ArrayList<Integer> alarmNumber;
    private int alarmCount, globalPosition = 0, alarm1hour, alarm2hour, alarm3hour, alarm4hour, alarm1minute, alarm2minute, alarm3minute, alarm4minute;
    private TimePickerDialog.OnTimeSetListener setListenerAlarm1, setListenerAlarm2, setListenerAlarm3, setListenerAlarm4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = getActivity().findViewById(R.id.spinnerAlarm);
        alarmView1 = getActivity().findViewById(R.id.alarmView1);
        alarmView2 = getActivity().findViewById(R.id.alarmView2);
        resetAlarm = getActivity().findViewById(R.id.resetAlarm);
        spinner.setOnItemSelectedListener(this);
        alarmView3 = getActivity().findViewById(R.id.alarmView3);
        alarmView4 = getActivity().findViewById(R.id.alarmView4);
        setAlarm1 = getActivity().findViewById(R.id.setAlarm1);
        setAlarm2 = getActivity().findViewById(R.id.setAlarm2);
        setAlarm3 = getActivity().findViewById(R.id.setAlarm3);
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

    private void resetAlarms() {
        alarmView1.setText("Brak");
        alarmView2.setText("Brak");
        alarmView3.setText("Brak");
        alarmView4.setText("Brak");
        saveAlarms();

    }

    private void saveAlarms() {
        if (alarm.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else if (alarm.size() == 1) {
            dogsDB.updateAlarm(alarmView1.getText().toString(),Integer.parseInt(ID.get(globalPosition)),1);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();

        } else if (alarm.size() == 2) {
            dogsDB.updateAlarm(alarmView1.getText().toString(),Integer.parseInt(ID.get(globalPosition)),1);
            dogsDB.updateAlarm(alarmView2.getText().toString(),Integer.parseInt(ID.get(globalPosition)),2);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
        } else if (alarm.size() == 3) {
            dogsDB.updateAlarm(alarmView1.getText().toString(),Integer.parseInt(ID.get(globalPosition)),1);
            dogsDB.updateAlarm(alarmView2.getText().toString(),Integer.parseInt(ID.get(globalPosition)),2);
            dogsDB.updateAlarm(alarmView3.getText().toString(),Integer.parseInt(ID.get(globalPosition)),3);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();

        } else if (alarm.size() == 4) {
            dogsDB.updateAlarm(alarmView1.getText().toString(),Integer.parseInt(ID.get(globalPosition)),1);
            dogsDB.updateAlarm(alarmView2.getText().toString(),Integer.parseInt(ID.get(globalPosition)),2);
            dogsDB.updateAlarm(alarmView3.getText().toString(),Integer.parseInt(ID.get(globalPosition)),3);
            dogsDB.updateAlarm(alarmView4.getText().toString(),Integer.parseInt(ID.get(globalPosition)),4);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAlarm1() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm1, alarm1hour, alarm1minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getAlarm2() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm2, alarm2hour, alarm2minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getAlarm3() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                setListenerAlarm3, alarm3hour, alarm3minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getAlarm4() {
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
