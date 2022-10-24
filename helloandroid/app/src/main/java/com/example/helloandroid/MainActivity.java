package com.example.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import lifecycle.ActivityA;

public class MainActivity extends AppCompatActivity {


    Button A_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText eName = (EditText) findViewById(R.id.editTextTextPersonName);
        Button bClick = (Button) findViewById(R.id.button);
        TextView tName = (TextView) findViewById(R.id.textView);

        A_button = findViewById(R.id.A_button);

        A_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityA.class);
                startActivity(intent);
            }
        });

        bClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String afisare= "Hello, " + eName.getText();
                tName.setText(afisare);
            }
        });
    }
}