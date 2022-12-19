package com.upt.cti.smartwallet;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MonthlyExpenses extends AppCompatActivity {

    public String month;
    private float income, expenses;


    public MonthlyExpenses(){

    }

    public MonthlyExpenses(String month, float income, float expenses){
        this.month = month;
        this.income = income;
        this.expenses = expenses;
    }

    public MonthlyExpenses(EditText income, EditText expenses) {
    }

    public String getMonth() {
        return month;
    }

    public float getIncome() {
        return income;
    }

    public float getExpenses() {
        return expenses;
    }


}
