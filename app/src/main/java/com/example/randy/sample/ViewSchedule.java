package com.example.randy.sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class ViewSchedule extends AppCompatActivity {

    String employeeID;
    Button monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    DatabaseHelper myDB;
    Cursor m, tu, w, th, f, sa, su;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        myDB = DatabaseHelper.getInstance(this);

        employeeID = getIntent().getStringExtra("EMPLOYEEID");

        monday = findViewById(R.id.viewScheduleMonday);
        tuesday = findViewById(R.id.viewScheduleTuesday);
        wednesday = findViewById(R.id.viewScheduleWednesday);
        thursday = findViewById(R.id.viewScheduleThursday);
        friday = findViewById(R.id.viewScheduleFriday);
        saturday = findViewById(R.id.viewScheduleSaturday);
        sunday = findViewById(R.id.viewScheduleSunday);

        m = myDB.getTimesForDay(employeeID, "'" + "Monday" + "'");
        tu = myDB.getTimesForDay(employeeID, "'" + "Tuesday" + "'");
        w = myDB.getTimesForDay(employeeID, "'" + "Wednesday" + "'");
        th = myDB.getTimesForDay(employeeID, "'" + "Thursday" + "'");
        f = myDB.getTimesForDay(employeeID, "'" + "Friday" + "'");
        sa = myDB.getTimesForDay(employeeID, "'" + "Saturday" + "'");
        su = myDB.getTimesForDay(employeeID, "'" + "Sunday" + "'");


        if(m!= null && m.moveToFirst() && tu!= null && tu.moveToFirst()&& w!= null && w.moveToFirst() && th!= null && th.moveToFirst() &&f!=null && f.moveToFirst() && sa!=null && sa.moveToFirst() && su!=null && su.moveToFirst()) {
            do {
                //displaying schedule for monday
                if (!Objects.equals(m.getString(0), "0")) { //zero in database represents off day
                    monday.setText("You work: " + m.getString(0));
                } else {
                    monday.setText("You do not work");
                }

                //displaying schedule for tuesday
                if (!Objects.equals(tu.getString(0), "0")) {
                    tuesday.setText("You work: " + tu.getString(0));
                } else {
                    tuesday.setText("You do not work");
                }

                //displaying schedule for wednesday
                if (!Objects.equals(w.getString(0), "0")) {
                    wednesday.setText("You work: " + w.getString(0));
                } else {
                    wednesday.setText("You do not work");
                }

                //displaying schedule for thursday
                if (!Objects.equals(th.getString(0), "0")) {
                    thursday.setText("You work: " + th.getString(0));
                } else {
                    thursday.setText("You do not work");
                }

                //displaying schedule for friday
                if (!Objects.equals(f.getString(0), "0")) {
                    friday.setText("You work: " + f.getString(0));
                } else {
                    friday.setText("You do not work");
                }

                //displaying schedule for saturday
                if (!Objects.equals(sa.getString(0), "0")) {
                    saturday.setText("You work: " + sa.getString(0));
                } else {
                    saturday.setText("You do not work");
                }

                //displaying schedule for sunday
                if (!Objects.equals(su.getString(0), "0")) {
                    sunday.setText("You work: " + su.getString(0));
                } else {
                    sunday.setText("You do not work");
                }
            }while(m.moveToNext()&& tu.moveToNext() &&w.moveToNext()&&th.moveToNext() && f.moveToNext() && sa.moveToNext() && su.moveToNext());
        }

        seeCoworkers();

        goBack();

    }

    public void seeCoworkers(){
        employeeID = getIntent().getStringExtra("EMPLOYEEID");
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Monday");
                startActivity(intent);
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Tuesday");
                startActivity(intent);
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Wednesday");
                startActivity(intent);
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Thursday");
                startActivity(intent);
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Friday");
                startActivity(intent);
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Saturday");
                startActivity(intent);
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewSchedule.this, SeeCoworkers.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                intent.putExtra("DAY", "Sunday");
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

    public void goBack() {
        employeeID = getIntent().getStringExtra("EMPLOYEEID");
        Button back = findViewById(R.id.viewScheduleBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewSchedule.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }
}
