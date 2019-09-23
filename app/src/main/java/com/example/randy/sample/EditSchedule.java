package com.example.randy.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class EditSchedule extends AppCompatActivity {

    CalendarView calander;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);
        myDb = DatabaseHelper.getInstance(this);
        requestDay();

        goBack();

    }

    public void requestDay() {
        calander = findViewById(R.id.editScheduleCalander);
        final AlertDialog.Builder requestOffConfirmation = new AlertDialog.Builder(this);
        final String EmployeeID = getIntent().getStringExtra("EMPLOYEEID");


        calander.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                final String date = (month + 1) + "/" + dayOfMonth + "/" + year;

                requestOffConfirmation.setTitle("Day off Request");
                requestOffConfirmation.setMessage("Are you sure you would like to request " + date + " off?");

                requestOffConfirmation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        myDb.insertDayOffRequest(EmployeeID, date, 0, 0); //0 if answer has not been given yet
                                                                                                        //employeeSeenNotification stays 0 until employee seens the answer

                    }
                });

                requestOffConfirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                requestOffConfirmation.show();

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
        Button back = findViewById(R.id.editScheduleBack);
        final String EmployeeID = getIntent().getStringExtra("EMPLOYEEID");

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditSchedule.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", EmployeeID);
                startActivity(intent);

            }
        });
    }
}
