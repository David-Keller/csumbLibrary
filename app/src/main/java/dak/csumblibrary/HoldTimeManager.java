package dak.csumblibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class HoldTimeManager extends AppCompatActivity {

//    Boolean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hold_time_manager);

        final DatePicker d = (DatePicker) findViewById(R.id.checkout_datePicker);
        final TimePicker t = (TimePicker) findViewById(R.id.checkout_timePicker);

//        Intent intent = getIntent();
//        String type = intent.getStringExtra("type");
//        String end = intent.getStringExtra("end");
////        if(type.equals("start")){
////            if(!end.equals("not set")){
////                d.setMaxDate();
////            }
  //      }


        final Button placehold = (Button) findViewById(R.id.submit_checkout_button);
        placehold.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(HoldTimeManager.this, CreateAcc.class);
                String date = d.getYear() +"/" +  d.getMonth() + "/" + d.getDayOfMonth() + " " + t.getCurrentHour() + ":" + t.getCurrentMinute() + ":00";
                i.putExtra("date", date);

                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
