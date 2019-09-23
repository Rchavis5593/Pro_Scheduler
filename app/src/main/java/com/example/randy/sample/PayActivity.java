package com.example.randy.sample;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class PayActivity extends AppCompatActivity {

    EditText employeeRole;
    EditText employeePayRate;
    EditText thisWeeksPay;
    EditText timeWorked;
    EditText vacationDate;

    double pay;
    double weeksPay;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        myDb = DatabaseHelper.getInstance(this);

        employeeRole = findViewById(R.id.Employee_Role);
        employeePayRate = findViewById(R.id.Employee_Pay_Rate);
        thisWeeksPay = findViewById(R.id.weeksPay);
        timeWorked = findViewById(R.id.Hours_Worked);
        vacationDate = findViewById(R.id.vacationDate);

        String employeeID = getIntent().getStringExtra("EMPLOYEEID");



        Cursor role = myDb.getRole(employeeID); //getting role of employee

        Cursor payRate = myDb.getPayRate(employeeID); //getting hourly wage of employee

        double timeWorkedThisWeek = (double) Math.round(myDb.getTimeWorked(employeeID) * 100.00) / 100.00; //getting time worked from database

        weeksPay = (double) Math.round(myDb.getWeeksPay(employeeID) * 100.00) / 100.00; //getting weeks pay from database


        if (role != null && role.moveToFirst() && payRate != null && payRate.moveToFirst()) {
            do {
                employeeRole.setText(role.getString(0));
                employeePayRate.setText(payRate.getString(0));
                pay = (double) Math.round(myDb.getWeeksPay(employeeID) * 100.00) / 100.00;

            } while (role.moveToNext() && payRate.moveToNext());
        }
        String tw = Objects.toString(timeWorkedThisWeek);//converting the timeWorked double into string\
        String wp = Objects.toString(weeksPay); //converting weeks pay to String

        thisWeeksPay.setText(wp); //setting the EditText to weeks earned pay from database
        timeWorked.setText(tw); //setting the Hours_Worked EditText to weekly time worked from database

        String startDay = myDb.getStartDate(employeeID);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy");
        try {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDate.parse(startDay)); //set the time to the employees starting date
            calendar.add(Calendar.DATE, 365); //adding year to date
            startDay = simpleDate.format(calendar.getTime());
            vacationDate.setText(startDay);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        String testDate = simpleDate.format(calendar.getTime());


        if (Objects.equals(testDate, vacationDate.getText().toString())) { //if it was a year since it last updated
            calendar.add(Calendar.DATE, 365); //add another year
            String nextVacationDate = simpleDate.format(calendar.getTime());
            vacationDate.setText(nextVacationDate);

        }






        goBack();


    }

    public void displayMessage(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }

    public void goBack() {
        Button back = findViewById(R.id.payActivityBack);
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }

}
