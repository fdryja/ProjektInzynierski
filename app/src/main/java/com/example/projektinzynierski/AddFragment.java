package com.example.projektinzynierski;

import android.os.Bundle;
import android.text.TextUtils;
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
    TextView poziomAktywnosci;
    SeekBar seekBar;
    Button button;
    EditText imiePsa, masaPsa;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        poziomAktywnosci = getView().findViewById(R.id.poziomAktywnosci);
        seekBar = getView().findViewById(R.id.seekBar);
        button = getView().findViewById(R.id.button);
        imiePsa = getView().findViewById(R.id.imiePsa);
        masaPsa = getView().findViewById(R.id.masaPsa);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);
    }


    public void dodajPsa(View view){

        if(TextUtils.isEmpty(imiePsa.getText().toString() )|| TextUtils.isEmpty(masaPsa.getText().toString())){
            Toast.makeText(getActivity().getApplicationContext(), "Nie wypełniono wszystich pól", Toast.LENGTH_SHORT).show();
        }else{
            //dodawnie psa do bazy
            Toast.makeText(getActivity().getApplicationContext(), "Dodano psa!", Toast.LENGTH_SHORT).show();
        }
    }

}
