package com.example.deepikasaini.imeijasoos;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by arpit on 11-09-2017.
 */

public class Result extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        JSONArray jsonArray = null;
        String temp="",str1,str2,output="";
        TableLayout restable;

        String jsonstring = getIntent().getStringExtra("JSONarray");

        try {
            jsonArray = new JSONArray(jsonstring);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                temp = jsonArray.getJSONObject(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int idx = temp.indexOf(':');
            str1 = temp.substring(2, idx - 1);
            str2 = temp.substring(idx + 2, temp.length() - 2);
            output = output + "\n" + str1 + " : " + str2;

            restable = (TableLayout) findViewById(R.id.resTable);

            TableRow tr = new TableRow(Result.this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            TextView a = new TextView(Result.this);
            a.setText(str1);
            a.setTextColor(Color.CYAN);
            a.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            a.setPadding(5,5,5,5);


            TextView b = new TextView(Result.this);
            b.setText(str2);
            b.setTextColor(Color.WHITE);
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

    }
}
