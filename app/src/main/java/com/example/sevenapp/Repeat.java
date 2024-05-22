package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Repeat extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RadioGroup repeatRadioGroup, durationRadioGroup;
    private RadioButton repeatRB, durationRB;
    private CheckBox monCB, tueCB, wenCB, thuCB, friCB, satCB, sunCB;
    private EditText everyET, repetitionET;
    private TextView dayTW, durationTW, everyTW, oldRepeatTV;

    private ImageButton calendarButton;
    private Button saveButton, clearButton;
    private Calendar c;
    private String currentDateString;
    private int DAY, YEAR, MONTH, ID, SERI;


    private boolean mon, tue, wed, thu, fri, sat, sun, oldRepeat = false, deleted = false, newRepeat = true;
    private Intent data;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat);
        EventDBHelper dbHelper = new EventDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        repeatRadioGroup = findViewById(R.id.repeatRadioGroup);
        durationRadioGroup = findViewById(R.id.durationRadioGroup);
        monCB = findViewById(R.id.mondayCB);
        tueCB = findViewById(R.id.tuesdayCB);
        wenCB = findViewById(R.id.wednesdayCB);
        thuCB = findViewById(R.id.thursdayCB);
        friCB = findViewById(R.id.fridayCB);
        satCB = findViewById(R.id.saturdayCB);
        sunCB = findViewById(R.id.sundayCB);
        everyET = findViewById(R.id.everyET);
        repetitionET = findViewById(R.id.repeatET);
        calendarButton = findViewById(R.id.calendarButton);
        dayTW = findViewById(R.id.dayTW);
        everyTW = findViewById(R.id.everyText);
        durationTW = findViewById(R.id.durationText);
        saveButton = findViewById(R.id.saveButtonRepeat);
        clearButton = findViewById(R.id.clearButton);
        oldRepeatTV = findViewById(R.id.oldRepeat);
        RadioButton neverRadioButton = findViewById(R.id.neverR);

        neverRadioButton.setChecked(true);
        visibilityController("Nev");

        DAY = getIntent().getIntExtra("DAY", 1);
        YEAR = getIntent().getIntExtra("YEAR", 2020);
        MONTH = getIntent().getIntExtra("MONTH", 0);

        if (getIntent().hasExtra("ID")) {
            ID = getIntent().getIntExtra("ID", -1);
            SERI = getIntent().getIntExtra("SERI", -1);
            hasOldRepeat();
        } else {
            clearButton.setVisibility(View.INVISIBLE);
            oldRepeatTV.setVisibility(View.INVISIBLE);
        }

        data = new Intent();

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putInt("DAY", DAY);
                args.putInt("YEAR", YEAR);
                args.putInt("MONTH", MONTH);
                datePicker.setArguments(args);
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOldRepeats();
                clearButton.setVisibility(View.INVISIBLE);
                oldRepeatTV.setVisibility(View.INVISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatRB = findViewById(repeatRadioGroup.getCheckedRadioButtonId());
                durationRB = findViewById(durationRadioGroup.getCheckedRadioButtonId());

                if (repeatRB != null && durationRB != null) {
                    if (!oldRepeat) {
                        String day = dayTW.getText().toString();
                        String every = everyET.getText().toString();

                        // Перевірка поточної мови
                        String currentLanguage = Locale.getDefault().getLanguage();
                        String text = "";

                        if (currentLanguage.equals("uk")) {
                            text = getString(R.string.every) + " " + every + " " + day + ".";
                        } else {
                            text = getString(R.string.every) + " " + every + " " + day + ".";
                        }

                        data.putExtra("Seri Type", text);
                        data.putExtra("Repeat Type", repeatRB.getText().toString());

                        if (!repeatRB.getText().toString().equals(getString(R.string.never))) {
                            data.putExtra("Repeat Frequency", everyET.getText().toString());
                            data.putExtra("Duration Type", durationRB.getText().toString());
                            newRepeat = true;
                        }

                        if (durationRB.getText().toString().equals(getString(R.string.repetitions))) {
                            data.putExtra("Repeat Count", repetitionET.getText().toString());
                            newRepeat = true;
                        } else if (durationRB.getText().toString().equals(getString(R.string.until))) {
                            data.putExtra("Until Date", currentDateString);
                            newRepeat = true;
                        }

                        if (repeatRB.getText().toString().equals(getString(R.string.weekly))) {
                            boolean[] daysOfWeek = new boolean[7];
                            daysOfWeek[0] = sun;
                            daysOfWeek[1] = mon;
                            daysOfWeek[2] = tue;
                            daysOfWeek[3] = wed;
                            daysOfWeek[4] = thu;
                            daysOfWeek[5] = fri;
                            daysOfWeek[6] = sat;
                            data.putExtra("Days of Week", daysOfWeek);
                            newRepeat = true;
                        }

                        if (!oldRepeat && !newRepeat) {
                            data.putExtra("Deleted", true);
                        }

                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        Snackbar mySnackbar = Snackbar.make(v, getString(R.string.repeat_twice), Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }
                } else {
                    Toast.makeText(Repeat.this, getString(R.string.select_repeat), Toast.LENGTH_SHORT).show();
                }
            }
        });

        repeatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                repeatCheck(group.findViewById(checkedId));
            }
        });
    }

    public void deleteOldRepeats() {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_SERI + "='" + SERI + "' ORDER BY datetime(" +
                EventDB.Event.COLUMN_START + ") ASC;";

        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
                if (idIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    mDatabase.delete(EventDB.Event.TABLE_NAME, EventDB.Event.COLUMN_ID + "=" + id, null);
                }
            } while (cursor.moveToNext());

            oldRepeat = false;
            deleted = true;

            ContentValues cv = new ContentValues();
            cv.put(EventDB.Event.COLUMN_SERI, -1);
            mDatabase.update(EventDB.Event.TABLE_NAME, cv, EventDB.Event.COLUMN_ID + "=" + ID, null);
        }
        cursor.close();
    }

    public void hasOldRepeat() {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_ID + "='" + ID + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        if (cursor.moveToFirst()) {
            int seriIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_SERI);
            if (seriIndex != -1) {
                do {
                    int seriValue = cursor.getInt(seriIndex);
                    if (seriValue != -1) {
                        int seriTypeIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_SERI_TYPE);
                        if (seriTypeIndex != -1) {
                            String seriType = cursor.getString(seriTypeIndex);
                            clearButton.setVisibility(View.VISIBLE);
                            oldRepeatTV.setVisibility(View.VISIBLE);
                            oldRepeatTV.setText(seriType);
                            oldRepeat = true;
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDateString = df.format(c.getTime());
    }

    public void dayChecked(View view) {
        mon = monCB.isChecked();
        tue = tueCB.isChecked();
        wed = wenCB.isChecked();
        thu = thuCB.isChecked();
        fri = friCB.isChecked();
        sat = satCB.isChecked();
        sun = sunCB.isChecked();
    }

    public void repeatCheck(View view) {
        int checkedRadioButtonId = repeatRadioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.neverR) {
            setVisibilityForNeverRepeat();
            durationRadioGroup.setVisibility(View.INVISIBLE);
            durationTW.setVisibility(View.INVISIBLE);

        } else if (checkedRadioButtonId == R.id.dailyR) {
            setVisibilityForDailyRepeat();
            durationRadioGroup.setVisibility(View.VISIBLE);
            durationTW.setVisibility(View.VISIBLE);

        } else if (checkedRadioButtonId == R.id.weeklyR) {
            setVisibilityForWeeklyRepeat();
            durationRadioGroup.setVisibility(View.VISIBLE);
            durationTW.setVisibility(View.VISIBLE);

        } else if (checkedRadioButtonId == R.id.monthlyR) {
            setVisibilityForMonthlyRepeat();
            durationRadioGroup.setVisibility(View.VISIBLE);
            durationTW.setVisibility(View.VISIBLE);

        } else if (checkedRadioButtonId == R.id.yearlyR) {
            setVisibilityForYearlyRepeat();
            durationRadioGroup.setVisibility(View.VISIBLE);
            durationTW.setVisibility(View.VISIBLE);

        } else {
            throw new IllegalArgumentException("Invalid radio button ID");
        }
    }
    private void setVisibilityForNeverRepeat() {
        everyTW.setVisibility(View.INVISIBLE);
        everyET.setVisibility(View.INVISIBLE);
        dayTW.setVisibility(View.INVISIBLE);
        setDaysVisibility(View.INVISIBLE);
        durationTW.setVisibility(View.INVISIBLE);
        durationRadioGroup.setVisibility(View.INVISIBLE);

    }

    private void setVisibilityForDailyRepeat() {
        everyTW.setText(R.string.every);
        dayTW.setText(R.string.days);
        everyTW.setVisibility(View.VISIBLE);
        everyET.setVisibility(View.VISIBLE);
        dayTW.setVisibility(View.VISIBLE);
        setDaysVisibility(View.INVISIBLE);
    }

    private void setVisibilityForWeeklyRepeat() {
        everyTW.setText(R.string.every);
        dayTW.setText(R.string.weekly);
        everyTW.setVisibility(View.VISIBLE);
        everyET.setVisibility(View.VISIBLE);
        dayTW.setVisibility(View.VISIBLE);
        setDaysVisibility(View.VISIBLE);
    }

    private void setVisibilityForMonthlyRepeat() {
        everyTW.setText(R.string.every);
        dayTW.setText(R.string.monthss);
        everyTW.setVisibility(View.VISIBLE);
        everyET.setVisibility(View.VISIBLE);
        dayTW.setVisibility(View.VISIBLE);
        setDaysVisibility(View.INVISIBLE);
    }
    private void setDaysVisibility(int visibility) {
        monCB.setVisibility(visibility);
        tueCB.setVisibility(visibility);
        wenCB.setVisibility(visibility);
        thuCB.setVisibility(visibility);
        friCB.setVisibility(visibility);
        satCB.setVisibility(visibility);
        sunCB.setVisibility(visibility);
    }

    private void setVisibilityForYearlyRepeat() {
        everyTW.setText(R.string.every);
        dayTW.setText(R.string.years);
        everyTW.setVisibility(View.VISIBLE);
        everyET.setVisibility(View.VISIBLE);
        dayTW.setVisibility(View.VISIBLE);
        setDaysVisibility(View.INVISIBLE);
    }

    public void durationCheck(View view) {
        int radioID = durationRadioGroup.getCheckedRadioButtonId();
        durationRB = findViewById(radioID);

        if (durationRB != null) {
            if (durationRB.getText().toString().equals(getString(R.string.repetitions))) {
                repetitionET.setVisibility(View.VISIBLE);
                calendarButton.setVisibility(View.INVISIBLE);
            } else if (durationRB.getText().toString().equals(getString(R.string.until))) {
                calendarButton.setVisibility(View.VISIBLE);
                repetitionET.setVisibility(View.INVISIBLE);
            } else {
                calendarButton.setVisibility(View.INVISIBLE);
                repetitionET.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void visibilityController(String name) {
        if (name.equals("Week")) {
            monCB.setVisibility(View.VISIBLE);
            tueCB.setVisibility(View.VISIBLE);
            wenCB.setVisibility(View.VISIBLE);
            thuCB.setVisibility(View.VISIBLE);
            friCB.setVisibility(View.VISIBLE);
            satCB.setVisibility(View.VISIBLE);
            sunCB.setVisibility(View.VISIBLE);
            everyTW.setVisibility(View.VISIBLE);
            durationTW.setVisibility(View.VISIBLE);
        } else {
            monCB.setVisibility(View.INVISIBLE);
            tueCB.setVisibility(View.INVISIBLE);
            wenCB.setVisibility(View.INVISIBLE);
            thuCB.setVisibility(View.INVISIBLE);
            friCB.setVisibility(View.INVISIBLE);
            satCB.setVisibility(View.INVISIBLE);
            sunCB.setVisibility(View.INVISIBLE);
            everyTW.setVisibility(View.INVISIBLE);
            durationTW.setVisibility(View.INVISIBLE);
        }

        if (name.equals("Nev")) {
            durationRadioGroup.setVisibility(View.INVISIBLE);
            everyET.setVisibility(View.INVISIBLE);
            dayTW.setVisibility(View.INVISIBLE);
            everyTW.setVisibility(View.INVISIBLE);
            durationTW.setVisibility(View.INVISIBLE);
        } else {
            durationRadioGroup.setVisibility(View.VISIBLE);
            everyET.setVisibility(View.VISIBLE);
            dayTW.setVisibility(View.VISIBLE);
            everyTW.setVisibility(View.VISIBLE);
            durationTW.setVisibility(View.VISIBLE);
        }

        repetitionET.setVisibility(View.INVISIBLE);
        calendarButton.setVisibility(View.INVISIBLE);
    }
}