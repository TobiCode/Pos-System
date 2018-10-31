package com.example.tobi.billingapp2.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;

import com.example.tobi.billingapp2.R;
import com.example.tobi.billingapp2.Utility.CsvContent;
import com.example.tobi.billingapp2.Utility.Row;
import org.apache.commons.lang3.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSV_Content extends AppCompatActivity {

    CsvContent csv_Data;
    Row[] customerContent = new Row[0];
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv__content);
        //Action Bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        BitmapDrawable background = new BitmapDrawable (BitmapFactory.decodeResource(getResources(), R.drawable.gemuese11));
        background.setTileModeX(Shader.TileMode.MIRROR);
        actionBar.setBackgroundDrawable(background);
        //ActionBarEnd
        Intent intent = getIntent();
        csv_Data = (CsvContent) intent.getSerializableExtra("csvData");
        table = (TableLayout) findViewById(R.id.TableLayout01);
        csv_Data.fill_table(table,this);
        //fill_table(csv_Data,table);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveToSdCard(View view) throws IOException {
        File sdCard = Environment.getExternalStorageDirectory();
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        File outFile = new File (sdCard.getAbsolutePath() + "/Gemuese-Files/" + ts + ".csv");
        FileOutputStream file = new FileOutputStream(outFile,false);
        BufferedWriter f = new BufferedWriter(new OutputStreamWriter(file));
        String[][] body = csv_Data.getBody();
        f.write(StringUtils.join(csv_Data.getHeader()[0],','));
        f.newLine();
        for (int i = 0; i < body.length; i++) {
            f.write(StringUtils.join(body[i],','));
            f.newLine();
        }
        f.close();
        csv_Data.updateBody(customerContent);
        csv_Data.fill_table(table,this);

        Toast.makeText(this, "Saved at: " + sdCard.getAbsolutePath() + "/Gemuese-Files/" + ts + ".csv",
                Toast.LENGTH_SHORT).show();
    }


    public void customerContent(View view) {
        EditText customer_id_input_field   = (EditText)findViewById(R.id.IdFilter);
        String customerID = customer_id_input_field.getText().toString();
        Integer indexOfId = csv_Data.indexOfName("ID");
        customerContent = csv_Data.customerContent(customerID,indexOfId);
        csv_Data.fillTableWithCustomerContent(customerContent,table,this);

        if(customer_id_input_field.getText().toString().equals("")){
            csv_Data.fill_table(table,this);
        }
    }

    public void printFileOnClick(View view){
        Intent printIntent = new Intent(getApplicationContext(), BluetoothPrinter.class);
        printIntent.putExtra("customerData", customerContent);
        startActivity(printIntent);
    }




}
