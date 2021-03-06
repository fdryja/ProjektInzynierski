package com.example.projektinzynierski;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class EatingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    DatabaseHelper dogsDB;
    private ArrayList<String> dogsList, ID, eatingDB, eatingCountDB;
    private ArrayAdapter<String> dogsAdapter;
    Spinner spinner;
    private String selectedDog;
    Button saveButton;
    EditText eating;
    TextView textViewCount, wielkoscPorcji, karmyNadzien;
    SeekBar eatingCount;
    int globalPosition = 0, grams = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wielkoscPorcji = getActivity().findViewById(R.id.wielkoscPorcji);
        saveButton = getActivity().findViewById(R.id.saveButton);
        eating = getActivity().findViewById(R.id.eating);
        karmyNadzien = getActivity().findViewById(R.id.karmyNadzien);
        textViewCount = getActivity().findViewById(R.id.textViewCount);
        eatingCount = getActivity().findViewById(R.id.eatingCount);
        dogsDB = new DatabaseHelper(getActivity());
        dogsList = new ArrayList<>();
        ID = new ArrayList<>();
        eatingDB = new ArrayList<>();
        eatingCountDB = new ArrayList<>();

        spinner = getActivity().findViewById(R.id.spinner);
        loadData(getView());
        spinner.setOnItemSelectedListener(this);

        eatingCountChange();
        loadEating();
        if (TextUtils.isEmpty(eating.getText())) {

        } else {
            grams = Integer.parseInt(eating.getText().toString());
        }
        int result = grams / eatingCount.getProgress();
        wielkoscPorcji.setText("Wielkość porcji: " + result + "g");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID.isEmpty()) {
                    Toast.makeText(getActivity(), "Baza danych jest pusta", Toast.LENGTH_SHORT).show();
                } else {
                    updateEating(
                            Integer.parseInt(ID.get(globalPosition)),
                            eatingCount.getProgress(),
                            Integer.parseInt(eating.getText().toString()));

                }

            }
        });

    }



    private void updateEating(int id, int eatingCount, int eating) {
        dogsDB.updateEating(id, eatingCount, eating);
        dogsDB.deleteAlarm(id);

        for(int i = 1; i<=eatingCount;i++){
            dogsDB.addAlarm(id, i);
        }

        ID.clear();
        dogsList.clear();
        eatingDB.clear();
        eatingCountDB.clear();
        spinner.setAdapter(null);
        loadData(getView());
        loadEating();
        spinner.setSelection(globalPosition);
        int result = eating/eatingCount;
        wielkoscPorcji.setText("Wielkość porcji: " + result + "g");
        Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();




    }

    private void eatingCountChange() {


        eatingCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (TextUtils.isEmpty(eating.getText())) {

                } else {
                    grams = Integer.parseInt(eating.getText().toString());
                }
                int result = grams / progress;
                wielkoscPorcji.setText("Wielkość porcji: " + result + "g");

                if (progress == 1) {
                    textViewCount.setText("1 raz dziennie");
                } else if (progress == 2) {
                    textViewCount.setText("2 razy dziennie");
                } else if (progress == 3) {
                    textViewCount.setText("3 razy dziennie");
                } else {
                    textViewCount.setText("4 razy dziennie");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void loadData(View v) {
        //wypełnienie tablicy
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else {
            eating.setClickable(true);
            saveButton.setVisibility(View.VISIBLE);
            karmyNadzien.setVisibility(View.VISIBLE);
            textViewCount.setVisibility(View.VISIBLE);
            wielkoscPorcji.setVisibility(View.VISIBLE);
            eating.setVisibility(View.VISIBLE);
            eatingCount.setVisibility(View.VISIBLE);
            while (data.moveToNext()) {
                Log.e("tag", data.getString(0));
                ID.add(data.getString(0));
                dogsList.add(data.getString(1));
                eatingDB.add(data.getString(5));
                eatingCountDB.add(data.getString(4));

            }
        }
        dogsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dogsList);
        spinner.setAdapter(dogsAdapter);

    }

    private void loadEating() {
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        } else {
            eating.setText(eatingDB.get(globalPosition));
            eatingCount.setProgress(Integer.parseInt(eatingCountDB.get(globalPosition)));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("tag", parent.getItemAtPosition(position).toString());
        selectedDog = parent.getItemAtPosition(position).toString();
        globalPosition = position;
        loadEating();
        if (TextUtils.isEmpty(eating.getText())) {

        } else {
            grams = Integer.parseInt(eating.getText().toString());
        }
        int result = grams / eatingCount.getProgress();
        wielkoscPorcji.setText("Wielkość porcji: " + result + "g");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
