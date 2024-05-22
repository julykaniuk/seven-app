package com.example.sevenapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.database.Cursor;
import com.example.sevenapp.EventDB.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
public class EventDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "calendar.db";
    public static final int DATABASE_VERSION = 1;

    public String dbPath;

    public EventDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + EventDB.Event.TABLE_NAME +
                " (" + EventDB.Event.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EventDB.Event.COLUMN_NAME + " TEXT NOT NULL, " +
                EventDB.Event.COLUMN_START + " TEXT NOT NULL, " +
                EventDB.Event.COLUMN_END + " TEXT NOT NULL, " +
                EventDB.Event.COLUMN_SERI + " INTEGER DEFAULT -1, " +
                EventDB.Event.COLUMN_SERI_TYPE + " TEXT, " +
                EventDB.Event.COLUMN_LOCATION + " TEXT, " +
                EventDB.Event.COLUMN_LOCATION_LINK + " TEXT, " +
                EventDB.Event.COLUMN_NOTE + " TEXT, " +
                EventDB.Event.COLUMN_IS_SPECIAL + " INTEGER DEFAULT 0" +
                ");";


        final String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " + Event.REMINDER_TABLE_NAME +
                "(" + Event.REMINDER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Event.REMINDER_COLUMN_EID + " INTEGER NOT NULL, " +
                Event.REMINDER_COLUMN_DATE + " TEXT NOT NULL " +
                ");";



        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REMINDER_TABLE);
        insertPredefinedEvents(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Event.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Event.REMINDER_TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
    private void insertPredefinedEvents(SQLiteDatabase db) {
        String[] predefinedEvents = {
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ('Новий Рік', '2024-01-01 00:00:00', '2024-01-01 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Різдво', '2024-12-25 00:00:00', '2024-12-25 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День Незалежності', '2024-08-24 00:00:00', '2024-08-24 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День захисників і захисниць України', '2024-10-01 00:00:00', '2024-10-01 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День Української Державності', '2024-07-15 00:00:00', '2024-07-15 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День української писемності та мови', '2024-10-27 00:00:00', '2024-10-27 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Міжнародний жіночий день', '2024-03-08 00:00:00', '2024-03-08 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День праці', '2024-05-01 00:00:00', '2024-05-01 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День пам’яті та перемоги над нацизмом', '2024-05-08 00:00:00', '2024-05-08 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День Конституції України', '2024-06-28 00:00:00', '2024-06-28 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День святого Миколая ', '2024-12-06 00:00:00', '2024-12-06 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Покров Пресвятої Богородиці', '2024-10-01 00:00:00', '2024-10-01 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Міжнародний день памяті жертв Холокоста', '2024-01-27 00:00:00', '2024-01-27 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Стрітення Господнє', '2024-02-02 00:00:00', '2024-02-02 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День Святого Валентина', '2024-02-14 00:00:00', '2024-02-14 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Міжнародний день рідної мови', '2024-02-21 00:00:00', '2024-02-21 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Благовіщення Пресвятої Богородиці', '2024-04-07 00:00:00', '2024-04-07 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День захисту дітей', '2024-06-01 00:00:00', '2024-06-01 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День Повітряних сил Збройниx Сил України', '2024-08-04 00:00:00', '2024-08-04 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День знань', '2024-09-01 00:00:00', '2024-09-01 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Міжнародний день миру', '2024-09-21 00:00:00', '2024-09-21 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Міжнародний день студентів', '2024-11-17 00:00:00', '2024-11-17 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День Гідності та Свободи', '2024-11-21 00:00:00', '2024-11-21 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Великдень', '2024-05-05 00:00:00', '2024-05-05 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Великдень', '2024-03-31 00:00:00', '2024-03-31 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Трійця', '2024-06-23 00:00:00', '2024-06-23 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Трійця', '2024-05-19 00:00:00', '2024-05-19 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Великдень', '2025-04-20 00:00:00', '2025-04-20 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Трійця', '2025-06-08 00:00:00', '2025-06-08 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Великдень', '2026-04-13 00:00:00', '2026-04-13 23:59:59', 1);",
                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'Трійця', '2026-05-31 00:00:00', '2026-05-31 23:59:59', 1);",

                "INSERT INTO " + EventDB.Event.TABLE_NAME + " (" +
                        EventDB.Event.COLUMN_NAME + ", " +
                        EventDB.Event.COLUMN_START + ", " +
                        EventDB.Event.COLUMN_END + ", " +
                        EventDB.Event.COLUMN_IS_SPECIAL +
                        ") VALUES ( 'День памяті героїв Крут', '2024-01-29 00:00:00', '2024-01-29 23:59:59', 1);"

        };

        for (String event : predefinedEvents) {
            db.execSQL(event);
        }
    }


    public Cursor findEventByName(String partialEventName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Event.TABLE_NAME +
                " WHERE " + Event.COLUMN_NAME + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + partialEventName + "%"});

        return cursor;
    }

    public Cursor getEventForToday() {
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = dateFormat.format(new Date());

        String query = "SELECT * FROM EVENTS WHERE START_DATE LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{today + "%"});

        return cursor;
    }
    public Cursor getStaticEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQLQuery = "SELECT * FROM " + EventDB.Event.TABLE_NAME +
                " WHERE " + EventDB.Event.COLUMN_IS_SPECIAL + " = 1" +
                " ORDER BY datetime(" + EventDB.Event.COLUMN_START + ") ASC;";
        return db.rawQuery(SQLQuery, null);
    }

}