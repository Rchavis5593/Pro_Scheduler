package com.example.randy.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ViewTodaysTask extends AppCompatActivity {
    ListView tasksForTodayList;
    DatabaseHelper myDB;
    Cursor tasksAndID, name, password, otherEmployees;
    Date date, now;
    Calendar today;
    String formatedDate, dayOfWeek, shiftSelected;
    ArrayList tasksList, secondaryEmployeeList;
    ArrayAdapter tasksAdapter, shiftAdaper;
    String[] splitTask, itemsForDropdown;
    EditText confirmationPassword;
    Spinner shiftSpinner;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todays_task);

        myDB = DatabaseHelper.getInstance(this);

        tasksForTodayList = findViewById(R.id.viewTodaysTasksListView);





        shiftSpinner = (Spinner) findViewById(R.id.shiftsSpinner);

        actingOnTasks(tasksForTodayList, myDB, shiftSpinner);

        itemsForDropdown = new String[]{"7am-3pm", "3pm-11pm", "11pm-7pm"};
        shiftAdaper = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsForDropdown);

        shiftSpinner.setAdapter(shiftAdaper);

        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiftSelected = Objects.toString(shiftSpinner.getItemAtPosition(position));
                populateTasksForToday(tasksForTodayList, myDB, shiftSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    public void populateTasksForToday(ListView list, DatabaseHelper myDB, String shiftSelected){
        //formatting today's date into a string value to add into database method parameter
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 1);
        date = today.getTime();
        formatedDate = format.format(date);

        tasksList = new ArrayList();
        tasksAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, tasksList);
        list.setAdapter(tasksAdapter);

        tasksAndID = myDB.getTasksForDay("'" + formatedDate + "'", "'" + shiftSelected + "'");


        //populating listView with tasks for today
        if(tasksAndID != null && tasksAndID.moveToFirst()){
            name = myDB.getName(tasksAndID.getString(1));
            do{
                if(name != null && name.moveToFirst()){
                    do{
                        tasksList.add(name.getString(0) + " " + "-" +  " " + tasksAndID.getString(1) + " " + ":" + " "  + tasksAndID.getString(0));
                        tasksAdapter.notifyDataSetChanged();
                    }while (name.moveToNext());
                }

            }while(tasksAndID.moveToNext());
        }
    }

    //method where employees have to confirm tasks assigned to them were completed
    public void actingOnTasks(final ListView list, final DatabaseHelper myDB, Spinner shiftSpinner){
        shiftSelected = Objects.toString(shiftSpinner.getSelectedItem());
        final AlertDialog.Builder confirmTaskBuilder = new AlertDialog.Builder(this);
        confirmTaskBuilder.setCancelable(true);
        confirmTaskBuilder.setTitle("Task Confirmation");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                splitTask = Objects.toString(list.getItemAtPosition(position)).split(" ");
                confirmTaskBuilder.setMessage("Has " + splitTask[4] + " been completed?"); //prompting user if task has been completed

                confirmTaskBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logInToConfirm(splitTask[2], myDB);
                    }
                });

                confirmTaskBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logInToConfirm(splitTask[2], myDB);
                    }
                });



                confirmTaskBuilder.show();
            }
        });


    }

    //method to confirm it is actually the employee in question confirming the task
    public void logInToConfirm(final String employeeID, final DatabaseHelper myDB){
        //pop up dialog for main employee
        final AlertDialog.Builder logInBuilder = new AlertDialog.Builder(this);
        logInBuilder.setCancelable(true);

        //pop up dialog for secondary employee
        final AlertDialog.Builder secondaryLogInBuilder = new AlertDialog.Builder(this);
        secondaryLogInBuilder.setCancelable(true);
        secondaryLogInBuilder.setTitle("Please Get Another Employee To Confirm");
        secondaryEmployeeList = new ArrayList();
        final ListView secondaryEmployeeListView = new ListView(this);
        final ArrayAdapter secondaryEmployeeAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, secondaryEmployeeList);
        secondaryEmployeeListView.setAdapter(secondaryEmployeeAdapter);
        secondaryEmployeeAdapter.notifyDataSetChanged();
        secondaryLogInBuilder.setView(secondaryEmployeeListView);

        now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        dayOfWeek = simpleDateFormat.format(now);


        confirmationPassword = new EditText(this); //editText for password
        confirmationPassword.setHint("Password");

        shiftSelected = shiftSpinner.getSelectedItem().toString();

        logInBuilder.setTitle("Enter Password to Confirm");
        password = myDB.getPassword("'" + employeeID + "'"); //getting password from corresponding employeeID
        logInBuilder.setView(confirmationPassword); //adding editText to dialog

        logInBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(password != null && password.moveToFirst()){
                    do{
                        if(Objects.equals(password.getString(0), confirmationPassword.getText().toString())){
                            otherEmployees = myDB.getOtherEmployeesOnShift(employeeID, "'" + dayOfWeek + "'", "'" + shiftSelected + "'");

                            if(otherEmployees != null && otherEmployees.moveToFirst()){
                                do{
                                    secondaryEmployeeList.add(otherEmployees.getString(0) + " " + otherEmployees.getString(1));
                                    secondaryEmployeeAdapter.notifyDataSetChanged();

                                }while(otherEmployees.moveToNext());
                            }

                            secondaryLogInBuilder.show();

                            secondaryEmployeeConfirmation(secondaryEmployeeListView, employeeID, dayOfWeek , splitTask[4]);
                        }else{
                            displayMessage("Wrong Password", "The wrong password has been entered for ID: " + employeeID);
                        }
                    }while(password.moveToNext());
                }
            }
        });
        logInBuilder.show(); //making logInBuilder visible
    }

    public void secondaryEmployeeConfirmation(final ListView list, final String employeeID, final String dayOfWeek, final String task){
        final AlertDialog.Builder logInBuilder = new AlertDialog.Builder(this);
        logInBuilder.setCancelable(true);

        confirmationPassword = new EditText(this); //editText for password
        confirmationPassword.setHint("Password");

        final String shiftSelected = shiftSpinner.getSelectedItem().toString();

        logInBuilder.setTitle("Enter Password to Confirm");
        logInBuilder.setView(confirmationPassword); //adding editText to dialog

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] secondaryEmployeeSplit = Objects.toString(list.getItemAtPosition(position)).split(" ");
                password = myDB.getPassword("'" + secondaryEmployeeSplit[1] + "'"); //getting password from corresponding employeeID

                logInBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(password != null && password.moveToFirst()){
                            do{
                                if(Objects.equals(password.getString(0), confirmationPassword.getText().toString())){
                                    myDB.updateTaskNotification(employeeID, "'" + dayOfWeek + "'", "'" + shiftSelected + "'", "'" + task + "'", 0, 1);

                                    intent = new Intent(ViewTodaysTask.this, ViewTodaysTask.class);
                                    Toast.makeText(ViewTodaysTask.this, task + " has been completed!", Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                }
                            }while(password.moveToNext());
                        }else{
                            displayMessage("ERROR", "Wrong id entered for " + secondaryEmployeeSplit[1]);
                        }
                    }
                });
                logInBuilder.show();
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
