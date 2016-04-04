package com.iceice.rosterplanner;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iceice on 3/25/2016.
 */
public class SearchAdapter extends ArrayAdapter<Event> implements Filterable{
    private Toolbar toolbar;
    private final Context context;
    private List<Event> eventList;
    private List<Event> eventFilterList;
    private ValueFilter valueFilter;

    public SearchAdapter(Context context, List<Event> eventList){
        super(context, R.layout.event_row_item);

        this.context = context;
        this.eventList = eventList;
        this.eventFilterList = new ArrayList<Event>(eventList);
    }

    public int getCount() {
        return eventList.size();
    }

    public Event getItem(int position) {
        return eventList.get(position);
    }

    public long getItemId(int position) {
        return eventList.get(position).get_eventId();
    }

    public int getListItemId(int position) {
        return eventList.get(position).get_eventId();
    }

    public View getView(int posotion, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.event_row_only_item, null);
            TextView eventName = (TextView) convertView.findViewById(R.id.event_only_title);

            eventName.setText(eventList.get(posotion).get_title());
        }
        return convertView;
    }

    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            String prefix = constraint.toString().toUpperCase();

            if(prefix == null || prefix.length() == 0){
                synchronized (this){
                    filterResults.values = eventList;
                    filterResults.count = eventList.size();
                }
            }
            else {
                ArrayList<Event> filterList = new ArrayList<Event>();
                ArrayList<Event> unfilterList = new ArrayList<Event>();
                synchronized (this){
                    unfilterList.addAll(eventList);
                }
                for(int i = 0; i < unfilterList.size(); i++){
                    if((unfilterList.get(i).get_title().toUpperCase())
                            .contains(constraint.toString().toUpperCase())){
                        filterList.add(eventList.get(i));
                    }

                    filterResults.count = filterList.size();
                    filterResults.values = filterList;
                }
            }
            return filterResults;
        }

        protected void publishResults(CharSequence constraint, FilterResults results){
            eventList = (ArrayList<Event>) results.values;
            notifyDataSetChanged();
            clear();
            int count = eventList.size();
            for (int i = 0; i < count; i++) {
                add(eventList.get(i));
                notifyDataSetChanged();
            }
        }
    }
}
