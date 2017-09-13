package com.example.deepikasaini.imeijasoos;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


public class History extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextId;
    private static final String SELECT_SQL = "SELECT * FROM History";
    private SQLiteDatabase db;

    private Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        openDatabase();
        editTextId = (EditText) findViewById(R.id.historyrecord);

        db.execSQL("CREATE TABLE IF NOT EXISTS History(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, imei VARCHAR, status VARCHAR, dateTime DATETIME DEFAULT CURRENT_TIMESTAMP);");
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        showRecords();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("HistoryDB", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {

        String info="";
        while(!c.isLast()) {
            String id = c.getString(0);
            String imei = c.getString(1);
            String status = c.getString(2);
            String date = c.getString(3);

            info+=id+"\nIMEI: "+imei+"\nStatus: "+status+"\nDate: "+date+"\n-------------------------------\n\n";
            c.moveToNext();
        }
        String id = c.getString(0);
        String imei = c.getString(1);
        String status = c.getString(2);
        String date = c.getString(3);
        info+=id+"\nIMEI: "+imei+"\nStatus: "+status+"\nDate: "+date+"\n-------------------------------\n\n";


        editTextId.setText(info);

    }


    @Override
    public void onClick(View v) {

    }


}
