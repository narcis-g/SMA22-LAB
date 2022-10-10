package com.example.helloandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText eName = (EditText) findViewById(R.id.editTextTextPersonName);
        Button bClick = (Button) findViewById(R.id.button);
        TextView tName = (TextView) findViewById(R.id.textView);

        bClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String afisare= "Hello, " + eName.getText();
                tName.setText(afisare);
            }
        });
    }
}