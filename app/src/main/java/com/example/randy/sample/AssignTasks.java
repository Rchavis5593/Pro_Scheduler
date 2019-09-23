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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;

public class AssignTasks extends AppCompatActivity {

    DatabaseHelper myDB;
    ListView list;
    String taskToEdit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_tasks);
        final ListView list = findViewById(R.id.assignTasksListView);
        final ArrayList array = new ArrayList();
        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, array);
        list.setAdapter(adapter);

        myDB = DatabaseHelper.getInstance(this);

        Cursor allTasks = myDB.getAllTasks(); //getting all tasks from databse

        if(allTasks != null && allTasks.moveToFirst()){
            do{
                array.add(allTasks.getString(0)); //adding all tasks to taskArray
                adapter.notifyDataSetChanged();
            }while (allTasks.moveToNext());
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object task = list.getItemAtPosition(position);
                chooseDay(task);
            }
        });

        ListView existingTasksList = findViewById(R.id.existingTasksListView);
        ArrayList existingTasksArray = new ArrayList();
        ArrayAdapter existingTasksAdaper = new ArrayAdapter(getApplicationContext(), R.layout.long_text_list_view, existingTasksArray);
        existingTasksList.setAdapter(existingTasksAdaper);

        Cursor tasksWithinfo = myDB.getTasksWithInfo();

        if(tasksWithinfo != null && tasksWithinfo.moveToFirst()){
            do{
                existingTasksArray.add("Name: " + tasksWithinfo.getString(2) + " ID: " + tasksWithinfo.getString(1) + " Task: " + tasksWithinfo.getString(0) + " Day: " + tasksWithinfo.getString(3) + " Shift: " + tasksWithinfo.getString(4));
                existingTasksAdaper.notifyDataSetChanged();
            }while (tasksWithinfo.moveToNext());
        }



        assignNewTask();
        goBack();
    }

    public void assignNewTask(){
        Button assignTask = findViewById(R.id.assignNewTaskButton);
        final EditText input = new EditText(this);

        assignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterTasks(input); //passing the input to enter tasks method
            }
        });

    }

    public void enterTasks(final EditText editText){
        final AlertDialog.Builder roles = new AlertDialog.Builder(this);
        ListView list = findViewById(R.id.assignTasksListView);
        final ArrayList array = new ArrayList();
        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, array);
        list.setAdapter(adapter);
        roles.setCancelable(true);

        roles.setTitle("Add Task");

        roles.setView(editText); //putting edit text inside of a dialog box

        roles.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.insertTasks(editText.getText().toString());
                array.add(editText.getText().toString());
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(AssignTasks.this, AssignTasks.class);
                startActivity(intent);

            }
        });

        roles.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        roles.show();

    }

    public void goBack() {
        Button back = findViewById(R.id.assignTasksBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssignTasks.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }

    public void chooseDay(final Object task){
        list = findViewById(R.id.assignTasksListView);
        final ListView daysList = new ListView(this);
        final EditText newTask = new EditText(this);
        final AlertDialog.Builder changeTaskAlert = new AlertDialog.Builder(this);
        final AlertDialog.Builder deleteTaskAlert = new AlertDialog.Builder(this);


        final String[] daysArray = new String[7];

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, daysArray);
        daysList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


        //LATER ON TRY TO MAKE UNIVERSAL ARRAY FOR THESE DAYS
        daysArray[0] = "Monday";
        daysArray[1] = "Tuesday";
        daysArray[2] = "Wednesday";
        daysArray[3] = "Thursday";
        daysArray[4] = "Friday";
        daysArray[5] = "Saturday";
        daysArray[6] = "Sunday";

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);







        alert.setTitle("Choose Day to Assign Task");
        alert.setView(daysList);

        alert.setPositiveButton("Edit Task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskToEdit = Objects.toString(task); //getting task originally clicked
                changeTaskAlert.setTitle("Replace " + taskToEdit + " with:");
                changeTaskAlert.setView(newTask);

                changeTaskAlert.setPositiveButton("Replace", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB.updateTask("'" + taskToEdit + "'", newTask.getText().toString()); //updating task
                        intent = new Intent(AssignTasks.this, AssignTasks.class);
                        startActivity(intent); //refreshing page to display replaced task
                    }
                });
                changeTaskAlert.show();
            }
        });

        alert.setNegativeButton("Delete Task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskToEdit = Objects.toString(task); //getting the original task selected
                deleteTaskAlert.setTitle("Are you sure you want to delete " + taskToEdit+ "?");
                deleteTaskAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB.deleteTask("'" + taskToEdit+ "'");
                        Toast.makeText(AssignTasks.this, taskToEdit + " was deleted", Toast.LENGTH_LONG).show();
                        intent = new Intent(AssignTasks.this, AssignTasks.class);
                        startActivity(intent);
                    }

                });
                deleteTaskAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                deleteTaskAlert.show();
            }
        });
        alert.show();


        daysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               chooseShift(task, daysList.getItemAtPosition(position));
            }
        });
    }

    public void chooseShift(final Object task, final Object day){
        //LATER ON TRY TO MAKE UNIVERSAL ARRAY FOR SHIFTS
        final String[] shiftArray = new String[3];
        shiftArray[0] = "7am-3pm";
        shiftArray[1] = "3pm-11pm";
        shiftArray[2] = "11pm-7am";

        final ListView shiftList = new ListView(this);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog displayShifts = alert.create();

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, shiftArray);
        shiftList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        displayShifts.setTitle("Select Shift");
        displayShifts.setView(shiftList);
        displayShifts.show();

        shiftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEmployees(day.toString(), shiftList.getItemAtPosition(position).toString(), task.toString());

            }
        });


    }

    public void showEmployees(final String day, final String hours, final String task){
        final ListView employeeList = new ListView(this);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog displayEmployees = alert.create();

        Cursor employees = myDB.getEmployeesOnDay("'" + day + "'", "'" + hours + "'");

        ArrayList employeeArray = new ArrayList();

        String count = Objects.toString(employees.getCount());

        displayMessage(count, count);

        if(employees != null && employees.moveToFirst()){
            do {
                employeeArray.add(employees.getString(0) + " " + employees.getString(1));
            }while(employees.moveToNext());

        }

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, employeeArray);
        employeeList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        displayEmployees.setTitle("Employees Available");
        if(employeeArray.size() < 2){ //making sure there are at least 2 employees on a shift
            displayEmployees.setMessage("Need at least 2 employees on a shift to assign task");
        }else {
            displayEmployees.setView(employeeList);
        }
        displayEmployees.show();

        employeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                insertAndConfirmation(day, hours, task, employeeList.getItemAtPosition(position).toString());
            }
        });
    }

    public void insertAndConfirmation(String day, String hours, String task, String name){

    String[] idAndName = name.split(" ");
    String id = idAndName[0];
    String employeeName = idAndName[1];

    myDB.insertTasksWithInformation(task, id, employeeName, day, hours, 1);

    Intent intent = new Intent(AssignTasks.this, AssignTasks.class);

    startActivity(intent);




    }




    public void displayMessage(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }
}
