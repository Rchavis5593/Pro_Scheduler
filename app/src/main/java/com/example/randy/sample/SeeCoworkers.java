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

public class SeeCoworkers extends AppCompatActivity {
    String employeeID;
    String day;
    ListView list;

    DatabaseHelper myDB;
    int coworkerCount;
    ArrayAdapter coworkerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_coworkers);
        myDB = DatabaseHelper.getInstance(this);
        employeeID = getIntent().getStringExtra("EMPLOYEEID");
        day = getIntent().getStringExtra("DAY");

        displayCowokers("'" + day + "'", employeeID);

        goBack();
        goHome();

    }

    public void displayCowokers(String day, String employeeID){

        list = findViewById(R.id.seeCoworkersList);

        ArrayList coworkersArray = new ArrayList();

      Cursor coworkers = myDB.getCoworkers(day, employeeID); //getting name, id, and hours for all employees that work a certain day

        coworkerCount = coworkers.getCount(); //getting the number of employees that work that day

        if(coworkers != null && coworkers.moveToFirst()){
            do{
                coworkersArray.add(coworkers.getString(0) + " " + coworkers.getString(1) + " " + coworkers.getString(2) + " ");

                coworkerCount--; //decreases for every employee added

            }while(coworkers.moveToNext() && coworkerCount >= 0); //while there are still employees left in the cursor object
        }

        coworkerAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, coworkersArray);

        list.setAdapter(coworkerAdapter);

        coworkerAdapter.notifyDataSetChanged();




    }

    public void displayMessage(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }

    public void goBack() {
        Button back = findViewById(R.id.seeCoworkersBack);
        employeeID = getIntent().getStringExtra("EMPLOYEEID");

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SeeCoworkers.this, ViewSchedule.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }

    public void goHome() {
        employeeID = getIntent().getStringExtra("EMPLOYEEID");
        Button back = findViewById(R.id.seeCoworkersHome);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SeeCoworkers.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }
}
