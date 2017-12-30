package dak.csumblibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CancleHoldList extends AppCompatActivity {
    DBHelper db;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_hold_list);
        db = new DBHelper(this);
        i = getIntent();
        String user = i.getStringExtra("name");

        ListView list = (ListView) findViewById(R.id.listofbooks);
        ArrayList<String> trans =  new ArrayList<>();


        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("select *  from " +DBHelper.TABLE_RESERVATIONS + " where name = '" + user + "'",null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex("receipt");
            int idy = cursor.getColumnIndex(DBHelper.NAME);
            int idz = cursor.getColumnIndex(DBHelper.TITLE);
            int idb = cursor.getColumnIndex(DBHelper.HOLDDATE);
            int idc = cursor.getColumnIndex(DBHelper.RETURNDATE);
            String title = cursor.getString(idz);
            String holddate = cursor.getString(idb);
            String returnDate = cursor.getString(idc);
            int test = cursor.getInt(idx);
            String name = cursor.getString(idy); //may not be  needed


            trans.add(test + " " +name + " Book: " + title + " holddate: " + holddate + " returndate: " + returnDate);
        }



        ArrayAdapter array = new ArrayAdapter(this, R.layout.listofbooks,R.id.listofbooks, trans);
        list.setAdapter(array);


        // populate the list
    }


    public void deletebook(View view){
        View parent = (View) view.getParent();
        TextView textView = (TextView) parent.findViewById(R.id.listofbooks);
        String holder = textView.getText().toString();
        //pars for the id code
        String [] trans = holder.split("[//s]");
        String arg = trans[0];
        db = new DBHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        //delete book from the reserved database based on the id code
        database.delete(DBHelper.TABLE_RESERVATIONS, "receipt = ?", new String[] {arg});
        //log the deletion in the transaction database
        db.transacton(DBHelper.TRANS.cancel, trans[0], trans[3], trans[6], trans[8]);

        //call the activity again to refresh the screen
        startActivity(i);
        
    }
}
