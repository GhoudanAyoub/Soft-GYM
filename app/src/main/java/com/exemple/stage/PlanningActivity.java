package com.exemple.stage;
/*
 * Created By GHOUADN AYOUB
 */


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.Adapters.PlanningAdapter;
import com.exemple.stage.modele.Planning;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlanningActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    List<Planning> List;
    ProgressBar progressBar5;
    Spinner spinnerDif;
    Button FIND;
    String l, gmail;
    private RecyclerView listView;
    private PlanningAdapter PlanningAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        listView = findViewById(R.id.PlanningRecycler);
        progressBar5 = findViewById(R.id.progressBar5);
        spinnerDif = findViewById(R.id.spinnerDif);
        FIND = findViewById(R.id.FIND);
        listView.setHasFixedSize(true);

        listView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        databaseReference = FirebaseDatabase.getInstance().getReference("Planning");
        List = new ArrayList<>();
        progressBar5.setVisibility(View.GONE);
        gmail = getIntent().getStringExtra("gmail");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String[] Tp2 = {"Choose a Difficulty :", "Beginner", "Advanced"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, Tp2);
        spinnerDif.setAdapter(adapter2);
        spinnerDif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Choose a Beginner Or Advanced !! ", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        l = "Beginner";
                        break;
                    case 2:
                        l = "Advanced";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FIND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("Name").equalTo(l).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Planning Planning = dataSnapshot1.getValue(Planning.class);
                                List.add(Planning);
                            }
                            PlanningAdapter = new PlanningAdapter(getApplicationContext(), List);
                            listView.setAdapter(PlanningAdapter);
                            progressBar5.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Planning Coming Soon !!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        progressBar5.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Planning Planning = dataSnapshot1.getValue(Planning.class);
                        List.add(Planning);
                    }
                    PlanningAdapter = new PlanningAdapter(getApplicationContext(), List);
                    listView.setAdapter(PlanningAdapter);
                    progressBar5.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Planning Coming Soon !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
