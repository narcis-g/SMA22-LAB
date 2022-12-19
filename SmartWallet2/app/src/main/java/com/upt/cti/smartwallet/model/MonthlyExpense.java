package com.upt.cti.smartwallet.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Alexander on 16-Oct-16.
 */
@IgnoreExtraProperties
public class MonthlyExpense {

    public String month;
    private float income;
    private float expenses;

    public MonthlyExpense() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getExpenses() {
        return expenses;
    }

    public Float getIncome() {
        return income;
    }

    @Override
    public String toString() {
        return month;
    }
}