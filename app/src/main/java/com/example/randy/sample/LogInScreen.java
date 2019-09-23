package com.example.randy.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class LogInScreen extends AppCompatActivity {

    public DatabaseHelper myDb;
    Button logIn, viewTodaysTasks;
    EditText logInId, logInPassword;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        logInId = findViewById(R.id.logInID);
        logInPassword = findViewById(R.id.logInPassword);
        myDb = DatabaseHelper.getInstance(this);
        logIn();
        viewTodaysTasks();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy"); //making a correct format for date

        Cursor nextPayday = myDb.getNextPayDate("'" + "0000" + "'");//getting global variable nextPayDate from database
        Cursor allIDs = myDb.getIdName();
        Cursor payInformation = myDb.getPayInformation();

        Calendar calendar = Calendar.getInstance();//getting instance of calendar

        String date = format.format(calendar.getTime());//formatting date to correct format

        ArrayList IdArray = new ArrayList();

        String npd = " ";


        if(allIDs != null && allIDs.moveToFirst()){
            do{
                IdArray.add(allIDs.getString(0));
            }while(allIDs.moveToNext());
        }


        if(nextPayday != null && nextPayday.moveToFirst()){
            do{
                npd = nextPayday.getString(0);
            }while(nextPayday.moveToNext());
        }

                    if(Objects.equals(date, npd)){ //if todays date is the same as the one in the database
                        Calendar calendar1 = Calendar.getInstance();
                        String todaysDate = format.format(calendar1.getTime());
                        int count = payInformation.getCount() -1;

                        if(payInformation != null && payInformation.moveToFirst()){
                            do{
                                myDb.insertPayInformation(payInformation.getString(0), payInformation.getString(1), payInformation.getString(2),todaysDate); //inserting pay information for the week

                                myDb.updateWeeksPayAndHours(payInformation.getString(0), 0, 0); //resetting the values to begin a new pay week

                                calendar1.add(Calendar.DATE, 7); //adding 7 days to the current day

                                String nextPay = format.format(calendar1.getTime());

                                 myDb.updateNextPayDay("'" + "0000" + "'" , nextPay); //setting the next pay day a week from now

                                    count--;
                            }while (payInformation.moveToNext() && count >= 0);

                        }

                    }






    }

    public void logIn() {
        logIn = findViewById(R.id.logInButton);






        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = Objects.toString(logInId.getHint());
                String passHint = Objects.toString(logInPassword.getHint());


                if (Objects.equals(hint, "Employee ID") && logInId.getText().length() == 0) {
                    displayMessage("ERROR", "PLEASE ENTER AN EMPLOYEE ID");
                } else if(Objects.equals(passHint, "Password")&& logInPassword.getText().length() == 0){
                    displayMessage("ERROR", "PLEASE ENTER PASSWORD");
                }
                else {

                    Cursor passwordResult = myDb.getPassword(logInId.getText().toString());
                    Cursor roleResult = myDb.getRole(logInId.getText().toString());
                    if (passwordResult != null && passwordResult.moveToFirst() && roleResult != null && roleResult.moveToFirst()) {
                        do {
                            if (Objects.equals(passwordResult.getString(0), logInPassword.getText().toString()) && Objects.equals(roleResult.getString(0).toUpperCase(), "MANAGER")) {
                                Intent intent = new Intent(LogInScreen.this, ManagerScreen.class);
                                intent.putExtra("EMPLOYEEID", logInId.getText().toString());
                                startActivity(intent);
                            } else if (Objects.equals(passwordResult.getString(0), logInPassword.getText().toString()) && Objects.equals(roleResult.getString(0).toUpperCase(), "MANAGER") != true) {
                                Intent intent = new Intent(LogInScreen.this, MainActivity.class);
                                intent.putExtra("EMPLOYEEID", logInId.getText().toString());
                                startActivity(intent);
                            } else {
                                displayMessage("ERROR", "Wrong Id or Password Entered");
                            }
                        } while (passwordResult.moveToNext() && roleResult.moveToNext());
                    }
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

    public void viewTodaysTasks(){
        viewTodaysTasks = findViewById(R.id.viewTodaysTasksButton);

        viewTodaysTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LogInScreen.this, ViewTodaysTask.class);
                startActivity(intent);
            }
        });

    }

}
