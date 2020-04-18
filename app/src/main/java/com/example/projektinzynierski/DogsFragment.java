package com.example.projektinzynierski;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class DogsFragment extends Fragment {
    ListView listView;
    DatabaseHelper dogsDB;
    ArrayList<String> dogsList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.listView);
        dogsList = new ArrayList<>();
        dogsDB = new DatabaseHelper(getActivity());
        loadData(getView());
    }

    public void loadData(View v){
        //wypełnienie tablicy
        Cursor data = dogsDB.showData();
        if(data.getCount()==0){
            Toast.makeText(getActivity().getApplicationContext(), "baza danych jest pusta", Toast.LENGTH_SHORT).show();

        }else{
            while(data.moveToNext()){
                dogsList.add(data.getString(1));
            }
        }
        ArrayAdapter<String> dogsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dogsList);
        listView.setAdapter(dogsAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dogs, container, false);
    }
}
