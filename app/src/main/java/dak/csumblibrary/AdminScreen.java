package dak.csumblibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class AdminScreen extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
        db = new DBHelper(this);
        //create an add book button
        final Button addBook = (Button) findViewById(R.id.addbook);
        addBook.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(AdminScreen.this, addBook.class);
                startActivity(i);
            }
        });
        addBook.setVisibility(View.GONE);
        //RelativeLayout l = (RelativeLayout) findViewById(R.id.activity_admin_screen);


        ListView list = (ListView) findViewById(R.id.listview);
        ArrayList<String>trans =  new ArrayList<>();

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("select *  from " +DBHelper.TABLE_TRANSACTIONS + "",null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DBHelper.TRANS_TYPE);
            int idy = cursor.getColumnIndex(DBHelper.NAME);
            int idz = cursor.getColumnIndex(DBHelper.TIMESTAMP);

            int test = cursor.getInt(idx);
            String name = cursor.getString(idy);
            String timestamp = cursor.getString(idz);
            if(test == DBHelper.TRANS.account.ordinal()){
                trans.add("Account Created: " + name + " \nTimeStamp:" +timestamp);
            } else {
                int ida = cursor.getColumnIndex(DBHelper.TITLE);
                int idb = cursor.getColumnIndex(DBHelper.HOLDDATE);
                int idc = cursor.getColumnIndex(DBHelper.RETURNDATE);
                String title = cursor.getString(ida);
                String holddate = cursor.getString(idb);
                String returnDate = cursor.getString(idc);
                trans.add("Hold Placed on " +title + " by: " + name + " from: " + holddate + " to: " + returnDate + " \nTimeStamp:" +timestamp);
            }


        }
        ArrayAdapter array = new ArrayAdapter(this, R.layout.listoftrans, trans);

        //array.addAll(trans);
        list.setAdapter(array);
        //array.notifyDataSetChanged();

        addBook.setVisibility(View.VISIBLE);

    }
}
