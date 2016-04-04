package com.iceice.rosterplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RosterPlanner";

    //FOR EVENT'S INFO TABLE
    private static final String EVENT_TABLE = "eventTable";
    private static final String EVENT_ID = "id";
    private static final String EVENT_CLUB = "eventClub";
    private static final String EVENT_TITLE = "eventName";
    private static final String EVENT_LOCATION = "eventLocation";
    private static final String EVENT_START_DATE = "startDate";
    private static final String EVENT_START_TIME = "startTime";
    private static final String EVENT_END_DATE = "endDate";
    private static final String EVENT_END_TIME = "endTime";
    private static final String EVENT_REMINDER = "eventReminder";
    private static final String EVENT_REPEATER = "eventRepeater";
    private static final String EVENT_DESCRIPTION = "eventDescription";

    //FOR EVENT'S TASK TABLE
    private static final String TASK_TABLE = "eventTaskTable";
    private static final String TASK_ID = "id";
    private static final String TASK_NAME = "taskName";
    private static final String TASK_DESCRIPTION = "taskDescription";
    private static final String NUMBER_OF_HELPERS = "numberOfHelpers";
    private static final String TASK_HELPER_ID = "taskHelperId";
    private static final String EVENT_TASK_ID = "eventId";

    //FOR EVENT'S SESSION TABLE
    private static final String SESSION_TABLE = "sessionTable";
    private static final String SESSION_ID = "id";
    private static final String SESSION_CONTACT_NUMBER = "sessionContactNumber";
    private static final String SESSION_TASK_ID = "sessionTaskId";

    //FOR HELPER'S TABLE
    private static final String HELPER_TABLE = "eventHelperTable";
    private static final String HELPER_ID = "helperId";
    private static final String HELPER_NAME = "helperName";
    private static final String HELPER_CONTACT_NUMBER = "helperContactNumber" ;


    //CREATE EVENT TABLE
    private static String CREATE_EVENT_TABLE =
            "CREATE TABLE " + EVENT_TABLE + " ( " +
                    EVENT_ID + " INTEGER PRIMARY KEY, " +
                    EVENT_CLUB + " TEXT, " +
                    EVENT_TITLE + " TEXT, " +
                    EVENT_DESCRIPTION + " TEXT, " +
                    EVENT_START_DATE + " DATE, " +
                    EVENT_START_TIME + " TIME, " +
                    EVENT_END_DATE + " DATE, " +
                    EVENT_END_TIME + " TIME, " +
                    EVENT_REMINDER + " TEXT, " +
                    EVENT_REPEATER + " TEXT, " +
                    EVENT_LOCATION + " TEXT " + " );";


    //CREATE TASK TABLE
    private static String CREATE_TASK_TABLE =
            "CREATE TABLE " + TASK_TABLE + "( " +
                    TASK_ID + " INTEGER PRIMARY KEY, " +
                    TASK_NAME + " TEXT, " +
                    TASK_DESCRIPTION + " TEXT, " +
                    NUMBER_OF_HELPERS + " INTEGER, " +
                    EVENT_TASK_ID + " INTEGER, " +
                    "FOREIGN KEY ( " + EVENT_TASK_ID + " ) REFERENCES " +
                    EVENT_TABLE + " ( " + EVENT_ID + " )" + ");";

    //CREATE SESSION TABLE
    private static String CREATE_SESSION_TABLE =
            "CREATE TABLE " + SESSION_TABLE + "( " +
                    SESSION_ID + " INTEGER PRIMARY KEY, " +
                    SESSION_CONTACT_NUMBER + " TEXT, " +
                    SESSION_TASK_ID + " TEXT, " +
                    "FOREIGN KEY ( " + SESSION_CONTACT_NUMBER + " ) REFERENCES " +
                    HELPER_TABLE + " ( " + HELPER_CONTACT_NUMBER + " )," +
                    "FOREIGN KEY ( " + SESSION_TASK_ID + " ) REFERENCES " +
                    TASK_TABLE + " ( " + TASK_ID + " ));";

    //CREATE HELPERS TABLE
    private static String CREATE_HELPER_TABLE =
            "CREATE TABLE " + HELPER_TABLE + " ( " +
                    HELPER_ID + " INTEGER PRIMARY KEY, " +
                    HELPER_NAME + " TEXT, " +
                    HELPER_CONTACT_NUMBER + " TEXT " + ");";


    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
        db.execSQL(CREATE_HELPER_TABLE);
        db.execSQL(CREATE_SESSION_TABLE);

    }

    //upgrade database table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HELPER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);

        onCreate(db);
    }

    /*
    EVENT TABLE METHOD
     */

    //add event
    public void addEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EVENT_CLUB, event.get_club());
        values.put(EVENT_TITLE, event.get_title());
        values.put(EVENT_DESCRIPTION, event.get_description());
        values.put(EVENT_START_DATE, String.valueOf(event.get_startDate()));
        values.put(EVENT_START_TIME, String.valueOf(event.get_startTime()));
        values.put(EVENT_END_DATE, String.valueOf(event.get_endDate()));
        values.put(EVENT_END_TIME, String.valueOf(event.get_endTime()));
        values.put(EVENT_REMINDER, event.get_reminder());
        values.put(EVENT_REPEATER, event.get_repeater());
        values.put(EVENT_LOCATION, event.get_location());
        db.insert(EVENT_TABLE, null, values);
        db.close();
    }

    //get Event
    public Event getEvent(int eventId){
        String query = "SELECT * FROM " + EVENT_TABLE +
                " WHERE " + EVENT_ID + " = " + eventId;
        Event event = new Event();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){
            cursor.moveToFirst();

            event.set_id(Integer.parseInt(cursor.getString(0)));
            event.set_club(cursor.getString(1));
            event.set_title(cursor.getString(2));
            event.set_description(cursor.getString(3));
            event.set_start_date(Date.valueOf(cursor.getString(4)));
            event.set_startTime(Time.valueOf(cursor.getString(5)));
            event.set_endDate(Date.valueOf(cursor.getString(6)));
            event.set_endTime(Time.valueOf(cursor.getString(7)));
            event.set_reminder(cursor.getString(8));
            event.set_repeater(cursor.getString(9));
            event.set_location(cursor.getString(10));
        }
        db.close();
        return event;
    }

    //get all event by date
    public List<Event> getAllEventByDate(String currentDate){

        String[] temp = currentDate.toString().split("-");
        int Year = Integer.parseInt(temp[0]);
        int Month = Integer.parseInt(temp[1]);
        int Day = Integer.parseInt(temp[2]);

        int compareDate = Year * 10000 + Month * 100 + Day;

        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE +
                " ORDER BY " + EVENT_START_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));
                    String[] start = cursor.getString(4).split("-");
                    int Year1 = Integer.parseInt(start[0]);
                    int Month1 = Integer.parseInt(start[1]);
                    int Day1 = Integer.parseInt(start[2]);

                    int startDate = Year1 * 10000 + Month1 * 100 + Day1;

                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    if(startDate >= compareDate){
                        eventList.add(event);
                    }
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    public List<Event> getEventWithinOneWeek(String currentDate){

        String[] temp = currentDate.toString().split("-");
        int Year = Integer.parseInt(temp[0]);
        int Month = Integer.parseInt(temp[1]);
        int Day = Integer.parseInt(temp[2]);

        int compareDate = Year * 10000 + Month * 100 + Day;

        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE +
                " ORDER BY " + EVENT_START_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));
                    String[] start = cursor.getString(4).split("-");
                    int Year1 = Integer.parseInt(start[0]);
                    int Month1 = Integer.parseInt(start[1]);
                    int Day1 = Integer.parseInt(start[2]);

                    int startDate = Year1 * 10000 + Month1 * 100 + Day1;

                    int different = startDate - compareDate;

                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    if(startDate >= compareDate && different < 7){
                        eventList.add(event);
                    }
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    public List<Event> getEventWithinThisMonth(String currentDate){

        String[] temp = currentDate.toString().split("-");
        int Year = Integer.parseInt(temp[0]);
        int Month = Integer.parseInt(temp[1]);
        int Day = Integer.parseInt(temp[2]);

        int compareDate = Year * 10000 + Month * 100 + Day;

        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE +
                " ORDER BY " + EVENT_START_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));
                    String[] start = cursor.getString(4).split("-");
                    int Year1 = Integer.parseInt(start[0]);
                    int Month1 = Integer.parseInt(start[1]);
                    int Day1 = Integer.parseInt(start[2]);

                    int startDate = Year1 * 10000 + Month1 * 100 + Day1;

                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    if(Year == Year1 && Month == Month1){
                        eventList.add(event);
                    }
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    //delete an event
    public void deleteEvent(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_TABLE, EVENT_ID + " = " + eventId, null);
        db.close();
    }

    public void deleteAllEvent(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( EVENT_TABLE, EVENT_ID + " >= 0 ", null);
        db.close();
    }

    //update an event
    public int updateEvent(Event event){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int eventId = event.get_eventId();
        values.put(EVENT_CLUB, event.get_club());
        values.put(EVENT_TITLE, event.get_title());
        values.put(EVENT_DESCRIPTION, event.get_location());
        values.put(EVENT_START_DATE, String.valueOf(event.get_startDate()));
        values.put(EVENT_START_TIME, String.valueOf(event.get_startTime()));
        values.put(EVENT_END_DATE, String.valueOf(event.get_endDate()));
        values.put(EVENT_END_TIME, String.valueOf(event.get_endTime()));
        values.put(EVENT_REMINDER, event.get_reminder());
        values.put(EVENT_REPEATER, event.get_repeater());
        values.put(EVENT_LOCATION, event.get_description());

        return db.update(EVENT_TABLE, values, EVENT_ID + " = " + eventId, null);
    }

    //GET EVENT ASCENDING ORDER BY NAME
    public List<Event> getAllEventAscendingOrder(){
        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE + " ORDER BY " + EVENT_TITLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));
                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    eventList.add(event);
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    public List<Event> getAllEventDescendingOrder(){
        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE + " ORDER BY " + EVENT_TITLE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));
                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    eventList.add(event);
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    //ascending date
    public List<Event> getAllEvent(String currentDate){

        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE +
                " ORDER BY " + EVENT_START_DATE ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));

                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    eventList.add(event);

                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    //decending date
    public List<Event> getAllEventsInDescDate(){
        List<Event> eventList = new ArrayList<Event>();
        String[] column = {
                EVENT_ID,
                EVENT_CLUB,
                EVENT_TITLE,
                EVENT_DESCRIPTION,
                EVENT_START_DATE,
                EVENT_START_TIME,
                EVENT_END_DATE,
                EVENT_END_TIME,
                EVENT_REMINDER,
                EVENT_REPEATER,
                EVENT_LOCATION

        };
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(EVENT_TABLE, column, null, null, null, null, EVENT_START_DATE + " DESC");

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Event event = new Event();
                    event.set_id(Integer.parseInt(cursor.getString(0)));
                    event.set_club(cursor.getString(1));
                    event.set_title(cursor.getString(2));
                    event.set_description(cursor.getString(3));
                    event.set_start_date(Date.valueOf(cursor.getString(4)));
                    event.set_startTime(Time.valueOf(cursor.getString(5)));
                    event.set_endDate(Date.valueOf(cursor.getString(6)));
                    event.set_endTime(Time.valueOf(cursor.getString(7)));
                    event.set_reminder(cursor.getString(8));
                    event.set_repeater(cursor.getString(9));
                    event.set_location(cursor.getString(10));

                    eventList.add(event);
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return eventList;
    }

    //add task for and event
    public void addEventTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK_NAME, task.get_taskName());
        values.put(TASK_DESCRIPTION, task.get_taskDescription());
        values.put(NUMBER_OF_HELPERS,task.get_numberOfHelpers());
        values.put(EVENT_TASK_ID, task.get_eventId());

        db.insert(TASK_TABLE, null, values);
        db.close();
    }

    public Task getTask(int taskId){
        String query = "SELECT * FROM " + TASK_TABLE +
                " WHERE " + TASK_ID + " = " + taskId;
        Task task = new Task();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){
            cursor.moveToFirst();

            task.set_id(Integer.parseInt(cursor.getString(0)));
            task.set_taskName(cursor.getString(1));
            task.set_taskDescription(cursor.getString(2));
            task.set_numberOfHelpers(Integer.parseInt(cursor.getString(3)));
            task.set_eventId(Integer.parseInt(cursor.getString(4)));
        }
        db.close();
        return task;
    }

    public List<Task> getAllTaskByEventIdAscByName(int eventId){
        String query = "SELECT * FROM " + TASK_TABLE +
                " WHERE " + EVENT_TASK_ID + " = " + eventId +
                " ORDER BY " + TASK_NAME + " ASC";
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();

                    task.set_id(Integer.parseInt(cursor.getString(0)));
                    task.set_taskName(cursor.getString(1));
                    task.set_taskDescription(cursor.getString(2));
                    task.set_numberOfHelpers(Integer.parseInt(cursor.getString(3)));
                    task.set_eventId(Integer.parseInt(cursor.getString(4)));

                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return taskList;
    }

    public List<Task> getAllTaskByEventIdDescByName(int eventId){
        String query = "SELECT * FROM " + TASK_TABLE +
                " WHERE " + EVENT_TASK_ID + " = " + eventId +
                " ORDER BY " + TASK_NAME + " DESC";
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();

                    task.set_id(Integer.parseInt(cursor.getString(0)));
                    task.set_taskName(cursor.getString(1));
                    task.set_taskDescription(cursor.getString(2));
                    task.set_numberOfHelpers(Integer.parseInt(cursor.getString(3)));
                    task.set_eventId(Integer.parseInt(cursor.getString(4)));

                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return taskList;
    }

    public List<Task> getAllTaskByEventIdAscByNumOfHelper(int eventId){
        String query = "SELECT * FROM " + TASK_TABLE +
                " WHERE " + EVENT_TASK_ID + " = " + eventId +
                " ORDER BY " + NUMBER_OF_HELPERS + " ASC";
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();

                    task.set_id(Integer.parseInt(cursor.getString(0)));
                    task.set_taskName(cursor.getString(1));
                    task.set_taskDescription(cursor.getString(2));
                    task.set_numberOfHelpers(Integer.parseInt(cursor.getString(3)));
                    task.set_eventId(Integer.parseInt(cursor.getString(4)));

                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return taskList;
    }

    public List<Task> getAllTaskByEventIdDescByNumOfHelper(int eventId){
        String query = "SELECT * FROM " + TASK_TABLE +
                " WHERE " + EVENT_TASK_ID + " = " + eventId +
                " ORDER BY " + NUMBER_OF_HELPERS + " DESC";
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();

                    task.set_id(Integer.parseInt(cursor.getString(0)));
                    task.set_taskName(cursor.getString(1));
                    task.set_taskDescription(cursor.getString(2));
                    task.set_numberOfHelpers(Integer.parseInt(cursor.getString(3)));
                    task.set_eventId(Integer.parseInt(cursor.getString(4)));

                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return taskList;
    }

    public int updateTask(Task task){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int taskId = task.get_taskId();
        values.put(TASK_NAME, task.get_taskName());
        values.put(TASK_DESCRIPTION, task.get_taskDescription());
        values.put(NUMBER_OF_HELPERS, task.get_numberOfHelpers());

        return db.update(TASK_TABLE, values, TASK_ID + " = " + taskId, null);
    }

    public void deleteTask(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_TABLE, TASK_ID + " = " + taskId, null);
        db.close();
    }

    public void addHelper(Helpers helpers){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HELPER_NAME, helpers.get_helperName());
        values.put(HELPER_CONTACT_NUMBER, helpers.get_helperContactNumber());

        db.insert(HELPER_TABLE, null, values);
        db.close();
    }

    public List<Helpers> getAllHelpers(){
        List<Helpers> helperList = new ArrayList<Helpers>();
        String selectQuery = "SELECT * FROM " + HELPER_TABLE +
                " ORDER BY " + HELPER_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Helpers helpers = new Helpers();
                    helpers.set_helper_id(Integer.parseInt(cursor.getString(0)));
                    helpers.set_helperName(cursor.getString(1));
                    helpers.set_helperContactNumber(cursor.getString(2));

                    helperList.add(helpers);

                }while (cursor.moveToNext());
            }
        }
        db.close();
        return helperList;
    }

    public List<Helpers> getAllHelpersContactNumber(){
        List<Helpers> helperList = new ArrayList<Helpers>();
        String selectQuery = "SELECT " + HELPER_CONTACT_NUMBER +
                " FROM " + HELPER_TABLE +
                " ORDER BY " + HELPER_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Helpers helpers = new Helpers();
                    helpers.set_helperContactNumber(cursor.getString(0));

                    helperList.add(helpers);

                }while (cursor.moveToNext());
            }
        }
        db.close();
        return helperList;
    }

    public void addDuplicateHelpers(Helpers helpers){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SESSION_CONTACT_NUMBER, helpers.get_helperContactNumber());
        values.put(SESSION_TASK_ID, helpers.get_task_id());

        db.insert(SESSION_TABLE, null, values);
        db.close();
    }

    public List<Helpers> getAllHelpersContactNumberWithTaskId(int taskId){
        List<Helpers> helperList = new ArrayList<Helpers>();
        String selectQuery = "SELECT " + SESSION_CONTACT_NUMBER +
                " FROM " + SESSION_TABLE +
                " WHERE " + SESSION_TASK_ID  + " = " + taskId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Helpers helpers = new Helpers();
                    helpers.set_helperContactNumber(cursor.getString(0));

                    helperList.add(helpers);
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return helperList;
    }

    public List<Helpers> getHelperFromJoinTable(int taskId){
        List<Helpers> helperList = new ArrayList<Helpers>();
        String selectQuery = "SELECT " +
                HELPER_TABLE + "." + HELPER_NAME + ", " +
                HELPER_TABLE + "." +HELPER_CONTACT_NUMBER + ", "+
                SESSION_TABLE + "." +SESSION_TASK_ID +
                " FROM " + SESSION_TABLE +
                " INNER JOIN " + HELPER_TABLE +
                " ON " + SESSION_TABLE  + "." +
                SESSION_CONTACT_NUMBER + " = " +
                HELPER_TABLE + "." + HELPER_CONTACT_NUMBER +
                " WHERE " + SESSION_TASK_ID  + " = " + taskId ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Helpers helpers = new Helpers();
                    helpers.set_helperName(cursor.getString(0));
                    helpers.set_helperContactNumber(cursor.getString(1));

                    helperList.add(helpers);
                }while (cursor.moveToNext());
            }
        }
        db.close();
        return helperList;
    }

    public void deleteSessionContactByTaskId(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_TABLE, SESSION_TASK_ID + " = " + taskId, null);
        db.close();
    }
}
