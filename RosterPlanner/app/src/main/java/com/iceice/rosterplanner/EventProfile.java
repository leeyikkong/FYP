package com.iceice.rosterplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by iceice on 3/5/2016.
 */
public class EventProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mClub;
    private TextView mTitle;
    private TextView mStartDateTime;
    private TextView mEndDateTime;
    private ImageView mBlank;
    private TextView mDescription;
    private TextView mLocation;
    private int EVENT_ID ;
    private final int EDIT = 2;

    String title;
    String description ;
    String location;
    String club;
    int all_day_status = 0;
    Date startDate;
    Time startTime;
    Date endDate;
    Time endTime;

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_profile);
        setUpToolbar();
        setSupportActionBar();
        setTitle("");

        declareElement();

        Intent intent = getIntent();
        EVENT_ID = intent.getExtras().getInt("ITEM ID", -1);
        setInformation();

    }

    public void declareElement(){
        mTitle = (TextView) findViewById(R.id.event_title);
        mClub = (TextView) findViewById(R.id.txt_club);
        mLocation = (TextView) findViewById(R.id.txt_location);
        mDescription = (TextView) findViewById(R.id.event_description);
        mStartDateTime = (TextView) findViewById(R.id.txt_start_date);
        mEndDateTime = (TextView) findViewById(R.id.txt_end_date);
        mBlank = (ImageView) findViewById(R.id.blank_end_date_image);
    }

    public void setInformation(){
        DbHandler db = new DbHandler(this);
        Event event = db.getEvent(EVENT_ID);

        club = event.get_club();
        title = event.get_title();
        description = event.get_description();
        startDate = event.get_startDate();

        Time startTime = event.get_startTime();
        String[] time = startTime.toString().split(":");
        String startHour = time[0];
        String startMin = time[1];

        String eventStartTime = startHour + ":" + startMin;
        endDate = event.get_endDate();

        Time endTime = event.get_endTime();
        String[] time1 = endTime.toString().split(":");
        String startHour1 = time1[0];
        String startMin1 = time1[1];

        String eventEndTime = startHour1 + ":" + startMin1;

        all_day_status = event.get_all_day_status();
        location = event.get_location();

        mTitle.setText(title);
        mClub.setText(club);
        mLocation.setText(location);
        mDescription.setText(event.get_description());
        mStartDateTime.setText("Start Date: " + startDate + " Time: " + eventStartTime);
        if (all_day_status == 0) {
            mEndDateTime.setText("End Date:   " + endDate + " Time: " + eventEndTime);
        } else {
            mEndDateTime.setVisibility(View.GONE);
            mBlank.setVisibility(View.GONE);
        }

    }

    public void onResume(){
        super.onResume();
        setInformation();
    }


    /******** menu ********/
    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setSupportActionBar() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventProfile.this.finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                confirmDeleteBox();
                return true;

            case R.id.action_edit:
                Intent intent = new Intent(this,EditEventActivity.class );
                intent.putExtra("eventID", EVENT_ID);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteBox(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete Event");
        alertDialogBuilder
                .setMessage("Confirm delete this event?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHandler db = new DbHandler(getBaseContext());
                        db.deleteEvent(EVENT_ID);
                        Toast.makeText(getApplicationContext(),
                                "Delete successful", Toast.LENGTH_LONG)
                                .show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
