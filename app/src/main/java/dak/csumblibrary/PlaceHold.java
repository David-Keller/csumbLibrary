package dak.csumblibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PlaceHold extends AppCompatActivity {
    DBHelper db;
    String start;
    String end = "not set";
    String name = "not set";
    TextView startDate;
    TextView endDate;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_hold);
        db = new DBHelper(this);
        final EditText title = (EditText) findViewById(R.id.titlegetter);
        Intent namepass = getIntent();
        name = namepass.getStringExtra("name");

        endDate = (TextView) findViewById(R.id.enddate);
        startDate = (TextView) findViewById(R.id.startdate);

        Button setStart = (Button) findViewById(R.id.setstart);
        setStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(PlaceHold.this, HoldTimeManager.class);
                i.putExtra("type", "start");
                i.putExtra("end", end);

                startActivityForResult(i, 1);
            }
        });
        Button setEnd = (Button) findViewById(R.id.setend);
        setEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(PlaceHold.this, HoldTimeManager.class);
//                i.putExtra("type", "end");

                startActivityForResult(i, 2);
            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setEnabled(false);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            String titleString = title.getText().toString();
                if(!db.checkBook(titleString) ){
                    Toast.makeText(PlaceHold.this, "sorry that book doesn't exist", Toast.LENGTH_LONG).show();
                } else {
                    Calendar HoldDate = new GregorianCalendar();
                    Calendar ReturnDate = new GregorianCalendar();

                    String[] temp = start.split("[/:\\s]");
                    HoldDate.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]));
                    temp = end.split("[/:\\s]");
                    ReturnDate.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]));
                    HoldDate.add(Calendar.DAY_OF_MONTH, 7);
                    if (HoldDate.before(ReturnDate)) {
                        Toast.makeText(PlaceHold.this, "cant rent books for more than 7 days", Toast.LENGTH_LONG).show();
                    } else {

                        Boolean test = db.PlaceHold(titleString, name, start, end);
                        if (test) {
                            db.transacton(DBHelper.TRANS.hold, name, titleString, start, end);
                            Toast.makeText(PlaceHold.this, "hold requested", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(PlaceHold.this, MainScreen.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(PlaceHold.this, "sorry the book isn't availible at that time", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            start = data.getStringExtra("date");
            startDate.setText(start);
            if(!end.equals("not set")){
                submit.setEnabled(true);
            }

        }else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            end = data.getStringExtra("date");
            endDate.setText(end);
            if(!start.equals("not set")) {
                submit.setEnabled(true);
            }
        }else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(PlaceHold.this, "Canceled??", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(PlaceHold.this, "Something went very wrong!!", Toast.LENGTH_LONG).show();
        }
    }
}