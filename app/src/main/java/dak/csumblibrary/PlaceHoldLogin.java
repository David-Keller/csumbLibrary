package dak.csumblibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlaceHoldLogin extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_hold_login);
        final EditText name = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        db = new DBHelper(this);
        Button login = (Button) findViewById(R.id.placeHoldLogInButton);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String userCheck = name.getText().toString();
                String passwordCheck = password.getText().toString();

                int error = db.checkUser(userCheck, passwordCheck);

                if(error == 0){
                    Toast.makeText(PlaceHoldLogin.this,"Incorrect Username or password", Toast.LENGTH_LONG).show();
                }
                 else{

                    Intent i = new Intent(PlaceHoldLogin.this, PlaceHold.class);
                    i.putExtra("name", userCheck);
                    startActivity(i);
                }

            }
        });
    }
}
