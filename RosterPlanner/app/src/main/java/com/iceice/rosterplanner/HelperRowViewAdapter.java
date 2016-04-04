package com.iceice.rosterplanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by iceice on 4/2/2016.
 */
public class HelperRowViewAdapter extends ArrayAdapter<Helpers>{

    private final Context context;
    private final List<Helpers> helpersList;
    String contactNumber;
    String name;
    public HelperRowViewAdapter(Context context, List<Helpers> helpersList){
        super(context, R.layout.helper_row_item,helpersList);

        this.context = context;
        this.helpersList = helpersList;
    }

    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.helper_row_item, parent, false);

        TextView helperName = (TextView) rowView.findViewById(R.id.helper_name);
        TextView helperContact = (TextView) rowView.findViewById(R.id.helper_contact);
        Button whatsapp = (Button) rowView.findViewById(R.id.whatsapps);
        Button sms = (Button) rowView.findViewById(R.id.sms);

        helperName.setText(helpersList.get(position).get_helperName());
        contactNumber = helpersList.get(position).get_helperContactNumber();
        helperContact.setText(contactNumber);
        name = helpersList.get(position).get_helperName();

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mUri = Uri.parse("smsto:" + contactNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, mUri);
                intent.setPackage("com.whatsapp");
                String text = name;
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return rowView;
    }
    public int getList(int position){
        return helpersList.get(position).get_helper_id();
    }
}
