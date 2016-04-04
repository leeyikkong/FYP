package com.iceice.rosterplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by iceice on 3/30/2016.
 */
public class EditTaskActivity extends AppCompatActivity{
    private int TASK_ID;
    private int EVENT_ID;
    private Toolbar toolbar;
    private EditText mTaskName;
    private EditText mTaskDescription;
    private EditText mNumberOfHelpers;
    private Button mSaveButton;

    Task task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task_activity);
        setUpToolbar();
        setSupportActionBar();
        setUpInterface();

        Intent intent = getIntent();
        TASK_ID = intent.getExtras().getInt("TASKID");

        DbHandler db = new DbHandler(this);
        task = db.getTask(TASK_ID);
        editTask(task);
    }

    public void onResume(){
        super.onResume();
        setUpInterface();
    }


    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    private void setSupportActionBar() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_cross);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditTaskActivity.this.finish();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpInterface(){
        mTaskName = (EditText)findViewById(R.id.taskName);
        mTaskDescription = (EditText)findViewById(R.id.task_Description);
        mNumberOfHelpers = (EditText)findViewById(R.id.numberOfHelers);
        mSaveButton = (Button)findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void editTask(Task task){
        mTaskName.setText(task.get_taskName());
        mTaskDescription.setText(task.get_taskDescription());
        mNumberOfHelpers.setText(String.valueOf(task.get_numberOfHelpers()));
    }

    private void saveTask(){
        Task task = new Task();
        task.set_id(TASK_ID);

        String taskName = mTaskName.getText().toString();
        String taskDescription = mTaskDescription.getText().toString();
        String numberOfHelpers = mNumberOfHelpers.getText().toString();

        if(TextUtils.isEmpty(taskName)){
            mTaskName.setError("Please Enter the Task Name");
            Toast.makeText(this.getApplicationContext(),
                    "Must have a Task Name", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(numberOfHelpers)){
            mNumberOfHelpers.setError("Please Enter the number of helpers for the task");
            Toast.makeText(this.getApplicationContext(),
                    "Must have number of helpers", Toast.LENGTH_LONG).show();
            return;
        }

        task.set_taskName(taskName);
        task.set_taskDescription(taskDescription);
        task.set_numberOfHelpers(Integer.parseInt(numberOfHelpers));
        task.set_eventId(EVENT_ID);

        DbHandler db = new DbHandler(getBaseContext());
        db.updateTask(task);
        Toast.makeText(this.getApplicationContext(),
                taskName + "Task is Updated",
                Toast.LENGTH_LONG).show();
        finish();
    }
}
