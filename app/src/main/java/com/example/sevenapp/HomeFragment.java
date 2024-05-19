package com.example.sevenapp;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter1 adapter;
    private SQLiteDatabase mDatabase;
    private boolean showHolidays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.weeklyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventDBHelper dbHelper = new EventDBHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SettingsActivity.MyPreferences, Context.MODE_PRIVATE);
        showHolidays = sharedPreferences.getBoolean("ShowHolidays", true);

        adapter = new EventAdapter1(getContext(), fetchEventsFromDatabase());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private Cursor fetchEventsFromDatabase() {
        String currentDateTime = getCurrentDateTime();
        String sqlQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_START + " >= '" + currentDateTime + "'";
        if (!showHolidays) {
            sqlQuery += " AND " + EventDB.Event.COLUMN_IS_SPECIAL + " = 0";
        }
        sqlQuery += " ORDER BY datetime(" + EventDB.Event.COLUMN_START + ") ASC;";
        return mDatabase.rawQuery(sqlQuery, null);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
