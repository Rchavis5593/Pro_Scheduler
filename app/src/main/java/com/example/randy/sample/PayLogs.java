package com.example.randy.sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class PayLogs extends AppCompatActivity {

    DatabaseHelper myDB;

    Cursor payLogs;

    ListView payList;

    ArrayList payArray;
    String label;

    ArrayAdapter payAdapter;

    Button back, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_logs);

        myDB = DatabaseHelper.getInstance(this); //getting instance of databse class

        payList = findViewById(R.id.payLogsList);

        payLogs = myDB.getPayLogs(); //getting all pay logs from database

        payArray = new ArrayList();

        payAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, payArray);

        payList.setAdapter(payAdapter); //setting adapter to payList

        label = "ID:     Hours Worked:     Weeks Pay     Date Entered:"; //String for header

       payArray.add(label);
       payAdapter.notifyDataSetChanged();

        for (int i = 0; i < payLogs.getCount(); i++) {
            while (payLogs.moveToNext()) {
                //payArray.add(PayLogs.getString(0) + "         " +PayLogs.getString(1) + "                      " + PayLogs.getString(2)+ "                " + PayLogs.getString(3));
                payArray.add("0000" + "         " +"40" + "                      " + "110.97"+ "            " + "1/28/2019"); //ONLY FOR TESTING
                payAdapter.notifyDataSetChanged();
            }
        }

        goBack();
        goHome();








    }

    public void goBack(){
        back = findViewById(R.id.payLogsBack);

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(PayLogs.this, LogDecision.class);
                startActivity(intent);

            }
        });
    }

    public void goHome() {
        home = findViewById(R.id.payLogsHome);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PayLogs.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }
}
