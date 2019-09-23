package com.example.randy.sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ClockInOutScreen extends AppCompatActivity {

    Button clockIn;
    Button clockOut;
    long clockInTime;
    long clockOutTime;
    long totalTimeWorked;
    public DatabaseHelper myDB;
    Double timeW;
    Double doublePay;
    double weeksTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in_out_screen);
        clockIn = findViewById(R.id.clockInButton);
        clockOut = findViewById(R.id.clockOutButton);
        myDB = DatabaseHelper.getInstance(this);
        clockInOut();

        goBack();
    }

    public void clockInOut(){
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");
       final  SimpleDateFormat formatter = new SimpleDateFormat("M-dd-yyyy hh:mm:ss");
       final SimpleDateFormat databaseFormatterDate = new SimpleDateFormat("M-dd-yyyy");
       final SimpleDateFormat databaseFormatterTime = new SimpleDateFormat("hh:mm:ss");
       final Cursor name = myDB.getName(employeeID);




        clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Date clockInDate = new Date(); //makes new date when the clockInButton is pressed

                clockInTime = System.currentTimeMillis();
                Toast.makeText(ClockInOutScreen.this, "You clocked in at: " + formatter.format(clockInDate), Toast.LENGTH_LONG).show();

                clockOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Date clockOutDate = new Date(); //makes new date for when clockOutButton is pressed

                        clockOutTime = System.currentTimeMillis();

                        totalTimeWorked = clockOutTime - clockInTime;

                        double tw = totalTimeWorked;
                        double minsWorked = tw / 60000;
                        //double hoursWorked = tw / 3600000;

                        myDB.updatePayAndTime(employeeID, minsWorked);

                        double pay = myDB.getWeeksPay(employeeID);
                        double time = myDB.getTimeWorked(employeeID);

                        //  String timeWorked = Objects.toString(hoursWorked, null);


                        myDB.insertTimeWorked(employeeID, minsWorked); //CHANGE BACK TO HOURS WORKED AFTER TESTING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                        timeW = (double) Math.round(minsWorked * 100.00) / 100.00;
                        doublePay = (double) Math.round(pay * 100.00) / 100.00;
                        weeksTime = (double) Math.round(time * 100.00) / 100.00;

                        String t = Objects.toString(weeksTime);
                        String timeWorked = Objects.toString(timeW, null);
                        String p = Objects.toString(doublePay);


                        Toast.makeText(ClockInOutScreen.this, "today you worked for: " + timeWorked + " MINUTES. With earnings of: " + p + " and a weekly total time of: "+ t, Toast.LENGTH_LONG).show();


                        if(name != null && name.moveToFirst()){
                            do{
                                myDB.insertClockInAndClockOut(employeeID,  name.getString(0) , databaseFormatterDate.format(clockOutDate), databaseFormatterTime.format(clockInDate), databaseFormatterTime.format(clockOutDate), timeWorked); //method to insert all at one time

                            }while(name.moveToNext());
                        }
                    }
                });
            }
        });




    }

    public void goBack() {
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        Button back = findViewById(R.id.clockInOutScreenBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ClockInOutScreen.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }




}
