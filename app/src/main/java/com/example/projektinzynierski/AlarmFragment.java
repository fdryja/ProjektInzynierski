package com.example.projektinzynierski;

import android.app.TimePickerDialog;
import android.database.Cursor;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AlarmFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private Spinner spinner;
    private TextView alarmView1, alarmView2, alarmView3, alarmView4;
    private Button setAlarm1, setAlarm2, setAlarm3, setAlarm4, saveAlarm;
    private DatabaseHelper dogsDB;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> ID, names, alarm;
    private int alarmCount, globalPosition = 0;
    private TimePickerDialog.OnTimeSetListener setListenerAlarm1,setListenerAlarm2,setListenerAlarm3,setListenerAlarm4;

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
        alarmView3 = getActivity().findViewById(R.id.alarmView3);
        alarmView4 = getActivity().findViewById(R.id.alarmView4);
        setAlarm1 = getActivity().findViewById(R.id.setAlarm1);
        setAlarm2 = getActivity().findViewById(R.id.setAlarm2);
        setAlarm3 = getActivity().findViewById(R.id.setAlarm3);
        setAlarm4 = getActivity().findViewById(R.id.setAlarm4);
        ID = new ArrayList<>();
        names = new ArrayList<>();
        alarm = new ArrayList<>();
        dogsDB = new DatabaseHelper(getActivity());
        try {
            loadData(getView());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest sss", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadData(View view) {
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {

            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        } else {

            while (data.moveToNext()) {
                Log.e("id", data.getString(0));
                ID.add(data.getString(0));
                names.add(data.getString(1));
            }
        }

        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
