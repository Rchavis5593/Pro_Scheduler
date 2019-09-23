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
import android.widget.ListView;

import java.util.ArrayList;

public class AssignRoles extends AppCompatActivity {
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_roles);
        ListView list = findViewById(R.id.currentRolesList);
        final ArrayList array = new ArrayList();
        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, array);
        list.setAdapter(adapter);

        myDB = DatabaseHelper.getInstance(this);

        Cursor allRoles = myDB.getAllRoles();

        if(allRoles != null && allRoles.moveToFirst()){
            do{
                array.add(allRoles.getString(0));
                adapter.notifyDataSetChanged();
            }while (allRoles.moveToNext());
        }

        assignNewRole();
        goBack();
    }

    public void assignNewRole(){
        Button assignRole = findViewById(R.id.addNewRoleButton);
        final EditText input = new EditText(this);

        assignRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterRoles(input);
            }
        });

    }

    public void enterRoles(final EditText editText){
        final AlertDialog.Builder roles = new AlertDialog.Builder(this);
        ListView list = findViewById(R.id.currentRolesList);
        final ArrayList array = new ArrayList();
        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customlistview, array);
        list.setAdapter(adapter);
        roles.setCancelable(true);

        roles.setTitle("Add Role");

        roles.setView(editText); //putting edit text inside of a dialog box

        roles.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.insertRole(editText.getText().toString());
                array.add(editText.getText().toString());
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(AssignRoles.this, AssignRoles.class);
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
        Button back = findViewById(R.id.assignRolesBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssignRoles.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }
}
