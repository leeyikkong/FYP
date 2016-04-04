package com.iceice.rosterplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

/**
 * Created by iceice on 3/25/2016.
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private Toolbar toolbar;
    private SearchAdapter adapter;
    private ListView listView;
    private SearchView searchView;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.search_layout);
        setUpToolbar();
        setUpNavDrawer();
        setTitle("Search");

        listView = (ListView) findViewById(R.id.search_list);
        searchView = (SearchView) findViewById(R.id.search_view);

        adapter = new SearchAdapter(getApplicationContext(), generateData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemID = adapter.getListItemId(position);
                viewEventProfile(itemID);
            }
        });

        searchView.setOnQueryTextListener(this);
    }

    public void onResume(){
        super.onResume();
    }
    private void viewEventProfile(int itemId){
        Intent intent = new Intent(this, EventProfile.class);
        intent.putExtra("ITEM ID", itemId);
        startActivity(intent);
    }

    private List<Event> generateData(){
        DbHandler db = new DbHandler(getBaseContext());
        List<Event> eventList = db.getAllEventAscendingOrder();
        return eventList;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    private void setUpNavDrawer() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchActivity.this.finish();
                }
            });
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    //setup the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
