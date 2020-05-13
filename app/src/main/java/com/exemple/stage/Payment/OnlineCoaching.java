package com.exemple.stage.Payment;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exemple.stage.R;

public class OnlineCoaching extends AppCompatActivity {

    TextView DISCOVER1, DISCOVER2, DISCOVER3;
    LinearLayout DISCOVER4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_online_coaching);
        } catch (Exception e) {
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String gmail = getIntent().getStringExtra("gmail");

        DISCOVER1 = findViewById(R.id.DISCOVER1);
        DISCOVER2 = findViewById(R.id.DISCOVER2);
        DISCOVER3 = findViewById(R.id.DISCOVER3);
        DISCOVER4 = findViewById(R.id.DISCOVER4);

        try {
            DISCOVER1.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), OnlineCoaching2.class);
                intent.putExtra("gmail", gmail);
                startActivity(intent);
            });
        } catch (Exception e) {
        }
        DISCOVER2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OnlineCoaching2.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        });
        DISCOVER3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OnlineCoaching2.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        });
        DISCOVER4.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OnlineCoaching2.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
