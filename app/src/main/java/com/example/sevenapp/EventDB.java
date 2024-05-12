package com.example.sevenapp;

import android.provider.BaseColumns;

public class EventDB {

    public EventDB() {}

    public static final class Event implements BaseColumns {
        public static final String TABLE_NAME = "EVENTS";
        public static final String COLUMN_IS_UKRAINIAN_HOLIDAY = "isUkrainianHoliday";

        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_START = "START_DATE";
        public static final String COLUMN_END = "END_DATE";
        public static final String COLUMN_SERI = "SERI_NO";
        public static final String COLUMN_SERI_TYPE = "SERI_TYPE";
        public static final String COLUMN_LOCATION = "LOCATION";
        public static final String COLUMN_LOCATION_LINK = "LOCATION_LINK";
        public static final String COLUMN_NOTE = "NOTE";
        public static final String REMINDER_TABLE_NAME = "REMINDERS";
        public static final String REMINDER_COLUMN_ID = "ID";
        public static final String REMINDER_COLUMN_EID = "EVENT_ID";
        public static final String REMINDER_COLUMN_DATE = "DATE";

    }
}