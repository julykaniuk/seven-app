package com.example.sevenapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private TextView eventNameTV, eventTimeTV;
    private RelativeLayout parentLayout;
    private SQLiteDatabase mDatabase;
    private int ID;
    private String type;
    private Locale currentLocale;

    public EventAdapter(Context context, Cursor cursor, String type) {
        mContext = context;
        mCursor = cursor;
        EventDBHelper dbHelper = new EventDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        this.type = type;
        currentLocale = context.getResources().getConfiguration().locale;

    }


    public class EventViewHolder extends RecyclerView.ViewHolder {

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTV = itemView.findViewById(R.id.eventName);
            eventTimeTV = itemView.findViewById(R.id.time);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.calendar_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final int nameColumnIndex = mCursor.getColumnIndex(EventDB.Event.COLUMN_NAME);
        final int startDateColumnIndex = mCursor.getColumnIndex(EventDB.Event.COLUMN_START);
        final int endDateColumnIndex = mCursor.getColumnIndex(EventDB.Event.COLUMN_END);
        final int idColumnIndex = mCursor.getColumnIndex(EventDB.Event.COLUMN_ID);

        if (nameColumnIndex != -1 && startDateColumnIndex != -1 && endDateColumnIndex != -1 && idColumnIndex != -1) {
            final String name = mCursor.getString(nameColumnIndex);
            final String startDate = mCursor.getString(startDateColumnIndex);
            final String endDate = mCursor.getString(endDateColumnIndex);
            ID = mCursor.getInt(idColumnIndex);


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date end = df.parse(endDate);
                Calendar c = Calendar.getInstance();
                Date currentDate = c.getTime();
                if (end != null && end.getTime() - currentDate.getTime() < 0) {
                    eventNameTV.setTextColor(Color.parseColor("#9B9B9B"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String date = "hey";
            eventNameTV.setText(name);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd", currentLocale);
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDate);
                date = dateFormat.format(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            eventTimeTV.setText(date);

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editEventIntent = new Intent(view.getContext(), EventDetails.class);
                    editEventIntent.putExtra("EVENT NAME", name);
                    editEventIntent.putExtra("START", startDate);
                    editEventIntent.putExtra("END", endDate);
                    view.getContext().startActivity(editEventIntent);
                }
            });
        } else {
        }

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
