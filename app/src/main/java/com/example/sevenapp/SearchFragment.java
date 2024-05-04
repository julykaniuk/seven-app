package com.example.sevenapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.database.Cursor;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    private EventDBHelper dbHelper;
    private RecyclerView recyclerView;
    private EventAdapter1 eventAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        dbHelper = new EventDBHelper(getActivity());
        recyclerView = view.findViewById(R.id.weeklyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EditText searchEditText = view.findViewById(R.id.search_query);
        ImageView searchButton = view.findViewById(R.id.search_button); // Передбачається, що є кнопка пошуку
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                performSearch(query);
            }
        });

        return view;
    }

    private void performSearch(String eventName) {
        if (eventName.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter an event name", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = dbHelper.findEventByName(eventName);
        eventAdapter = new EventAdapter1(getActivity(), cursor);
        recyclerView.setAdapter(eventAdapter);

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No events found", Toast.LENGTH_SHORT).show();
        }
    }
}
