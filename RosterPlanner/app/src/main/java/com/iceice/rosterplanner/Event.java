package com.iceice.rosterplanner;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by iceice on 3/1/2016.
 */
public class Event {

    int _id;
    String _club;
    String _title;
    String _location;
    Date _startDate;
    Time _startTime;
    Date _endDate;
    Time _endTime;
    String _reminder;
    String _repeater;
    int _all_day_status;
    String _description;
    ArrayList<Task> _eventTask;

    //constructor
    public Event(){

    }

    //constuctor
    public Event(int id,
                 String club,
                 String title,
                 String description,
                 int allDayStatus,
                 Date startDate,
                 Time startTime,
                 Date endDate,
                 Time endTime,
                 String reminder,
                 String repeater,
                 String location){
        this._id = id;
        this._club = club;
        this._title = title;
        this._description = description;
        this._all_day_status = allDayStatus;
        this._startDate = startDate;
        this._startTime = startTime;
        this._endDate = endDate;
        this._endTime = endTime;
        this._reminder = reminder;
        this._repeater = repeater;
        this._location = location;
    }

    //constructor
    public Event (int keyId){
        this._id = keyId;
    }

    public void set_id(int id){
        this._id = id;
    }

    public int get_eventId(){
        return this._id;
    }

    public void set_club(String club) {
        this._club = club;
    }

    public String get_club(){
        return this._club;
    }

    public void set_title(String title){
        this._title = title;
    }

    public String get_title(){
        return this._title;
    }

    public void set_location(String location){
        this._location = location;
    }

    public String get_location(){
        return this._location;
    }

    public void set_all_day_status(int all_day_status){
        this._all_day_status = all_day_status;
    }

    public int get_all_day_status(){
        return this._all_day_status;
    }

    public void set_start_date(Date startDate){
        this._startDate = startDate;
    }

    public Date get_startDate(){
        return this._startDate;
    }

    public void set_startTime(Time startTime){
        this._startTime = startTime;
    }

    public Time get_startTime(){
        return this._startTime;
    }

    public void set_endDate(Date endDate){
        this._endDate = endDate;
    }

    public Date get_endDate(){
        return this._endDate;
    }

    public void set_endTime(Time endTime){
        this._endTime = endTime;
    }

    public Time get_endTime(){
        return this._endTime;
    }

    public void set_reminder(String reminder){
        this._reminder = reminder;
    }

    public String get_reminder(){
        return this._reminder;
    }

    public void set_repeater(String repeater){
        this._repeater = repeater;
    }

    public String get_repeater(){
        return this._repeater;
    }

    public void set_description(String description){
        this._description = description;
    }

    public String get_description(){
        return this._description;
    }

    public void set_eventTask(Task task){
        this._eventTask.add(task);
    }

    public ArrayList<Task> get_eventTask(){
        return this._eventTask;
    }
}
