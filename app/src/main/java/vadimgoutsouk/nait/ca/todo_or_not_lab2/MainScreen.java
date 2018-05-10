package vadimgoutsouk.nait.ca.todo_or_not_lab2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainScreen extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    DBManager dbhelper;
    Spinner spinner;
    Button saveListButton;
    Button saveItemButton;
    Button archiveItemButton;
    EditText inputList;
    EditText inputItem;
    ListView itemView;
    Cursor itemCursor;
    static String chosenItem;
    static int ChosenItemID;
    ListViewCursorAdapter adapter;
    SQLiteDatabase db;
    SharedPreferences settings;
    View mainView;
    //int completed = 0;
//    final String[] FROM = {DBManager.C_ITEM, DBManager.ITEM_ID, DBManager.LIST_ID}; //listview fields
//    final int[] TO = {R.id.cursor_item, R.id.cursor_date, R.id.cursor_message}; // cursor firelds


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        //mainView = findViewById(R.id.activity_main)
         setContentView(R.layout.activity_main_screen);

        //ChosenItemID = 1;
        spinner = (Spinner) findViewById(R.id.spinner_list_names);
        spinner.setOnItemSelectedListener(this);
        dbhelper = new DBManager(this);
        saveListButton = (Button) findViewById(R.id.button_save_list);
        saveItemButton = (Button) findViewById(R.id.button_add_item);
        int completed = 0;

        //archiveItemButton = (Button)findViewById(R.id.button_archive);
        inputList = (EditText) findViewById(R.id.input_list);
        inputItem = (EditText) findViewById(R.id.input_item);
        itemView = (ListView) findViewById(R.id.listview_items);
        db = dbhelper.getReadableDatabase();

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        settings.registerOnSharedPreferenceChangeListener(this);

        mainView = findViewById(R.id.activity_main);
        String bgColor = settings.getString("main_bg_color_list","#660000");
        mainView.setBackgroundColor(Color.parseColor(bgColor));
        loadListViewWithItems();
        //loading the spinner
        loadSpinner();


        saveListButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                loadListViewWithItems();
                String listtitle = inputList.getText().toString();


                if(listtitle.trim().length() > 0)
                //if(li)
                {
                    DBManager database = new DBManager(getApplicationContext());

                    // inserting another list title to the local db
                    database.insertListTitle(listtitle);

                    inputList.setText(""); //setting the input field back to blank

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputList.getWindowToken(), 0);

                    //loading the spinner
                    loadSpinner();
                    Toast.makeText(getApplicationContext(), " Great success!",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You must enter a List name first",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

        saveItemButton.setOnClickListener(new View.OnClickListener()    //********************save item BELOW********************
        {

            @Override
            public void onClick(View view)
            {
                //collect the input
                String listitem = inputItem.getText().toString();


                // see if we can collect the list id instead of just the name

                if(listitem.trim().length() > 0)
                {
                   // DBManager databaseHelper = new DBManager(getApplicationContext());
                    DBManager database = new DBManager(getApplicationContext());
                    // creating an item to the database
                    createItem(listitem);

                    inputItem.setText(""); //setting the input field back to blank

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputList.getWindowToken(), 0);

                    //listview populate


                    //add item success message
                    Toast.makeText(getApplicationContext(), " Great success!",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You must enter an item first",
                            Toast.LENGTH_LONG).show();

                }

                //loadListViewWithItems();

            }
        });

