package com.example.deepikasaini.imeijasoos;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String line;
            String m ="";
            Log.d("debug",".............................BackgroundWorker");
            String type = params[0];
            String IMEInumber = params[1];
//        int imei_number = Integer.parseInt(IMEInumber.substring(0,7);
//        String username = "abc";
//        String password = "abc";
//            String login_url = "http://10.0.2.2/IMEIjasoos/valid.php";
            String login_url = "http://192.168.0.102/IMEIjasoos/valid.php";
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

                if (result.equals("invalid")) {
                    Log.d("debug", ".......................................invalid");
                } else {
                    Log.d("debug", "...........................................valid");
                    Log.d("debug", result.getClass().getName());

                    JSONObject object;
                    try {
                        object = new JSONObject(result);
                        JSONArray jsonArray = object.getJSONArray("arr");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        m = jsonObject.getString("Manufacturer");

//                        status = (TextView) ((Activity)context).findViewById(R.id.status);
//                        status.setText("hello");

                        Log.d("debug", m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("debug", ".........................................after");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
//                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return m;
        }


    @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("loginStatus");
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
            alertDialog.setMessage(result);
            alertDialog.show();
            status = (TextView) ((Activity)context).findViewById(R.id.status);
            status.setText(result);

        }


//    }




}
