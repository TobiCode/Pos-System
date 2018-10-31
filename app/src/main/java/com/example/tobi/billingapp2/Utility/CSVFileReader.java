package com.example.tobi.billingapp2.Utility;

/**
 * Created by Tobi on 24.06.2018.
 */
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 22.06.2018.
 */

public class CSVFileReader {

    InputStream inputStream;
    CsvContent content;

    public CSVFileReader(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public CsvContent getContent() {
        return content;
    }

    public  void read(){
        //List resultList = new ArrayList();
        ArrayList<String[]> resultList = new ArrayList();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                csvLine = csvLine.replaceAll(",(?!(?:[^\"]*\"[^\"]*\")*[^\"]*$)" , "");
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            Log.i("Error: ", "Not able to read CSV");
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


        CsvContent csvContent = new CsvContent();
        csvContent.fromArrayList(resultList);
        this.content = csvContent;
    }
}