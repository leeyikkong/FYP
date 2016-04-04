package com.iceice.rosterplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.List;

/**
 * Created by iceice on 3/30/2016.
 */
public class TaskProfile extends AppCompatActivity{

    List list = null;
    List list1 = null;

    Context context = this;
    private TextView mTaskName;
    private TextView mTaskDescription;
    ListView mListView;
    private TextView mNumberOfHelpersNeeded;
    private int TASK_ID;
    private Toolbar toolbar;
    private Button mAddHelperButton;
    private Button mEditHelperButton;
    private Button whatsappGroup;
    private Button whatsapp;
    private Button sms;

    private String club;
    private String eventName;
    private String eventDescription;
    private String eventStartDate;
    private String eventStartTime;
    private String eventEndDate;
    private String eventEndTime;
    private String location;
    private String taskName;
    private String taskDescription;

    private String GroupMessage;

    String numberOfHelpersNeeded;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        TASK_ID = intent.getExtras().getInt("Task ID");
        eventDetails(TASK_ID);

        setContentView(R.layout.task_profile);
        setUpToolbar();
        setSupportActionBar();
        declareElement();
        setUpInterface();
        showHelperList();
    }

    protected void onResume(){
        super.onResume();
        eventDetails(TASK_ID);
        setUpInterface();
        showHelperList();

    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setSupportActionBar() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_cross);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskProfile.this.finish();
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
                Intent intent = new Intent(this,EditTaskActivity.class );
                intent.putExtra("TASKID", TASK_ID);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void declareElement(){
        mTaskName = (TextView)findViewById(R.id.task_name);
        mTaskDescription = (TextView)findViewById(R.id.task_Description);
        mEditHelperButton = (Button)findViewById(R.id.edit_helper);
        whatsappGroup = (Button)findViewById(R.id.send_to_group);
    }

    private void setUpInterface(){
        DbHandler db = new DbHandler(this);
        Task task = db.getTask(TASK_ID);
        taskName = task.get_taskName();
        taskDescription = task.get_taskDescription();

        mTaskName.setText(taskName);
        mTaskDescription.setText(taskDescription);

        GroupMessage = groupMessage();

        mEditHelperButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EditContactListActivity.class);
                intent.putExtra("TASK_ID", TASK_ID);
                startActivity(intent);
            }
        });

        whatsappGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.putExtra(Intent.EXTRA_TEXT, GroupMessage);
                waIntent.setPackage("com.whatsapp");
                waIntent.setType("text/plain");
                startActivity(waIntent);
            }
        });
    }

    private void showHelperList(){
        mListView = (ListView) findViewById(R.id.helperList);
        final HelperRowViewAdapter adapter =
            new HelperRowViewAdapter(this.getApplicationContext(), helpersListWithTaskId(TASK_ID));
        mListView.setAdapter(adapter);
    }

    private List<Helpers> helpersListWithTaskId(int id) {
        DbHandler db = new DbHandler(this);
        list = db.getHelperFromJoinTable(id);
        return list;
    }

    private void confirmDeleteBox(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete Task");
        alertDialogBuilder
                .setMessage("Confirm delete this task?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHandler db = new DbHandler(getBaseContext());
                        db.deleteTask(TASK_ID);
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

    private void eventDetails(int taskId){
        DbHandler dbHandler = new DbHandler(this);
        Task task = dbHandler.getTask(taskId);

        int eventId = task.get_eventId();

        Event event = dbHandler.getEvent(eventId);
        club = event.get_club();
        eventName = event.get_title();
        eventDescription = event.get_description();
        if(event.get_description() == null){
            eventDescription = "None";
        }
        eventStartDate = event.get_startDate().toString();

        Time startTime = event.get_startTime();
        String[] time = startTime.toString().split(":");
        String startHour = time[0];
        String startMin = time[1];

        eventStartTime = startHour + ":" + startMin;
        eventEndDate = event.get_endDate().toString();

        Time endTime = event.get_endTime();
        String[] time1 = endTime.toString().split(":");
        String startHour1 = time1[0];
        String startMin1 = time1[1];

        eventEndTime = startHour1 + ":" + startMin1;
        location = event.get_location();

        taskName = task.get_taskName();
        taskDescription = task.get_taskDescription();
        if (task.get_taskDescription() == null){
            taskDescription = "None";
        }
    }

    private String groupMessage(){
        String eventDetails;

        eventDetails = "Hello Dear, the " + club + " club has organise an event (" +
                eventName + ")" + " on " + eventStartDate + " Time ( " + eventStartTime + " ) " +
                "until " + eventEndDate + " Time ( " + eventEndTime + " )." +
                "Here is the details for the event : " + taskDescription +
                ". I sincerely to invite you to become a helper of the event." +
                "The task that assign to you is " + taskName +
                " for this event. Here is the details about the task " + taskDescription +
                " for more information, please contact me personally. Thank You.";

        return eventDetails;
    }
}
