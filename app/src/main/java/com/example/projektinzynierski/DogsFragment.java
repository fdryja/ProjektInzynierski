package com.example.projektinzynierski;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormatSymbols;

public class DogsFragment extends Fragment {
    ListView listView;
    String[] months;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.listView);
        months = new DateFormatSymbols().getMonths();
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, months);
        listView.setAdapter(monthsAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dogs, container, false);
    }
}
