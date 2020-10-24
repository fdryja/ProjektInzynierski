package com.example.projektinzynierski;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.RemoteCallbackList;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    DatabaseHelper dogsDB;
    Spinner spinnerSettings;
    //    Switch switchKarmienie, swithcWeterynarz;
    SeekBar seekBarCritical;
    private String selectedDog;
    private ArrayList<String> dogsList, ID, packageDB, package_fullDB, notkarDB, notwetDB, eatingDB;
    private ArrayAdapter<String> dogsAdapter;
    Button saveSettings;
    EditText editTextPackage;
    TextView settingsWarning1, settingsWarning2, textViewPercentage, textViewPrediction, textViewCriticalDay;
    int globalPosition = 0;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        switchKarmienie = getActivity().findViewById(R.id.switchKarmienie);
//        swithcWeterynarz = getActivity().findViewById(R.id.switchWeterynarz);

        seekBarCritical = getActivity().findViewById(R.id.seekBarCritical);
        textViewPercentage = getActivity().findViewById(R.id.textViewPercentage);
        textViewPrediction = getActivity().findViewById(R.id.textViewPrediction);
        textViewCriticalDay = getActivity().findViewById(R.id.textViewCriticalDay);
        editTextPackage = getActivity().findViewById(R.id.editTextPackage);

        dogsDB = new DatabaseHelper(getActivity());
        dogsList = new ArrayList<>();
        ID = new ArrayList<>();
        packageDB = new ArrayList<>();
        package_fullDB = new ArrayList<>();
        eatingDB = new ArrayList<>();
        notkarDB = new ArrayList<>();
        notwetDB = new ArrayList<>();
        saveSettings = getActivity().findViewById(R.id.saveSettings);
        settingsWarning1 = getActivity().findViewById(R.id.settingsWarning1);
        settingsWarning2 = getActivity().findViewById(R.id.settingsWarning2);

        spinnerSettings = getActivity().findViewById(R.id.spinnerSettings);
        loadData(getView());
        spinnerSettings.setOnItemSelectedListener(this);

        textViewPercentage.setText("30%" + " (" + decimalFormat.format(Integer.parseInt(packageDB.get(globalPosition)) * 0.3) + " kg)");
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID.isEmpty()) {
                    Toast.makeText(getActivity(), "Baza danych jest pusta", Toast.LENGTH_SHORT).show();
                } else {
                    updatePackage(Integer.parseInt(ID.get(globalPosition)),
                            Float.parseFloat(editTextPackage.getText().toString()), seekBarCritical.getProgress());
                }

            }
        });
        seekBarCritical.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                criticalPrediction(progress);
                int package_weight = Integer.parseInt(package_fullDB.get(globalPosition));

                if (progress == 1) {
                    textViewPercentage.setText("10%" + " (" + decimalFormat.format(package_weight * 0.1) + " kg)");
                } else if (progress == 2) {
                    textViewPercentage.setText("20%" + " (" + decimalFormat.format(package_weight * 0.2) + " kg)");
                } else if (progress == 3) {
                    textViewPercentage.setText("30%" + " (" + decimalFormat.format(package_weight * 0.3) + " kg)");
                } else if (progress == 4) {
                    textViewPercentage.setText("40%" + " (" + decimalFormat.format(package_weight * 0.4) + " kg)");
                } else {
                    textViewPercentage.setText("50%" + " (" + decimalFormat.format(package_weight * 0.5) + " kg)");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        switchKarmienie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ID.isEmpty()) {
//                    Toast.makeText(getActivity(), "Baza danych jest pusta", Toast.LENGTH_SHORT).show();
//                } else {
//                    updateNotKarmienie(Integer.parseInt(ID.get(globalPosition)),(switchKarmienie.isChecked()) ? 1:0);
//                }
//            }
//        });
//        swithcWeterynarz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ID.isEmpty()) {
//                    Toast.makeText(getActivity(), "Baza danych jest pusta", Toast.LENGTH_SHORT).show();
//                } else {
//                    updateNotWet(Integer.parseInt(ID.get(globalPosition)),(swithcWeterynarz.isChecked()) ? 1:0);
//                }
//            }
//        });

    }


    private void updatePackage(int id, float packageWeight, int sbprogress) {
        if (packageWeight >= 0) {
            dogsDB.updatePackage(id, packageWeight);
            dogsDB.updatePackageCritical(id, sbprogress);

            ID.clear();
            dogsList.clear();
            packageDB.clear();
            eatingDB.clear();
            package_fullDB.clear();
            notwetDB.clear();
            notkarDB.clear();
            spinnerSettings.setAdapter(null);
            loadData(getView());
            loadSettings();
            spinnerSettings.setSelection(globalPosition);
            Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Ilość karmy nie może być mniejsza niż 0", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadData(View v) {
        ID.clear();
        dogsList.clear();
        package_fullDB.clear();
        packageDB.clear();
        notkarDB.clear();
        notwetDB.clear();
        eatingDB.clear();
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
                eatingDB.add(data.getString(5));

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
            if (packageDB.get(globalPosition).equals("0")) {
                seekBarCritical.setProgress(3);
            } else {
                seekBarCritical.setProgress(Integer.parseInt(packageDB.get(globalPosition)));
            }
            int notkar = 0, notwet = 0;
            notkar = Integer.parseInt(notkarDB.get(globalPosition));
            notwet = Integer.parseInt(notwetDB.get(globalPosition));
            if (notkar == 0) {
//                switchKarmienie.setChecked(false);
            } else {
//                switchKarmienie.setChecked(true);
            }
            if (notwet == 0) {
//                swithcWeterynarz.setChecked(false);
            } else {
//                swithcWeterynarz.setChecked(true);
            }
            int eating = Integer.parseInt(eatingDB.get(globalPosition)),
                    daysRemaining = 0;
            float daysRemainingF = 0,
                    packageWeight = Float.parseFloat(package_fullDB.get(globalPosition));


            if (packageWeight > 0 && eating > 0) {
                settingsWarning1.setVisibility(View.VISIBLE);
                settingsWarning2.setVisibility(View.VISIBLE);
                packageWeight = packageWeight * 1000;
                daysRemainingF = packageWeight / eating;
                daysRemaining = (int) daysRemainingF;
//                settingsWarning2.setText(Integer.toString(daysRemaining)); //to pokazuje ile dni jest do końca karmy

                DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

                Date currentDate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(currentDate);
                c.add(Calendar.DATE, daysRemaining);
                Date remainingDate = c.getTime();
                settingsWarning2.setText(dateformat.format(remainingDate));

            } else {
                settingsWarning1.setVisibility(View.INVISIBLE);
                settingsWarning2.setVisibility(View.INVISIBLE);
            }

            if(Integer.parseInt(packageDB.get(globalPosition))>0){
                criticalPrediction(Integer.parseInt(packageDB.get(globalPosition)));
            }else{
                criticalPrediction(0);
            }


        }


    }

    private void criticalPrediction(int progress) {
        int eating = Integer.parseInt(eatingDB.get(globalPosition)),
                daysRemainingCritical = 0;
        float daysRemainingFCritical = 0,
                packageWeight = Float.parseFloat(package_fullDB.get(globalPosition)),
                packageWeightCritical = progress;
        if (packageWeightCritical > 0 && eating > 0 && packageWeight > 0) {
            textViewPrediction.setVisibility(View.VISIBLE);
            textViewCriticalDay.setVisibility(View.VISIBLE);
            packageWeight*=1000;

            if (packageWeightCritical == 1) {
                packageWeight = packageWeight*0.9f;
                daysRemainingFCritical = packageWeight / eating;
                daysRemainingCritical = (int) daysRemainingFCritical;
            } else if (packageWeightCritical == 2) {
                packageWeight = packageWeight*0.8f;
                daysRemainingFCritical = packageWeight / eating;
                daysRemainingCritical = (int) daysRemainingFCritical;
            } else if (packageWeightCritical == 3) {
                packageWeight = packageWeight*0.7f;
                daysRemainingFCritical = packageWeight / eating;
                daysRemainingCritical = (int) daysRemainingFCritical;
            } else if (packageWeightCritical == 4) {
                packageWeight = packageWeight*0.6f;
                daysRemainingFCritical = packageWeight / eating;
                daysRemainingCritical = (int) daysRemainingFCritical;
            } else{
                packageWeight = packageWeight*0.5f;
                daysRemainingFCritical = packageWeight / eating;
                daysRemainingCritical = (int) daysRemainingFCritical;
            }


//                settingsWarning2.setText(Integer.toString(daysRemaining)); //to pokazuje ile dni jest do końca karmy

            DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, daysRemainingCritical);
            Date remainingDate = c.getTime();
            textViewCriticalDay.setText(dateformat.format(remainingDate));

        } else {
            textViewCriticalDay.setVisibility(View.INVISIBLE);
            textViewPrediction.setVisibility(View.INVISIBLE);
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

    private void updateNotKarmienie(int id, int switchKarmienie) {
        dogsDB.updateNotKarmienie(id, switchKarmienie);
        ID.clear();
        dogsList.clear();
        packageDB.clear();
        package_fullDB.clear();
        notwetDB.clear();
        notkarDB.clear();
        eatingDB.clear();
        spinnerSettings.setAdapter(null);
        loadData(getView());
        loadSettings();
        spinnerSettings.setSelection(globalPosition);
//        Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
    }

    private void updateNotWet(int id, int switchWeterynarz) {
        dogsDB.updateNotWet(id, switchWeterynarz);
        ID.clear();
        dogsList.clear();
        packageDB.clear();
        eatingDB.clear();
        package_fullDB.clear();
        notwetDB.clear();
        notkarDB.clear();
        spinnerSettings.setAdapter(null);
        loadData(getView());
        loadSettings();
        spinnerSettings.setSelection(globalPosition);
//        Toast.makeText(getActivity().getApplicationContext(), "Zapisano", Toast.LENGTH_SHORT).show();
    }
}
