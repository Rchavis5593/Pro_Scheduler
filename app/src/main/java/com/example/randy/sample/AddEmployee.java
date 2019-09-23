package com.example.randy.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddEmployee extends AppCompatActivity {

    EditText name, address, employeeID, password, startPay;

    Button save, viewAllEmployees, searchEmployee, roleButton;

    public DatabaseHelper myDb;

    String EmployeeID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        name = findViewById(R.id.editText);
        address = findViewById(R.id.editText2);
        employeeID = findViewById(R.id.editText3);
        password = findViewById(R.id.employeePassword);
        myDb = DatabaseHelper.getInstance(this);
        roleButton = findViewById(R.id.rolesButton);
        startPay = findViewById(R.id.startPayEditText);

        addEmployee();
        viewEmployees();
        searchByID();
        addRoles();

        String newName = getIntent().getStringExtra("NEW_EMPLOYEE_NAME");
        String newAddress = getIntent().getStringExtra("NEW_EMPLOYEE_ADDRESS");
        String newRole = getIntent().getStringExtra("NEW_EMPLOYEE_ROLE");
        String sameEmployeeID = getIntent().getStringExtra("NEW_EMPLOYEE_ID");
        String deleteOrNot = getIntent().getStringExtra("DELETE");
        String deleteEmployee = getIntent().getStringExtra("DELETE_EMPLOYEE");
        String newStartingPay = getIntent().getStringExtra("NEW_START_PAY");

        if (newName != null || newAddress != null || newRole != null || newStartingPay != null) {
            name.setText(newName);
            address.setText(newAddress);
            employeeID.setText(sameEmployeeID);
            roleButton.setText(newRole);
            startPay.setText(newStartingPay);
            myDb.update(newName, newAddress, newRole, sameEmployeeID, newStartingPay); //updating values based on new values that are now in AddEmployee
        }

        if (deleteOrNot != null) {
            myDb.delete(deleteEmployee);
            Toast.makeText(AddEmployee.this, "Employee was deleted", Toast.LENGTH_LONG).show();
        }


        goBack();
    }

    public void addEmployee() {


        save = findViewById(R.id.button14);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertEmployee(name.getText().toString(), address.getText().toString(), roleButton.getText().toString(), employeeID.getText().toString(), password.getText().toString(), startPay.getText().toString());
                if (isInserted) {
                    Toast.makeText(AddEmployee.this, "Data was inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddEmployee.this, "Data was NOT inserted", Toast.LENGTH_LONG).show();
                }

                myDb.insertSchedule(employeeID.getText().toString(), "Monday", "0", name.getText().toString());
                myDb.insertSchedule(employeeID.getText().toString(), "Tuesday", "0", name.getText().toString());
                myDb.insertSchedule(employeeID.getText().toString(), "Wednesday", "0", name.getText().toString());
                myDb.insertSchedule(employeeID.getText().toString(), "Thursday", "0", name.getText().toString());
                myDb.insertSchedule(employeeID.getText().toString(), "Friday", "0", name.getText().toString());
                myDb.insertSchedule(employeeID.getText().toString(), "Saturday", "0", name.getText().toString());
                myDb.insertSchedule(employeeID.getText().toString(), "Sunday", "0", name.getText().toString());


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

    public void viewEmployees() {

        viewAllEmployees = findViewById(R.id.viewCurrentEmployees);
        viewAllEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor result = myDb.getAllEmployees();
                if (result.getCount() == 0) {
                    displayMessage("Error", "NO EMPLOYEES TO DISPLAY");
                } else {
                    StringBuffer buffer = new StringBuffer();
                    while (result.moveToNext()) {
                        buffer.append("Employee ID : " + result.getString(0) + " ");
                        buffer.append("Name : " + result.getString(1) + " ");
                        buffer.append("Address : " + result.getString(2) + " ");
                        buffer.append("Role : " + result.getString(3) + " ");
                        buffer.append("Employee ID : " + result.getString(4) + " ");
                        buffer.append("Employee Start Date: " + result.getString(5) + " ");
                        buffer.append("Employee Starting Pay: " + result.getString(6) + " ");
                    }
                    displayMessage("Employee List", buffer.toString());
                }
            }
        });
    }

    public void searchByID() {
        final EditText input = new EditText(this);
        searchEmployee = findViewById(R.id.searchByID);


        searchEmployee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            displaySearch(input);

            }
        });
    }

    public void goBack() {
        Button back = findViewById(R.id.addEmployeeBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddEmployee.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }

    public void displaySearch(final EditText input) {
        startPay = findViewById(R.id.startPayEditText);
        final AlertDialog.Builder searchPop = new AlertDialog.Builder(this);
        searchPop.setCancelable(true);

        searchPop.setTitle("Search");
        searchPop.setMessage("Search for an employee using employee ID");

        searchPop.setView(input);

        searchPop.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EmployeeID = input.getText().toString();


                Cursor cursor = myDb.getName(EmployeeID);

                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append(cursor.getString(0));
                }

                Cursor addrressCursor = myDb.getAddress(EmployeeID);
                StringBuffer addressBuffer = new StringBuffer();

                while (addrressCursor.moveToNext()) {
                    addressBuffer.append(addrressCursor.getString(0));
                }

                Cursor roleCursor = myDb.getRole(EmployeeID);
                StringBuffer roleBuffer = new StringBuffer();

                while (roleCursor.moveToNext()) {
                    roleBuffer.append(roleCursor.getString(0));
                }

                Cursor startPayCursor = myDb.getPayRate(EmployeeID);
                StringBuffer payBuffer = new StringBuffer();

                while(startPayCursor.moveToNext()){
                    payBuffer.append(startPayCursor.getString(0));
                }


                Intent intent = new Intent(AddEmployee.this, EditEmployee.class);
                intent.putExtra("EMPLOYEE_NAME", buffer.toString());
                intent.putExtra("EMPLOYEE_ADDRESS", addressBuffer.toString());
                intent.putExtra("EMPLOYEE_ROLE", roleBuffer.toString());
                intent.putExtra("EMPLOYEEID", EmployeeID);
                intent.putExtra("STARTPAY", payBuffer.toString());
                startActivity(intent);


            }
        });

        searchPop.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        searchPop.show();
    }

    public void addRoles(){
        final Button roleButton = findViewById(R.id.rolesButton);
        ArrayList array = new ArrayList();
        Cursor roles = myDb.getAllRoles();
        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, array);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        roleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("                Select Role").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        roleButton.setText(adapter.getItem(which).toString());


                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        if(roles != null && roles.moveToFirst()){
            do{
                array.add(roles.getString(0));
            }while(roles.moveToNext());
        }
    }
}
