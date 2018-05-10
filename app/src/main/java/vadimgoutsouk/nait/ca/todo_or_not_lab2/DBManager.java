package vadimgoutsouk.nait.ca.todo_or_not_lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
/**
 * Created by laptop on 3/7/2018.
 */

public class DBManager extends SQLiteOpenHelper
{

    Date currentTime = Calendar.getInstance().getTime();
    //database logcat
    static final String TAG = "DBManager";

    //database name
    static final String DB_NAME = "TODO.db";

    //Database version
    static final int    DB_VERSION =27;

    //table names
    static final String LIST_TABLE = "lists";
    static final String ITEM_TABLE = "items";

    //id fields
    static final String LIST_ID = "list_id";
    static final String ITEM_ID = BaseColumns._ID;

    //list table columns
    static final String C_LIST = "title_name";

    //**items table columns**
    static final String C_ITEM = "item_name";

    //status + created date
    static final String CREATED = "created_at";
    static final String COMPLETED = "completed";
//    //title-to-list table columns (tags)
//    static final String C_LIST_ID = "list_id";
//    static final String C_ITEM_ID= "item_id";


    //create list table
    String create_list = "create table "
            + LIST_TABLE + " ("
            + LIST_ID + " integer primary key autoincrement, "
            + C_LIST + " text, "
            + CREATED + " text" + ")";
//------------------------------------------------------------------------------------

    //create item table statement
    String create_item = "CREATE TABLE "
            + ITEM_TABLE + "("
            + ITEM_ID + " INTEGER PRIMARY KEY autoincrement,"
            + C_ITEM + " text,"
            + C_LIST + " text,"
            + LIST_ID + " integer, "
            + COMPLETED + " integer default 0, "
            + CREATED + " DATETIME" + ")";

    //-----------------------------------------------------------------------------------------


    public DBManager(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    //create list table statement
    public void onCreate(SQLiteDatabase database)
    {


        database.execSQL(create_list);
        database.execSQL(create_item);
        //database.execSQL(create_item_list);


    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("drop table if exists " +LIST_TABLE);
        database.execSQL("drop table if exists " +ITEM_TABLE);
        //database.execSQL("drop table if exists " +ITEM_LIST_TAG);// drops the old database
       // Log.d(TAG, "onUpdated");

        //recreate the database after delete and upgrade
        onCreate(database);
    }

    //insert list
    public void insertListTitle(String title)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_LIST, title);
        //values.put(LIST_ID, title.getTitleId());


        database.insert(LIST_TABLE, null, values);
        database.close();
    }



    //--------------------creating a list array of ListTitles to use with the spinner *******************lists
    public List<ListTitles> getListTitles()
    {
        List<ListTitles> listTitles = new ArrayList<ListTitles>();
        String queryAll = "SELECT  * FROM " + LIST_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryAll, null);


        //looping through the rows and adding to the list
        if (cursor.moveToFirst())
        {
            do
            {
                ListTitles titles = new ListTitles();
                //titles.setTitleId(cursor.getInt(cursor.getColumnIndex(C_ID)));
                titles.setTitle(cursor.getString(cursor.getColumnIndex(C_LIST)));

                listTitles.add(titles);

            }
            while (cursor.moveToNext());
        }
        //closing the connection to cursor and database
        cursor.close();
        database.close();

        return listTitles;
         }
//------------------------------------------------------------get list items------------------------
    public Cursor getListItems()
    {

//        // declare variable to take from spinner // changed the name to id
//        String chosenListName = MainScreen.chosenItem;

        int chosenitemID = MainScreen.ChosenItemID;


        SQLiteDatabase database = this.getReadableDatabase();
        String queryAll = "SELECT  * FROM " + ITEM_TABLE + " WHERE " + LIST_ID + " = '" + chosenitemID + "'"; // add it here

        Cursor cursor = database.rawQuery(queryAll, null);

        return cursor;
    }

    //--------------------update list with a new item --------------------------------------------------------------

    public void updateItem(String newitem, String id, String listid, int completedint)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + ITEM_TABLE
                + " SET "
                + C_ITEM + " = '" + newitem + "', "
                + COMPLETED + " = " + completedint +
                " WHERE " + ITEM_ID + " = '" + id + "'" +
                " AND " + LIST_ID + " = '" + listid +"'";

        db.execSQL(query);


    }
//
//    SQLiteDatabase db = this.getWritableDatabase();
//    String query = "UPDATE " + ITEM_TABLE + " SET " + C_ITEM + " = '" + newitem + "' WHERE " + ITEM_ID + " = '" + id + "'" +
//            " AND " + LIST_ID + " = '" + listid +"'";
//
//        db.execSQL(query);

}



//
//    public Cursor getItemID(String name)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " +
//    }
    //creating a list array of ListItems  &**************** items
//    public List<ListItems> getListItems()
//    {
//
//        // declare variable to take from spinner
//        long chosenListID = MainScreen.chosenItem;
//
//        List<ListItems> listItems = new ArrayList<ListItems>();
//
//        String queryAll = "SELECT  * FROM " + ITEM_TABLE + " WHERE " + LIST_ID + " = " + chosenListID; // add it here
//
//        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor cursor = database.rawQuery(queryAll, null);
//
//        //looping through the rows and adding to the list
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
//        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listItems);
//
//        //closing the connection to cursor and database
//        cursor.close();
//        database.close();
//
//        return listItems;
//    }

//    populateListView()
//    {
//
//    }




//class ListViewCursorAdapter extends SimpleCursorAdapter
//{
//    static final String[] FROM = {DBManager.C_ITEM}; //fields in the cursor
//    static final int[] TO = {R.id.listview_items}; // fields in the listview
//
//    public ListViewCursorAdapter(Context context, Cursor cursor)
//    {
//
//        super(context, R.layout.listview_row, cursor, FROM, TO);
//
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        super.bindView(view, context, cursor);
//        //called for each row
//    }
//}



