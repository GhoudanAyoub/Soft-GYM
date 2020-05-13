package com.exemple.stage;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.exemple.stage.Company.ContactUs;


public class RS extends AppCompatActivity {


    Button fac, ins, gma;

    public RS() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rs);

        fac = findViewById(R.id.Facebook);
        ins = findViewById(R.id.Instagram);
        gma = findViewById(R.id.Gmail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://facebook.com/ayoub_ghoudan");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.setPackage("com.facebook.appmanager");
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://facebook.com")));
                }
            }
        });
        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/ayoub_ghoudan");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.setPackage("com.instagram.android");
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com")));
                }
            }
        });
        gma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
