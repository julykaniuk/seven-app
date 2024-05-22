package com.example.sevenapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.BroadcastReceiver;
import android.content.Context;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventDetails extends AppCompatActivity {
    private TextView eventNameTV,  dateTV, reminderTV, repeatTV, noteTV;
    private Button shareButton, editButton;
    private SQLiteDatabase mDatabase;
    private String start, end, eventName, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        EventDBHelper dbHelper = new EventDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        eventNameTV = findViewById(R.id.eventNameTV);
        dateTV = findViewById(R.id.dateTV);
        reminderTV = findViewById(R.id.reminderTV);
        repeatTV = findViewById(R.id.repeatTV);
        noteTV = findViewById(R.id.noteTV);
        shareButton = findViewById(R.id.shareButton);
        editButton = findViewById(R.id.editButton);

        eventName = getIntent().getStringExtra("EVENT NAME");
        start = getIntent().getStringExtra("START");
        end = getIntent().getStringExtra("END");

        findRow();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editEventIntent = new Intent(EventDetails.this, NewEvent.class);
                editEventIntent.putExtra("EVENT NAME", eventName);
                editEventIntent.putExtra("START", start);
                editEventIntent.putExtra("END", end);
                editEventIntent.putExtra("EDIT", "true");
                startActivity(editEventIntent);
                Intent updateIntent = new Intent("com.example.sevenapp.ACTION_UPDATE_EVENT_WIDGET");
                v.getContext().sendBroadcast(updateIntent);
            }

        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent1 = new Intent(Intent.ACTION_SEND);
                shareIntent1.setType("text/plain");
                String text = "Event Name: "+ eventNameTV.getText().toString() +"\n" +
                        "Date: " + dateTV.getText().toString();

                shareIntent1.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(shareIntent1);
            }
        });


    }

    public void onResume() {
        super.onResume();
        findRow();
    }

    public void writeToTV(String note, String location, String seriType) {
        eventNameTV.setText(eventName);
        String text;
        if(start.split(" ")[0].equals(end.split(" ")[0]))
            text = start.split(" ")[0] + " • " + start.split(" ")[1] + " - " +
                    end.split(" ")[1];
        else
            text = start.split(" ")[0] + " • " + start.split(" ")[1] + " - " +
                    end.split(" ")[0] + " • " + end.split(" ")[1];
        dateTV.setText(text);

        if (note != null)
            noteTV.setText(note);
        else
            noteTV.setText(" ");

        if (seriType != null)
            repeatTV.setText(seriType);
    }

    public void findRow() {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_START + "='" + start + "' AND " +
                EventDB.Event.COLUMN_END + "='" + end + "' AND " + EventDB.Event.COLUMN_NAME + "='"
                + eventName + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        int IDIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_ID);
        int noteIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_NOTE);
        int seriIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_SERI);
        int locationIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_LOCATION);
        int seriTypeIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_SERI_TYPE);
        int linkIndex = cursor.getColumnIndex(EventDB.Event.COLUMN_LOCATION_LINK);

        int ID = -1, SERI = -1;
        String note = null, location = null, seriType = null;

        if (cursor.moveToFirst()) {
            ID = cursor.getInt(IDIndex);
            if (noteIndex != -1) {
                note = cursor.getString(noteIndex);
            }
            if (seriIndex != -1) {
                SERI = cursor.getInt(seriIndex);
            }
            if (locationIndex != -1) {
                location = cursor.getString(locationIndex);
            }
            if (SERI != -1 && seriTypeIndex != -1) {
                seriType = cursor.getString(seriTypeIndex);
            }
            if (linkIndex != -1) {
                link = cursor.getString(linkIndex);
            }
        }
        cursor.close();
        findReminders(ID);
        writeToTV(note, location, seriType);
    }


    public void findReminders(int ID) {
        String SQLQuery = "SELECT * FROM " + EventDB.Event.REMINDER_TABLE_NAME +
                " WHERE " + EventDB.Event.REMINDER_COLUMN_EID + "='" + ID + "';";
        Cursor cursor = mDatabase.rawQuery(SQLQuery, null);
        String dateText = "";
        int columnIndexDate = cursor.getColumnIndex(EventDB.Event.REMINDER_COLUMN_DATE);
        if (columnIndexDate != -1) {
            if (cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(columnIndexDate);
                    dateText = dateText + date + "\n";
                } while (cursor.moveToNext());
            }
        } else {
            dateText = "REMINDER_COLUMN_DATE не знайдено";
        }
        cursor.close();
        reminderTV.setText(dateText);
    }


}