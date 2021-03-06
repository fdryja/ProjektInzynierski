package com.example.projektinzynierski;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddFragment extends Fragment {
    private TextView poziomAktywnosci;
    private SeekBar seekBar;
    private Button button;
    private EditText imiePsa, masaPsa;
    private DatabaseHelper dogsDB;
    int dogID = -1;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        poziomAktywnosci = getView().findViewById(R.id.poziomAktywnosci);
        seekBar = getView().findViewById(R.id.seekBar);
        button = getView().findViewById(R.id.button);
        imiePsa = getView().findViewById(R.id.imiePsa);
        masaPsa = getView().findViewById(R.id.masaPsa);
        dogsDB = new DatabaseHelper(getActivity());
        poziomAktywnosci.setText("Normalny");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    poziomAktywnosci.setText("Niski");
                } else if (progress == 1) {
                    poziomAktywnosci.setText("Normalny");
                } else {
                    poziomAktywnosci.setText("Wysoki");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajPsa(getView());
            }
        });

    }

    private void display(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    private void dodajPsa(View view){

        if(TextUtils.isEmpty(imiePsa.getText().toString() )|| TextUtils.isEmpty(masaPsa.getText().toString())){
            Toast.makeText(getActivity().getApplicationContext(), "Nie wypełniono wszystich pól", Toast.LENGTH_SHORT).show();
        }else{
            //dodawnie psa do bazy
            String name = imiePsa.getText().toString();
            int weight = Integer.parseInt(masaPsa.getText().toString()) ;
            int activityLevel = seekBar.getProgress();
            boolean insertData = dogsDB.addData(name, weight, activityLevel);


            if(insertData){
                Toast.makeText(getActivity().getApplicationContext(), "Dodano psa!", Toast.LENGTH_SHORT).show();
                imiePsa.setText("");
                masaPsa.setText("");
                seekBar.setProgress(1);
                Cursor cursor = dogsDB.getLastId();
                if(cursor.getCount()==0){
                    Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();
                }else{
                    while (cursor.moveToNext()) {
                        Log.e("tag", cursor.getString(0));
                        dogID = Integer.parseInt(cursor.getString(0));
                        dogsDB.addDataDates(dogID);
                    }


                }

            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Błąd przy dodawaniu psa!", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
