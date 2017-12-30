package dak.csumblibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by david on 11/29/2016.
 */

public class DBHelper extends SQLiteOpenHelper {


    public static final String TABLE_BOOKS = "books";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";

    public static final String HOLDDATE = "holddate";
    public static final String RETURNDATE ="returndate";
    public static final String TITLE ="title";
    public static final String TRANS_TYPE ="type";
    public static final String TIMESTAMP = "time";

    public static final String CREATE_TABLE = "";
    public static final String DB_NAME = "MYDB.db";
    public static final int DB_VERSION = 1;

    public static enum TRANS {account(0) , hold(1), cancel(2), book(3);
        private final int value;
        private TRANS(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_BOOKS +" (isbn integer primary key, title text, author text, onhold integer DEFAULT 0)");
        db.execSQL("create table " + TABLE_USERS + " (name text primary key, password text, hold integer default 0, admin integer)"); //the hold integer will be the isbn number
        db.execSQL("create table " + TABLE_TRANSACTIONS + " (id INT AUTO_INCREMENT PRIMARY KEY, type integer, name text, title text,  holddate text, returndate text, time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("create table " + TABLE_RESERVATIONS + " (receipt INT AUTO_INCREMENT PRIMARY KEY, name text, title real, holddate real, returndate text)");
        db.execSQL("insert into " + TABLE_USERS + " (name, password, admin) VALUES ('pop', 'pops', '1')");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS+"");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS+"");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS+"");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS+"");
        onCreate(db);
    }


    public int addBook(int isbn, String title, String author){
        SQLiteDatabase db = this.getWritableDatabase();
        //check to see if the book exists
        Cursor cursor = db.rawQuery("select * from " + TABLE_BOOKS + " where isbn='" +isbn +"'", null);
        if(cursor.getCount() > 0){
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("isbn", isbn);
        contentValues.put("title", title);
        contentValues.put("author", author);
        db.insert(TABLE_BOOKS, null, contentValues);
        return 1;
    }

    public int addUser(String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        //check to see if the user exists
        Cursor cursor = db.rawQuery("select * from " +TABLE_USERS+ " where name='" + name + "'", null);
        boolean test = cursor.getCount() == 0;
        if(test){
            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, name);
            contentValues.put(PASSWORD, password);
            db.insert(TABLE_USERS, null, contentValues);
            transacton(TRANS.account, name);
            return 0;
        }

        return 1;
    }
    public int checkUser(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        //check to see if the user exists
        Cursor cursor = db.rawQuery("select * from " + TABLE_USERS + " where name='" + name + "' AND password='" + password + "'", null);
        boolean test = cursor.getCount() > 0;
        if (test) {
            int admin = cursor.getColumnIndex("admin");
            cursor.moveToFirst();
            admin = cursor.getInt(admin);
            if (admin == 1) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }
    public boolean checkBook(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        //check to see if the user exists
        Cursor cursor = db.rawQuery("select * from " +TABLE_BOOKS+ " where title='" + title + "'", null);
        boolean test = cursor.getCount() != 0;
        if(test){
            return true;
        }else {
            return false;
        }
    }
    public void transacton(TRANS type, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into " + TABLE_TRANSACTIONS + " (type, name) values ('" + type.toString() + "', '" + name + "')");
    }

    public void transacton(TRANS type,String name, String title, String holdDate, String returnDate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into " + TABLE_TRANSACTIONS + " (type, title, name, holddate, returndate) values ('" +
                type.ordinal() + "','" +title + "','" + name + "', '" + holdDate + "', '" + returnDate +"')");
    }
    public void transacton(TRANS type,String title, int isbn, String author){
        SQLiteDatabase db = this.getWritableDatabase();
        //isnn is going into holddate
        db.execSQL("insert into " + TABLE_TRANSACTIONS + " (type, title, name, holddate) values ('" +
                type.ordinal() + "','" + title + "','" + author + "', '" + isbn+"')");
    }

    public Boolean PlaceHold(String title,String name, String holdDate, String returnDate){
        SQLiteDatabase db = this.getWritableDatabase();
            Calendar HoldDate = new GregorianCalendar();
            Calendar ReturnDate = new GregorianCalendar();

            String [] temp = holdDate.split("[/:\\s]");
            HoldDate.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]));
            temp = returnDate.split("[/:\\s]");
            ReturnDate.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]));


            Cursor cursor = db.rawQuery("select * from " + TABLE_RESERVATIONS + " where title='" + title+ "'", null);
            if(cursor.getCount() == 0){
                db.execSQL("insert into " + TABLE_RESERVATIONS + " (name, title, holddate, returndate) values ('" + name + "','" + title + "','" +holdDate+"','" + returnDate + "')");
                return true;
            }
            while(cursor.moveToNext()) {
                int idb = cursor.getColumnIndex("returndate");
                String returndate = cursor.getString(idb);
                Calendar calendar = new GregorianCalendar();
                String[] fool = returndate.split("[/:\\s]");//split the string on spaces, colins and slashes
                calendar.set(Integer.parseInt(fool[0]), Integer.parseInt(fool[1]), Integer.parseInt(fool[2]), Integer.parseInt(fool[3]), Integer.parseInt(fool[4]));
                Boolean test1 = calendar.before(HoldDate);
                test1 = test1|calendar.equals(HoldDate);
                idb = cursor.getColumnIndex("holddate");
                String holddate = cursor.getString(idb);
                fool = holddate.split("[/:\\s]");
                calendar = new GregorianCalendar();
                calendar.set(Integer.parseInt(fool[0]), Integer.parseInt(fool[1]), Integer.parseInt(fool[2]), Integer.parseInt(fool[3]), Integer.parseInt(fool[4]));
                Boolean test2 = calendar.before(ReturnDate);
                test2 = test2|calendar.equals(ReturnDate);

                if (test1||test2) {
                    if (test1&&test2)
                        continue;
                    return false;
                }
            }
            db.execSQL("insert into " + TABLE_RESERVATIONS + " (name, title, holddate, returndate) values ('" + name + "','" + title + "','" +holdDate+"','" + returnDate + "')");
            return true;
        }


    //}

    public void cancleHold(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("select * from " + TABLE_RESERVATIONS + " where name='"+ name + "'", null);
    }
}
