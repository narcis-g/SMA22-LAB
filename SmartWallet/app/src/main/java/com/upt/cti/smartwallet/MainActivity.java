package com.upt.cti.smartwallet;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseReference databaseReference;
    private String currentMonth;
    private ValueEventListener databaseListener;
    private TextView entries;
    private EditText income, expenses;
    private Spinner searchRes;
    private final static String PREFS_SETTINGS = "prefs_settings";
    private SharedPreferences prefsUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefsUser = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);


        searchRes = findViewById(R.id.eSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchRes.setAdapter(adapter);
        searchRes.setOnItemSelectedListener(this);

        entries = findViewById(R.id.tStatus);
        income =  findViewById(R.id.eIncome);
        expenses =  findViewById(R.id.eExpenses);
        FirebaseApp.initializeApp(this);

        databaseReference= FirebaseDatabase.getInstance().getReference();


    }

    private  void createNewDBListener() {
        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(currentMonth).removeEventListener(databaseListener);
        databaseListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                MonthlyExpenses monthlyExpense = dataSnapshot.getValue(MonthlyExpenses.class);
                // explicit mapping of month name from entry key
                try{
                    monthlyExpense.month = dataSnapshot.getKey();
                    income.setText(String.valueOf(monthlyExpense.getIncome()));
                    expenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                    entries.setText("Found entry for " + currentMonth);
                }catch (NullPointerException e){
                    entries.setText("No entries found for " + currentMonth + "!");
                    income.setText("");
                    expenses.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        databaseReference.child("calendar").child(currentMonth).addValueEventListener((ValueEventListener) databaseListener);
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bSearch:
                if (!currentMonth.isEmpty()) {
                    // save text to lower case (all our months are stored online in lower case)
                    entries.setText("Searching ...");
                    createNewDBListener();
                } else {
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bUpdate:
                if(currentMonth.isEmpty()){
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_SHORT).show();
                }else if(income.getText().toString().isEmpty()){
                    Toast.makeText(this, "Income field may not be empty", Toast.LENGTH_SHORT).show();
                }else if(expenses.getText().toString().isEmpty()){
                    Toast.makeText(this, "Expenses field may not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    insertData();
                }

                break;
        }
    }

    public void insertData(){
        MonthlyExpenses item = new MonthlyExpenses(currentMonth, Float.parseFloat(income.getText().toString()), Float.parseFloat(expenses.getText().toString()));
        entries.setText("Updating ...");
        databaseReference.child("calendar").child(currentMonth).setValue(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        currentMonth = parent.getItemAtPosition(position).toString().toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}