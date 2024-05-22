package com.example.sevenapp;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.view.MenuInflater;
import android.view.Menu;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import static com.example.sevenapp.SettingsActivity.DarkMode;
import static com.example.sevenapp.SettingsActivity.MyPreferences;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.content.res.Resources;

import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarView mCalendarView;
    private Calendar c;
    private Toolbar toolbar;
    private TextView monthTV, yearTV;
    private Button todayButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        Resources resources = getResources();

        mCalendarView = view.findViewById(R.id.calendarView);
        monthTV = view.findViewById(R.id.monthText);
        yearTV = view.findViewById(R.id.yearText);
        String[] monthsArray = resources.getStringArray(R.array.months);

        c = Calendar.getInstance();
        monthTV.setText(monthsArray[c.get(Calendar.MONTH)]);
        yearTV.setText(Integer.toString(c.get(Calendar.YEAR)));


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month,
                                            int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                monthTV.setText(monthsArray[c.get(Calendar.MONTH)]);
                yearTV.setText(Integer.toString(c.get(Calendar.YEAR)));
                String currentDateString = DateFormat.getDateFormat(requireContext()).format(c.getTime());

                Intent eventListIntent = new Intent(requireContext(), EventList.class);
                eventListIntent.putExtra("DAY", day);
                eventListIntent.putExtra("MONTH", month);
                eventListIntent.putExtra("YEAR", year);
                eventListIntent.putExtra("DAY_NAME", currentDateString.split(",")[0]);
                startActivity(eventListIntent);
            }
        });

        todayButton = view.findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setDate(c.getTimeInMillis());
                c = Calendar.getInstance();
                monthTV.setText(monthsArray[c.get(Calendar.MONTH)]);
                yearTV.setText(Integer.toString(c.get(Calendar.YEAR)));
            }
        });

        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.example_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.weekly) {
            Intent weeklyIntent = new Intent(requireContext(), WeeklyListActivity.class);
            weeklyIntent.putExtra("Type", "Weekly");
            startActivity(weeklyIntent);
            return true;
        } else if (itemId == R.id.monthly) {
            Intent monthlyIntent = new Intent(requireContext(), WeeklyListActivity.class);
            monthlyIntent.putExtra("Type", "Monthly");
            startActivity(monthlyIntent);
            return true;
        } else if (itemId == R.id.settings) {
            Intent settingsIntent = new Intent(requireContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
