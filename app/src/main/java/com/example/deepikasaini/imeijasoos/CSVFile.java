package com.example.deepikasaini.imeijasoos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public ArrayList<String>  read(){
        ArrayList<String> result = new ArrayList<String>();
        String line = "";
        String cvsSplitBy = ",";
        int i=0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {

                // use comma as separator
                String[] temp = csvLine.split(cvsSplitBy);

//                Log.d("debug",temp[0]);
//                result[i]=country[0];
                temp[0] = temp[0].substring(1,temp[0].length()-1);
                result.add(temp[0]);
//                System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
                i++;
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return result;
    }
}