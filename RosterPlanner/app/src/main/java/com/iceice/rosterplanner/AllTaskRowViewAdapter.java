package com.iceice.rosterplanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by iceice on 3/28/2016.
 */
public class AllTaskRowViewAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final List<Task> taskList;

    public AllTaskRowViewAdapter(Context context, List<Task> taskList){
        super(context, R.layout.task_row_item,taskList);

        this.context = context;
        this.taskList = taskList;
    }

    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_row_item, parent, false);

        TextView eventTitle = (TextView) rowView.findViewById(R.id.task_name_list);
        TextView numberOfHelper = (TextView) rowView.findViewById(R.id.num_of_helper);
        TextView helperText = (TextView) rowView.findViewById(R.id.helper);
        TextView helperNeeded = (TextView) rowView.findViewById(R.id.neededHelper);
        numberOfHelper.setTextColor(Color.parseColor("#111111"));
        helperText.setTextColor(Color.parseColor("#111111"));
        helperNeeded.setTextColor(Color.parseColor("#111111"));

        eventTitle.setText(taskList.get(position).get_taskName());
        int numHelper = (taskList.get(position).get_numberOfHelpers());
        String displayNumberOfHelper = String.valueOf(numHelper);
        numberOfHelper.setText(displayNumberOfHelper);

        return rowView;
    }

    public int getList(int position){
        return taskList.get(position).get_taskId();
    }
}
