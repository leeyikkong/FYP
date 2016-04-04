package com.iceice.rosterplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iceice on 3/10/2016.
 */
public class TaskListActivity extends AppCompatActivity{

    private int STATUS;
    List list = null;
    ListView mListView;
    TextView content;
    Context context = this;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    DbHandler db;
    private int EVENT_ID;
    private Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_task_list_activity);
        setUpToolbar();
        setSupportActionBar();

        db = new DbHandler(this);
        db.getWritableDatabase();
        Intent intent = getIntent();
        EVENT_ID = intent.getExtras().getInt("Event ID");
        showTaskListAscByName();

    }

    protected void onResume(){
        super.onResume();
        switch (STATUS){
            case 1:
                showTaskListAscByName();
                break;
            case 2:
                showTaskListDescByName();
                break;
            case 3:
                showTaskListAscByNumOfHelpers();
                break;
            case 4:
                showTaskListDescByNumOfHelpers();
                break;
            default:
                showTaskListAscByName();
                break;
        }

    }

    private void showTaskListAscByName(){

        mListView = (ListView) findViewById(R.id.task_list);
        final AllTaskRowViewAdapter adapter =
                new AllTaskRowViewAdapter(this.getApplication(), generateDataAscendingByName(EVENT_ID));
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = adapter.getList(position);
                Intent intent = new Intent(context, TaskProfile.class);
                intent.putExtra("Task ID", itemId);

                startActivity(intent);
            }
        });
    }

    private void showTaskListDescByName(){

        mListView = (ListView) findViewById(R.id.task_list);
        final AllTaskRowViewAdapter adapter =
                new AllTaskRowViewAdapter(this.getApplication(), generateDataDescendingByName(EVENT_ID));

        mListView.setAdapter(adapter);
    }

    private void showTaskListAscByNumOfHelpers(){

        mListView = (ListView) findViewById(R.id.task_list);
        final AllTaskRowViewAdapter adapter =
                new AllTaskRowViewAdapter(this.getApplication(), generateDataAscendingByNumOfHelpers(EVENT_ID));

        mListView.setAdapter(adapter);
    }

    private void showTaskListDescByNumOfHelpers(){
        mListView = (ListView) findViewById(R.id.task_list);
        final AllTaskRowViewAdapter adapter =
                new AllTaskRowViewAdapter(this.getApplication(), generateDataDescendingByNumOfHelpers(EVENT_ID));

        mListView.setAdapter(adapter);
    }

    private List<Task> generateDataAscendingByName(int eventId){
        DbHandler db = new DbHandler(getApplicationContext());
        list = db.getAllTaskByEventIdAscByName(eventId);
        return list;
    }

    private List<Task> generateDataDescendingByName(int eventId){
        DbHandler db = new DbHandler(getApplicationContext());
        list = db.getAllTaskByEventIdDescByName(eventId);
        return list;
    }

    private List<Task> generateDataAscendingByNumOfHelpers(int eventId){
        DbHandler db = new DbHandler(getApplicationContext());
        list = db.getAllTaskByEventIdAscByNumOfHelper(eventId);
        return list;
    }

    private List<Task> generateDataDescendingByNumOfHelpers(int eventId){
        DbHandler db = new DbHandler(getApplicationContext());
        list = db.getAllTaskByEventIdDescByNumOfHelper(eventId);
        return list;
    }

    //---------------------------Menu----------------------------
    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    //setup the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(this,AddTaskActivity.class );
                intent.putExtra("eventID", EVENT_ID);
                startActivity(intent);
                return true;

            case R.id.action_sort_by_name_asc:
                showTaskListAscByName();
                STATUS = 1;
                break;

            case R.id.action_sort_by_name_desc:
                showTaskListDescByName();
                STATUS = 2;
                break;

            case R.id.action_sort_by_num_of_helper_asc:
                showTaskListAscByNumOfHelpers();
                STATUS = 3;
                break;

            case R.id.action_sort_by_num_of_helper_desc:
                showTaskListDescByNumOfHelpers();
                STATUS = 4;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //setup the toolbar home title can be clicked and open the drawer
    private void setSupportActionBar() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_cross);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskListActivity.this.finish();
                }
            });
        }
    }

}
