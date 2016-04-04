package com.iceice.rosterplanner;

import android.content.Context;
import android.content.DialogInterface;
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


public class ReminderFragment extends Fragment{

    private int STATUS = 0;
    Context context = this.getActivity();

    List list = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);
        return view;
    }

    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));

        final CountDownEventRowViewAdapter adapter =
                new CountDownEventRowViewAdapter(getActivity().getApplication(), generateData(currentDate));

        final ListView listView = (ListView) getActivity().findViewById(R.id.home_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getList(position);
                Intent intent = new Intent(ReminderFragment.this.getActivity(),
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
                showComingSoonEvent();
                break;
            case 2:
                showComingSoonEvent();
                break;
            case 3:
                showAllEvent();
                break;
            default:
                showComingSoonEvent();
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_countdown, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                triggerSearch();
                STATUS = 1;
                break;
            case R.id.action_show_coming_soon_event:
                showComingSoonEvent();
                STATUS = 2;
                break;
            case R.id.action_show_all_event:
                showAllEvent();
                STATUS = 2;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Event> generateData(Date date){
        DbHandler db1 = new DbHandler(getActivity().getApplicationContext());
        list = db1.getAllEventByDate(String.valueOf(date));
        return list;
    }

    private List<Event> generateData1(Date date){
        DbHandler db1 = new DbHandler(getActivity().getApplicationContext());
        list = db1.getAllEvent(String.valueOf(date));
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
        final CountDownEventRowViewAdapter adapter =
                new CountDownEventRowViewAdapter(getActivity().getApplication(), generateData(currentDate));

        final ListView listView = (ListView) getActivity().findViewById(R.id.home_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId =adapter.getList(position);
                Intent intent = new Intent(ReminderFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }

    private void showAllEvent(){
        final Date currentDate = Date.valueOf(String.valueOf(getCurrentDate()));
        final CountDownEventRowViewAdapter adapter =
                new CountDownEventRowViewAdapter(getActivity().getApplication(), generateData1(currentDate));

        final ListView listView = (ListView) getActivity().findViewById(R.id.home_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getList(position);
                Intent intent = new Intent(ReminderFragment.this.getActivity(),
                        EventProfile.class);
                intent.putExtra("ITEM ID", itemId);
                startActivity(intent);
            }
        });
    }

    private void deleteAllEvent(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Delete All Events");
        alertDialogBuilder
                .setMessage("Confirm Delete All Events?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHandler db = new DbHandler(context);
                        db.deleteAllEvent();
                        Toast.makeText(context,
                                "Delete successful", Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
