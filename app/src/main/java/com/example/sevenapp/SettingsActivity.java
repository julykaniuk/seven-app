package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.database.Cursor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.Arrays;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {
    EditText amount;
    Spinner timeSpinner;
    Button changeButton, saveButtonSettings;
    Switch  ukrainianHolidaysSwitch, notiSwitch;
    SharedPreferences sharedPreferences;
    int dateSP;
    boolean darkSP;
    String notificationSP, amountSP;

    public static final String MyPreferences = "MyPrefs";
    public static final String ReminderAmount = "ReminderAmountKey";
    public static final String ReminderDate = "ReminderDateKey";
    public static final String NotificationSound = "NotificationSoundKey";
    public static final String DarkMode = "DarkModeKey";

    private boolean showHolidays = true;

    public static final String NotificationEnabled = "NotificationEnabledKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        amount = findViewById(R.id.amountET);
        timeSpinner = findViewById(R.id.timeSpinner);
        changeButton = findViewById(R.id.changeButton);

        saveButtonSettings = findViewById(R.id.saveButtonSettings);
        ukrainianHolidaysSwitch = findViewById(R.id.holiSwitch);
        notiSwitch = findViewById(R.id.notiSwitch);

        sharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        showHolidays = sharedPreferences.getBoolean("ShowHolidays", true);
        ukrainianHolidaysSwitch.setChecked(showHolidays);
        ukrainianHolidaysSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showHolidays = isChecked;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("ShowHolidays", isChecked);
                editor.apply();
            }
        });




        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(NotificationEnabled, isChecked);
                editor.apply();
            }
        });
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(SettingsActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.date_array));
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(dateAdapter);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = timeSpinner.getSelectedItemPosition();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(ReminderDate, pos);
                editor.apply();
                loadAndUpdateData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationSP = sharedPreferences.getString(NotificationSound,
                        RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this,
                                RingtoneManager.TYPE_NOTIFICATION).toString());
                final Uri currentTone =  Uri.parse(notificationSP);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                startActivityForResult(intent, 0);
            }
        });

        saveButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ReminderAmount, amount.getText().toString());
                editor.apply();
                loadAndUpdateData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            if (data != null) {
                Uri uri = (Uri) data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(NotificationSound, uri.toString());
                    editor.apply();
                }
            }
        }
    }


    public void loadAndUpdateData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        darkSP = sharedPreferences.getBoolean(DarkMode, false);
        amountSP = sharedPreferences.getString(ReminderAmount, "10");
        dateSP = sharedPreferences.getInt(ReminderDate, 0);
        notiSwitch.setChecked(sharedPreferences.getBoolean(NotificationEnabled, false));

        amount.setText(amountSP);
        timeSpinner.setSelection(dateSP);
    }
}