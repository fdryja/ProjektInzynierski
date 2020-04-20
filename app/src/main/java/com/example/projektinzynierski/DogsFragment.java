package com.example.projektinzynierski;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DogsFragment extends Fragment implements UpdateDialog.UpdateDialogListener {
    private ListView listView;
    private DatabaseHelper dogsDB;
    private ArrayAdapter<String> dogsAdapter;
    private int globalPosition = 0;
    private ArrayList<String> dogsList, ID, weight, activityLevel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.listView);
        dogsList = new ArrayList<>();

        ID = new ArrayList<>();
        dogsDB = new DatabaseHelper(getActivity());
        weight = new ArrayList<>();
        activityLevel = new ArrayList<>();
        loadData(getView());
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity().getApplicationContext(), "Wybrano: "+ dogsList.get(position), Toast.LENGTH_SHORT).show();
                deleteDog(position);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMoreInfo(position);
            }
        });


    }




    @Override
    public void applyTexts(String name, int weightt, int activityLevell) {
        dogsDB.updateDogData(name,globalPosition,weightt,activityLevell);
        globalPosition = 0;
//        Toast.makeText(getActivity().getApplicationContext(), "Usunięto psa o imieniu " + imie, Toast.LENGTH_SHORT).show();
        dogsAdapter.clear();
        dogsList.clear();
        activityLevel.clear();
        weight.clear();
        ID.clear();
        listView.setAdapter(null);
        loadData(getView());
    }

    private void showMoreInfo(int position) {
        String poziomAktywności;
        if (activityLevel.get(position).equals("0")) {
            poziomAktywności = "Niski";
        } else if (activityLevel.get(position).equals("1")) {
            poziomAktywności = "Normalny";
        } else {
            poziomAktywności = "Wysoki";
        }
        display("Wybrany pies", "Imię: " + dogsList.get(position) + "\nMasa: "
                + weight.get(position) + " kg" + "\nPoziom aktywności: " + poziomAktywności,position);
    }


    private void deleteDog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Usuwanie psa")
                .setMessage("Czy na pewno chcesz usunąc psa o imieniu " + dogsList.get(position) + "?")
                .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //usuwanie psa
                        String imie = dogsList.get(position);
                        dogsDB.deleteData(Integer.parseInt(ID.get(position)));
                        Toast.makeText(getActivity().getApplicationContext(), "Usunięto psa o imieniu " + imie, Toast.LENGTH_SHORT).show();
                        dogsAdapter.clear();
                        dogsList.clear();
                        ID.clear();
                        activityLevel.clear();
                        weight.clear();
                        listView.setAdapter(null);
                        loadData(getView());
                    }
                }).setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void display(String title, String message, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Edytuj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openDialog(dogsList.get(position),weight.get(position),Integer.parseInt(activityLevel.get(position)));
                globalPosition = Integer.parseInt(ID.get(position));

            }
        }).show();
    }
    public void openDialog(String name, String weight, int activityLevel){
        UpdateDialog updateDialog = new UpdateDialog();
        updateDialog.importData(name, weight,activityLevel);
        updateDialog.show(getFragmentManager().beginTransaction(), "dialog");
        updateDialog.setTargetFragment(DogsFragment.this, 1);
    }
    private void loadData(View v) {
        //wypełnienie tablicy
        Cursor data = dogsDB.showData();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        } else {

            while (data.moveToNext()) {
                ID.add(data.getString(0));
                dogsList.add(data.getString(1));
                weight.add(data.getString(2));
                activityLevel.add(data.getString(3));

            }
        }
        dogsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dogsList);
        listView.setAdapter(dogsAdapter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dogs, container, false);
    }
}
