package com.example.randy.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workstation.db";
    private static final String TABLE_NAME = "workstationTable";
    private static final String TIMETABLE = "timeWorkedTable";
    private static final String DAYOFFREQUESTS = "dayOffRequestsTable";
    private static final String SCHEDULETABLE = "scheduleTable";
    private static final String EMPLOYEEHOURS = "employeeHoursTable";
    private static final String PAYMENTINFORMATION = "paymentTable";
    private static final String ROLES = "rolesTable";
    private static final String TASKS_TABLE = "tasksTable";
    private static final String TASKS_WITH_INFO_TABLE = "tasksWithInfoTable";
    private static DatabaseHelper instance;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT,ADDRESS TEXT,ROLE TEXT, EMPLOYEEID TEXT, STARTDATE TEXT, STARTPAY INTEGER, PASSWORD TEXT, PAYTHISWEEK DOUBLE, WEEKTIMEWORKED DOUBLE)");
        db.execSQL("create table " + TIMETABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMPLOYEEID TEXT,DAYWORKED DATE,TIMEWORKED DOUBLE)");
        db.execSQL("create table " + DAYOFFREQUESTS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMPLOYEEID TEXT,DAY TEXT,ANSWER INTEGER, SEENBYNOTIFICATION INTEGER)");
        db.execSQL("create table " + SCHEDULETABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMPLOYEEID TEXT, NAME TEXT, DAY TEXT,HOURS TEXT)");
        db.execSQL("create table " + EMPLOYEEHOURS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMPLOYEEID TEXT, NAME TEXT, DAY INTEGER,CLOCKINTIME INTEGER,CLOCKOUTTIME INTEGER, HOURSFORDAY INTEGER)");
        db.execSQL("create table " + PAYMENTINFORMATION + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMPLOYEEID TEXT, HOURSWORKED TEXT, WEEKSPAY TEXT, NEXTPAYDAY TEXT, DATE TEXT)");
        db.execSQL("create table " + ROLES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ROLES TEXT)");
        db.execSQL("create table " + TASKS_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT)");
        db.execSQL("create table " + TASKS_WITH_INFO_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASKS TEXT, EMPLOYEEID TEXT, NAME TEXT, DAY TEXT, TASKDATE TEXT, TIME TEXT, SEENBYEMPLOYEE INTEGER, ISTASKCOMPLETE INTEGER)");

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, 7);
        String employeeID = "1234";

        String nextPayDay = format.format(calendar.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("EMPLOYEEID", "0000");
        contentValues.put("NEXTPAYDAY", nextPayDay);
         db.insert(PAYMENTINFORMATION, null, contentValues);

         ContentValues tableNameContentValues = new ContentValues();

         tableNameContentValues.put("NAME", "Administrator");
         tableNameContentValues.put("EMPLOYEEID", employeeID);
         tableNameContentValues.put("ROLE", "manager");
         tableNameContentValues.put("PASSWORD", "1234");
         db.insert(TABLE_NAME, null, tableNameContentValues);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());

        }
        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DAYOFFREQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + SCHEDULETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EMPLOYEEHOURS);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENTINFORMATION);
        db.execSQL("DROP TABLE IF EXISTS " + ROLES);
        onCreate(db);
    }

    public boolean insertEmployee(String name, String address, String role, String employeeID, String password, String startPay) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("ADDRESS", address);
        contentValues.put("ROLE", role);
        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("STARTDATE", formatter.format(date));
        contentValues.put("ROLE", role);
        contentValues.put("PASSWORD", password);
        contentValues.put("STARTPAY", startPay);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertTimeWorked(String employeeID, Double timeWorked) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("DAYWORKED", formatter.format(date));
        contentValues.put("TIMEWORKED", timeWorked);

        long result = db.insert(TIMETABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertDayOffRequest(String employeeID, String day, int answer, int employeeSeenNotification) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("DAY", day);
        contentValues.put("ANSWER", answer);
        contentValues.put("SEENBYNOTIFICATION", employeeSeenNotification);

        long result = db.insert(DAYOFFREQUESTS, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insertSchedule(String employeeID, String day, String hours, String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("NAME", name);
        contentValues.put("DAY", day);
        contentValues.put("HOURS", hours);

        long result = db.insert(SCHEDULETABLE, null, contentValues);

        if(result ==-1){
            return  false;
        }else {
            return true;
        }
    }

    public boolean insertWeeksPay(String employeeID, String timeWorked, String payAmount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("HOURSWORKED", timeWorked);
        contentValues.put("WEEKSPAY", payAmount);

        long result = db.insert(PAYMENTINFORMATION, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

   public boolean insertClockInAndClockOut(String employeeID, String name, String date, String clockInTime, String clockOutTime, String timeWorked){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("NAME", name);
        contentValues.put("DAY", date);
        contentValues.put("CLOCKINTIME", clockInTime);
        contentValues.put("CLOCKOUTTIME", clockOutTime);
        contentValues.put("HOURSFORDAY", timeWorked);

        long result = db.insert(EMPLOYEEHOURS, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

     public boolean insertPayInformation(String employeeID, String hoursWorked, String weeksPay, String date){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("HOURSWORKED", hoursWorked);
        contentValues.put("WEEKSPAY", weeksPay);
        contentValues.put("DATE", date);

        long result = db.insert(PAYMENTINFORMATION, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }



     }

     public boolean insertRole(String role){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("ROLES", role);
        long result = db.insert(ROLES, null, contentValues);

        if(result ==-1){
            return false;
        }else{
            return true;
        }

     }

     public boolean insertTasks(String task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("TASK", task);

        long result = db.insert(TASKS_TABLE, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return  true;
        }
     }

     public boolean insertTasksWithInformation(String task, String employeeID, String name, String day, String time, int seenByEmployee){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        int dayOfWeek = 0;
        //calculating date of assigned task
         if(Objects.equals(day, "Monday")){
             dayOfWeek = 2;
         }else if(Objects.equals(day, "Tuesday")){
             dayOfWeek = 3;
         }else if(Objects.equals(day, "Wednesday")){
             dayOfWeek = 4;
         }else if(Objects.equals(day, "Thursday")){
             dayOfWeek = 5;
         }else if(Objects.equals(day, "Friday")){
             dayOfWeek = 6;
         }else if(Objects.equals(day, "Saturday")){
             dayOfWeek = 7;
         }else if(Objects.equals(day, "Sunday")){
             dayOfWeek = 1;
         }


         Calendar today = Calendar.getInstance();
         int currentWeekDay = today.get(Calendar.DAY_OF_WEEK);
         int daysBetween = (Calendar.SATURDAY - currentWeekDay + dayOfWeek) % 7;
         today.add(Calendar.DAY_OF_YEAR, daysBetween);
         Date taskDate = today.getTime();
         String formattedTaskDate = format.format(taskDate);


        ContentValues contentValues = new ContentValues();

        contentValues.put("TASKS", task);
        contentValues.put("EMPLOYEEID", employeeID);
        contentValues.put("NAME", name);
        contentValues.put("DAY", day);
        contentValues.put("TASKDATE", formattedTaskDate);
        contentValues.put("TIME", time);
        contentValues.put("SEENBYEMPLOYEE", seenByEmployee);
        contentValues.put("ISTASKCOMPLETE", 0);



        long result = db.insert(TASKS_WITH_INFO_TABLE, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
     }


    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor employeeInfo = db.rawQuery("select * from " + TABLE_NAME, null);
        return employeeInfo;
    }

    public Cursor getName(String employeeID) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor name = db.rawQuery("select NAME from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        return name;
    }

    public Cursor getAddress(String employeeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor address = db.rawQuery("select ADDRESS from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        return address;
    }

    public Cursor getRole(String employeeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor role = db.rawQuery("select ROLE from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        return role;
    }

    public Cursor getPassword(String employeeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor password = db.rawQuery("select PASSWORD from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        return password;
    }

    public Cursor getPayRate(String employeeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor payRate = db.rawQuery("select STARTPAY from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        return payRate;
    }

    public double getTimeWorked(String employeeID) {
        double tw = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor payRate = db.rawQuery("select WEEKTIMEWORKED from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        while (payRate.moveToNext()) {
            tw = payRate.getDouble(0); //getting double value from Cursor
        }
        return tw;//return the double that was in the Cursor
    }

    public double getWeeksPay(String employeeID) {
        double wp = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor weeksPay = db.rawQuery("select PAYTHISWEEK from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        while (weeksPay.moveToNext()) {
            wp = weeksPay.getDouble(0);
        }

        return wp;
    }

    public String getStartDate(String employeeID) {
        String startDate = "";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor sd = db.rawQuery("select STARTDATE from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`=" + employeeID, null); //getting startdate from TABLE_NAME

        while (sd.moveToNext()) {
            startDate = sd.getString(0); //getting string from cursor, putting it into startDate
        }
        return startDate;
    }

    public Cursor getRequests() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gnr = db.rawQuery("select * from " + DAYOFFREQUESTS + " where `" + "ANSWER" + "`=" + 0, null);

        return gnr;
    }

    public Cursor getAnswer(String employeeID) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor ga = db.rawQuery("select DAY, ANSWER from " + DAYOFFREQUESTS + " where `" + "EMPLOYEEID" + "`=" + employeeID + " AND `" + "ANSWER" + "`!=" + 3, null);

        return ga;
    }

    public Cursor getIdName(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gin = db.rawQuery("select NAME, EMPLOYEEID from " + TABLE_NAME + " where `" + "EMPLOYEEID" + "`!= 1234" , null);

        return gin;
    }

    public Cursor getTimes(String day){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gt = db.rawQuery("select HOURS from " + SCHEDULETABLE + " where `" + "DAY" + "`=" + day , null);

        return gt;
    }

    public Cursor getTimesForDay(String employeeID, String day){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gtfd = db.rawQuery("select HOURS from " + SCHEDULETABLE + " where `" + "EMPLOYEEID" + "`=" + employeeID + " AND `" + "DAY" + "`=" + day, null);

        return  gtfd;

    }

    public Cursor getCoworkers(String day , String employeeID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gc = db.rawQuery("select NAME, EMPLOYEEID, HOURS from " + SCHEDULETABLE + " where `" + "DAY" + "`=" + day + " AND `" + "EMPLOYEEID" + "`!=" + employeeID + " AND `" + "HOURS" + "`!=" + "0", null);

        return gc;
    }

    public Cursor getRecords(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gr = db.rawQuery("select EMPLOYEEID, NAME, CLOCKINTIME, CLOCKOUTTIME, HOURSFORDAY from " + EMPLOYEEHOURS, null);

        return gr;
    }

    public Cursor getNextPayDate(String employeeID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gnpd = db.rawQuery("select NEXTPAYDAY from " + PAYMENTINFORMATION + " where `" + "EMPLOYEEID" + "`=" + employeeID , null);

        return gnpd;
    }


    public Cursor getPayInformation(){
        SQLiteDatabase db = this.getWritableDatabase();

       Cursor gpi =  db.rawQuery("select EMPLOYEEID, WEEKTIMEWORKED, PAYTHISWEEK from " + TABLE_NAME , null);

        return gpi;
    }

    public Cursor getPayLogs(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gpl = db.rawQuery("select EMPLOYEEID, HOURSWORKED, WEEKSPAY, DATE from " + PAYMENTINFORMATION , null);

        return gpl;
    }

    public Cursor getAllRoles(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gar = db.rawQuery("select ROLES from " + ROLES , null);

        return  gar;
    }

    public Cursor getAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gat = db.rawQuery("select TASK from " + TASKS_TABLE , null);

        return gat;
    }

    public Cursor getEmployeesOnDay(String day, String hours){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor geod = db.rawQuery("select EMPLOYEEID, NAME from " + SCHEDULETABLE + " where `" + "DAY" + "`=" + day +  " AND `" + "HOURS" + "`=" + hours, null);

        return  geod;
    }

    public Cursor getTasksWithInfo(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gtwi = db.rawQuery("select TASKS, EMPLOYEEID, NAME, DAY, TIME from " + TASKS_WITH_INFO_TABLE , null);

        return gtwi;
    }

    public int getRequestsCountForEmployee(String employeeID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor grc = db.rawQuery("select ANSWER from " + DAYOFFREQUESTS + " where `" + "ANSWER" + "`=" + 1 + " OR `" + "ANSWER" + "`=" + 2 + " AND `" + "EMPLOYEEID"
                + "`=" + employeeID + " AND `" + "SEENBYNOTIFICATION" + "`=" + 1, null);

        Cursor gtc = db.rawQuery("select TASKS from " + TASKS_WITH_INFO_TABLE + " where `" + "EMPLOYEEID" + "`=" + employeeID +
                " AND `" + "SEENBYEMPLOYEE" + "`=" + 1 , null);
        return grc.getCount() + gtc.getCount();


    }

    public int getRequestsCountForManager(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor grcfm = db.rawQuery("select ANSWER from " + DAYOFFREQUESTS + " where `" + "SEENBYNOTIFICATION" + "`=" + 0, null);

        Cursor taskCont = db.rawQuery("select TASKS from " + TASKS_WITH_INFO_TABLE + " where `" + "SEENBYEMPLOYEE" + "`=" + 2 + " AND `" + "ISTASKCOMPLETE" + "`=" + 2, null);

        return grcfm.getCount() + taskCont.getCount();
    }

    public Cursor getEmployeeTasknotification(String employeeID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor geti = db.rawQuery("select TASKS, DAY, TIME from " + TASKS_WITH_INFO_TABLE + " where `" + "EMPLOYEEID" + "`=" + employeeID + " AND `" + "SEENBYEMPLOYEE" + "`=" + 1, null);

        return geti;
    }

    public Cursor getTasksForEmployee(String employeeID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gtfe = db.rawQuery("select TASKS, NAME, DAY, TIME from " + TASKS_WITH_INFO_TABLE + " where `" + "EMPLOYEEID" + "`=" + employeeID, null);

        return gtfe;
    }

    public Cursor getAllTasksWithInfo(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gatwi = db.rawQuery("select * from " + TASKS_WITH_INFO_TABLE, null);

        return gatwi;
    }

    public Cursor getTasksForDay(String day, String shift){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor gtfd = db.rawQuery("select TASKS, EMPLOYEEID from " + TASKS_WITH_INFO_TABLE + " where `" + "TASKDATE" + "`=" + day + " AND `" + "TIME" + "`=" + shift  + " AND `" + "ISTASKCOMPLETE" + "`=" + 0, null);

        return gtfd;
    }

    public Cursor getOtherEmployeesOnShift(String employeeID, String day, String shift){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor goeos = db.rawQuery("select NAME, EMPLOYEEID from " + SCHEDULETABLE + " where `" + "EMPLOYEEID" + "`!=" + employeeID + " AND `" + "DAY" + "`=" + day + " AND `" + "HOURS" + "`=" + shift, null);

        return  goeos;
    }



    public void update(String name, String address, String role, String employeeID, String startPay) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "EMPLOYEEID=" + employeeID;
        ContentValues content = new ContentValues();
        content.put("NAME", name);
        content.put("ADDRESS", address);
        content.put("ROLE", role);
        content.put("STARTPAY", startPay);

        db.update(TABLE_NAME, content, where, null);

    }

    public void updatePayAndTime(String employeeID, double timeWorked) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "EMPLOYEEID=" + employeeID;
        double pr = 0;
        double currentWeeksPay;
        double payForDay;
        double newWeeksPay;
        double newTime;
        Cursor payRate = getPayRate(employeeID); //Cursor for payrate
        double tw = getTimeWorked(employeeID); //Cursor for weeks time worked
        double roundedPay;

        while (payRate.moveToNext()) {
            pr = payRate.getDouble(0); //getting the double value from the pay rate Cursor

        }

        //CALCULATING NEW PAY
        payForDay = timeWorked * pr;

        currentWeeksPay = getWeeksPay(employeeID); //getting the weeks pay that is currently in the database

        newWeeksPay = payForDay + currentWeeksPay; //adding the pay for the day to the total weeks pay

        roundedPay = (double) Math.round(newWeeksPay * 100.00) / 100.00; //rounding to two decimal places



        //END CALCULATING NEW PAY

        //CALCULATING NEW TIME WORKED

        newTime = tw + timeWorked; //adding the days work time to the weekly total work time

        //END CALCULATING NEW TIME WORKED

        ContentValues content = new ContentValues();
        content.put("PAYTHISWEEK", roundedPay);
        content.put("WEEKTIMEWORKED", newTime);

        db.update(TABLE_NAME, content, where, null);
    }

    public void updateRequest(int answer, String employeeID, String day, int seenByEmployee) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "EMPLOYEEID=" + employeeID + " AND DAY=" + day;
        ContentValues contentValues = new ContentValues();

        contentValues.put("ANSWER", answer);
        contentValues.put("SEENBYNOTIFICATION", seenByEmployee);

        db.update(DAYOFFREQUESTS, contentValues, where, null);
    }

    public void updateSchedule(String employeeID, String day, String hours){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "EMPLOYEEID=" + employeeID + " AND DAY=" + day;

        ContentValues contentValues = new ContentValues();

        contentValues.put("HOURS", hours);

        db.update(SCHEDULETABLE, contentValues, where, null);
    }

    public void updateWeeksPayAndHours(String employeeID, double pay, double hours) {

        SQLiteDatabase db = this.getWritableDatabase();

        String where = "EMPLOYEEID=" + employeeID;

        ContentValues contentValues = new ContentValues();

        contentValues.put("PAYTHISWEEK", pay);
        contentValues.put("WEEKTIMEWORKED", hours);

        db.update(TABLE_NAME, contentValues, where, null);
    }

    public void updateNextPayDay(String employeeID, String day){

        SQLiteDatabase db = this.getWritableDatabase();

        String where = "EMPLOYEEID=" + employeeID;

        ContentValues contentValues = new ContentValues();

        contentValues.put("NEXTPAYDAY", day);

        db.update(PAYMENTINFORMATION, contentValues, where,  null);
    }

    public void updateTaskNotification(String employeeID, String day, String time, String task, int needsToBeSeen, int isFinished){

        SQLiteDatabase db = this.getWritableDatabase();

        String where = "EMPLOYEEID=" + employeeID + " AND DAY=" + day+ " AND TIME=" + time + " AND TASKS=" + task;

        ContentValues contentValues = new ContentValues();

        contentValues.put("SEENBYEMPLOYEE", needsToBeSeen);
        contentValues.put("ISTASKCOMPLETE", isFinished);

        db.update(TASKS_WITH_INFO_TABLE, contentValues, where, null);

    }

    public void updateTask(String task, String newTask){
        SQLiteDatabase db = this.getWritableDatabase();

        String where = "TASK=" + task;

        ContentValues contentValues = new ContentValues();

        contentValues.put("TASK", newTask);

        db.update(TASKS_TABLE, contentValues, where, null);
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();

        String where = "TASK=" + task;

        db.delete(TASKS_TABLE, where, null);
    }





    public void delete(String EmployeeID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String where = "EMPLOYEEID=" + EmployeeID;

        db.delete(TABLE_NAME, where, null);


    }


}
