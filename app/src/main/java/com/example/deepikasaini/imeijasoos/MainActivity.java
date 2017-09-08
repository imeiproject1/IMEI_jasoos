package com.example.deepikasaini.imeijasoos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public String resstatus;
    //public TableLayout restable;
    //public ProgressDialog progDailog = new ProgressDialog(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("debug",".............................OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream inputStream = getResources().openRawResource(R.raw.table_manufacturer_distinct);
        CSVFile csvFile = new CSVFile(inputStream);
//        String[] string_Array = new String[10];
//        string_Array[10] = csvFile.read();
        ArrayList<String> string_Array = csvFile.read();
        int len = string_Array.size();
        Log.d("debug",Integer.toString(len));

        Log.d("debug","........................................");
//        Log.d("debug",string_Array[0]);
//        Log.d("debug",string_Array[6]);
//        Log.d("debug", String.valueOf(string_Array[0].getClass()));



//        String[] languages = { "C","C++","Java","C#","PHP","JavaScript","jQuery","AJAX","JSON" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, string_Array);
        //Find TextView control
        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(1);
        //Set the adapter
        acTextView.setAdapter(adapter);
        Log.d("debug","........................................");




        scanButton = (ImageButton) findViewById(R.id.scanButton);
        checkButton = (Button) findViewById(R.id.checkButton);
        IMEI_editText = (EditText) findViewById(R.id.IMEInumber);

        //restable = (TableLayout) findViewById(R.id.resTable);

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
        String status = resstatus;//---------------------------change this

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
           // Log.d("debug",resstatus);

        }
    }
    public class BackgroundWorker extends AsyncTask<String,String,String> {   //Activity,AsyncTask<String,String,String> {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);


        Context context;
        AlertDialog alertDialog;
        BackgroundWorker(Context ctx){
            context = ctx;
        }
        TextView status;
        TableLayout restable;
        ProgressDialog progDailog = new ProgressDialog(MainActivity.this);
        JSONObject object;


        @Override
        protected String doInBackground(String... params) {

            String result = "";
            String line;
            String imei_status="";
            String retval ="";
            Log.d("debug",".............................BackgroundWorker");
            String type = params[0];
            String IMEInumber = params[1];
            if(IMEInumber.length()!=15)
            {
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Please Enter a 15 digit IMEI");
                alertDialog.show();
                return "";

            }

            String login_url = "http://172.16.0.171/IMEIjasoos/valid.php";
            if(type.equals("check")) try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                Log.d("debug", "....................................................connected");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("IMEInumber", "UTF-8") + "=" + URLEncoder.encode(IMEInumber, "UTF-8");
//              URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                Log.d("debug", "...........................................result");
                Log.d("debug", result);

                try {
                    object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("arr");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    imei_status = jsonObject.getString("Status");
                    Log.d("debug","--------------JSON status");
                    Log.d("debug",imei_status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                retval = imei_status;

                if(!retval.equals("IMEI is invalid")){
                    int sumval=0;
                    String even_nu="";
                    for(int i = 0; i<14;i++){
                        //Log.d("mydebug", String.valueOf(imei_code.charAt(i)));
                        if(i%2==0)
                            sumval += Character.getNumericValue(IMEInumber.charAt(i));
                        else {
                            int temp = Character.getNumericValue(IMEInumber.charAt(i)) * 2;
                            even_nu += temp;
                        }
                    }
                    Log.d("debug",Integer.toString(sumval));
                    Log.d("debug",even_nu);
                    for(int i = 0; i<even_nu.length(); i++) {
                        sumval += (Character.getNumericValue(even_nu.charAt(i)));
                    }
                    Log.d("debug",Integer.toString(sumval));
                    int t2 = sumval%10;
                    if(t2>0)
                        t2=10-t2;
                     if(t2==Character.getNumericValue(IMEInumber.charAt(14)))
                        retval = "IMEI is Valid";
                    else
                        retval = "IMEI is Invalid";

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return retval;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retval;

        }


        @Override
        protected void onPreExecute() {
            Log.d("debug",".............................onPreExecute");
            super.onPreExecute();

            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();

            restable = new TableLayout(MainActivity.this);
            restable.removeAllViews();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String  result) {

            Log.d("debug", ".............................onPostExecute");
            Log.d("debug", result);

            status = (TextView) ((Activity) context).findViewById(R.id.status);
           // status.setTextColor(Color.BLACK);
            String output = "", str1 = "", str2 = "";
            String temp = "";
            if (result.equals("IMEI is Valid")) {
                resstatus = "Valid";
                try {
                    JSONArray jsonArray = object.getJSONArray("arr");
                    Log.d("debug", Integer.toString(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        temp = jsonArray.getJSONObject(i).toString();
                        int idx = temp.indexOf(':');
                        str1 = temp.substring(2, idx - 1);
                        str2 = temp.substring(idx + 2, temp.length() - 2);
                        output = output + "\n" + str1 + " : " + str2;

                        restable = (TableLayout) findViewById(R.id.resTable);

                        TableRow tr = new TableRow(MainActivity.this);
                        tr.setLayoutParams(new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        TextView a = new TextView(MainActivity.this);
                        a.setText(str1);
                        a.setTextColor(Color.BLUE);
                        a.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                        a.setPadding(5,5,5,5);


                        TextView b = new TextView(MainActivity.this);
                        b.setText(str2);
                        b.setTextColor(Color.BLACK);
                        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                        b.setPadding(5,5,5,5);
                        b.setWidth(750);
                        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        tr.addView(a);
                        tr.addView(b);

                        restable.addView(tr, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                restable = (TableLayout) findViewById(R.id.resTable);

                TableRow tr = new TableRow(MainActivity.this);
                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView a = new TextView(MainActivity.this);
                a.setText("Status");
                a.setTextColor(Color.BLUE);
                a.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                a.setPadding(5,5,5,5);


                TextView b = new TextView(MainActivity.this);
                b.setText("Invalid");
                b.setTextColor(Color.BLACK);
                b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                b.setPadding(5,5,5,5);
                b.setWidth(750);
                b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tr.addView(a);
                tr.addView(b);

                restable.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                resstatus = "Invalid";
            }
            //status.setText(output);
            progDailog.dismiss();
            createDatabase();
            insertIntoDB();
        }

    }
 }

