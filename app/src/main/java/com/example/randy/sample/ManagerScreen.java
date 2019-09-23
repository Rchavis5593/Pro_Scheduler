package com.example.randy.sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ManagerScreen extends AppCompatActivity {
    Button homeManagerScreen, seeNotifications;
    DatabaseHelper myDB;
    Cursor name, tasks;
    TextView welcomeName;
    Intent intent;
    Date date, comparisonDate;
    Calendar today;
    long daysDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_screen);
        myDB = DatabaseHelper.getInstance(this);
        goBack();
        seeWorkLogs();
        assignRoles();
        assignTasks();
        seeNotifications();
        checkIfTasksAreDone();
        displayManagerUnreadNotifications(myDB); //letting manager know how many unread notifications they have
        String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        name = myDB.getName(employeeID);
        welcomeName = findViewById(R.id.welcomeNameTextView);


        if(name != null && name.moveToFirst()){
            do {
                welcomeName.setText("Welcome, " + name.getString(0));
            }while(name.moveToNext());
        }


    }

    public void AddRemoveEmployee(View v){
        startActivity(new Intent(ManagerScreen.this,AddEmployee.class));
    }

    public void editSchedule(View v){
        startActivity(new Intent(ManagerScreen.this, ScheduleEdit.class));
    }

//    public void dayOffRequests(View v){
//        startActivity(new Intent(this, DayOffRequests.class));
//    }

    public void goBack(){
        homeManagerScreen = findViewById(R.id.managerScreenBack);

        homeManagerScreen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ManagerScreen.this, LogInScreen.class);
                startActivity(intent);

            }
        });
    }

    public void seeWorkLogs(){
        final Button workLogs = findViewById(R.id.workLogsButton);

        workLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerScreen.this, LogDecision.class);
                startActivity(intent);
            }
        });
    }

    public void assignRoles(){
        final Button assignRoles = findViewById(R.id.assignRolesButton);

        assignRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerScreen.this, AssignRoles.class);
                startActivity(intent);
            }
        });
    }

    public void assignTasks(){
        final Button assignTasks = findViewById(R.id.managerScreenAssignTasks);

        assignTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerScreen.this, AssignTasks.class);
                startActivity(intent);
            }
        });


    }

    public void displayManagerUnreadNotifications(DatabaseHelper myDB){
        Button unreadNotifications = findViewById(R.id.unreadManagerNotifications);

        int unreadNotificationsCount = myDB.getRequestsCountForManager(); //gets number of requests that have not been answered from database

        String num = Objects.toString(unreadNotificationsCount);

      unreadNotifications.setText("You have " + num + " notifications!");


    }

    public void seeNotifications(){
        seeNotifications = findViewById(R.id.unreadManagerNotifications);

        seeNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ManagerScreen.this, ManagerNotifications.class);
                startActivity(intent);

            }
        });

    }

    public void checkIfTasksAreDone() {
        tasks = myDB.getAllTasksWithInfo(); //get all task info from database
        today = Calendar.getInstance();
       // today.add(Calendar.DAY_OF_YEAR, 2); //TEST TO PROVE THE METHOD WORKS
        comparisonDate = today.getTime();

        if (tasks != null && tasks.moveToFirst()) {
            do {
                try {
                    date = new SimpleDateFormat("MM/dd/yyyy").parse(tasks.getString(5)); //getting task date from database and turning it into date object
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            } while (tasks.moveToNext());
        }



        if(tasks != null && tasks.moveToFirst()){
            do{
            daysDifference = date.getTime() - comparisonDate.getTime();
           long daysDifferenceToCompare = TimeUnit.DAYS.convert(daysDifference, TimeUnit.MILLISECONDS); //converting to whole number
            if(daysDifferenceToCompare < 0 && tasks.getInt(8) == 0){ //0 not finished, 1 finished //if the task due date is passed todays date and it is not finished
                myDB.updateTaskNotification(tasks.getString(2), "'" + tasks.getString(4) + "'",
                        "'"+tasks.getString(6)+"'", "'" + tasks.getString(1) + "'", 2, 2); //why isnt this working
            }
            }while(tasks.moveToNext());
        }

    }


    public void displayMessage(String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }



}


