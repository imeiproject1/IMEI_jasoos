package com.example.deepikasaini.imeijasoos;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

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
//        int imei_number = Integer.parseInt(IMEInumber.substring(0,7);
//        String username = "abc";
//        String password = "abc";
//            String login_url = "http://10.0.2.2/IMEIjasoos/valid.php";
            String login_url = "http://192.168.43.95/IMEIjasoos/valid.php";
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

//                        status = (TextView) ((Activity)context).findViewById(R.id.status);
//                        status.setText("hello");
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
                    //  Log.d("mydebug",Integer.toString(t2));

                    //Log.d("mydebug",Integer.toString(Character.getNumericValue(imei_code.charAt(14))));

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
            /*alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("loginStatus");*/
            Log.d("debug",".............................onPreExecute");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("debug",".............................onPostExecute");
            Log.d("debug",result);

            status = (TextView) ((Activity)context).findViewById(R.id.status);
            String output="",str1="",str2="";
            String temp="";
            if(result.equals("IMEI is Valid")) {
                try {
                    JSONArray jsonArray = object.getJSONArray("arr");
                    Log.d("debug", Integer.toString(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        temp = jsonArray.getJSONObject(i).toString();
                        int idx = temp.indexOf(':');
                        str1 = temp.substring(2, idx - 1);
                        str2 = temp.substring(idx + 2, temp.length() - 2);
                        output = output + "\n" + str1 + " : " + str2;

                        /*TableRow tr = new TableRow(context);
                        tr.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.FILL_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        TextView a = new TextView(context);
                        a.setText(str1);
                        TextView b = new TextView(context);
                        b.setText(str2);

                        tr.addView(a);
                        tr.addView(b);

                        restable.addView(tr);*/


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                output = "Status : " + result;
                /*TableRow tr = new TableRow(context);
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView a = new TextView(context);
                a.setText("Status");
                TextView b = new TextView(context);
                b.setText(result);

                tr.addView(a);
                tr.addView(b);

                restable.addView(tr);*/
            }


            status.setText(output);

        }


//    }




}
