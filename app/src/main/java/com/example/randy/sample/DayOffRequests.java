package com.example.randy.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class DayOffRequests extends AppCompatActivity {

    ListView list;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    DatabaseHelper myDb;
    Cursor requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_off_requests);

        myDb = DatabaseHelper.getInstance(this);

        list = findViewById(R.id.requestsList);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.customlistview, arrayList);
        list.setAdapter(adapter);
        requests = myDb.getRequests();


        for (int i = 0; i < requests.getCount(); i++) {
            while (requests.moveToNext()) {
                arrayList.add(requests.getString(1) + " " + requests.getString(2));

                adapter.notifyDataSetChanged();
            }
        }
        answerRequests();
        goBack();
    }

    public void answerRequests() {
        final AlertDialog.Builder answerRequest = new AlertDialog.Builder(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = list.getItemAtPosition(position);
                final String[] split = o.toString().split(" ");
                requests = myDb.getRequests();
                answerRequest.setTitle("Answer Request");
                answerRequest.setMessage("Give this employee the selected day off?");

                answerRequest.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.updateRequest(1, split[0], "'" + split[1] + "'", 1);
                    }
                });

                answerRequest.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.updateRequest(2, split[0], "'" + split[1] + "'", 1);
                    }
                });

                answerRequest.show();

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

    public void goBack() {
        Button back = findViewById(R.id.dayOffRequestsBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DayOffRequests.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }
}
