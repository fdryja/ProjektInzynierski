package com.example.projektinzynierski;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UpdateDialog extends AppCompatDialogFragment {
    private SeekBar seekBarDialog;
    private TextView poziomAktywnosciDialog;
    private EditText editName;
    private EditText editWeight;
    private UpdateDialogListener listener;
    public static String nameImported ="";
    public static String weightImported ="";
    public static int activityLevelImported =-1;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);


        builder.setView(view)
                .setTitle("Edycja")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(editName.getText().toString()) || TextUtils.isEmpty(editWeight.getText().toString())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Nie wypełniono wszystich pól", Toast.LENGTH_SHORT).show();
                } else {
                    String name = editName.getText().toString();
                    int weight = Integer.parseInt(editWeight.getText().toString());
                    int activityLevel = seekBarDialog.getProgress();
                    listener.applyTexts(name, weight, activityLevel);
                }

            }
        });



        seekBarDialog = view.findViewById(R.id.seekBarDialog);
        poziomAktywnosciDialog = view.findViewById(R.id.poziomAktywnosciDialog);
        editName = view.findViewById(R.id.editName);
        editWeight = view.findViewById(R.id.editWeight);
        editName.setText(nameImported);
        editWeight.setText(weightImported);
        seekBarDialog.setProgress(activityLevelImported);

        if (activityLevelImported == 0) {
            poziomAktywnosciDialog.setText("Niski");
        } else if (activityLevelImported == 1) {
            poziomAktywnosciDialog.setText("Normalny");
        } else {
            poziomAktywnosciDialog.setText("Wysoki");
        }

        seekBarDialog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    poziomAktywnosciDialog.setText("Niski");
                } else if (progress == 1) {
                    poziomAktywnosciDialog.setText("Normalny");
                } else {
                    poziomAktywnosciDialog.setText("Wysoki");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return builder.create();
    }
    public void importData(String name, String weight, int al){
        nameImported = name;
        weightImported = weight;
        activityLevelImported = al;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (UpdateDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement UpdateDialogListener");
        }
    }

    public interface UpdateDialogListener {
        void applyTexts(String name, int weight, int activityLevel);
    }

}


