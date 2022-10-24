package com.example.tema2;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText eName = (EditText) findViewById(R.id.editTextTextPersonName);
        Button bClick = (Button) findViewById(R.id.button);
        TextView tName = (TextView) findViewById(R.id.textView);

        FloatingActionButton buttonShare = (FloatingActionButton) findViewById(R.id.shareButton);
        FloatingActionButton buttonSearch = (FloatingActionButton) findViewById(R.id.searchButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        bClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String afisare= "Hello, " + eName.getText();
                builder.setMessage(afisare);
                builder.setCancelable(true);

                //buton pozitiv
                builder.setPositiveButton("happy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "You alive", Toast.LENGTH_SHORT).show();//folosim toast pentru durata de afisare a mesajului
                    }
                });
                //buton negativ
                builder.setNegativeButton("sad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "You dead", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();//afisarea alertei
            }


        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String name = tName.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_TEXT, name);
                startActivity(Intent.createChooser(shareIntent, "Share using: "));
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(Intent.ACTION_VIEW);
                String text = tName.getText().toString();
                String url = "https://www.google.ro" + text;
                searchIntent.setDataAndType(Uri.parse(url), "text/plain");
                startActivity(searchIntent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String colour = spinner.getItemAtPosition(i).toString();
                switch (colour) {
                    case "Blue":
                        bClick.setBackgroundColor(Color.rgb(0,0,255));
                        break;
                    case "Green":
                        bClick.setBackgroundColor(Color.rgb(0,255,0));
                        break;
                    case "Black":
                        bClick.setBackgroundColor(Color.rgb(0,0,0));
                        break;
                    case "Orange":
                        bClick.setBackgroundColor(Color.rgb(255,165,0));
                        break;
                    case "Red":
                        bClick.setBackgroundColor(Color.rgb(255, 0, 0));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}