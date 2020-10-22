package com.example.projektinzynierski;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    DatabaseHelper dogsDB;
    Spinner spinnerSettings;
    Switch switchKarmienie, swithcWeterynarz;
    private String selectedDog;
    private ArrayList<String> dogsList, ID, packageDB, package_fullDB, notkarDB, notwetDB;
    private ArrayAdapter<String> dogsAdapter;
    Button saveSettings;
    EditText editTextPackage;
    int globalPosition = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextPackage = getActivity().findViewById(R.id.editTextPackage);
        switchKarmienie = getActivity().findViewById(R.id.switchKarmienie);
        swithcWeterynarz = getActivity().findViewById(R.id.switchWeterynarz);
        dogsDB = new DatabaseHelper(getActivity());
        dogsList = new ArrayList<>();
        ID = new ArrayList<>();
        packageDB = new ArrayList<>();
        package_fullDB = new ArrayList<>();
        notkarDB = new ArrayList<>();
        notwetDB = new ArrayList<>();
        saveSettings = getActivity().findViewById(R.id.saveSettings);

        spinnerSettings = getActivity().findViewById(R.id.spinnerSettings);
        loadData(getView());
        spinnerSettings.setOnItemSelectedListener(this);

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID.isEmpty()) {
                    Toast.makeText(getActivity(), "Baza danych jest pusta", Toast.LENGTH_SHORT).show();
                } else {
                    updatePackage(Integer.parseInt(ID.get(globalPosition)),
                            Float.parseFloat(editTextPackage.getText().toString()));

                }

            }
        });

    }

    private void updatePackage(int id, float packageWeight){
        dogsDB.updatePackage(id, packageWeight);

        ID.clear();
        dogsList.clear();
        packageDB.clear();
        package_fullDB.clear();
        notwetDB.clear();
        notkarDB.clear();
        spinnerSettings.setAdapter(null);
        loadData(getView());
        loadSettings();
        spinnerSettings.setSelection(globalPosition);
        Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();

    }

    private void loadData(View v) {
        //wypełnienie tablicy
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
        } else {
//            editTextPackage.setClickable(true);

            while (data.moveToNext()) {
                Log.e("tag", data.getString(0));
                ID.add(data.getString(0));
                dogsList.add(data.getString(1));
                package_fullDB.add(data.getString(6));
                packageDB.add(data.getString(7));
                notkarDB.add(data.getString(8));
                notwetDB.add(data.getString(9));

            }
        }
        dogsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dogsList);
        spinnerSettings.setAdapter(dogsAdapter);

    }

    private void loadSettings() {
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        } else {
            editTextPackage.setText((package_fullDB.get(globalPosition)));
            int notkar = 0, notwet = 0;
            notkar = Integer.parseInt(notkarDB.get(globalPosition));
            notwet = Integer.parseInt(notwetDB.get(globalPosition));
            if(notkar==0){
                switchKarmienie.setChecked(false);
            }else{
                switchKarmienie.setChecked(true);
            }
            if(notwet==0){
                swithcWeterynarz.setChecked(false);
            }else{
                swithcWeterynarz.setChecked(true);
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("tag", parent.getItemAtPosition(position).toString());
        selectedDog = parent.getItemAtPosition(position).toString();
        globalPosition = position;
        loadSettings();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
