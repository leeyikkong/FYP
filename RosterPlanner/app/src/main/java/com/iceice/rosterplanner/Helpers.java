package com.iceice.rosterplanner;

import java.util.ArrayList;

/**
 * Created by iceice on 3/23/2016.
 */
public class Helpers {

    int _helper_id;
    int _task_id;
    String _helperName;
    String _helperContactNumber;
    ArrayList<Task> _task;

    public Helpers(){

    }

    public Helpers(
            String helperName,
            String helperContactNumber){

        this._helperName = helperName;
        this._helperContactNumber = helperContactNumber;
    }

    public int get_helper_id() {
        return _helper_id;
    }

    public void set_helper_id(int _helper_id) {
        this._helper_id = _helper_id;
    }

    public int get_task_id() {
        return _task_id;
    }

    public void set_task_id(int _task_id) {
        this._task_id = _task_id;
    }

    public String get_helperName() {
        return _helperName;
    }

    public void set_helperName(String _helperName) {
        this._helperName = _helperName;
    }

    public String get_helperContactNumber() {
        return _helperContactNumber;
    }

    public void set_helperContactNumber(String _helperContactNumber) {
        this._helperContactNumber = _helperContactNumber;
    }

    public ArrayList<Task> get_task() {
        return _task;
    }

    public void set_task(ArrayList<Task> _task) {
        this._task = _task;
    }
}
