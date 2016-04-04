package com.iceice.rosterplanner;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

/**
 * Created by iceice on 3/5/2016.
 */
public class CountDownEventRowViewAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final List<Event> eventList;

    private int yy;
    private int mm;
    private int dd;


    public CountDownEventRowViewAdapter(Context context, List<Event> eventList){
        super(context, R.layout.event_row_item,eventList);

        this.context = context;
        this.eventList = eventList;

    }

    public View getView (int position, View convertView , ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.remind_event_row_item, parent, false);

        TextView eventTitle = (TextView) rowView.findViewById(R.id.event_title);
        TextView eventDay = (TextView) rowView.findViewById(R.id.label_date);
        eventDay.setTextColor(Color.parseColor("#111111"));//black color
        TextView days = (TextView) rowView.findViewById(R.id.label_days);
        days.setTextColor(Color.parseColor("#111111"));//black color
        TextView remain = (TextView) rowView.findViewById(R.id.label_remain);
        remain.setTextColor(Color.parseColor("#111111"));//black color

        Date startdate = eventList.get(position).get_startDate();
        String[] date = startdate.toString().split("-");
        int day = Integer.parseInt(date[2]);
        int month = Integer.parseInt(date[1]) - 1;
        int year = Integer.parseInt(date[0]);

        Time startTime = eventList.get(position).get_startTime();
        String[] time = startTime.toString().split(":");
        int hour = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);

        Calendar start = Calendar.getInstance();
        start.set(year, month, day, 0, 0, 0);

        long startDate = start.getTimeInMillis();

        Calendar current = Calendar.getInstance();
        yy = current.get(Calendar.YEAR);
        mm = current.get(Calendar.MONTH);
        dd = current.get(Calendar.DAY_OF_MONTH);

        current.set(yy, mm, dd, 0, 0, 0);
        long currentDate = current.getTimeInMillis();

        long different = startDate - currentDate;
        long daysRemain = (different / (24 * 60 * 60 * 1000));
        Log.i("days", String.valueOf(daysRemain));

        eventTitle.setText(eventList.get(position).get_title());
        eventDay.setText(String.valueOf(daysRemain));

        return rowView;
    }

    public int getList(int position){
        return eventList.get(position).get_eventId();
    }
}
