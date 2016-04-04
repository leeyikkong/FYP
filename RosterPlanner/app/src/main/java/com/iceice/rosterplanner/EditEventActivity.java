package com.iceice.rosterplanner;

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

public class EditEventActivity extends AppCompatActivity {

    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;
    private EditText mClub;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mDescription;

    private Calendar calendar = Calendar.getInstance();
    private int EVENT_ID;
    private  int ADD_OR_EDIT_STATUS;
    private int ALL_DAY_STATUS = 1;
    private int REMINDER_STATUS = 0;
    private int REPEAT_STATUS = 0;
    private static final int END_TIME_OR_DATE = 1;
    private static final int START_TIME_OR_DATE = 2;

    private Spinner reminderSpinner;
    private Spinner repeaterSpinner;

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

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMin;

    private int startyear;
    private int startmonth;
    private int startday;
    private int startHour;
    private int startMinute;

    Event event;

    private int sYear, sMonth, sDay;
    private int endYear, endMonth, endDay;
    private int eventStartHour, eventStartMinute;
    private int eventEndHour, eventEndMinute;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);
        DbHandler db = new DbHandler(this);

        setUpToolbar();
        setSupportActionBar();
        setUI();
        Intent intent = getIntent();
        EVENT_ID = intent.getExtras().getInt("eventID", -1);
        getDateString();

        event = db.getEvent(EVENT_ID);
        editEvent(event);

        onClickListener();

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

    protected void onResume(){
        super.onResume();
    }

    private void setUI(){
        mEndDateTimeSector = (LinearLayout) findViewById(R.id.end_date_time_sector);
        mReminderSector = (LinearLayout) findViewById(R.id.spinner_reminder_sector);
        mRepeatSector = (LinearLayout) findViewById(R.id.spinner_repeat_sector);
        mEndSection = (TextView) findViewById(R.id.end_section);

    //set up elements
        mClub = (EditText) findViewById(R.id.update_club);
        mTitle = (EditText) findViewById(R.id.update_title);
        mDescription = (EditText) findViewById(R.id.update_description);
        switchAllDay = (Switch) findViewById(R.id.update_switch_all_day);
        mStartDate = (TextView) findViewById(R.id.update_txt_start_date);
        mEndDate = (TextView) findViewById(R.id.update_txt_end_date);
        mStartTime = (TextView) findViewById(R.id.update_txt_start_time);
        mEndTime = (TextView) findViewById(R.id.update_txt_end_time);
        switchReminder = (Switch) findViewById(R.id.update_switch_reminder);
        reminderSpinner = (Spinner) findViewById(R.id.update_reminder_spinner);

    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.reminder_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        switchRepeat = (Switch) findViewById(R.id.update_switch_repeat);
        reminderSpinner.setAdapter(adapter);


        repeaterSpinner = (Spinner) findViewById(R.id.update_repeat_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.repeat_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeaterSpinner.setAdapter(adapter1);
        repeaterSpinner.setSelection(0, false);
        mLocation = (EditText) findViewById(R.id.update_txt_location);
    }

    private void editEvent(Event event){

        mClub.setText(event.get_club());
        mTitle.setText(event.get_title());
        mDescription.setText(event.get_description());

        Date startDate = event.get_startDate();

        String[] date = startDate.toString().split("-");
        sYear = Integer.parseInt(date[0]);
        sMonth = Integer.parseInt(date[1]) - 1;
        sDay = Integer.parseInt(date[2]);


        Date endDate = event.get_endDate();
        String[] enddate = endDate.toString().split("-");
        endYear = Integer.parseInt(enddate[0]);
        endMonth = Integer.parseInt(enddate[1]) - 1;
        endDay = Integer.parseInt(enddate[2]);

        Time startTime = event.get_startTime();
        String[] starttime = startTime.toString().split(":");
        String timex = String.valueOf(starttime[0]);
        String timey = String.valueOf(starttime[1]);
        eventStartHour = Integer.parseInt(starttime[0]);
        eventStartMinute = Integer.parseInt(starttime[1]);

        Time endTime = event.get_endTime();
        String[] endtime = endTime.toString().split(":");
        String time1 = String.valueOf(endtime[0]);
        String time2 = String.valueOf(endtime[1]);
        eventEndHour = Integer.parseInt(endtime[0]);
        eventEndMinute = Integer.parseInt(endtime[1]);

        mStartDate.setText(event.get_startDate().toString());
        mStartTime.setText(timex + ":" + timey);
        mEndDate.setText(event.get_endDate().toString());
        mEndTime.setText(time1 + ":" + time2);


        String setSwitch = event.get_endTime().toString();
        String[] time = setSwitch.split(":");
        int second = Integer.parseInt(time[2]);

        if(second == 59){
            switchAllDay.setChecked(true);
            mEndDateTimeSector.setVisibility(View.GONE);
            mEndSection.setVisibility(View.GONE);
            ALL_DAY_STATUS = 0;
        }else
        {
            switchAllDay.setChecked(false);
            mEndDateTimeSector.setVisibility(View.VISIBLE);
            mEndSection.setVisibility(View.VISIBLE);
            ALL_DAY_STATUS = 1;
        }

        String reminder = event.get_reminder();
        if(reminder != null) {
            switchReminder.setChecked(true);
            mReminderSector.setVisibility(View.VISIBLE);
            REMINDER_STATUS = 1;
            String[] reminderId = getResources().getStringArray(R.array.reminder_array);
            Log.i("reminderid", String.valueOf(reminderId));

            int reminderPosition = 0;
            String reminderStatus = event.get_reminder();
            Log.i("reminderStatus", reminderStatus);

            for (int i = 0; i < reminderId.length; i++) {
                if (reminderId[i].equals(reminderStatus)) {
                    reminderPosition = i;
                    Log.i("reminderPosition=>", String.valueOf(reminderPosition));
                    reminderSpinner.setSelection(reminderPosition, false);
                }
            }
        }
        else
        {
            switchReminder.setChecked(false);
            mReminderSector.setVisibility(View.GONE);
            reminderSpinner.setSelection(0, false);
        }

        String repeat = event.get_repeater();
        if(repeat != null) {
            switchRepeat.setChecked(true);
            mRepeatSector.setVisibility(View.VISIBLE);
            REPEAT_STATUS = 1;
            String[] repeatId = getResources().getStringArray(R.array.repeat_array);

            int repeatPosition = 0;
            String reminderStatus = event.get_reminder();

            for (int i = 0; i < repeatId.length; i++) {
                if (repeatId[i].equals(reminderStatus)) {
                    repeatPosition = i;

                    reminderSpinner.setSelection(repeatPosition, false);
                }
            }
        }
        else
        {
            switchRepeat.setChecked(false);
            mRepeatSector.setVisibility(View.GONE);
            repeaterSpinner.setSelection(0, false);
        }

        //repeat spinner
        String[] repeaterId = getResources().getStringArray(R.array.repeat_array);
        int repeaterPosition = 0;
        String repeaterStatus = event.get_repeater();
        for(int i = 0; i < repeaterId.length; i++){
            if(repeaterId[i].equals(repeaterStatus)){
                repeaterPosition = i;
            }
        }

        repeaterSpinner.setSelection(repeaterPosition, false);

        mLocation.setText(event.get_location());

    }

    private void onClickListener() {
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
    }


         /* -----------------date time picker method ------------------------ */
//        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarStartDate = Calendar.getInstance();

        private void showStartDatePicker(){
            DatePickerDialog startDatePicker = new DatePickerDialog(this,

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        calendarStartDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        long date = calendarStartDate.getTimeInMillis();
                        Date startDate = new Date(date);
                        Date eventStartDate = new Date(calendarStartDate.getTimeInMillis());
//                       Log.i("eventStartDate", String.valueOf(eventStartDate));
                        String[] format = eventStartDate.toString().split("-");
                        sYear = Integer.parseInt(format[0]);
                        sMonth = Integer.parseInt(format[1]) - 1;
                        sDay = Integer.parseInt(format[2]);
                        mStartDate.setText(startDate.toString());
                    }
                }, sYear, sMonth, sDay);

            startDatePicker.setTitle("Select Date");

            startDatePicker.updateDate(sYear, sMonth, sDay);

            startDatePicker.show();
        }

    Calendar calendarEndDate = Calendar.getInstance();
    private void showEndDatePicker(){

        DatePickerDialog endDatePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        calendarEndDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        long date = calendarEndDate.getTimeInMillis();

                        Date endDate = new Date(date);
                        Date eventEndDate = new Date(calendarEndDate.getTimeInMillis());
                        String[] format = eventEndDate.toString().split("-");
                        endYear = Integer.parseInt(format[0]);
                        endMonth = Integer.parseInt(format[1]) - 1;
                        endDay = Integer.parseInt(format[2]);

                        mEndDate.setText(endDate.toString());
                    }
                }, endYear, endMonth , endDay);
        endDatePicker.setTitle("Select Date");

        endDatePicker.updateDate(endYear, endMonth,endDay);
        endDatePicker.show();
    }

    Calendar calendarStartTime = Calendar.getInstance();
    private void showStartTimePicker(){

        TimePickerDialog startTimeDatePicker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendarStartTime.set(0,0,0,hourOfDay, minute, 0);
                        long time = calendarStartTime.getTimeInMillis();

                        Time startTime = new Time(time);
                        //Time eventStartTime = new Time(calendarStartDate.getTimeInMillis());

                        String[] timetime = startTime.toString().split(":");

                        eventStartHour = Integer.parseInt(timetime[0]);
                        eventStartMinute = Integer.parseInt(timetime[1]);

                        mStartTime.setText(timetime[0] + ":" + timetime[1]);

                    }
                },eventStartHour, eventStartMinute, true);
        startTimeDatePicker.setTitle("Select Time");

        startTimeDatePicker.updateTime(eventStartHour, eventStartMinute);
        startTimeDatePicker.show();
    }

    Calendar calendarEndTime = Calendar.getInstance();
    private void showEndTimePicker(){

        TimePickerDialog endTimeDatePicker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendarEndTime.set(0,0,0,hourOfDay, minute, 0);
                        long time = calendarEndTime.getTimeInMillis();

                        Time endTime = new Time(time);
                        String[] timetime = endTime.toString().split(":");

                        eventEndHour = Integer.parseInt(timetime[0]);
                        eventEndMinute = Integer.parseInt(timetime[1]);

                        mEndTime.setText(timetime[0] + ":" + timetime[1]);
                    }
                },eventEndHour, eventEndMinute, true);
        endTimeDatePicker.setTitle("Select Time");

        endTimeDatePicker.updateTime(eventEndHour, eventEndMinute);
        endTimeDatePicker.show();
    }

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
        getMenuInflater().inflate(R.menu.menu_update_event, menu);
        return true;
    }

    //setup menu item (save button)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_update:
                updateEvent();
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
                    EditEventActivity.this.finish();
                }
            });
        }
    }
    //save event
    private void updateEvent(){
        Event event = new Event();
        event.set_id(EVENT_ID);

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
            endDate = Date.valueOf(mEndDate.getText().toString());
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

        if(eventStartDate < currentDate){
            mStartDate.setError("Event start date already past");
            Toast.makeText(this.getApplicationContext(),
                    "Event start date must same or after current date", Toast.LENGTH_LONG).show();
            return;
        }

        if(eventEndtDate < eventStartDate){
            mEndDate.setError("Event end date is set earlier than start date");
            Toast.makeText(this.getApplicationContext(),
                    "Event end date must late than start date", Toast.LENGTH_LONG).show();
            return;
        }

        if(switchReminder.isChecked()){
            String reminder = reminderSpinner.getSelectedItem().toString();
            if(reminderSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(this.getApplicationContext(),
                        "Please Select the time of the reminder", Toast.LENGTH_LONG).show();
                return;
            }else
                event.set_reminder(reminder);
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

        if(TextUtils.isEmpty(location)){
            mLocation.setError("Please Enter the Event's Location");
            Toast.makeText(this.getApplicationContext(),
                    "Must have a Event's Location", Toast.LENGTH_LONG).show();
            return;
        }

        //notification
        if(switchReminder.isChecked()) {
            NotificationManager reminderNotification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.drawable.ic_notification_purple, "testing", System.currentTimeMillis());
            PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

            notification.setLatestEventInfo(getApplicationContext(),title,"event info", pending);
            reminderNotification.notify(0, notification);
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
        db.updateEvent(event);

        Toast.makeText(this.getApplicationContext(),
                "Update Successfully",
                Toast.LENGTH_LONG).show();
        finish();

    }
}
