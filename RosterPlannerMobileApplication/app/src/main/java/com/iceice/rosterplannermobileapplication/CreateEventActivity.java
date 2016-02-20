package com.iceice.rosterplannermobileapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;


public class CreateEventActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Spinner spinner = (Spinner) findViewById(R.id.reminder_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    //Date Picker

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this,hour,minute,true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String min;
            String time;
            if (minute < 10) {
                min = "0" + minute;
            } else {
                min = String.valueOf(minute);
            }

            if (hourOfDay > 11){
                hourOfDay -= 12;
                time = hourOfDay + ":" + min + " PM";
            }
            else{

                time = hourOfDay + ":" + min + " AM";
            }

            ((EditText)getActivity().findViewById(R.id.time_from)).setText(time);
        }
    }


    public static class TimePickerFragment2 extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this,hour,minute,true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String min;
            String time;
            if (minute < 10) {
                min = "0" + minute;
            } else {
                min = String.valueOf(minute);
            }

            if (hourOfDay > 11){
                hourOfDay -= 12;
                time = hourOfDay + ":" + min + " PM";
            }
            else{

                time = hourOfDay + ":" + min + " AM";
            }

            ((EditText)getActivity().findViewById(R.id.time_to)).setText(time);
        }
    }

    //from time
    public void showTimePickerDialog(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"timePicker");
    }

    //to time
    public void showTimePickerDialog2(View v){
        DialogFragment newFragment = new TimePickerFragment2();
        newFragment.show(getFragmentManager(),"timePicker2");
    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showDatePickerDialog2(View v){
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
