package com.upt.cti.smartwallet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upt.cti.smartwallet.model.MonthlyExpense;
//import com.upt.cti.smartwallet.receivers.PaymentReceiver;

import java.util.HashMap;
import java.util.Map;

import static java.util.logging.Logger.global;

public class MainActivity4 extends Activity {

    private final static String PREFS_SETTINGS = "prefs_settings";
    private final static String KEY_SEARCH = "key_search";
    private SharedPreferences prefsSettings;
    // ui
    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;
    // firebase
    private DatabaseReference databaseReference;
    private String currentMonth = null, previousMonth;
    private ValueEventListener databaseListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        tStatus = (TextView) findViewById(R.id.tStatus);
        eSearch = (EditText) findViewById(R.id.eSearch);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);
        tStatus.setText("Search for a month");

//        Button bSearch = (Button) findViewById(R.id.bSearch);
//        bSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity4.this, "Toast from listener", Toast.LENGTH_SHORT).show();
//            }
//        });

        // setup firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        // named preference file
        prefsSettings = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        String lastSearch = prefsSettings.getString(KEY_SEARCH, null);
        if (lastSearch != null)
            eSearch.setText(lastSearch);

//        BroadcastReceiver receiver = new PaymentReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BATTERY_LOW);
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        this.registerReceiver(receiver, filter); // receptor global
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter); // receptor local

//        Intent bIntent = new Intent();
//        bIntent.setAction("com.upt.broadcast.NOTIFICAREA_MEA");
//        bIntent.putExtra("nume", "User123");
//        bIntent.putExtra("suma", 12345);
//        bIntent.setPackage("com.upt");
//        sendBroadcast(bIntent);
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bSearch:
                Toast.makeText(this, "Toast from clicked method", Toast.LENGTH_SHORT).show();
                if (!eSearch.getText().toString().isEmpty()) {
                    // save text to lower case (all our months are stored online in lower case)
                    previousMonth = currentMonth;
                    currentMonth = eSearch.getText().toString().toLowerCase(); // Warning!

                    tStatus.setText("Searching ...");
                    prefsSettings.edit().putString(KEY_SEARCH, currentMonth).apply();
                    createNewDBListener();
                } else {
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bUpdate:
                updateDB();
                break;
        }
    }

    private void createNewDBListener() {
        // remove previous databaseListener
        if (databaseReference != null && previousMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(previousMonth).removeEventListener(databaseListener);

        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                MonthlyExpense monthlyExpense = dataSnapshot.getValue(MonthlyExpense.class);
                // explicit mapping of month name from entry key
                //monthlyExpense.month = dataSnapshot.getKey();

                eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                tStatus.setText("Found entry for " + currentMonth);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        // set new databaseListener
        databaseReference.child("calendar").child(currentMonth).addValueEventListener(databaseListener);
    }

    private void updateDB() {
        if (!eIncome.getText().toString().isEmpty() && !eExpenses.getText().toString().isEmpty()) {
            try {
                double income = Double.parseDouble(eIncome.getText().toString());
                double expenses = Double.parseDouble(eExpenses.getText().toString());

                /*Map<String, Object> map1 = new HashMap<>();
                map1.put("income", 1750);
                map1.put("expenses", 1020.5);

                Map<String, Object> map2 = new HashMap<>();
                map2.put("credits", 200);
                map2.put("expenses", 512.25);

                databaseReference.child("calendar").child("january").updateChildren(map1);
                databaseReference.child("calendar").child("february").setValue(45);
                databaseReference.child("calendar").child("march").updateChildren(map2);*/

                Map<String, Object> map = new HashMap<>();
                map.put("income", income);
                map.put("expenses", expenses);

                if (databaseReference != null && currentMonth != null)
                    databaseReference.child("calendar").child(currentMonth).updateChildren(map);

            } catch (Exception e) {
                Toast.makeText(this, "Income and expenses must be in numeric format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Income and expenses fields cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
