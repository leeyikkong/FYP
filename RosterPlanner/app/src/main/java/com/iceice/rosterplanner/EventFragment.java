package com.iceice.rosterplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;


public class EventFragment extends Fragment {
    private int STATUS = 0;

    List list = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));

        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplication(), generateData(currentDate));

        final ListView listView = (ListView) getActivity().findViewById(R.id.event_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getListItemId(position);
                Intent intent = new Intent(EventFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }


    public void onResume(){
        super.onResume();
        switch (STATUS){
            case 1:
                showAllEvent();
                break;
            case 2:
                showComingSoonEvent();
                break;
            case 3:
                showEventWithinThisOneWeek();
                break;
            case 4:
                showEventWithinThisMonth();
                break;
            default:
                showComingSoonEvent();
                break;
        }
    }

    private List<Event> generateData(Date date){
        DbHandler db1 = new DbHandler(getActivity().getApplicationContext());
        list = db1.getAllEventByDate(String.valueOf(date));
        return list;
    }

    private List<Event> generateData1(Date date){
        DbHandler db2 = new DbHandler(getActivity().getApplicationContext());
        list = db2.getAllEvent(String.valueOf(date));
        return list;
    }

    private List<Event> generateData2(Date date){
        DbHandler db3 = new DbHandler(getActivity().getApplicationContext());
        list = db3.getEventWithinOneWeek(String.valueOf(date));
        return list;
    }

    private List<Event> generateData3(Date date){
        DbHandler db4 = new DbHandler(getActivity().getApplicationContext());
        list = db4.getEventWithinThisMonth(String.valueOf(date));
        return list;
    }

    private Date getCurrentDate(){
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

        return Date.valueOf(yy + "-" + mmFormat + "-" + ddFormat);
    }
    /*set up menu bar*/
    //setup the menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                triggerSearch();
                break;
            case R.id.action_add:
                Intent intent = new Intent(this.getActivity(),CreateEventActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_all_event:
                showAllEvent();
                STATUS = 1;
                break;

            case R.id.action_event_coming_soon:
                showComingSoonEvent();
                STATUS = 2;
                break;

            case R.id.action_event_within_one_week:
                showEventWithinThisOneWeek();
                STATUS = 3;
                break;

            case R.id.action_event_within_one_month:
                showEventWithinThisMonth();
                STATUS = 4;
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void triggerSearch(){
        DbHandler db = new DbHandler(getActivity().getApplicationContext());
        List<Event> tempList = db.getAllEventAscendingOrder();
        if(!tempList.isEmpty()){
            Intent searchIntent = new Intent(this.getActivity(), SearchActivity.class);
            this.startActivity(searchIntent);
        }else{
            Toast.makeText(getActivity().getApplicationContext(),
                    "Do not have this event", Toast.LENGTH_LONG).show();
        }
    }

    private void showComingSoonEvent(){
        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));

        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplicationContext(),
                        generateData(currentDate));
        final ListView listView = (ListView) getActivity().findViewById(R.id.event_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getListItemId(position);
                Intent intent = new Intent(EventFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }

    private void showAllEvent(){
        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));

        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplicationContext(),
                        generateData1(currentDate));
        final ListView listView = (ListView) getActivity().findViewById(R.id.event_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getListItemId(position);
                Intent intent = new Intent(EventFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }

    private void showEventWithinThisOneWeek(){
        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));

        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplicationContext(),
                        generateData2(currentDate));
        final ListView listView = (ListView) getActivity().findViewById(R.id.event_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getListItemId(position);
                Intent intent = new Intent(EventFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }

    private void showEventWithinThisMonth(){
        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));

        final EventRowViewAdapter adapter =
                new EventRowViewAdapter(getActivity().getApplicationContext(),
                        generateData3(currentDate));
        final ListView listView = (ListView) getActivity().findViewById(R.id.event_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getListItemId(position);
                Intent intent = new Intent(EventFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }
}


