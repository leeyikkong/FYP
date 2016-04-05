//package com.iceice.rosterplanner;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.SparseBooleanArray;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by iceice on 3/31/2016.
// */
//public class ContactListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
//    List<String> contactName = new ArrayList<String>();
//    List<String> contactNumber = new ArrayList<String>();
//    List<Helpers> list = null;
//
//    ContactListAdapter contactListAdapter;
//    private int TASK_ID;
//    String name;
//    String number;
//    Button confirmButton;
//    private Toolbar toolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.contact_list_view);
//        Intent intent = getIntent();
//        TASK_ID = intent.getExtras().getInt("TASK_ID");
//
//        setUpToolbar();
//        setSupportActionBar();
//        helpersList();
//
//        getAllContacts(this.getContentResolver());
//        ListView contactListView = (ListView) findViewById(R.id.contact_list_view);
//        contactListAdapter = new ContactListAdapter();
//        contactListView.setAdapter(contactListAdapter);
//        contactListView.setOnItemClickListener(this);
//        contactListView.setItemsCanFocus(false);
//        contactListView.setTextFilterEnabled(true);
//        // adding
//        confirmButton = (Button) findViewById(R.id.confirm_button);
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            System.out.println(".............." + contactListAdapter.mCheckStates.size());
//            for (int i = 0; i < contactName.size(); i++)
//            {
//
//                if (contactListAdapter.mCheckStates.get(i) == true) {
//                    boolean status = true;
//                    name = contactName.get(i).toString();
//                    number = contactNumber.get(i).toString();
//
//                    //helpersList();
//                    for (Helpers item : list) {
//                        if (number.equals(item.get_helperContactNumber())) {
//                            status = false;
//                        } else {
//                            System.out.println("Not Checked......" + contactName.get(i).toString());
//                        }
//                    }
//                    if(status){
//                        saveHelpers();}
//                    saveDuplicateHelpersWithTaskId();
//                    Toast.makeText(ContactListActivity.this, name, Toast.LENGTH_SHORT).show();
//                }
//            }
//            }
//        });
//    }
//
//    private List<Helpers> helpersList() {
//        DbHandler db = new DbHandler(this);
//        list = db.getAllHelpersContactNumber();
//        return list;
//    }
//
//    private void saveDuplicateHelpersWithTaskId() {
//
//        Helpers helpers = new Helpers();
//
//        String helperContactNumber = number;
//
//        helpers.set_helperContactNumber(helperContactNumber);
//        helpers.set_task_id(TASK_ID);
//
//        DbHandler db = new DbHandler(getBaseContext());
//        db.addDuplicateHelpers(helpers);
//
//        finish();
//    }
//
//    private void saveHelpers() {
//
//        Helpers helpers = new Helpers();
//
//        String helperName = name;
//        String helperContactNumber = number;
//
//        helpers.set_helperName(helperName);
//        helpers.set_helperContactNumber(helperContactNumber);
//
//        DbHandler db = new DbHandler(getBaseContext());
//        db.addHelper(helpers);
//        Toast.makeText(this.getApplicationContext(),
//                "helper is added",
//                Toast.LENGTH_LONG).show();
//        finish();
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        // TODO Auto-generated method stub
//        contactListAdapter.toggle(arg2);
//    }
//
//    public void getAllContacts(ContentResolver cr) {
//        //boolean status = true;
//        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        while (phones.moveToNext()) {
//            boolean status = true;
//            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//            for (String item : contactNumber){
//                if(phoneNumber.equals(item)){
//                    status = false;
//                }
//            }
//            if(status){
//            contactName.add(name);
//            contactNumber.add(phoneNumber);
//            }
//        }
//        phones.close();
//    }
//
//    class ContactListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
//        private SparseBooleanArray mCheckStates;
//        LayoutInflater mInflater;
//        TextView contactNameAndNumberView;
//        CheckBox checkBox;
//
//        ContactListAdapter() {
//            mCheckStates = new SparseBooleanArray(contactName.size());
//            mInflater = (LayoutInflater) ContactListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return contactName.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//
//            return 0;
//        }
//
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            View view = convertView;
//            if (convertView == null)
//                view = mInflater.inflate(R.layout.contact_row_item, null);
//            TextView tv = (TextView) view.findViewById(R.id.contact_name);
//            contactNameAndNumberView = (TextView) view.findViewById(R.id.phone_number);
//            checkBox = (CheckBox) view.findViewById(R.id.checkBox_id);
//            tv.setText(contactName.get(position));
//            contactNameAndNumberView.setText("Phone No :" + contactNumber.get(position));
//            checkBox.setTag(position);
//            checkBox.setChecked(mCheckStates.get(position, false));
//            checkBox.setOnCheckedChangeListener(this);
//
//            return view;
//        }
//
//        public boolean isChecked(int position) {
//            return mCheckStates.get(position, false);
//        }
//
//        public void setChecked(int position, boolean isChecked) {
//            mCheckStates.put(position, isChecked);
//            System.out.println("hello...........");
//            notifyDataSetChanged();
//        }
//
//        public void toggle(int position) {
//            setChecked(position, !isChecked(position));
//        }
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView,
//                                     boolean isChecked) {
//            // TODO Auto-generated method stub
//
//            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
//        }
//    }
//
//    private void setUpToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }
//    }
//
//    private void setSupportActionBar() {
//        if (toolbar != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            toolbar.setNavigationIcon(R.drawable.ic_cross);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ContactListActivity.this.finish();
//                }
//            });
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
//        return true;
//    }
//}
