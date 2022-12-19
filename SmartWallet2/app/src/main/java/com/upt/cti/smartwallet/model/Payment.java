package com.upt.cti.smartwallet.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Alexander on 30-Oct-16.
 */
@IgnoreExtraProperties
public class Payment implements Serializable {

    public String timestamp;
    private double cost;
    private String name;
    private String type;
    private String iconUrl;

    public Payment() {
        // Default constructor required for calls to DataSnapshot.getValue(Payment.class)
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Payment) {
            Payment p = (Payment) o;
            return p.timestamp.equals(this.timestamp);
        } else
            return false;
    }

    public void update(Payment payment) {
        this.name = payment.name;
        this.cost = payment.cost;
        this.type = payment.type;
    }
}