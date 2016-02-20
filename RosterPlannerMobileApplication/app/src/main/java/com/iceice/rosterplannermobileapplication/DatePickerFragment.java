package com.iceice.rosterplannermobileapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by iceice on 2/4/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this,year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        EditText editText1 = (EditText)getActivity().findViewById(R.id.date_from);

        month += 1;

        String date = day + "-" + month + "-" + year;

        editText1.setText(date);
    }

}