//        archiveItemButton.setOnClickListener(new View.OnClickListener()
//    {
//        @Override
//        public void onClick(View v)
//        {
//           //String userName = settings.getString("user_name", "unknown");
//
//        }
//
//
//    });
 }

    @Override
    protected void onResume()
    {
        super.onResume();
        //loadSpinner();
        loadListViewWithItems();
    }



    //insert item
    public long createItem(String listitem) // here we need to fix this and add the name and the id not just the name
    {
        int completed = 0;
        Spinner spinner = (Spinner) findViewById(R.id.spinner_list_names);
       //String listname = spinner.getSelectedItem().toString();
//        long listname = spinner.getSelectedItemId(); // check this to change to numbers
//        int ChosenItemID = (int)listname;
//
//
//        chosenItem = listname;


        //staging
       // DBManager db = new DBManager(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.LIST_ID, ChosenItemID);
        values.put(dbhelper.C_ITEM, listitem);
        values.put(dbhelper.CREATED, Calendar.getInstance().getTime().toString());
        values.put(dbhelper.COMPLETED, completed );
        //values.put(dbhelper.LIST_ID, listid);

          // put this back to "listitem" if it doesnt work
        //values.put(DBManager.CREATED, Calendar.getInstance().getTime().toString());
        // values.put(C_STATUS, listitem.getStatus());


        //inserting the row
        long item_id = db.insertOrThrow(DBManager.ITEM_TABLE, null, values);
        db.close();

        loadListViewWithItems();

        return item_id;
    }



    public void loadListViewWithItems()
    {

        Cursor cursor = dbhelper.getListItems();
        final ArrayList<ListItems> listItems = new ArrayList<>();
        if (cursor.moveToFirst())
        {


            do
            {

                ListItems temp = new ListItems();
                temp.setId(cursor.getInt(cursor.getColumnIndex(DBManager.ITEM_ID)));
                temp.setItem(cursor.getString(cursor.getColumnIndex(DBManager.C_ITEM)));
                temp.setCreated(cursor.getString(cursor.getColumnIndex(DBManager.CREATED)));
                temp.setStatus(cursor.getInt(cursor.getColumnIndex(DBManager.COMPLETED)));
                listItems.add(temp);

//titles.setTitleId(cursor.getInt(cursor.getColumnIndex(C_ID)));
            }
            while (cursor.moveToNext());
        }
        ListViewCursorAdapter adapter = new ListViewCursorAdapter(this, cursor);//associates the cursor with the adapter
        itemView.setAdapter(adapter); //associates the adapter with the listview

        //set on item clicked listener
    itemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            String itemname = listItems.get(position).getItem(); // this is gonna be the name
            //long itemid = adapterView.getItemIdAtPosition(position); //replace this one with example

            int realitemid = listItems.get(position).getId();

            //int itemid2 =(int)itemid;
            //String itemid3 = String.valueOf(itemid2);// this is going to be the item id in the shown list (as a string)
            int listid = ChosenItemID;
            String listid2 = String.valueOf(listid);

            //int completedextra = completed;
//            String completedextrastr = String.valueOf(DBManager.COMPLETED);
            String completedextrastr = listItems.get(position).getStatus() + "";
            String createddate =listItems.get(position).getCreated() + "";

            Toast.makeText(getApplicationContext(), "you selectd " + itemname +  " and its position is: " + realitemid + "The list you selected is" + listid ,
                    Toast.LENGTH_LONG).show();

            Intent editscreenIntent = new Intent(getApplicationContext(), EditDataActivity.class );
            editscreenIntent.putExtra("name", itemname);
            editscreenIntent.putExtra("id", realitemid +"");
            editscreenIntent.putExtra("list", listid2);
            editscreenIntent.putExtra("completed", completedextrastr);
            editscreenIntent.putExtra("created", createddate);
            startActivity(editscreenIntent);



        }
    });

    }

//    private void loadListViewWithItems()
//    {
//        DBManager database = new DBManager(getApplicationContext());
//        List<ListItems> listitems = database.getListItems();
//
//
//        Cursor cursor = dbhelper.getListItems();
//        if (cursor.moveToFirst())
//        {
//            do
//            {
//                ListItems items = new ListItems();
//                items.setItem(cursor.getString(cursor.getColumnIndex(C_ITEM)));
//                listItems.add(items);
////titles.setTitleId(cursor.getInt(cursor.getColumnIndex(C_ID)));
//            }
//            while (cursor.moveToNext());
//        }
//
//
//    }

    private void loadSpinner()
        {
            DBManager database = new DBManager(getApplicationContext());

            //drop down elements on spinner
            List<ListTitles> listtitles = database.getListTitles();

            //adapter for spinner
            ArrayAdapter<ListTitles> spinadapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, listtitles);

            //setting up drop down style
            spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(spinadapter);

//            if(bNew == false)
//            {
//                spinner.setSelection(ChosenItemID -1);
//            }
//            else
//            {
//                spinner.setSelection(listtitles.size() -1);
//            }

        }


    @Override // menu inflater
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_item_view_archive:
            {
                Intent intent = new Intent(this,ViewArchived.class);
                this.startActivity(intent);
                break;
            }
            case R.id.menu_item_preference:
            {
                Intent intent = new Intent(this,PrefsActivity.class);
                this.startActivity(intent);
                break;
            }


        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) //from the spinner
    {
        //when selecting a spinner item
        String list = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + list,
                Toast.LENGTH_SHORT).show();

        long listnameid = spinner.getSelectedItemId();
        ChosenItemID = (int)listnameid;

        chosenItem = list;
        loadListViewWithItems();
    }







    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        String bgColor = settings.getString("main_bg_color_list","#660000");
        mainView.setBackgroundColor(Color.parseColor(bgColor));
    }

    public String ToastMethod(String string)
    {
        Toast.makeText(getApplicationContext(),string, Toast.LENGTH_LONG).show();
        return string;
    }

//    public class MyCursorAdapter extends SimpleCursorAdapter
//    {
//        static final String[] FROM = {DBManager.C_ITEM, DBManager., DBManager.}; //listview fields
//        static final int[] TO = {R.id.cursor_sender, R.id.cursor_date, R.id.cursor_message}; //
//
//        public MyCursorAdapter(Context context, Cursor cursor)
//        {
//            super(context, R.layout.cursor_row, cursor, FROM, TO );
//        }
//
//        // the following function is called once for each row in the result set
//        @Override
//        public void bindView(View row, Context context, Cursor cursor)
//        {
//            // this function binds the data to the controls
//            super.bindView(row, context, cursor);
//
//            // the following code is an example of how to 'massage' the data if desired
////        String strDate = cursor.getString(cursor.getColumnIndex(DBManager.C_DATE));
////        String strShortDate = strDate.substring(7,17);
////        TextView textView = (TextView)row.findViewById(R.id.cursor_date);
////        textView.setText(strShortDate);
//        }
//    }
}
