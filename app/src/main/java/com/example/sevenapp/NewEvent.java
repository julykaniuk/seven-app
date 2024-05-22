package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.sevenapp.SettingsActivity.MyPreferences;
import static com.example.sevenapp.SettingsActivity.ReminderAmount;
import static com.example.sevenapp.SettingsActivity.ReminderDate;

public class NewEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {    private SQLiteDatabase mDatabase;
    private EditText eventNameET, noteET;
    private TextView title;
    private ImageButton startDateButton, endDateButton, startTimeButton, endTimeButton;
    private Button saveButton, deleteButton, repeatButton, reminderButton;
    private String currentDateString, start, end, eventName, repeatType, untilDate, repeatCount,
            note, link, repeatFrequency, durationType, startDB, endDB, realStart,
            seriText, amountSP;
    private Calendar c;
    public int ID = -1, SERI = -1, dateSP;
    public boolean sTimeSet, eTimeSet, isStart, reminded =false, saved=false, repeated=false,
            deleteAll = false, located=false;
    public boolean[] daysOfWeek = new boolean[7];
    private Bundle args;
    private ContentValues cv;
    private MyDate startDatem, endDatem, diffDatem;
    private ArrayList<Integer> minReminder, hourReminder, dayReminder;
    private ArrayList<String> dateReminder;
    SharedPreferences sharedPreferences;


    public class MyDate {
        public int year, month, day, hour, minute;
        public String dateString;
        private Calendar c;

        public MyDate(int year, int month, int day, int hour, int minute) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
        }

        public void setDateString() {
            Date date = getDate();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.dateString = df.format(date);
        }

        public String getDateString() {
            setDateString();
            return dateString;
        }

        private void setCalendar() {
            c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
        }

        public Date getDate() {
            setCalendar();
            return c.getTime();
        }

        public Calendar getCalendar() {
            setCalendar();
            return c;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        EventDBHelper dbHelper = new EventDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        sharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        amountSP = sharedPreferences.getString(ReminderAmount, "10");
        dateSP = sharedPreferences.getInt(ReminderDate, 0);

        eventNameET = findViewById(R.id.addTaskTitle);
        noteET = findViewById(R.id.addTaskDescription);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        saveButton = findViewById(R.id.saveEvent);
        deleteButton = findViewById(R.id.closeEvent);
        reminderButton = findViewById(R.id.reminderButton);
        repeatButton = findViewById(R.id.repeatButton);


        if (getIntent().getStringExtra("EDIT") != null) {
            eventName = getIntent().getStringExtra("EVENT NAME");
            start = getIntent().getStringExtra("START");
            end = getIntent().getStringExtra("END");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date startDate = df.parse(start);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                startDatem = new MyDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Date endDate = df.parse(end);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                endDatem = new MyDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            eventNameET.setText(eventName);
            sTimeSet = true;
            eTimeSet = true;
            findRow();
            if (note != null) {
                noteET.setText(note);
            }
        }

        else {
            c = Calendar.getInstance();
            startDatem = new MyDate(c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE));
            endDatem = new MyDate(c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE));

