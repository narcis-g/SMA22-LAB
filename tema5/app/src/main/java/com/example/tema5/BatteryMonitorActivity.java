package com.example.tema5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;


public class BatteryMonitorActivity extends AppCompatActivity {

    Button button,batteryStatusButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

        button = findViewById(R.id.button);
        batteryStatusButton = findViewById(R.id.batteryStatusButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BatteryMonitorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null,intentFilter);

        batteryStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

                String chargingStatus, chargingType;

                if (isCharging)
                    chargingStatus = "Charging";
                else
                    chargingStatus = "Not charging";

                if (usbCharge)
                    chargingType = "Connected to USB";
                else if (acCharge)
                    chargingType = "Connected to AC";
                else
                    chargingType = "Not connected";


                Intent newIntent = new Intent(BatteryMonitorActivity.this, MainActivity.class);
                newIntent.putExtra("status",status);
                PendingIntent pendingIntent = PendingIntent.getActivity(BatteryMonitorActivity.this, 0, newIntent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BatteryMonitorActivity.this, MainActivity.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Charging status:")
                        .setContentText(chargingStatus).
                        setSubText(chargingType)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BatteryMonitorActivity.this);

                notificationManager.notify(MainActivity.notificationId, mBuilder.build());
            }
        });

    }
}