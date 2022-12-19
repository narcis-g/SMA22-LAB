package com.upt.cti.smartwallet.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.upt.cti.smartwallet.R;
import com.upt.cti.smartwallet.model.Payment;
import com.upt.cti.smartwallet.model.PaymentType;

import java.util.List;

/**
 * Created by Alexander on 30-Oct-16.
 */
public class PaymentAdapter extends ArrayAdapter<Payment> {

    private final Context context;
    private final List<Payment> payments;
    private final int layoutResID;

    public PaymentAdapter(Context context, int layoutResourceID, List<Payment> payments) {
        super(context, layoutResourceID, payments);
        this.context = context;
        this.payments = payments;
        this.layoutResID = layoutResourceID;
    }

    @Override
    @SuppressWarnings("all")
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            viewHolder = new ViewHolder();

            view = inflater.inflate(layoutResID, parent, false);
            viewHolder.tIndex = (TextView) view.findViewById(R.id.tIndex);
            viewHolder.tName = (TextView) view.findViewById(R.id.tName);
            viewHolder.lHeader = (RelativeLayout) view.findViewById(R.id.lHeader);
            viewHolder.tDate = (TextView) view.findViewById(R.id.tDate);
            viewHolder.tTime = (TextView) view.findViewById(R.id.tTime);
            viewHolder.tCost = (TextView) view.findViewById(R.id.tCost);
            viewHolder.tType = (TextView) view.findViewById(R.id.tType);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Payment payment = payments.get(position);

        viewHolder.tIndex.setText(String.valueOf(position + 1));
        viewHolder.tName.setText(payment.getName());
        viewHolder.lHeader.setBackgroundColor(PaymentType.getColorFromPaymentType(payment.getType()));
        viewHolder.tCost.setText(payment.getCost() + " LEI");
        viewHolder.tType.setText(payment.getType());
        viewHolder.tDate.setText("Date: " + payment.timestamp.substring(0, 10));
        viewHolder.tTime.setText("Time: " + payment.timestamp.substring(11));

        return view;
    }

    private static class ViewHolder {
        TextView tIndex, tName;
        RelativeLayout lHeader;
        TextView tDate, tTime;
        TextView tCost, tType;
    }
}