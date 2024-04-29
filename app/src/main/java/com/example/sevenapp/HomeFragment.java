package com.example.sevenapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  {
    private RecyclerView recyclerView;
    private EventAdapter1 adapter;
    private SQLiteDatabase mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.weeklyRecyclerView); // переконайтеся, що це правильний ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventDBHelper dbHelper = new EventDBHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();

        adapter = new EventAdapter1(getContext(), fetchEventsFromDatabase()); // Ваш адаптер
        recyclerView.setAdapter(adapter);

        return view;
    }

    private Cursor fetchEventsFromDatabase() {
        // Змініть SQL-запит відповідно до вашої таблиці подій
        String sqlQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME + " ORDER BY datetime(" + EventDB.Event.COLUMN_START + ") ASC;";
        return mDatabase.rawQuery(sqlQuery, null);
    }
}
