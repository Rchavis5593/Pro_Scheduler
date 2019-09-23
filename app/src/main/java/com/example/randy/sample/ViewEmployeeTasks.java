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

public class ViewEmployeeTasks extends AppCompatActivity {
    String employeeID;
    DatabaseHelper myDB;
    ListView daysTaskList, timesTaskList, tasksList;
    Cursor tasksInfo;
    ArrayList daysArray, timeArray, tasksArray;
    ArrayAdapter daysAdapter, timeAdapter, tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee_tasks);

        myDB = DatabaseHelper.getInstance(this);
        employeeID = getIntent().getStringExtra("EMPLOYEEID");

        //FILLING DAYS LIST
        tasksInfo = myDB.getTasksForEmployee(employeeID);
        daysTaskList = findViewById(R.id.dayForTasksListView);
        daysArray = new ArrayList();
        daysAdapter = new ArrayAdapter(getApplication(), R.layout.customlistview, daysArray);
        daysTaskList.setAdapter(daysAdapter);

        daysArray.add("Time");
        daysAdapter.notifyDataSetChanged();


        if(tasksInfo != null && tasksInfo.moveToFirst()){
        do{
            daysArray.add(tasksInfo.getString(2));
            daysAdapter.notifyDataSetChanged();
        }while (tasksInfo.moveToNext());
   }
   //FILLING DAYS LIST

        // FILLING TIME LIST

        timesTaskList = findViewById(R.id.timeForTaskListView);
        timeArray = new ArrayList();
        timeAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, timeArray);
        timesTaskList.setAdapter(timeAdapter);

        timeArray.add("Time");
        timeAdapter.notifyDataSetChanged();

        if(tasksInfo != null && tasksInfo.moveToFirst()){
            do{
                timeArray.add(tasksInfo.getString(3));
                timeAdapter.notifyDataSetChanged();
            }while(tasksInfo.moveToNext());
        }
        //FILLING TIME LIST

        //FILLING TASK LIST

        tasksList = findViewById(R.id.tasksListView);
        tasksArray = new ArrayList();
        tasksAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, tasksArray);
        tasksList.setAdapter(tasksAdapter);

        tasksArray.add("Task");
        tasksAdapter.notifyDataSetChanged();

        if(tasksInfo != null && tasksInfo.moveToFirst()){
            do{
                tasksArray.add(tasksInfo.getString(0));
                tasksAdapter.notifyDataSetChanged();
            }while(tasksInfo.moveToNext());
        }







    }

//    public void populateTasks(DatabaseHelper myDB, ListView list, String employeeID){
//        adapter = new ArrayAdapter(getApplication(), R.layout.customlistview, taskArray);
//        list.setAdapter(adapter);
//    headerRow = "Day         Time         Task"; //making header row
//        taskArray.add(headerRow);
//        adapter.notifyDataSetChanged();
//
//    tasksWithInfo = myDB.getTasksForEmployee(employeeID); //getting day, task, time from database
//
//        if(tasksWithInfo != null && tasksWithInfo.moveToFirst()){
//        do{
//            taskArray.add(tasksWithInfo.getString(2) + "     " + tasksWithInfo.getString(3) + "      " + tasksWithInfo.getString(0));
//            adapter.notifyDataSetChanged();
//        }while (tasksWithInfo.moveToNext());
//    }





    public void goBack() {
        Button back = findViewById(R.id.viewEmployeeTasksBackButton);
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewEmployeeTasks.this, EmployeeNotifications.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }

}
