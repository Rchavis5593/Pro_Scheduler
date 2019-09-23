package com.example.randy.sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class WorkLogs extends AppCompatActivity {

    DatabaseHelper myDB;
    ListView list;
    ArrayList<String> array;
    ArrayAdapter adapter;
    String label;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_logs);

        myDB = DatabaseHelper.getInstance(this);

        Cursor logs = myDB.getRecords();
        list = findViewById(R.id.workLogsList);
        array = new ArrayList();
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, array);
        list.setAdapter(adapter);
        label = "  ID            Name                 CIT                     COT                    Total Time Worked";

        array.add(label);
        adapter.notifyDataSetChanged();



        for (int i = 0; i < logs.getCount(); i++) {
            while (logs.moveToNext()) {
                array.add(logs.getString(0) + "          " +  logs.getString(1) + "             " + logs.getString(2) + "             " +  logs.getString(3) + "                        " + logs.getString(4));
                adapter.notifyDataSetChanged();
            }
        }

        goBack();
        goHome();
    }

    public void goBack() {
        Button back = findViewById(R.id.workLogsBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WorkLogs.this, LogDecision.class);
                startActivity(intent);

            }
        });
    }

    public void goHome() {
        home = findViewById(R.id.workLogsHome);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WorkLogs.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }



    public void displayMessage(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }
}
