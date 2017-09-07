package com.example.deepikasaini.imeijasoos;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MyIMEI extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextView myIMEI;
    Spinner spinner;

    String second, first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_imei);

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



        TelephonyManager telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getFirstMethod = telephonyClass.getMethod("getDeviceId", parameter);
            Log.d("SimData", getFirstMethod.toString());
            Object[] obParameter = new Object[1];
            obParameter[0] = 0;
//            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            first = (String) getFirstMethod.invoke(telephony, obParameter);
            obParameter[0] = 1;
            second = (String) getFirstMethod.invoke(telephony, obParameter);
            //myIMEI.setText("SimData: " + "first :" + first + "\n" + "SimData: " + "Second :" + second + "\n" + System.getProperty("os.version") + "\n" + Build.VERSION.SDK + "\n" + Build.DEVICE + "\n" + Build.MODEL + "\n" + Build.PRODUCT);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getItemAtPosition(pos).toString().equals("SIM Slot 1")) {
            myIMEI.setText("Sim Slot 1 Information: \n" + first + "\nOS Version: " + System.getProperty("os.version") + "\nSDK: " + Build.VERSION.SDK + "\nDevice: " + Build.DEVICE + "\nModel: " + Build.MODEL);

        } else {
            myIMEI.setText("Sim Slot 2 Information: \n" + second + "\nOS Version: " + System.getProperty("os.version") + "\nSDK: " + Build.VERSION.SDK + "\nDevice: " + Build.DEVICE + "\nModel: " + Build.MODEL);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
