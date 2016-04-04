package com.iceice.rosterplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class RosterFragment extends Fragment {

    List list = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_roster, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        final String currentDate = getCurrentDate();

        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplication(), generateData(currentDate));

        final ListView listView = (ListView) getActivity().findViewById(R.id.roster_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int eventId = adapter.getListItemId(position);
                Intent intent = new Intent(RosterFragment.this.getActivity(),
                        TaskListActivity.class);
                intent.putExtra("Event ID", eventId);
                Log.i("ITEM", String.valueOf(eventId));
                startActivity(intent);
            }
        });
    }


    public void onResume(){
        super.onResume();
        final String currentDate = getCurrentDate();
        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplicationContext(),
                        generateData(currentDate));
        final ListView listView = (ListView) getActivity().findViewById(R.id.roster_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getListItemId(position);
                Intent intent = new Intent(RosterFragment.this.getActivity(),
                        TaskListActivity.class);
                intent.putExtra("Event ID", itemId);

                startActivity(intent);
            }
        });
    }

    private List<Event> generateData(String date){
        DbHandler db1 = new DbHandler(getActivity().getApplicationContext());
        list = db1.getAllEventByDate(String.valueOf(Date.valueOf(date)));
        return list;
    }

    private String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH) +1;
        int dd = c.get(Calendar.DAY_OF_MONTH);

        String mmFormat = "";
        if(mm<10)
            mmFormat = "0"+mm;
        else
            mmFormat = ""+mm;

        String ddFormat = "";
        if(dd<10)
            ddFormat = "0"+dd;
        else
            ddFormat=""+dd;

        return yy + "-" + mmFormat + "-" + ddFormat  ;
    }
    /*set up menu bar*/
    //setup the menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_roster_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}