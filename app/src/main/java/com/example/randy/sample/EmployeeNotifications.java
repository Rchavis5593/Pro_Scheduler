package com.example.randy.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class EmployeeNotifications extends AppCompatActivity {
    String employeeID;
    Cursor answer, tasks;
    int approved, denied;
    ArrayList notificationList;
    ArrayAdapter notificationAdapter;
    ListView notificationListView;
    DatabaseHelper myDB;
    Button viewTasksButton;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_notifications);
        employeeID = getIntent().getStringExtra("EMPLOYEEID");
        notificationListView = findViewById(R.id.employeeNotificationsListView);
        myDB = DatabaseHelper.getInstance(this);

        populateEmployeeNotifications(employeeID, notificationListView, myDB);

        confirmViewing(employeeID, notificationListView, myDB);

        goBack();
        viewTasks();
    }

    public void populateEmployeeNotifications(String employeeID, ListView list, DatabaseHelper myDB){
        answer = myDB.getAnswer(employeeID);
        tasks = myDB.getEmployeeTasknotification(employeeID);
        approved = 1;
        denied = 2;
        notificationList = new ArrayList();
        notificationAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, notificationList);
        list.setAdapter(notificationAdapter);

        //cursor at position 1 is answer, at position 0 is day
        while (answer.moveToNext()) {
            if (Objects.equals(answer.getInt(1), approved)) {

                    notificationList.add("Manager approved your leave request for: " + answer.getString(0));
                    notificationAdapter.notifyDataSetChanged();
            } else if (Objects.equals(answer.getInt(1), denied)) {

                    notificationList.add("Manager denied your leave request for: " + answer.getString(0));
                    notificationAdapter.notifyDataSetChanged();
            }
        }

        while(tasks.moveToNext()){
            notificationList.add("Manager has assigned you the task of: " + tasks.getString(0)
                    + "on the day " + tasks.getString(1)+ " on the shift " + tasks.getString(2));
            notificationAdapter.notifyDataSetChanged();
        }


    }

    public void confirmViewing(final String employeeID, final ListView list, final DatabaseHelper myDB){
        final AlertDialog.Builder attentionPopUp = new AlertDialog.Builder(this);
        attentionPopUp.setCancelable(true);
        attentionPopUp.setTitle("ATTENTION");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String splitMessage[] = list.getItemAtPosition(position).toString().split(" ");
                attentionPopUp.setMessage(list.getItemAtPosition(position).toString());

                attentionPopUp.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(Objects.equals(splitMessage[1], "approved")) { //if the item that is clicked is for a day off request
                           myDB.updateRequest(3, employeeID, "'" + splitMessage[6] + "'", 4);
                        }
                        if(Objects.equals(splitMessage[1], "has")){ //if the item that is clicked is for a task assignment
                            myDB.updateTaskNotification(employeeID, "'" + splitMessage[11] + "'", "'" + splitMessage[15] + "'", "'" + splitMessage[7]+ "'", 3, 0); //employeeid, day, time, task
                        }

                        Intent intent = new Intent(EmployeeNotifications.this, EmployeeNotifications.class);
                        intent.putExtra("EMPLOYEEID", employeeID);
                        startActivity(intent);
                    }
                });
                attentionPopUp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                attentionPopUp.show();
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
        Button back = findViewById(R.id.employeeNotificationsBackButton);
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeNotifications.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }

    public void viewTasks(){
        viewTasksButton = findViewById(R.id.viewTasksButton);
        employeeID = getIntent().getStringExtra("EMPLOYEEID");

        viewTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeNotifications.this, ViewEmployeeTasks.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);
            }
        });
    }

}
