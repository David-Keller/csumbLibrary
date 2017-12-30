package dak.csumblibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SystemAdminLogIn extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin);
        db = new DBHelper(this);

        final EditText name = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        Button button = (Button) findViewById(R.id.manageSystemLogInButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int test = db.checkUser(name.getText().toString(), password.getText().toString());
                if(test == 2) {
                    Intent i = new Intent(SystemAdminLogIn.this, AdminScreen.class);
                    startActivity(i);
                }else {
                    Toast.makeText(SystemAdminLogIn.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
