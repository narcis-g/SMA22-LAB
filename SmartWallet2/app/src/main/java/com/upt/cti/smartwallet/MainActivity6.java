package com.upt.cti.smartwallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upt.cti.smartwallet.model.AppState;
import com.upt.cti.smartwallet.model.Month;
import com.upt.cti.smartwallet.model.Payment;
import com.upt.cti.smartwallet.ui.PaymentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity6 extends Activity {

    // ui
    private TextView tStatus;
    private int currentMonth;
    private final List<Payment> payments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        tStatus = (TextView) findViewById(R.id.tStatus);
        ListView listPayments = (ListView) findViewById(R.id.listPayments);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, payments);
        listPayments.setAdapter(adapter);

//        //  data to be displayed
//        List<String> payments = new ArrayList<>();
//        payments.add("45.25");
//        payments.add("5.50");
//        payments.add("15.60");
//
//        // initialize list view from activity layout
//        ListView listPayments = (ListView) findViewById(R.id.listPayments);
//        // create adapter for payments list
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, payments);
//        // set adapter to list view
//        listPayments.setAdapter(listAdapter);
//
//        // update list: add one element, the remove element at index 2
//        payments.add("10.25");
//        payments.remove(2);
//        // update UI
//        listAdapter.notifyDataSetChanged();


        // e.g. extracts "10" from "2016-10-26 11:45:08"
        currentMonth = Month.monthFromTimestamp(AppState.getCurrentTimeDate());
        tStatus.setText("Loading payments for " + Month.intToMonthName(currentMonth) + " ...");

        // setup firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);

        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Payment payment = dataSnapshot.getValue(Payment.class);

                    if (payment.timestamp != null) {
                        payments.add(payment);
                        adapter.notifyDataSetChanged();
                    }

                    tStatus.setText("Found " + payments.size() + " payments for " + Month.intToMonthName(currentMonth) + ".");
                } catch (Exception e) {
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    Payment payment = dataSnapshot.getValue(Payment.class);
                    payment.timestamp = dataSnapshot.getKey();
                    if (payments.contains(payment))
                        payments.set(payments.indexOf(payment), payment);
                    else
                        payments.add(payment);
                    adapter.notifyDataSetChanged();

                    tStatus.setText("Found " + payments.size() + " payments for " + Month.intToMonthName(currentMonth) + ".");
                } catch (Exception e) {
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                try {
                    Payment payment = dataSnapshot.getValue(Payment.class);
                    payment.timestamp = dataSnapshot.getKey();
                    payments.remove(payment);
                    adapter.notifyDataSetChanged();

                    tStatus.setText("Found " + payments.size() + " payments for " + Month.intToMonthName(currentMonth) + ".");
                } catch (Exception e) {
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bPrevious:
                break;
            case R.id.bNext:
                break;
            case R.id.fabAdd:
                break;
        }
    }
}
