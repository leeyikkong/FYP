package com.iceice.rosterplanner;

import java.util.ArrayList;

/**
 * Created by iceice on 3/23/2016.
 */
public class Task {
    int _taskId;
    int _eventId;
    String _taskName;
    String _taskDescription;
    int _numberOfHelpers;
    ArrayList<Helpers> _helper;

    public Task(){

    }

    public Task(
                String taskName,
                String taskDescription,
                int numberOfHelpers){

        this._taskName = taskName;
        this._taskDescription = taskDescription;
        this._numberOfHelpers = numberOfHelpers;
    }

    public Task (int keyId) {
        this._taskId = keyId;
    }

    public void set_id(int id){
        this._taskId = id;
    }

    public int get_taskId(){
        return this._taskId;
    }

    public void set_taskName(String taskName){
        this._taskName = taskName;
    }

    public String get_taskName(){
        return this._taskName;
    }

    public void set_taskDescription(String taskDescription){
        this._taskDescription = taskDescription;
    }

    public String get_taskDescription(){
        return this._taskDescription;
    }

    public void set_numberOfHelpers(int numberOfHelpers){
        this._numberOfHelpers = numberOfHelpers;
    }

    public int get_numberOfHelpers(){
        return this._numberOfHelpers;
    }

    public void set_eventId(int eventId){
        this._eventId = eventId;
    }

    public int get_eventId(){
        return this._eventId;
    }
}

