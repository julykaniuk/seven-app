package com.example.sevenapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventAdapter1 extends RecyclerView.Adapter<EventAdapter1.EventViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private Locale currentLocale;


    public EventAdapter1(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        currentLocale = context.getResources().getConfiguration().locale;

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_task, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String eventName = mCursor.getString(mCursor.getColumnIndexOrThrow(EventDB.Event.COLUMN_NAME));
        String description = mCursor.getString(mCursor.getColumnIndexOrThrow(EventDB.Event.COLUMN_NOTE));
        String startDate = mCursor.getString(mCursor.getColumnIndexOrThrow(EventDB.Event.COLUMN_START));
        String endDate = mCursor.getString(mCursor.getColumnIndexOrThrow(EventDB.Event.COLUMN_END));

        holder.title.setText(eventName);
        holder.description.setText(description);

        if (startDate == null || startDate.isEmpty()) {
            holder.time.setText("N/A");
        } else {
            String time = extractTime(startDate, currentLocale); // Виклик методу з поточною локалізацією
            holder.time.setText(time);
        }

        setDateDetails(holder, startDate, currentLocale); // Виклик методу з поточною локалізацією

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EventDetails.class);
                intent.putExtra("EVENT NAME", eventName);
                intent.putExtra("START", startDate);
                intent.putExtra("END", endDate);
                view.getContext().startActivity(intent);
            }
        });
    }

    private String extractTime(String fullDateTime, Locale locale) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", locale);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", locale);

        try {
            Date date = inputFormat.parse(fullDateTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private void setDateDetails(EventViewHolder holder, String startDate, Locale locale) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", locale); // Пн, Вт, Ср, ...
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", locale); // 1, 2, 3, ...
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", locale); // Січ, Лют, ...

        if (startDate == null || startDate.isEmpty()) {
            return;
        }

        try {
            Date date = inputFormat.parse(startDate);
            if (date != null) {
                holder.day.setText(dayFormat.format(date));
                holder.date.setText(dateFormat.format(date));
                holder.month.setText(monthFormat.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
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

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, time, day, date, month;

        public EventViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
        }
    }
}
