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
        spinner.setOnItemSelectedListener(this);
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

        alarmVisibility(getView());
        Log.e("alarmCount",Integer.toString(alarmCount));

    }

    private void alarmVisibility(View v){
        if(alarmCount == 1){
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.INVISIBLE);
            setAlarm2.setVisibility(View.INVISIBLE);
            alarmView3.setVisibility(View.INVISIBLE);
            setAlarm3.setVisibility(View.INVISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
        }else if(alarmCount == 2){
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.VISIBLE);
            setAlarm2.setVisibility(View.VISIBLE);
            alarmView3.setVisibility(View.INVISIBLE);
            setAlarm3.setVisibility(View.INVISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
        }else if(alarmCount == 3){
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.VISIBLE);
            setAlarm2.setVisibility(View.VISIBLE);
            alarmView3.setVisibility(View.VISIBLE);
            setAlarm3.setVisibility(View.VISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
        }else if(alarmCount == 4){
            alarmView1.setVisibility(View.VISIBLE);
            setAlarm1.setVisibility(View.VISIBLE);
            alarmView2.setVisibility(View.VISIBLE);
            setAlarm2.setVisibility(View.VISIBLE);
            alarmView3.setVisibility(View.VISIBLE);
            setAlarm3.setVisibility(View.VISIBLE);
            alarmView4.setVisibility(View.VISIBLE);
            setAlarm4.setVisibility(View.VISIBLE);
        }else{
            alarmView1.setVisibility(View.INVISIBLE);
            setAlarm1.setVisibility(View.INVISIBLE);
            alarmView2.setVisibility(View.INVISIBLE);
            setAlarm2.setVisibility(View.INVISIBLE);
            alarmView3.setVisibility(View.INVISIBLE);
            setAlarm3.setVisibility(View.INVISIBLE);
            alarmView4.setVisibility(View.INVISIBLE);
            setAlarm4.setVisibility(View.INVISIBLE);
        }
    }

    private void loadAlarms(int id){

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
            Log.e("id teraz",ID.get(globalPosition));

            Cursor count = dogsDB.showCount(Integer.parseInt(ID.get(globalPosition)) );
            while (count.moveToNext()) {

                alarmCount = count.getInt(0);

            }
        }


        Log.e("alarmCount",Integer.toString(alarmCount));

        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getAlarmCount(int id){
        Cursor data = dogsDB.showCount(Integer.parseInt( ID.get(globalPosition)));
        while (data.moveToNext()) {

            alarmCount = data.getInt(0);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        loadData(getView());
        Log.e("alarmCount",Integer.toString(alarmCount));
        globalPosition = position;
        Log.e("id teraz",ID.get(globalPosition));
        getAlarmCount(Integer.parseInt(ID.get(globalPosition)));
        loadAlarms(Integer.parseInt( ID.get(globalPosition)));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
