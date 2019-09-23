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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class ManagerNotifications extends AppCompatActivity {
    DatabaseHelper myDB;
    ListView managerNotificationsList;
    ArrayList managerNotificationsArray;
    ArrayAdapter managerNotificationsAdapter;
    Cursor requests;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_notifications);

        myDB = DatabaseHelper.getInstance(this);

        managerNotificationsList = findViewById(R.id.managerNotificationsListView);

        populateNotifications(myDB, managerNotificationsList);
        actOnNotifications(myDB, managerNotificationsList);
    }

    public void populateNotifications(DatabaseHelper myDB, ListView list){
        managerNotificationsArray = new ArrayList();
        managerNotificationsAdapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, managerNotificationsArray);
        list.setAdapter(managerNotificationsAdapter);

        requests = myDB.getRequests(); //getting requests from employees from database

        if (requests != null && requests.moveToFirst()){
            do{

                    managerNotificationsArray.add("Leave request for " + requests.getString(1) + " on " + requests.getString(2));
                    managerNotificationsAdapter.notifyDataSetChanged();

            }while(requests.moveToNext());
        }

        Cursor tasks = myDB.getAllTasksWithInfo();

        if(tasks != null && tasks.moveToFirst()){
            do{
                if(tasks.getInt(8) == 2){ //if task is not finished
                    managerNotificationsArray.add(tasks.getString(2) + " did not complete " + tasks.getString(1) + " on " + tasks.getString(4)
                    + " ( " + tasks.getString(5) + " ) " + "during the shift: " + tasks.getString(6));
                }
            }while(tasks.moveToNext());
        }




    }

    public void actOnNotifications(final DatabaseHelper myDB, final ListView list){
        final AlertDialog.Builder answerRequest = new AlertDialog.Builder(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = list.getItemAtPosition(position); //gettting the item that was clicked

                final String[] split = object.toString().split(" "); //splitting text in list view at ' " " '


                if(Objects.equals(split[0], "Leave")){ //if the notificaiton is a day off request
                    requests = myDB.getRequests(); //getting all current requests that have not been answered

                    answerRequest.setTitle("Answer Request");
                    answerRequest.setMessage("Give this employee the selected day off?");

                    answerRequest.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDB.updateRequest(1, split[3], "'" + split[5] + "'", 1); //1 sends yes to the employee

                            intent = new Intent(ManagerNotifications.this, ManagerNotifications.class); //refreshes page so the most recently answered one will not be present in the list
                            startActivity(intent);

                        }
                    });

                    answerRequest.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDB.updateRequest(2, split[3], "'" + split[5] + "'", 1); //2 sends no to the employee

                            intent = new Intent(ManagerNotifications.this, ManagerNotifications.class); //refreshes page so the most recently answered one will not be present in the list
                            startActivity(intent);
                        }
                    });

                    answerRequest.show();
                }

                if(Objects.equals(split[1], "did")){ //if the notification is a task notification
                    answerRequest.setTitle("Verification");
                    answerRequest.setMessage(split[0] + " did not complete " + split[4] + " on " + split[6] + " during the shift: " + split[13]);

                    answerRequest.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDB.updateTaskNotification(split[0], "'" + split[6] + "'", "'" + split[13] + "'", "'"+ split[4]+ "'", 0, 1);

                            intent = new Intent(ManagerNotifications.this, ManagerNotifications.class);
                            startActivity(intent); //refreshing page
                        }
                    });

                    answerRequest.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { //closes dialog box

                        }
                    });

                    answerRequest.show();
                }





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
