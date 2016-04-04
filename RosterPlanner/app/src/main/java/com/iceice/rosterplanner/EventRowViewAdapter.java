package com.iceice.rosterplanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.util.List;

/**
 * Created by iceice on 3/5/2016.
 */
public class EventRowViewAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final List<Event> eventList;

    public EventRowViewAdapter(Context context, List<Event> eventList){
        super(context, R.layout.event_row_item,eventList);

        this.context = context;
        this.eventList = eventList;

    }

    public View getView (int position, View convertView , ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.event_row_item, parent, false);

        TextView eventTitle = (TextView) rowView.findViewById(R.id.event_title);
        TextView eventDay = (TextView) rowView.findViewById(R.id.label_date);
        eventDay.setTextColor(Color.parseColor("#111111"));//black color
        TextView eventMonth = (TextView) rowView.findViewById(R.id.label_month);
        eventMonth.setTextColor(Color.parseColor("#111111"));//black color

        Date startdate = eventList.get(position).get_startDate();
        String[] date = startdate.toString().split("-");
        int day = Integer.parseInt(date[2]);
        int month = Integer.parseInt(date[1]);

        eventTitle.setText(eventList.get(position).get_title());
        eventDay.setText("" + day);
        eventMonth.setText(defineMonth(month));

        return rowView;
    }

    private String defineMonth(int monthValue){
        String monthString = "";
        switch(monthValue) {
            case 1:
                monthString = "Jan";
                break;
            case 2 :
                monthString = "Feb";
                break;
            case 3 :
                monthString = "Mar";
                break;
            case 4 :
                monthString = "Apr";
                break;
            case 5 :
                monthString = "May";
                break;
            case 6 :
                monthString = "Jun";
                break;
            case 7 :
                monthString = "Jul";
                break;
            case 8 :
                monthString = "Aug";
                break;
            case 9:
                monthString = "Sep";
                break;
            case 10 :
                monthString = "Oct";
                break;
            case 11:
                monthString = "Nov";
                break;
            case 12:
                monthString = "Dec";
                break;
        }

        return monthString;
    }

    public int getListItemId(int position){
        return eventList.get(position).get_eventId();
    }
}
