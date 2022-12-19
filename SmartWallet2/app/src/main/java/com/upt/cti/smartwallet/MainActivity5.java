package com.upt.cti.smartwallet;

import android.app.Activity;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upt.cti.smartwallet.model.MonthlyExpense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity5 extends Activity {

    private Spinner sSearch;
    private TextView tStatus;
    private EditText eIncome, eExpenses;
    // firebase
    private DatabaseReference databaseReference;
    private String currentMonth = null;
    // prefs
    private final static String PREFS_SETTINGS = "prefs_settings";
    private final static String KEY_SEARCH = "key_search";
    private SharedPreferences prefsSettings;
    private boolean initialized = false;
    // data structures
    private List<MonthlyExpense> monthlyExpenses;
    private List<String> monthNames;
    private ArrayAdapter<String> sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        sSearch = (Spinner) findViewById(R.id.sSearch);
        tStatus = (TextView) findViewById(R.id.tStatus);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);

        // data structures
        monthlyExpenses = new ArrayList<>();
        monthNames = new ArrayList<>();

        // spinner adapter
        sAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, monthNames);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSearch.setAdapter(sAdapter);

        sSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                tStatus.setText("Details for " + monthNames.get(index));
                eIncome.setText(String.valueOf(monthlyExpenses.get(index).getIncome()));
                eExpenses.setText(String.valueOf(monthlyExpenses.get(index).getExpenses()));
                currentMonth = monthNames.get(index);
                prefsSettings.edit().putString(KEY_SEARCH, currentMonth).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // setup Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        attachValueEventListener();
        //attachChildEventListener();
    }

    private void attachValueEventListener() {
        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                monthlyExpenses.clear();
                monthNames.clear();

                for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()) {
                    try {
                        // create a new instance of MonthlyExpense
                        MonthlyExpense monthlyExpense = monthSnapshot.getValue(MonthlyExpense.class);

                        // save the month and month name
                        monthlyExpenses.add(monthlyExpense);
                        monthNames.add(monthlyExpense.month);

                        // update UI if month is selected
                        if (currentMonth != null && currentMonth.equals(monthlyExpense.month)) {
                            eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                            eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                        }
                    } catch (Exception e) {
                    }
                }

                // notify the spinner that data may have changed
                sAdapter.notifyDataSetChanged();
                if (!initialized)
                    initPrefs(monthNames);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void attachChildEventListener() {
        databaseReference.child("calendar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // create instance for child
                MonthlyExpense monthlyExpense = dataSnapshot.getValue(MonthlyExpense.class);

                // update model
                monthlyExpenses.add(monthlyExpense);
                monthNames.add(monthlyExpense.month);

                // update UI if month is selected
                if (currentMonth != null && currentMonth.equals(monthlyExpense.month)) {
                    eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                    eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                }

                sAdapter.notifyDataSetChanged();
                if (!initialized)
                    initPrefs(monthNames);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // create instance for child
                MonthlyExpense monthlyExpense = dataSnapshot.getValue(MonthlyExpense.class);

                for (MonthlyExpense exp : monthlyExpenses) {
                    if (exp.month.equals(monthlyExpense.month)) {
                        exp.setExpenses(monthlyExpense.getExpenses());
                        exp.setIncome(monthlyExpense.getIncome());
                    }
                }

                // update UI if month is selected
                if (currentMonth != null && currentMonth.equals(monthlyExpense.month)) {
                    eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                    eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                }

                sAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initPrefs(List<String> monthNames) {
        prefsSettings = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        String lastMonth = prefsSettings.getString(KEY_SEARCH, null);
        if (lastMonth != null) {
            if (monthNames.contains(lastMonth)) {
                sSearch.setSelection(monthNames.indexOf(lastMonth));
            }
        }
        initialized = true;
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bUpdate:
                updateDB();
                break;
        }
    }

    private void updateDB() {
        // texts in eIncome and eExpenses are not empty
        if (!eIncome.getText().toString().isEmpty() && !eExpenses.getText().toString().isEmpty()) {
            try {
                // parse data to double
                double income = Double.parseDouble(eIncome.getText().toString());
                double expenses = Double.parseDouble(eExpenses.getText().toString());

                // create a Map<String, Object> for income and expenses
                Map<String, Object> map = new HashMap<>();
                map.put("income", income);
                map.put("expenses", expenses);

                // call update children on database reference
                if (databaseReference != null && !currentMonth.isEmpty())
                    databaseReference.child("calendar").child(currentMonth).updateChildren(map);

            } catch (Exception e) {
                Toast.makeText(this, "Income and expenses must be in numeric format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Income and expenses fields cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }


//        final String currentMonth = "march";
//
//        databaseReference.child("calendar").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                double d1 = (double) dataSnapshot.child("expenses").getValue();
//                //double d2 = (double) dataSnapshot.child(currentMonth).child("expenses").getValue();
//                //boolean b1 = (boolean) dataSnapshot.child(currentMonth).child("expenses").getValue();
//                float f1 = dataSnapshot.child("expenses").getValue(Float.class);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
}