            sTimeSet = false;
            eTimeSet = false;
        }

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = true;
                DialogFragment datePicker = new DatePickerFragment();
                args = new Bundle();
                args.putInt("DAY", startDatem.day);
                args.putInt("YEAR", startDatem.year);
                args.putInt("MONTH", startDatem.month);
                datePicker.setArguments(args);
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = true;
                DialogFragment timePicker = new TimePickerFragment();
                if (sTimeSet) {
                    args = new Bundle();
                    args.putInt("HOUR", startDatem.hour);
                    args.putInt("MINUTE", startDatem.minute);
                    timePicker.setArguments(args);
                }
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = false;
                DialogFragment datePicker = new DatePickerFragment();
                args = new Bundle();
                args.putInt("DAY", endDatem.day);
                args.putInt("YEAR", endDatem.year);
                args.putInt("MONTH", endDatem.month);
                datePicker.setArguments(args);
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = false;
                DialogFragment timePicker = new TimePickerFragment();
                if (eTimeSet) {
                    args = new Bundle();
                    args.putInt("HOUR", endDatem.hour);
                    args.putInt("MINUTE", endDatem.minute);
                    timePicker.setArguments(args);
                }
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeated = true;
                Intent repeatIntent = new Intent(NewEvent.this, Repeat.class);
                if (getIntent().getStringExtra("EDIT") != null) {
                    repeatIntent.putExtra("ID", ID);
                    repeatIntent.putExtra("SERI", SERI);
                }
                repeatIntent.putExtra("DAY", startDatem.day);
                repeatIntent.putExtra("YEAR", startDatem.year);
                repeatIntent.putExtra("MONTH", startDatem.month);
                startActivityForResult(repeatIntent, 1);
            }
        });

        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dateBoundValid()) {
                    Snackbar mySnackbar = Snackbar.make(v, getString(R.string.date_interval_invalid), Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                } else {
                    reminded = true;
                    Intent reminderIntent = new Intent(NewEvent.this, Reminder.class);
                    reminderIntent.putExtra("DAY", startDatem.day);
                    reminderIntent.putExtra("YEAR", startDatem.year);
                    reminderIntent.putExtra("MONTH", startDatem.month);
                    reminderIntent.putExtra("HOUR", startDatem.hour);
                    reminderIntent.putExtra("MINUTE", startDatem.minute);
                    reminderIntent.putExtra("ID", ID);
                    startActivityForResult(reminderIntent, 2);
                }
            }
        });



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                eventName = eventNameET.getText().toString();
                start = startDatem.getDateString();
                end = endDatem.getDateString();
                note = noteET.getText().toString();

                Snackbar mySnackbar;
                if (!dateBoundValid()) {
                    mySnackbar = Snackbar.make(view, getString(R.string.invalid_date_interval), Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                } else if (eventNameET.getText().toString().trim().length() == 0) {
                    mySnackbar = Snackbar.make(view, getString(R.string.event_name_empty), Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                }
                else if (SERI != -1 && getIntent().getStringExtra("EDIT") != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);
                    builder.setTitle(getString(R.string.update_event));
                    builder.setPositiveButton("Only this event", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertToCV(eventName, start, end, note);
                            cv.put(EventDB.Event.COLUMN_SERI, SERI);
                            updateRow(view, ID);
                            if (!reminded)
                                defaultReminderSettings();
                            addReminder(cv);
                            MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
                            manager.findDates();
                        }
                    });
                    if (!isFirst()) {
                        builder.setNegativeButton("This and future events", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insertToCV(eventName, start, end, note);
                                cv.put(EventDB.Event.COLUMN_SERI, SERI);
                                updateRow(view, ID);
                                if (!reminded)
                                    defaultReminderSettings();
                                addReminder(cv);
                                MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
                                manager.findDates();
                                futureEvents(view, "Update");
                            }
                        });
                    }
                    builder.setNeutralButton("All events in series", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allEvents(view, "Update");
                        }
                    });
                    builder.show();
                }
                else {
                    insertToCV(eventName, start, end, note);
                    if (repeatType != null && !repeatType.equals("Never"))
                        repeater(view);
                    else {
                        cv.put(EventDB.Event.COLUMN_SERI, -1);
                        if (getIntent().getStringExtra("EDIT") == null) {
                            insertToDB(view);
                            if (!reminded)
                                defaultReminderSettings();
                            addReminder(cv);
                            MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
                            manager.findDates();
                        }
                        else if (ID != -1) { // update
                            updateRow(view, ID);
                            if (!reminded)
                                defaultReminderSettings();
                            addReminder(cv);
                            MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
                            manager.findDates();
                        }
                    }
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (SERI != -1 && getIntent().getStringExtra("EDIT") != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);
                    builder.setTitle("Delete Event");
                    builder.setPositiveButton("Only this event", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRow(view, ID);
                            deleteReminder(ID);
                        }
                    });
                    if (!isFirst()) {
                        builder.setNegativeButton("This and future events", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRow(view, ID);
                                deleteReminder(ID);
                                futureEvents(view, "Delete");
                            }
                        });
                    }
                    builder.setNeutralButton("All events in series", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allEvents(view, "Delete");
                        }
                    });
                    builder.show();
                }
                else if (ID != -1) {
                    deleteRow(view, ID);
                    deleteReminder(ID);
                }
                else {
                    eventNameET.getText().clear();
                    noteET.getText().clear();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Call super method

        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("Repeat Type")) {
                seriText = data.getStringExtra("Seri Type");
                repeatType = data.getStringExtra("Repeat Type");
                if (!getString(R.string.never).equals(repeatType)) {
                    repeatFrequency = data.getStringExtra("Repeat Frequency");
                    durationType = data.getStringExtra("Duration Type");
                    if (getString(R.string.repetitions).equals(durationType))
                        repeatCount = data.getStringExtra("Repeat Count");
                    else if (getString(R.string.until).equals(durationType))
                        untilDate = data.getStringExtra("Until Date");
                    if (getString(R.string.weekly).equals(repeatType)) {
                        daysOfWeek = data.getBooleanArrayExtra("Days of Week");
                    }
                }
                if (data.hasExtra("Deleted")) {
                    SERI = -1;
                }
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 2) {
            deleteAll = data.getBooleanExtra("deleteAll", false);
            if (deleteAll) {
                mDatabase.delete(EventDB.Event.REMINDER_TABLE_NAME,
                        EventDB.Event.REMINDER_COLUMN_EID + "=" + ID,
                        null);
            }
            else {
                dateReminder = data.getStringArrayListExtra("dates");
                hourReminder = data.getIntegerArrayListExtra("hours");
                minReminder = data.getIntegerArrayListExtra("minutes");
                dayReminder = data.getIntegerArrayListExtra("days");
            }

            if (ID == -1)
                ID = getMaxID() + 1;
        }
        else if  (resultCode == RESULT_OK && requestCode == 3) {

        }
    }


    public void defaultReminderSettings() {
        minReminder = new ArrayList<Integer>();
        hourReminder = new ArrayList<Integer>();
        dayReminder = new ArrayList<Integer>();
        dateReminder = new ArrayList<String>();
        int amount = Integer.parseInt(amountSP);
        ID = getMaxID();

        if (dateSP == 0) { // minute
            minReminder.add(amount);
        }
        else if (dateSP == 1) { // hour
            hourReminder.add(amount);
        }
        else if (dateSP == 2) { // day
            dayReminder.add(amount);
        }
    }

    public void addReminder(ContentValues cv) {

        String eventName = cv.getAsString(EventDB.Event.COLUMN_NAME);
        String start = cv.getAsString(EventDB.Event.COLUMN_START);
        String end = cv.getAsString(EventDB.Event.COLUMN_END);
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_START + "='" + start + "' AND " +
                EventDB.Event.COLUMN_END + "='" + end + "' AND " + EventDB.Event.COLUMN_NAME + "='"
                + eventName + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
            if (idColumnIndex != -1) {
                do {
                    id = cursor.getInt(idColumnIndex);
                } while (cursor.moveToNext());
            } else {
                 }
        }

        if (id == ID) {
            realStart = start;
        }
        reminderListTraverse(id, start);
    }

    public void reminderListTraverse(int id, String start) {

        for (int minute: minReminder) {
            Calendar cal = strToCal(start);
            cal.add(Calendar.MINUTE, -minute);
            insertToReminderTable(cal, id);
        }
        for (int hour: hourReminder) {
            Calendar cal = strToCal(start);
            cal.add(Calendar.HOUR_OF_DAY, -hour);
            insertToReminderTable(cal, id);
        }
        for (int day: dayReminder) {
            Calendar cal = strToCal(start);
            cal.add(Calendar.DAY_OF_MONTH, -day);
            insertToReminderTable(cal, id);
        }
        for (String date: dateReminder) {
            Calendar cal = strToCal(date);
            if (start.equals(realStart))
                insertToReminderTable(cal, id);
            else {
                Calendar real = strToCal(realStart);
                MyDate diff = new MyDate(-(real.get(Calendar.YEAR) - cal.get(Calendar.YEAR)),
                        -(real.get(Calendar.MONTH) - cal.get(Calendar.MONTH)),
                        -(real.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH)),
                        -(real.get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY)),
                        -(real.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE)));
                Calendar startCal = strToCal(start);
                startCal = addBuffer(diff, startCal);
                insertToReminderTable(startCal, id);
            }
        }
    }

    public void insertToReminderTable(Calendar c, int id) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime());

        ContentValues cv2 = new ContentValues();
        cv2.put(EventDB.Event.REMINDER_COLUMN_EID, id);
        cv2.put(EventDB.Event.REMINDER_COLUMN_DATE, dateStr);
        mDatabase.insert(EventDB.Event.REMINDER_TABLE_NAME, null, cv2);
    }

    public Calendar strToCal(String date) {
        Calendar startCal = Calendar.getInstance();
        try {
            Date startD = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
            startCal.setTime(startD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startCal;
    }


    private void deleteReminder(int id) {
        mDatabase.delete(EventDB.Event.REMINDER_TABLE_NAME,
                EventDB.Event.REMINDER_COLUMN_EID + "=" + id,
                null);
    }

    public void repeater(View view) {
        SERI = findMaxSeri() + 1;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        diffDatem = new MyDate(endDatem.year - startDatem.year, endDatem.month - startDatem.month,
                endDatem.day - startDatem.day, endDatem.hour - startDatem.hour,
                endDatem.minute - startDatem.minute);
        Date until = startDatem.getDate();
        Date today = new Date();
        if (durationType.equals(getString(R.string.until))) {
            try {
                until = df.parse(untilDate);
                today = startDatem.getDate();
                repeatCount = "0";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(startDatem.getDate());
            c.add(Calendar.DAY_OF_MONTH, 1);
            today = c.getTime();
        }

        if (durationType.equals(getString(R.string.forever)))
            repeatCount = "100";
        int i = 0;

        while (i < Integer.parseInt(repeatCount) || today.before(until)) {
            int every = Integer.parseInt(repeatFrequency);
            if (repeatType.equals(getString(R.string.daily))) {
                MyDate bufferDate = new MyDate(0, 0, i * every, 0, 0);
                today = repeatEventCreator(bufferDate, until, view);
            } else if (repeatType.equals(getString(R.string.weekly))) {
                int dayOfWeek = startDatem.getCalendar().get(Calendar.DAY_OF_WEEK);
                if (i == 0) {
                    for (int k = dayOfWeek; k < 8; k++) {
                        if (daysOfWeek[k - 1] && !(dayOfWeek == 1 && k == 1)) {
                            MyDate bufferDate = new MyDate(0, 0, k - dayOfWeek, 0, 0);
                            today = repeatEventCreator(bufferDate, until, view);
                        }
                        if (k == 7 && daysOfWeek[0]) {
                            int day = 7 - dayOfWeek + 1;
                            if (dayOfWeek == 1) day = 0;
                            MyDate bufferDate = new MyDate(0, 0, day, 0, 0);
                            today = repeatEventCreator(bufferDate, until, view);
                        }
                    }
                } else {
                    for (int k = 1; k < 7; k++) {
                        int sunday = 7 - dayOfWeek + 1;
                        if (daysOfWeek[k] && !((i == Integer.parseInt(repeatCount) - 1) && (dayOfWeek == 1))) {
                            MyDate bufferDate = new MyDate(0, 0, (sunday + k) + 7 * (i - 1), 0, 0);
                            today = repeatEventCreator(bufferDate, until, view);
                        }
                        if (k == 6 && daysOfWeek[0]) {
                            MyDate bufferDate;
                            if (dayOfWeek == 1)
                                bufferDate = new MyDate(0, 0, (sunday) + 7 * (i - 1), 0, 0);
                            else
                                bufferDate = new MyDate(0, 0, (sunday) + 7 * i, 0, 0);
                            today = repeatEventCreator(bufferDate, until, view);
                        }
                    }
                }
            } else if (repeatType.equals(getString(R.string.monthly))) {
                MyDate bufferDate = new MyDate(0, i * every, 0, 0, 0);
                today = repeatEventCreator(bufferDate, until, view);
            } else if (repeatType.equals(getString(R.string.yearly))) {
                MyDate bufferDate = new MyDate(i * every, 0, 0, 0, 0);
                today = repeatEventCreator(bufferDate, until, view);
            }
            if (!durationType.equals(getString(R.string.until))) today = until;
            i++;
        }

        MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
        manager.findDates();
    }

    public Date repeatEventCreator(MyDate buffer, Date until, View view) {
        Calendar startT = startDatem.getCalendar();
        Calendar endT = endDatem.getCalendar();
        Calendar start = addBuffer(buffer, startT);
        Calendar end = addBuffer(buffer, endT);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String startStr = df.format(start.getTime());
        String endStr = df.format(end.getTime());

        if ((getString(R.string.until).equals(durationType) && start.getTime().before(until) &&
                end.getTime().before(until)) || !getString(R.string.until).equals(durationType)) {

            cv = new ContentValues();
            cv.put(EventDB.Event.COLUMN_NAME, eventName);
            cv.put(EventDB.Event.COLUMN_START, startStr);
            cv.put(EventDB.Event.COLUMN_END, endStr);
            cv.put(EventDB.Event.COLUMN_NOTE, noteET.getText().toString());
            cv.put(EventDB.Event.COLUMN_SERI, SERI);
            cv.put(EventDB.Event.COLUMN_SERI_TYPE, seriText);
            insertToDB(view);
            if (!reminded) defaultReminderSettings();
            addReminder(cv);
        }
        return end.getTime();
    }


    public Calendar addBuffer (MyDate buffer, Calendar c) {
        c.add(Calendar.DAY_OF_MONTH, buffer.day);
        c.add(Calendar.MONTH, buffer.month);
        c.add(Calendar.YEAR, buffer.year);
        c.add(Calendar.HOUR_OF_DAY, buffer.hour);
        c.add(Calendar.MINUTE, buffer.minute);
        return c;
    }


    public boolean isFirst() {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_SERI + "='" + SERI + "' ORDER BY datetime(" +
                EventDB.Event.COLUMN_START + ") ASC;";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        int id = 0;
        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
            if (idColumnIndex != -1) {
                id = cursor.getInt(idColumnIndex);
            } else {
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return id == ID;
    }


    public void allEvents(View view, String type) {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_SERI + "='" + SERI + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
            if (idColumnIndex != -1) {
                do {
                    int id = cursor.getInt(idColumnIndex);
                    if (getString(R.string.delete).equals(durationType)) {
                        deleteRow(view, id);
                        deleteReminder(id);
                    }
                    else if (getString(R.string.until).equals(durationType)) {
                        String date = findStartDate(id);
                        String newStartDate = date.split(" ")[0] + " " + start.split(" ")[1];
                        String newEndDate = date.split(" ")[0] + " " + end.split(" ")[1];
                        insertToCV(eventName, newStartDate, newEndDate, note);
                        cv.put(EventDB.Event.COLUMN_SERI, SERI);
                        updateRow(view, id);
                        if (reminded && deleteAll) {
                            mDatabase.delete(EventDB.Event.REMINDER_TABLE_NAME,
                                    EventDB.Event.REMINDER_COLUMN_EID + "=" + ID,
                                    null);
                        }
                        if (!reminded)
                            defaultReminderSettings();
                        addReminder(cv);
                    }
                } while (cursor.moveToNext());
            } else {
            }
        }

        MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
        manager.findDates();

    }

    public void futureEvents(View view, String type) {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_SERI + "='" + SERI + "' AND " +
                EventDB.Event.COLUMN_START + " > '" + startDB + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
            if (idColumnIndex != -1) {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(idColumnIndex);
                        if (getString(R.string.delete).equals(durationType)) {
                            deleteRow(view, id);
                            deleteReminder(id);
                        }else if (getString(R.string.until).equals(durationType)) {
                            String date = findStartDate(id);
                            String newStartDate = date.split(" ")[0] + " " + start.split(" ")[1];
                            String newEndDate = date.split(" ")[0] + " " + end.split(" ")[1];
                            insertToCV(eventName, newStartDate, newEndDate, note);
                            cv.put(EventDB.Event.COLUMN_SERI, SERI);
                            updateRow(view, id);
                            if (reminded && deleteAll) {
                                mDatabase.delete(EventDB.Event.REMINDER_TABLE_NAME,
                                        EventDB.Event.REMINDER_COLUMN_EID + "=" + ID,
                                        null);
                            }
                            if (!reminded)
                                defaultReminderSettings();
                            addReminder(cv);
                        }
                    } while (cursor.moveToNext());
                }
            } else { }
        }
        MyAlarmManager manager = new MyAlarmManager(ID, getApplicationContext());
        manager.findDates();
    }

    public String findStartDate(int id) {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_ID + "='" + id + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        String start = null;
        if (cursor.moveToFirst()) {
            int startColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_START);
            if (startColumnIndex != -1) {
                if (cursor.moveToFirst()) {
                    do {
                        start = cursor.getString(startColumnIndex);
                    } while (cursor.moveToNext());
                }
            } else {}

        }
        return start;
    }

    public void findRow() {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_START + "='" + start + "' AND " +
                EventDB.Event.COLUMN_END + "='" + end + "' AND " + EventDB.Event.COLUMN_NAME + "='"
                + eventName + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
            int noteColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_NOTE);
            int seriColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_SERI);
            int startDBColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_START);
            int endDBColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_END);

            if (idColumnIndex != -1 && noteColumnIndex != -1 && seriColumnIndex != -1 &&
                    startDBColumnIndex != -1 && endDBColumnIndex != -1) {
                do {
                    ID = cursor.getInt(idColumnIndex);
                    note = cursor.getString(noteColumnIndex);
                    SERI = cursor.getInt(seriColumnIndex);
                    startDB = cursor.getString(startDBColumnIndex);
                    endDB = cursor.getString(endDBColumnIndex);
                } while (cursor.moveToNext());
            } else {}
        }
    }


    public int findMaxSeri() {
        String SQLQuery = EventDB.Event.COLUMN_SERI +  "=(SELECT MAX(" + EventDB.Event.COLUMN_SERI +
                ") FROM " + EventDB.Event.TABLE_NAME + ")";
        Cursor cursor = mDatabase.query(EventDB.Event.TABLE_NAME, null, SQLQuery,
                null, null, null, null);
        int seri = -1;
        if (cursor.moveToFirst()) {
            int seriColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_SERI);
            if (seriColumnIndex != -1) {
                do {
                    seri = cursor.getInt(seriColumnIndex);
                } while (cursor.moveToNext());
            } else {}
        }
        return seri;
    }

    public int getMaxID() {
        String SQLQuery = EventDB.Event.COLUMN_ID +  "=(SELECT MAX(" + EventDB.Event.COLUMN_ID +
                ") FROM " + EventDB.Event.TABLE_NAME + ")";
        Cursor cursor = mDatabase.query(EventDB.Event.TABLE_NAME, null, SQLQuery,
                null, null, null, null);
        int id = -1; // Initialize id with a default value
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
            if (idColumnIndex != -1) { // Check if idColumnIndex is valid
                do {
                    id = cursor.getInt(idColumnIndex);
                } while (cursor.moveToNext());
            } else { }
        }
        return id;
    }

    public void deleteRow(View view, int id) {
        mDatabase.delete(EventDB.Event.TABLE_NAME, EventDB.Event.COLUMN_ID + "=" + id,
                null);
        Snackbar mySnackbar = Snackbar.make(view, getString(R.string.event_deleted), Snackbar.LENGTH_SHORT);


        mySnackbar.show();
    }

    public void insertToCV(String eventName, String start, String end, String note) {
        cv = new ContentValues();
        cv.put(EventDB.Event.COLUMN_NAME, eventName);
        cv.put(EventDB.Event.COLUMN_START, start);
        cv.put(EventDB.Event.COLUMN_END, end);
        cv.put(EventDB.Event.COLUMN_NOTE, note);

    }

    public void updateRow(View view, int id) {
        mDatabase.update(EventDB.Event.TABLE_NAME, cv, EventDB.Event.COLUMN_ID + "=" + id,
                null);
        Snackbar mySnackbar = Snackbar.make(view, getString(R.string.event_updated), Snackbar.LENGTH_SHORT);

        mySnackbar.show();
        saved = true;
        eventNameET.getText().clear();
    }

    public void insertToDB(View view) {
        mDatabase.insert(EventDB.Event.TABLE_NAME, null, cv);
        Snackbar mySnackbar = Snackbar.make(view, getString(R.string.event_created), Snackbar.LENGTH_SHORT);

        mySnackbar.show();

        eventNameET.getText().clear();
        saved = true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        if (isStart) {
            startDatem.day = dayOfMonth;
            startDatem.month = month;
            startDatem.year = year;
        } else {
            endDatem.day = dayOfMonth;
            endDatem.month = month;
            endDatem.year = year;
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (isStart) {
            eTimeSet = true;
            startDatem.hour = hourOfDay;
            startDatem.minute = minute;
        } else {
            sTimeSet = true;
            endDatem.hour = hourOfDay;
            endDatem.minute = minute;
        }
    }

    public boolean dateBoundValid() {
        if (startDatem.year > endDatem.year)
            return false;
        else if (startDatem.year < endDatem.year)
            return true;
        else {
            if (startDatem.month > endDatem.month)
                return false;
            else if (startDatem.month < endDatem.month)
                return true;
            else {
                if (startDatem.day > endDatem.day)
                    return false;
                else if (startDatem.day < endDatem.day)
                    return true;
                else {
                    if (startDatem.hour > endDatem.hour)
                        return false;
                    else if (startDatem.hour < endDatem.hour)
                        return true;
                    else {
                        if (startDatem.minute > endDatem.minute)
                            return false;
                        else if (startDatem.minute == endDatem.minute)
                            return false;
                        else if (startDatem.minute < endDatem.minute)
                            return true;
                    }
                }
            }
        }
        return false;
    }

}