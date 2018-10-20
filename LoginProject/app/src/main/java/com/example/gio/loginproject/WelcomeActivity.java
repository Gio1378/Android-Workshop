package com.example.gio.loginproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import model.SQLiteDB;
import model.User;

public class WelcomeActivity extends AppCompatActivity {

    private static final String FULL_NAME = "fullName";
    private static final String USER_TABLE_NAME = "user";
    private final AppCompatActivity activity = WelcomeActivity.this;

    private TableLayout tableWelcomeLayout;
    private TableRow tableRowWelcomeLayout;
    private TableRow.LayoutParams tableRowParams;
    private TextView titleUserFullName;
    private TextView titleView;
    private TextView userDescription;
    private SQLiteDB myDatabase = new SQLiteDB(activity);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();

        titleView = (TextView) findViewById(R.id.listName);
        titleView.setText(R.string.user_list);

        if (intent != null) {
            titleUserFullName = (TextView) findViewById(R.id.userFullName);
            titleUserFullName.setText(intent.getStringExtra(FULL_NAME));
            titleUserFullName.setTextSize(22);
        }
        tableWelcomeLayout = (TableLayout) findViewById(R.id.tableWelcomeLayout);

/*            for (User user : myDatabase.getAllUser()) {
                String uDescriptionString = user.getFullName() + " \n " + user.getUserEmail();
                userDescription.setText(uDescriptionString);
                tableRowWelcomeLayout.addView(userDescription);
                tableWelcomeLayout.addView(tableRowWelcomeLayout);
            }*/

        //setting query to get all records from table
        String query = "select * from " + USER_TABLE_NAME;
        Log.d("", "" + query);

        //getting contents of the table which user selected from the table
        ArrayList<Cursor> allRecord = myDatabase.getData(query);
        final Cursor oneRecord = allRecord.get(0);

        //display the first row of the table with column names of the table selected by the user
        tableRowWelcomeLayout = new TableRow(getApplicationContext());
        tableRowWelcomeLayout.setBackgroundColor(Color.BLACK);
        tableRowWelcomeLayout.setPadding(0, 2, 0, 2);

        tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(0, 0, 2, 0);

        for (int k = 0; k < oneRecord.getColumnCount(); k++) {
            LinearLayout cell = new LinearLayout(activity);
            cell.setBackgroundColor(Color.WHITE);
            cell.setLayoutParams(tableRowParams);

            final TextView tableheadercolums = new TextView(getApplicationContext());
            // tableheadercolums.setBackgroundDrawable(gd);
            tableheadercolums.setPadding(0, 0, 4, 3);
            tableheadercolums.setText("" + oneRecord.getColumnName(k));
            tableheadercolums.setTextColor(Color.parseColor("#000000"));

            //columsView.setLayoutParams(tableRowParams);
            cell.addView(tableheadercolums);
            tableRowWelcomeLayout.addView(cell);

        }
        tableWelcomeLayout.addView(tableRowWelcomeLayout);

        do {
            tableRowWelcomeLayout = new TableRow(getApplicationContext());
            tableRowWelcomeLayout.setBackgroundColor(Color.BLACK);
            tableRowWelcomeLayout.setPadding(0, 2, 0, 2);

            for (int j = 0; j < oneRecord.getColumnCount(); j++) {
                LinearLayout cell = new LinearLayout(this);
                cell.setBackgroundColor(Color.WHITE);
                cell.setLayoutParams(tableRowParams);
                userDescription = new TextView(getApplicationContext());
                String column_data = "";
                try {
                    column_data = oneRecord.getString(j);
                } catch (Exception e) {
                    // Column data is not a string , do not display it
                }
                userDescription.setText(column_data);
                userDescription.setTextColor(Color.parseColor("#000000"));
                userDescription.setPadding(0, 0, 4, 3);
                cell.addView(userDescription);
                tableRowWelcomeLayout.addView(cell);

            }

            tableWelcomeLayout.addView(tableRowWelcomeLayout);
            //currentrow = currentrow + 1;

        } while (oneRecord.moveToNext());


        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
