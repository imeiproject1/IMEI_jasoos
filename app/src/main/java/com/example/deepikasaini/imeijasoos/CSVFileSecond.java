package com.example.deepikasaini.imeijasoos;

/**
 * Created by Neetika on 9/11/2017.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CSVFileSecond {
    InputStream inputStream;
    String Manufacturer;

    public CSVFileSecond(InputStream inputStream,String Manufacture){
        this.inputStream = inputStream;
        this.Manufacturer = Manufacture;
    }

    public ArrayList<String>  read(){
        ArrayList<String> result = new ArrayList<String>();
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
//                temp[0] = temp[0].substring(1,temp[0].length()-1);
//                temp[1] = temp[1].substring(1,temp[1].length()-1);
                int len_s= temp[1].length();
                temp[0]= temp[0].toString();
                temp[1]=temp[1].toString();
//                temp[1] = temp[1].substring(1,len_s-1);
                if(temp[0].equals(Manufacturer))
                    result.add(temp[1]);
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
