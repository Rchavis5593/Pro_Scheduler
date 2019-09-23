package com.example.randy.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogDecision extends AppCompatActivity {

    Button workLogs;
    Button payLogs;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_decision);

        seeWorkLogs();
        seePayLogs();
        goBack();
    }

    public void seeWorkLogs(){
        workLogs = findViewById(R.id.seeWorkLogs);

        workLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogDecision.this, WorkLogs.class);

                startActivity(intent);
            }
        });
    }

    public void seePayLogs(){
        payLogs = findViewById(R.id.seePayLogs);

        payLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogDecision.this, PayLogs.class);

                startActivity(intent);
            }
        });
    }



    public void goBack() {
         back = findViewById(R.id.workLogsBack);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LogDecision.this, ManagerScreen.class);
                startActivity(intent);

            }
        });
    }
}
