package com.example.randy.sample;


import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

public class EditEmployee extends AppCompatActivity {

    Button saveChanges, deleteEmployee, roleButton;

    EditText name, address,newID, newStartPay;

    DatabaseHelper myDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        name = findViewById(R.id.changeName);
        address = findViewById(R.id.changeAddress);
        roleButton = findViewById(R.id.editEmployeeRoleButton);
        newID = findViewById(R.id.newIDNumber);
        newStartPay = findViewById(R.id.editEmployeeStartingPay);
        myDb = DatabaseHelper.getInstance(this);



        //getting the values passed from add employee
        String employeeName = getIntent().getStringExtra("EMPLOYEE_NAME");
        String employeeAddress = getIntent().getStringExtra("EMPLOYEE_ADDRESS");
        String employeeRole = getIntent().getStringExtra("EMPLOYEE_ROLE");
        String employeeID = getIntent().getStringExtra("EMPLOYEEID");
        String startPay = getIntent().getStringExtra("STARTPAY");


        //setting initial values to values from AddEmployee class
        name.setText(employeeName);
        address.setText(employeeAddress);
        roleButton.setText(employeeRole);
        newID.setText(employeeID);
        newStartPay.setText(startPay);

        saveChanges();
        deleteEmployee();
        addRoles();

        goBack();
        goHome();
    }


    public void saveChanges() {

        saveChanges = findViewById(R.id.saveChanges);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(EditEmployee.this, AddEmployee.class);

                //sending values back to AddEmployee class

                intent.putExtra("NEW_EMPLOYEE_NAME", name.getText().toString());
                intent.putExtra("NEW_EMPLOYEE_ADDRESS", address.getText().toString());
                intent.putExtra("NEW_EMPLOYEE_ROLE", roleButton.getText().toString());
                intent.putExtra("NEW_EMPLOYEE_ID", newID.getText().toString());
                intent.putExtra("NEW_START_PAY", newStartPay.getText().toString());

                startActivity(intent);

                Toast.makeText(EditEmployee.this, "Employee was Updated", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void deleteEmployee() {
        deleteEmployee = findViewById(R.id.deleteEmployee);

        deleteEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEmployee.this, AddEmployee.class);
                intent.putExtra("DELETE", "true");
                intent.putExtra("DELETE_EMPLOYEE", newID.getText().toString());
                startActivity(intent);
            }
        });
    }
    public void displayMessage(String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }

    public void goBack() {
        Button back = findViewById(R.id.editEmployeeBack);
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditEmployee.this, AddEmployee.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }

    public void goHome() {
        Button back = findViewById(R.id.editEmployeeHome);
        final String employeeID = getIntent().getStringExtra("EMPLOYEEID");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditEmployee.this, ManagerScreen.class);
                intent.putExtra("EMPLOYEEID", employeeID);
                startActivity(intent);

            }
        });
    }

    public void addRoles(){
        final Button roleButton = findViewById(R.id.editEmployeeRoleButton);
        ArrayList array = new ArrayList();
        Cursor roles = myDb.getAllRoles();
        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, array);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        roleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("                Select Role").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        roleButton.setText(adapter.getItem(which).toString()); //setting the text to whatever item was clicked


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
