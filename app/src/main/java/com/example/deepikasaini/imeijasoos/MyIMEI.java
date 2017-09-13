package com.example.deepikasaini.imeijasoos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyIMEI extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextView myIMEI;
    Spinner spinner;

    String second, first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_imei);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        myIMEI = (TextView) findViewById(R.id.myIMEI);
        spinner = (Spinner) findViewById(R.id.spinner);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sim_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(MyIMEI.this);



        TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        first = telephony.getDeviceId(0);
        second  = telephony.getDeviceId(1);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getItemAtPosition(pos).toString().equals("SIM Slot 1")) {
            myIMEI.setText("IMEI: \n" + first + "\nModel: " + Build.MODEL);

        } else {
            myIMEI.setText("IMEI: \n" + second + "\nModel: " + Build.MODEL);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void verifyimei(View view)
    {
        Intent intent = new Intent(MyIMEI.this,MainActivity.class);
        if (spinner.getSelectedItem().toString().equals("SIM Slot 1"))
            intent.putExtra("IMEI",first);
        else if(spinner.getSelectedItem().toString().equals("SIM Slot 2"))
            intent.putExtra("IMEI",second);
        else{
            AlertDialog alert = new AlertDialog.Builder(MyIMEI.this).create();
            alert.setMessage("Please Select a SIM");
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }
        startActivity(intent);
    }
}
