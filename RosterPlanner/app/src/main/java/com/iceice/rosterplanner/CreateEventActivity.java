package com.iceice.rosterplanner;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;
    private EditText mClub;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mDescription;

    private Spinner reminderSpinner;
    private Spinner repeaterSpinner;

    private static final int CALENDAR_STATUS = 1;
    private int EVENT_ID;
    private int ALL_DAY_STATUS=1;
    private int REMINDER_STATUS = 0;
    private int REPEAT_STATUS = 0;
    private static final int END_TIME_OR_DATE = 1;
    private static final int START_TIME_OR_DATE = 2;

    private LinearLayout mEndDateTimeSector;
    private LinearLayout mReminderSector;
    private LinearLayout mRepeatSector;
    private TextView mEndSection;
    private Switch switchAllDay;
    private Switch switchReminder;
    private Switch switchRepeat;

    private String currentDateString;
    private String currentTimeString;
    private int yy;
    private int mm;
    private int dd;
    private int hour;
    private int minute;

    private int startyear;
    private int startmonth;
    private int startday;
    private int startHour;
    private int startMinute;

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMin;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Set a Toolbar to replace the ActionBar.
        setUpToolbar();
        setSupportActionBar();
        setInformation();
        setCurrentDate();


        reminderSpinner = (Spinner) findViewById(R.id.reminder_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        reminderSpinner.setAdapter(adapter);


        repeaterSpinner = (Spinner) findViewById(R.id.repeat_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.repeat_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repeaterSpinner.setAdapter(adapter1);

    }

        private void setInformation(){
        mEndDateTimeSector = (LinearLayout) findViewById(R.id.end_date_time_sector);
        mReminderSector = (LinearLayout) findViewById(R.id.spinner_reminder_sector);
        mRepeatSector = (LinearLayout) findViewById(R.id.spinner_repeat_sector);
        mEndSection = (TextView) findViewById(R.id.end_section);

        //set up elements
        mClub = (EditText) findViewById(R.id.club);
        mTitle = (EditText) findViewById(R.id.edit_title);
        mDescription = (EditText) findViewById(R.id.edit_description);
        switchAllDay = (Switch) findViewById(R.id.switch_all_day);
        mStartDate = (TextView) findViewById(R.id.txt_start_date);
        mEndDate = (TextView) findViewById(R.id.txt_end_date);
        mStartTime = (TextView) findViewById(R.id.txt_start_time);
        mEndTime = (TextView) findViewById(R.id.txt_end_time);
        mLocation = (EditText) findViewById(R.id.txt_location);
        switchReminder = (Switch) findViewById(R.id.switch_reminder);
        switchRepeat = (Switch) findViewById(R.id.switch_repeat);

        }

        private void setCurrentDate(){
        //set the current date

        mStartDate.setText(getDateString());
        mEndDate.setText(getDateString());
        mStartTime.setText(getTimeString());
        mEndTime.setText(getTimeString());


        /**** -------------- Button listener -------------------*/
        mStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showStartDatePicker();
                }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showEndDatePicker();
            }
        });

        mStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showStartTimePicker();
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showEndTimePicker();
            }
        });

        //set the switch to off
        switchAllDay.setChecked(false);

        //attach a listener to check for changes in state
        switchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mEndDateTimeSector.setVisibility(View.GONE);
                    mEndSection.setVisibility(View.GONE);
                    ALL_DAY_STATUS = 0;
                } else {
                    mEndDateTimeSector.setVisibility(View.VISIBLE);
                    mEndSection.setVisibility(View.VISIBLE);
                    ALL_DAY_STATUS = 1;

                }
            }
        });


        switchReminder.setChecked(false);

        switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mReminderSector.setVisibility(View.VISIBLE);
                    REMINDER_STATUS = 1;

                } else {
                    mReminderSector.setVisibility(View.GONE);
                    REMINDER_STATUS = 0;

                }
            }
        });


        switchRepeat.setChecked(false);

        switchRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mRepeatSector.setVisibility(View.VISIBLE);
                    REPEAT_STATUS = 1;
                } else {
                    mRepeatSector.setVisibility(View.GONE);
                    REPEAT_STATUS = 0;

                }
            }
        });
        }

         /* -----------------date time picker method ------------------------ */

        private String getDateString(){
        final Calendar c = Calendar.getInstance();
        yy = c.get(Calendar.YEAR);
        mm = c.get(Calendar.MONTH) +1;
        dd = c.get(Calendar.DAY_OF_MONTH);

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

        currentYear = yy;
        currentMonth = mm - 1;
        currentDay = dd;

        currentDateString = yy + "-" + mmFormat + "-" + ddFormat  ;
        return currentDateString;
    }

    private String getTimeString(){
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Log.i("hour", String.valueOf(hour));

        String hour24 = "";
        if(hour>0 && hour< 10)
            hour24 = "0" + hour;
        else if(hour == 0)
            hour24 = "0" + hour;
        else
            hour24 = "" + hour;


        String minute24 = "";
        if(minute<10)
            minute24 = "0" + minute;
        else
            minute24 = ""+minute;

        currentHour = hour;
        currentMin = minute;

        currentTimeString = hour24 + ":" + minute24;
        return currentTimeString;
    }

    //show the date picker
    Calendar calendarStartDate = Calendar.getInstance();
    private void showStartDatePicker(){

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendarStartDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        long date = calendarStartDate.getTimeInMillis();
                        Date startDate = new Date(date);
                        mStartDate.setText(startDate.toString());
                    }
                }, yy, mm -1 , dd);
        dpd.setTitle("Select Date");

        Date startDate = new Date(calendarStartDate.getTimeInMillis());
        String[] format = startDate.toString().split("-");
        dpd.updateDate(Integer.parseInt(format[0]), Integer.parseInt(format[1]) - 1, Integer.parseInt(format[2]));
        dpd.show();
    }
    Calendar calendarEndDate = Calendar.getInstance();
    private void showEndDatePicker(){

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        calendarEndDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        long date = calendarEndDate.getTimeInMillis();
                        Date startDate = new Date(date);

                        mEndDate.setText(startDate.toString());
                    }
                }, yy, mm -1 , dd);
        dpd.setTitle("Select Date");

        Date startDate = new Date(calendarEndDate.getTimeInMillis());
        String[] format = startDate.toString().split("-");
        dpd.updateDate(Integer.parseInt(format[0]), Integer.parseInt(format[1]) - 1, Integer.parseInt(format[2]));
        dpd.show();

    }

    Calendar calendarStartTime = Calendar.getInstance();
    private void showStartTimePicker(){
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendarStartTime.set(0,0,0,hourOfDay, minute, 0);
                        long date = calendarStartTime.getTimeInMillis();
                        Time startTime = new Time(date);
                        String[] Time = startTime.toString().split(":");
                        mStartTime.setText(Time[0] + ":" + Time[1]);
                    }
                },hour, minute, true);
        tpd.setTitle("Select Time");
        Time startDate = new Time(calendarStartTime.getTimeInMillis());
        String[] format = startDate.toString().split(":");
        tpd.updateTime(Integer.parseInt(format[0]), Integer.parseInt(format[1]));
        tpd.show();
    }

    Calendar calendarEndTime = Calendar.getInstance();
    private void showEndTimePicker(){
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendarEndTime.set(0,0,0,hourOfDay, minute, 0);
                        long endTime = calendarEndTime.getTimeInMillis();
                        Time time = new Time(endTime);
                        String[] Time = time.toString().split(":");
                        mEndTime.setText(Time[0] + ":" + Time[1]);
                    }
                },hour, minute, true);
        tpd.setTitle("Select Time");
        Time startDate = new Time(calendarEndTime.getTimeInMillis());
        String[] format = startDate.toString().split(":");
        tpd.updateTime(Integer.parseInt(format[0]), Integer.parseInt(format[1]));
        tpd.show();
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
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    //setup menu item (save button)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveEvent();
                //CreateEventActivity.this.finish();
                return true;
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
                    CreateEventActivity.this.finish();
                }

            });
        }
    }


    //save event
    private void saveEvent(){
        Event event = new Event();

        String club = mClub.getText().toString();
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

        Date startDate = Date.valueOf(mStartDate.getText().toString());

        Time startTime = Time.valueOf(mStartTime.getText().toString() + ":00");


        //20160304 int format of date

        Date endDate;

        Time endTime;
        boolean allDayStatus;
        boolean reminderStatus;
        boolean repeatStatus;
        String location = mLocation.getText().toString();

        if(ALL_DAY_STATUS == 0){
            endDate = startDate;
            endTime = Time.valueOf("23:59:59");

            allDayStatus = true;
        }else{
            endDate = Date.valueOf(mEndDate.getText().toString() );
            endTime = Time.valueOf(mEndTime.getText().toString() + ":00");

            allDayStatus = false;
        }
        String[] date0 = currentDateString.split("-");
        int year0 = Integer.parseInt(date0[0]);
        int month0 = Integer.parseInt(date0[1]);
        int day0 = Integer.parseInt(date0[2]);
        int currentDate = year0 * 10000 + month0 * 100 + day0;

        String[] date1 = startDate.toString().split("-");
        int year1 = Integer.parseInt(date1[0]);
        int month1 = Integer.parseInt(date1[1]);
        int day1 = Integer.parseInt(date1[2]);

        int eventStartDate = year1 * 10000 + month1 * 100 + day1;

        String[] date2 = endDate.toString().split("-");
        int year2 = Integer.parseInt(date2[0]);
        int month2 = Integer.parseInt(date2[1]);
        int day2 = Integer.parseInt(date2[2]);

        int eventEndtDate = year2 * 10000 + month2 * 100 + day2;

        if(TextUtils.isEmpty(club)){
            mClub.setError("Please Enter the Club Name");
            Toast.makeText(this.getApplicationContext(),
                    "Must have a Club Name", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(title)){
            mTitle.setError("Please Enter the Event Name");
            Toast.makeText(this.getApplicationContext(),
                    "Must have a Event Name", Toast.LENGTH_LONG).show();
            return;
        }

        if(switchReminder.isChecked()){
            String reminder = reminderSpinner.getSelectedItem().toString();
            if(reminderSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(this.getApplicationContext(),
                        "Please Select the time of the reminder", Toast.LENGTH_LONG).show();
                return;
            }else{
                event.set_reminder(reminder);
            }
        }

        if(switchRepeat.isChecked()){
            String repeater = repeaterSpinner.getSelectedItem().toString();

            if(repeaterSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(this.getApplicationContext(),
                        "Please Select the time of repeat", Toast.LENGTH_LONG).show();
                return;
            }
            event.set_repeater(repeater);
        }

        if(eventEndtDate < eventStartDate){
            mEndDate.setError("Event end date is set earlier than start date");
            Toast.makeText(this.getApplicationContext(),
                    "Event end date must late than start date", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(location)){
            mLocation.setError("Please Enter the Event's Location");
            Toast.makeText(this.getApplicationContext(),
                    "Must have a Event's Location", Toast.LENGTH_LONG).show();
            return;
        }

        event.set_club(club);
        event.set_title(title);
        event.set_description(description);
        event.set_all_day_status(ALL_DAY_STATUS);
        event.set_start_date(startDate);
        event.set_startTime(startTime);
        event.set_endDate(endDate);
        event.set_endTime(endTime);
        event.set_location(location);

        DbHandler db = new DbHandler(getBaseContext());
        db.addEvent(event);
        Toast.makeText(this.getApplicationContext(),
                title + " event is create successfully!",
                Toast.LENGTH_LONG).show();
        int status = reminderSpinner.getSelectedItemPosition();
        setReminder(status);
        finish();

    }

    private void setReminder(int status){

            switch (status){
                case 1: reminder12Hour();
                    break;
                case 2: reminder1Day();
                    break;
                case 3: reminder1Week();
                    break;
                default:String error = "invalid input";
            }
        }

    private void reminder12Hour(){

        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMin, 0);
        long currentDayMillist = calendar.getTimeInMillis();

        calendar.set(startyear, startmonth, startday,
                startHour, startMinute, 0);
        long eventStartMillis = calendar.getTimeInMillis();
        long differentDay = eventStartMillis - currentDayMillist ;
        long halfDay = 43200000;
        long reminderDay = differentDay - halfDay;
        long setReminderDay = currentDayMillist + reminderDay;

        notificationFunction(setReminderDay);

    }

    private void reminder1Day(){

        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMin, 0);
        long currentDayMillist = calendar.getTimeInMillis();
        Log.i("currentDayMillist", String.valueOf(currentDayMillist));

        calendar.set(startyear, startmonth, startday,
                startHour, startMinute, 0);
        long eventStartMillis = calendar.getTimeInMillis();
        Log.i("eventStartMillis", String.valueOf(eventStartMillis));


        long differentDay = eventStartMillis - currentDayMillist ;
        long oneDay = 86400000;
        long reminderDay = differentDay - oneDay;
        long setReminderDay = currentDayMillist + reminderDay;
        Log.i("differentDay", String.valueOf(differentDay));

        notificationFunction(setReminderDay);
    }

    private void reminder1Week(){

        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMin, 0);
        long currentDayMillist = calendar.getTimeInMillis();
        Log.i("currentDayMillist", String.valueOf(currentDayMillist));

        calendar.set(startyear, startmonth, startday,
                startHour, startMinute, 0);
        long eventStartMillis = calendar.getTimeInMillis();
        Log.i("eventStartMillis", String.valueOf(eventStartMillis));


        long differentDay = eventStartMillis - currentDayMillist ;
        long oneWeek = 604800000;
        long reminderDay = differentDay - oneWeek;
        long setReminderDay = currentDayMillist + reminderDay;
        Log.i("differentDay", String.valueOf(differentDay));

        notificationFunction(setReminderDay);
    }

    private void notificationFunction(long time){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        NotificationManager reminderNotification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_notification_purple, "testing", System.currentTimeMillis());
        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

        notification.setLatestEventInfo(getApplicationContext(), "event name", "event info", pending);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pending);
        Log.i("alarm", String.valueOf(time));

        reminderNotification.notify(0, notification);
    }
}
