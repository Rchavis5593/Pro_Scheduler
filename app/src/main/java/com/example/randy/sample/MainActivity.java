package com.example.randy.sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    private Cursor answer;
    Cursor name;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = DatabaseHelper.getInstance(this);


        String employeeID = getIntent().getStringExtra("EMPLOYEEID");



        name = myDB.getName(employeeID); //getting name from database
        welcome = findViewById(R.id.welcomeEmployeeTextView);
        if(name != null && name.moveToFirst()){
            do{
                welcome.setText("Welcome, " + name.getString(0));
            }while(name.moveToNext());
        }





        goBack();
        seeNotifications();
        setNumberOfUnreadNotifications(employeeID, myDB);
    }

    public void ClockInScreen(View v) {
        String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        Intent intent = new Intent(MainActivity.this, ClockInOutScreen.class);
        intent.putExtra("EMPLOYEEID", employeeID);
        startActivity(intent);
    }

    public void PaymentScreen(View v) {
        String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        Intent intent = new Intent(MainActivity.this, PayActivity.class);
        intent.putExtra("EMPLOYEEID", employeeID);
        startActivity(intent);

    }

    public void ViewSchedule(View v) {
        String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        Intent intent = new Intent(MainActivity.this, ViewSchedule.class);

        intent.putExtra("EMPLOYEEID", employeeID);
        startActivity(intent);
    }

    public void EditSchedule(View v) {
        String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        Intent intent = new Intent(MainActivity.this, EditSchedule.class);
        intent.putExtra("EMPLOYEEID", employeeID);
        startActivity(intent);
    }

    public void displayMessage(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }

    public void goBack() {
        Button back = findViewById(R.id.employeeScreenBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogInScreen.class);
                startActivity(intent);

            }
        });
    }

    public void seeNotifications(){
        Button seeNotifications = findViewById(R.id.unreadNotificationsButton);
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        seeNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmployeeNotifications.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);
            }
        });
    }

    public void setNumberOfUnreadNotifications(String employeeID, DatabaseHelper myDB){
        Button seeNotifications = findViewById(R.id.unreadNotificationsButton);

        int numberOfRequests = myDB.getRequestsCountForEmployee(employeeID);

        String num = Objects.toString(numberOfRequests);

        seeNotifications.setText("You have " + num + " unread notifications!");



    }
}
