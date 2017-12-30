package dak.csumblibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    DBHelper dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        dataBase = new DBHelper(this);

        final Button createAccount = (Button) findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MainScreen.this, CreateAcc.class);
                startActivity(i);
            }
        });

        final Button placeHold = (Button) findViewById(R.id.placeHold);
        placeHold.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MainScreen.this, PlaceHoldLogin.class);
                startActivity(i);
            }
        });
        final Button cancelHold = (Button) findViewById(R.id.cancelHold);
        cancelHold.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MainScreen.this, CancleHold.class);
                startActivity(i);
            }
        });
        final Button manageSystem = (Button) findViewById(R.id.manageSystem);
        manageSystem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MainScreen.this, SystemAdminLogIn.class);
                startActivity(i);
            }
        });
        final Button clearDB = (Button) findViewById(R.id.clearDB);
        clearDB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dataBase.clearDB();
                Toast.makeText(MainScreen.this, "DataBase Cleared", Toast.LENGTH_LONG).show();
            }
        });
    }




}
