package lifecycle;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.helloandroid.MainActivity;
import com.example.helloandroid.R;

public class ActivityA extends AppCompatActivity {

    Button A_button, B_button, C_button,Home_button ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_act);

        Log.d(TAG, "onCreate A");

        Home_button = findViewById(R.id.Home_button);
        A_button = findViewById(R.id.A_button);
        B_button = findViewById(R.id.B_button);
        C_button = findViewById(R.id.C_button);


        Home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ActivityA.this, MainActivity.class);
                startActivity(intent);
            }
        });

        A_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ActivityA.this, ActivityA.class);
                startActivity(intent);
            }
        });

        B_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA.this, ActivityB.class);
                startActivity(intent);
            }
        });

        C_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA.this, ActivityC.class);
                startActivity(intent);
            }
        });
    }
}