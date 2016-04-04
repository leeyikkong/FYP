package com.iceice.rosterplanner;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    NavigationView mNavigationView;

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        setUpToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //setup the drawer
        setUpNavDrawer();
        mNavigationView = (NavigationView) findViewById(R.id.nvView);

        //setup the content in the nav drawer
        setupDrawerContent(mNavigationView);


        Menu menu= mNavigationView.getMenu();
        selectDrawerItem(menu.getItem(0));
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = ReminderFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = EventFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = RosterFragment.class;
                break;
            default:
                fragmentClass = EventFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }


    //setup menu
    public boolean onCreateOptionsMenu(Menu item) {
        getMenuInflater().inflate(R.menu.menu_main, item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //setup the toolbar
    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    //setup the toolbar home title can be clicked and open the drawer
    private void setUpNavDrawer() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_burger);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    //setup the even listener in the nav drawer
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

}
