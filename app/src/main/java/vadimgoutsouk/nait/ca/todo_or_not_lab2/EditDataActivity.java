package vadimgoutsouk.nait.ca.todo_or_not_lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


//---------------------------------------------------------------------------
public class EditDataActivity extends AppCompatActivity implements View.OnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{

    Button btnUpdate, btnDelete, btnArchive;
    EditText editable_item;

    public String selectedName;
    public String selectedID;
    public String selectedList;
    public String completedextra;
    public int completedchanged;
    public String createddate;
    MainScreen mainScreen;
    SharedPreferences settings;
    View mainView;

    CheckBox completed_check;
    SQLiteDatabase db;
    DBManager dbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_edit_data);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);

       // mainView = findViewById(R.id.);

        dbhelper = new DBManager(this);
        btnUpdate = (Button) findViewById(R.id.button_update);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnArchive = (Button) findViewById(R.id.button_archive);
        editable_item = (EditText) findViewById(R.id.edittext_edit_data);
        mainScreen = new MainScreen();
        completed_check = (CheckBox) findViewById(R.id.checkbox_completed);
        completed_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    //completedextra.equals(1);
                    completedchanged = 1;
                }
                else
                {
                    completedchanged = 0;
                }
            }

        });


        //receive intent
        Intent receiveIntent = getIntent();

        selectedID = receiveIntent.getStringExtra("id");
        selectedName = receiveIntent.getStringExtra("name");
        selectedList = receiveIntent.getStringExtra("list");
        completedextra = receiveIntent.getStringExtra("completed");
        createddate = receiveIntent.getStringExtra("created");

        editable_item.setText(selectedName);

//        if(completedextra.equals("0"))
//        {
//            completed_check.setChecked(false);
//        }
//        else if(completedextra.equals(1))
//        {
//            completed_check.setChecked(true);
//        }
        if (completedextra.equals("0")) {
            completed_check.setChecked(false);
        }
        else// if (completedextra.equals("1"))
            //
        {
            completed_check.setChecked(true);
        }
//        } else {
//            Toast.makeText(getApplicationContext(), "non of each",
//                    Toast.LENGTH_LONG).show();
//        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(completed_check.isChecked())
//                {
//                    //completedextra.equals(1);
//                    completedchanged.equals("1");
//                }
//                else
//                {
//                    completedchanged.equals("0");
//                }

                String item = editable_item.getText().toString();

                if (!item.equals("")) {
                    dbhelper.updateItem(item, selectedID, selectedList, completedchanged);
//                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
//                    startActivity(intent);

                    EditDataActivity.this.finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You cant leave this empty :-( database doesn't like it...",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(this, "cursor index: " + currentListIndex, Toast.LENGTH_LONG).show();
                int id = Integer.parseInt(selectedID);

                String whereClause = DBManager.ITEM_ID + "=" + id;
                db = dbhelper.getWritableDatabase();
                db.delete(DBManager.ITEM_TABLE, whereClause, null);
                EditDataActivity.this.finish();
                editable_item.setText("");
                Toast.makeText(getApplicationContext(), "Item: " + selectedName + " has been deleted. ",
                        Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
//                startActivity(intent);

            }
        });




        btnArchive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

           postToArchive(selectedList, selectedName, createddate, completedextra);

                //archive and then hopefully delete locally
                int id = Integer.parseInt(selectedID);

                String whereClause = DBManager.ITEM_ID + "=" + id;
                db = dbhelper.getWritableDatabase();
                db.delete(DBManager.ITEM_TABLE, whereClause, null);
                EditDataActivity.this.finish();
                editable_item.setText("");
                Toast.makeText(getApplicationContext(), "Item: " + selectedName + " has been deleted. ",
                        Toast.LENGTH_LONG).show();

            }
        });


    }


    private void postToArchive(String list, String item, String Created, String Completed)
    {
        String userName = settings.getString("user_name", "username");
        String password = settings.getString("user_password", "password");

        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://www.youcode.ca/Lab02Post.jsp");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("LIST_TITLE", list));
            postParameters.add(new BasicNameValuePair("CONTENT", item));
            postParameters.add(new BasicNameValuePair("COMPLETED_FLAG", Completed));
            postParameters.add(new BasicNameValuePair("ALIAS", userName));
            postParameters.add(new BasicNameValuePair("PASSWORD", password));
            postParameters.add(new BasicNameValuePair("CREATED_DATE", Created ));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            post.setEntity(formEntity);
            client.execute(post);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Item" + selectedName + "archived successfuly", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onClick(View v) {

    }


//    private class Archiver extends AsyncTask<String ,Void,String>
//    {
//
//        @Override
//        protected String doInBackground(String... strings)
//        {
//
//            String userName = settings.getString("user_name", "username");
//            String password = settings.getString("user_password", "password");
//
//
//            try
//            {
//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost("http://www.youcode.ca/Lab02Post.jsp");
//                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//                postParameters.add(new BasicNameValuePair("LIST_TITLE", selectedList));
//                postParameters.add(new BasicNameValuePair("CONTENT", selectedName));
//                postParameters.add(new BasicNameValuePair("COMPLETED_FLAG", completedextra));
//                postParameters.add(new BasicNameValuePair("ALIAS", userName));
//                postParameters.add(new BasicNameValuePair("PASSWORD", password));
//                postParameters.add(new BasicNameValuePair("CREATED_DATE", createddate ));
//                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
//                post.setEntity(formEntity);
//                client.execute(post);
//
//            }
//            catch(Exception e)
//            {
//                Toast.makeText(EditDataActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s)
//        {
//
//        }
//    }


}

