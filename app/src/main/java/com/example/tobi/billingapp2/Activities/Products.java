package com.example.tobi.billingapp2.Activities;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobi.billingapp2.R;
import com.example.tobi.billingapp2.Utility.CSVFileReader;
import com.example.tobi.billingapp2.Utility.CsvContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Products extends AppCompatActivity {


    private File pPath = new File(Environment.getExternalStorageDirectory() + "/Gemuese-Files/Products/productlist_woocommerce.csv");
    ArrayList<String[]> rowList;
    CsvContent csvData;
    private EditText editText;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        //Action Bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.gemuese11));
        background.setTileModeX(Shader.TileMode.MIRROR);
        actionBar.setBackgroundDrawable(background);
        //ActionBarEnd

        //Hide SoftInput
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        loadProducts();
        table = (TableLayout) findViewById(R.id.TableLayoutProducts);
        fill_table(csvData.getBody(), table);

        editText = (EditText) findViewById(R.id.search_filter_edit_text);

    }

    private void loadProducts() {
        try {
            InputStream inputStream =
                    new FileInputStream(pPath);
            CSVFileReader csvFile = new CSVFileReader(inputStream);
            CSVFileReader reader = new CSVFileReader(inputStream);
            reader.read();
            csvData = reader.getContent();


            /*for (String[] row : rowList) {
                for (int i = 0; i < row.length; i++) {
                    System.out.println("DEBUG Item " + i + " :" + row[i].toString());
                }
            } */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "YOu have to add a File in: " + pPath.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    //Fill Table with OnClick Method for each row
    private void fill_table(String[][] data, TableLayout tbl) {
        tbl.removeAllViews();
        for (int row_index = 0; row_index < data.length; row_index++) {
            TableRow row = new TableRow(this);
            String[] cells = data[row_index];
            for (int cell_index = 0; cell_index < cells.length; cell_index++) {
                TextView cell = new TextView(this);
                // set the text to cell"
                cell.setText(" " + cells[cell_index] + " ");
                // add the TextView and the CheckBox to the new TableRow
                row.addView(cell);
            }
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Platzhalter, hier kommt die Logik für Produkt ändern!
                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    TableRow row = (TableRow) view;
                    createChangeDialogue(row);
                }
            });
            // add the TableRow to the TableLayout
            tbl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
    //TODO: Continue Work here: This methos should open a dialogue where the suer can change product items-> Called in OnCLick from rowitem
    private void createChangeDialogue(TableRow row) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Products.this);
        alertDialog.setTitle("Change Products Attribute");
        alertDialog.setMessage("Enter Password");


        final EditText post_title = new EditText(Products.this);
        final EditText post_name = new EditText(Products.this);
        final EditText id = new EditText(Products.this);
        final EditText regular_price = new EditText(Products.this);
        final EditText tax_class = new EditText(Products.this);
        //final EditText images = new EditText(Products.this);
        final EditText category = new EditText(Products.this);
        final EditText brand = new EditText(Products.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        post_title.setLayoutParams(lp);
        post_name.setLayoutParams(lp);
        id.setLayoutParams(lp);
        regular_price.setLayoutParams(lp);
        tax_class.setLayoutParams(lp);
        category.setLayoutParams(lp);
        brand.setLayoutParams(lp);

        post_title.setHint("post_title");
        post_name.setHint("post_name");
        id.setHint("id");
        regular_price.setHint("regular_price");
        tax_class.setHint("tax_class");
        category.setHint("category");
        brand.setHint("brand");

        String post_title_current = ((TextView) row.getChildAt(0)).getText().toString();

    }

    private TableLayout search_filter_table(String[][] data, String filterString, TableLayout table) {
        fill_table(data, table);
        int fix_deleting_index = 0;
        //Row index = 1, becasue the first row is the header and we don't want to filter the header away
        for (int row_index = 1; row_index < data.length; row_index++) {
            String[] row = data[row_index];
            String name = row[0];
            name = name.toUpperCase();
            filterString = filterString.toUpperCase();
            //System.out.println(name);
            //System.out.println(filterString);
            if (!name.contains(filterString)) {
                int delete_Index = row_index - fix_deleting_index;
                table.removeViewAt(delete_Index);
                fix_deleting_index += 1;
            }
        }
        return table;
    }

    //OnCLickMethod
    public void filterOnClick(View view) {
        String text = editText.getText().toString();
        search_filter_table(csvData.getBody(), text, table);
    }
}
