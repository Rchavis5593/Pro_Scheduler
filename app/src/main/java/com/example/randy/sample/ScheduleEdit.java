package com.example.randy.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class ScheduleEdit extends AppCompatActivity {

    DatabaseHelper myDb;

    Button addRemove, back;

    TableLayout scheduleTable;
    TableRow space;
    TableRow monday;
    TableRow tuesday;
    TableRow wednesday;
    TableRow thursday;
    TableRow friday;
    TableRow saturday;
    TableRow sunday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        myDb = DatabaseHelper.getInstance(this);
        scheduleTable = findViewById(R.id.tableLayout);



        changeSchedule();
        backScheduleEdit();
        tableSetUp(scheduleTable);

    }

    public void changeSchedule(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog changeSchedule = alert.create();
        addRemove = findViewById(R.id.addRemoveScheduleEdit);
        final ListView daylist = new ListView(this);
        final ArrayList dayList = new ArrayList();
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thursday");
        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, dayList);
        daylist.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        addRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeSchedule.setTitle("Change Schedule");
                changeSchedule.setView(daylist);


                changeSchedule.show();




                daylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object day = daylist.getItemAtPosition(position);

                        editSchedule(day);
                    }
                });
            }
        });
    }
    public void editSchedule(final Object day){
        final ListView employeeList = new ListView(this);
        ArrayList nameArray = new ArrayList();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
       final AlertDialog displayEmployees = alert.create();

        Cursor employees = myDb.getIdName();

        int employeeCount = employees.getCount();
        if(employees!=null && employees.moveToFirst()){
            do{

                    nameArray.add(employees.getString(0) + " " + employees.getString(1));

                employeeCount--;
            }while(employees.moveToNext()&& employeeCount>=0);
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, nameArray);

        employeeList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        displayEmployees.setTitle("Who to add to the schedule for this day?");
        displayEmployees.setView(employeeList);
        displayEmployees.show();


        employeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object nameAndId = employeeList.getItemAtPosition(position);

               assignHours(day, nameAndId);
            }
        });
    }

    public void assignHours(final Object day, final Object nameAndId){
        final ListView hoursList = new ListView(this);
        ArrayList hoursArray = new ArrayList();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog displayHours = alert.create();

        hoursArray.add("7am-3pm");
        hoursArray.add("3pm-11pm");
        hoursArray.add("11pm-7pm");

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, hoursArray);

        hoursList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        displayHours.setTitle("Select hours for employee");
        displayHours.setView(hoursList);
        displayHours.show();

        hoursList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object hours = hoursList.getItemAtPosition(position);

                String days = Objects.toString(day);
                String hour = Objects.toString(hours);

                final String[] split = nameAndId.toString().split(" ");

                myDb.updateSchedule(split[1], "'" + days + "'", hour);

                Intent intent = new Intent(ScheduleEdit.this, ScheduleEdit.class);

                startActivity(intent);




            }
        });
    }

    public void backScheduleEdit(){
        back = findViewById(R.id.backScheduleEdit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleEdit.this, ManagerScreen.class);

                startActivity(intent);
            }
        });

    }

    public void tableSetUp(TableLayout tableLayout){

        //making borders for elements
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.FILL);
        border.getPaint().setColor(Color.parseColor("#e1d176"));

        //making the table rows
        space = new TableRow(this);
        space.setPadding(20,20,20,20);
        monday = new TableRow(this);
        monday.setPadding(20,20,20,20);
        tuesday = new TableRow(this);
        tuesday.setPadding(20,20,20,20);
        wednesday= new TableRow(this);
        wednesday.setPadding(20,20,20,20);
        thursday = new TableRow(this);
        thursday.setPadding(20,20,20,20);
        friday = new TableRow(this);
        friday.setPadding(20,20,20,20);
        saturday = new TableRow(this);
        saturday.setPadding(20,20,20,20);
        sunday = new TableRow(this);
        sunday.setPadding(20,20,20,20);



        //making text views to put into tableRows
        TextView s = new TextView(this);
        s.setText(" ");
        TextView m = new TextView(this);
        m.setText("Monday");
        m.setTextColor(getResources().getColor(R.color.mainButton));
        m.setBackground(border);
        TextView tu = new TextView(this);
        tu.setText("Tuesday");
        tu.setTextColor(getResources().getColor(R.color.mainButton));
        tu.setBackground(border);
        TextView w = new TextView(this);
        w.setText("Wednesday");
        w.setTextColor(getResources().getColor(R.color.mainButton));
        w.setBackground(border);
        TextView th = new TextView(this);
        th.setText("Thursday");
        th.setTextColor(getResources().getColor(R.color.mainButton));
        th.setBackground(border);
        TextView f = new TextView(this);
        f.setText("Friday");
        f.setTextColor(getResources().getColor(R.color.mainButton));
        f.setBackground(border);
        TextView sat = new TextView(this);
        sat.setText("Saturday");
        sat.setTextColor(getResources().getColor(R.color.mainButton));
        sat.setBackground(border);
        TextView sun = new TextView(this);
        sun.setText("Sunday");
        sun.setTextColor(getResources().getColor(R.color.mainButton));
        sun.setBackground(border);

        //adding the text to the rows
        space.addView(s);
        monday.addView(m);
        tuesday.addView(tu);
        wednesday.addView(w);
        thursday.addView(th);
        friday.addView(f);
        saturday.addView(sat);
        sunday.addView(sun);

        //adding the rows to the tableLayout
        tableLayout.addView(space);
        tableLayout.addView(monday);
        tableLayout.addView(tuesday);
        tableLayout.addView(wednesday);
        tableLayout.addView(thursday);
        tableLayout.addView(friday);
        tableLayout.addView(saturday);
        tableLayout.addView(sunday);

        addNamesAndID(space);
        addTimes(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    public void addNamesAndID(TableRow tableRow){
        Cursor nameAndId = myDb.getIdName();
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.FILL);
        border.getPaint().setColor(Color.parseColor("#e1d176"));

        int i = nameAndId.getCount();
        if(nameAndId!=null && nameAndId.moveToFirst()){
            do{

                TextView textView = new TextView(this);
                textView.setText(nameAndId.getString(0) + " " + nameAndId.getString(1)); // adds all employes except for admin
                textView.setBackground(border); //coloring background
                tableRow.addView(textView); //adding it to table
                i--;
            }while (nameAndId.moveToNext() && i >=0);
        }

    }

    public void addTimes(TableRow monday, TableRow tuesday, TableRow wednesday, TableRow thursday, TableRow friday, TableRow saturday,TableRow sunday){
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.FILL);
        border.getPaint().setColor(Color.parseColor("#623cea"));

        ShapeDrawable solidColor = new ShapeDrawable(new RectShape());
        solidColor.getPaint().setColor(Color.parseColor("#c0b283"));


        Cursor m = myDb.getTimes("'" + "Monday" + "'");
        Cursor tu = myDb.getTimes("'" + "Tuesday" + "'");
        Cursor w = myDb.getTimes("'" + "Wednesday" + "'");
        Cursor th = myDb.getTimes("'" + "Thursday" + "'");
        Cursor f = myDb.getTimes("'" + "Friday" + "'");
        Cursor sat = myDb.getTimes("'" + "Saturday" + "'");
        Cursor sun = myDb.getTimes("'" + "Sunday" + "'");

        int countMonday = m.getCount();
        if(m!=null && m.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(m.getString(0), "0")){
                    textView.setBackground(solidColor);
                    monday.addView(textView);
                    countMonday--;
                }else {
                    textView.setText(m.getString(0));
                    textView.setBackground(border);
                    monday.addView(textView);
                    countMonday--;
                }
            }while(m.moveToNext()&& countMonday>=0);
        }

        int countTuesday = tu.getCount();
        if(tu!=null && tu.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(tu.getString(0), "0")){
                    textView.setBackground(solidColor);
                    tuesday.addView(textView);
                    countTuesday--;
                }else {
                    textView.setText(tu.getString(0));
                    textView.setBackground(border);
                    tuesday.addView(textView);
                    countTuesday--;
                }
            }while(tu.moveToNext()&& countTuesday>=0);
        }

        int countWednesday = w.getCount();
        if(w!=null && w.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(w.getString(0), "0")){
                    textView.setBackground(solidColor);
                    wednesday.addView(textView);
                    countWednesday--;
                }else {
                    textView.setText(w.getString(0));
                    textView.setBackground(border);
                    wednesday.addView(textView);
                    countWednesday--;
                }
            }while(w.moveToNext()&& countWednesday>=0);
        }

        int countThursday = th.getCount();
        if(th!=null && th.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(th.getString(0), "0")){
                    textView.setBackground(solidColor);
                    thursday.addView(textView);
                    countThursday--;
                }else {
                    textView.setText(th.getString(0));
                    textView.setBackground(border);
                    thursday.addView(textView);
                    countThursday--;
                }
            }while(th.moveToNext()&& countThursday>=0);
        }

        int countFriday = f.getCount();
        if(f!=null && f.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(f.getString(0), "0")){
                    textView.setBackground(solidColor);
                    friday.addView(textView);
                    countFriday--;
                }else {
                    textView.setText(f.getString(0));
                    textView.setBackground(border);
                    friday.addView(textView);
                    countFriday--;
                }
            }while(f.moveToNext()&& countFriday>=0);
        }

        int countSaturday = sat.getCount();
        if(sat!=null && sat.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(sat.getString(0), "0")){
                    textView.setBackground(solidColor);
                    saturday.addView(textView);
                    countSaturday--;
                }else {
                    textView.setText(sat.getString(0));
                    textView.setBackground(border);
                    saturday.addView(textView);
                    countSaturday--;
                }
            }while(sat.moveToNext()&& countSaturday>=0);
        }

        int countSunday = sun.getCount();
        if(sun!=null && sun.moveToFirst()){
            do{
                TextView textView = new TextView(this);
                if(Objects.equals(sun.getString(0), "0")){
                    textView.setBackground(solidColor);
                    sunday.addView(textView);
                    countSunday--;
                }else {

                    textView.setText(sun.getString(0));

                    textView.setBackground(border);
                    sunday.addView(textView);
                    countSunday--;
                }

            }while(sun.moveToNext()&& countSunday>=0);
        }

    }


    public void displayMessage(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }
}
