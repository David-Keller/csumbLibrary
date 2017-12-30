package dak.csumblibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAcc extends AppCompatActivity {
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText password = (EditText) findViewById(R.id.password);
        db = new DBHelper(this);
        Button create = (Button) findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String newUser = name.getText().toString();
                String newPassword = password.getText().toString();

                int error = db.addUser(newUser, newPassword);

                if(error == 1){
                    Toast.makeText(CreateAcc.this,"duplicate user", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CreateAcc.this,"Account successfully created", Toast.LENGTH_LONG).show();
                }

                //Intent i = new Intent(CreateAcc.this, MainScreen.class);
                //startActivity(i);
            }
        });
    }
}
