package com.example.deepikasaini.imeijasoos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    public ImageButton scanButton;
    public Button checkButton;
    public EditText IMEI_editText;
    public TextView status;
    public int strlength;

    private SQLiteDatabase db;
    private Cursor queryOutput;

    private static final int RC_BARCODE_CAPTURE = 9001;
    public TableLayout restable;
    //TableRow tr = new TableRow(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("debug",".............................OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (ImageButton) findViewById(R.id.scanButton);
        checkButton = (Button) findViewById(R.id.checkButton);
        IMEI_editText = (EditText) findViewById(R.id.IMEInumber);
        status = (TextView) findViewById(R.id.status);
        restable = (TableLayout) findViewById(R.id.resTable);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarCodeScan.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        IMEI_editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) {
                TextView textView = (TextView)findViewById(R.id.LenDisplay);
                textView.setText(String.valueOf(s.toString().length())+"/15");
                strlength = s.toString().length();
                if(strlength==15){
                    textView.setTextColor(Color.parseColor("#2ABD13"));
                }
                else {
                    textView.setTextColor(Color.RED);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            { }
            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            { }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public void createDatabase(){
        db=openOrCreateDatabase("HistoryDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS History(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, imei VARCHAR, status VARCHAR, dateTime DATETIME DEFAULT CURRENT_TIMESTAMP);");
    }
    public void insertIntoDB(){
        String imei = IMEI_editText.getText().toString();
        Log.d("debug","############### pt 1");
        String status = "Valid/Invalid";//---------------------------change this

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        Log.d("debug","############### pt 2");
        String query1 = "SELECT * FROM History WHERE dateTime LIKE '"+formattedDate+"%'";
        queryOutput = db.rawQuery(query1, null);
        Log.d("debug","############### pt 3");
        int rows= queryOutput.getCount();
        String str = String.valueOf(rows);

        Log.d("datetime output now: ", formattedDate );
        Log.d("datetime output: ", str );


        queryOutput.close();
        if(rows<=17) {
            String query = "INSERT INTO History (imei, status) VALUES('" + imei + "', '" + status + "');";
            db.execSQL(query);
            Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Maximum limit of daily queries exceeded", Toast.LENGTH_LONG).show();
        }
    }

    private void showPeoples() {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.MyIMEI) {
            Intent intent = new Intent(MainActivity.this, MyIMEI.class);
            startActivity(intent);

        } else if (id == R.id.History) {
            showPeoples();

        } else if (id == R.id.AboutUs) {
            Intent intent = new Intent(MainActivity.this, AboutUs.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            Log.d("debug", "Barcode read: ");
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarCodeScan.BarcodeObject);
                    IMEI_editText.setText(barcode.displayValue);
                    Log.d("debug", "Barcode read: " + barcode.displayValue);
                } else {

                    Log.d("debug", "No barcode captured, intent data is null");
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void check_imei(View view){
        if(strlength<15)
        {
            AlertDialog al = new AlertDialog.Builder(MainActivity.this).create();
            al.setMessage("PLease Enter IMEI of length 15");
            al.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            al.show();

        }
        else {
            String IMEInumber = IMEI_editText.getText().toString();
//        Toast.makeText(getApplicationContext(),IMEInumber,Toast.LENGTH_LONG).show();
            String type = "check";

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        {
//            @Override
//            public void onResult(String s) {
//                status.setText(s);
//            }
//        });
            backgroundWorker.execute(type, IMEInumber);
            createDatabase();
            insertIntoDB();
        }
    }



}
