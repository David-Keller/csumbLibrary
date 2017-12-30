package dak.csumblibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class addBook extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        db = new DBHelper(this);
        final TextView title = (TextView) findViewById(R.id.title);
        final TextView author = (TextView) findViewById(R.id.author);
        final TextView isbn = (TextView) findViewById(R.id.isbn);


        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String Title = title.getText().toString();
                String Authro = author.getText().toString();
                String Isbn = isbn.getText().toString();
                int test = db.addBook(Integer.parseInt(Isbn), Title, Authro);
                if(test == 1){
                    Toast.makeText(addBook.this,"book added", Toast.LENGTH_LONG).show();
                    db.transacton(DBHelper.TRANS.book, Title, Integer.parseInt(Isbn), Authro);

                }else{
                    Toast.makeText(addBook.this,"book not added", Toast.LENGTH_LONG).show();

                }


            }
        });
    }
}
