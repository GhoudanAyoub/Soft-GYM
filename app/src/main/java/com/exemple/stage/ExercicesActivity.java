package com.exemple.stage;
/*
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.Adapters.ExerciceAdapter;
import com.exemple.stage.modele.Exercice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExercicesActivity extends AppCompatActivity {


    DatabaseReference databaseReference;
    ProgressBar progressBar4;
    String gmail;
    private RecyclerView listView;
    private List<Exercice> exerciceList;
    private ExerciceAdapter exerciceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_exercices);
        } catch (Exception E) {
            Toast.makeText(getApplicationContext(), "Your Account Have benn suspended by Admin  !!! ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), NewStart.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        databaseReference = FirebaseDatabase.getInstance().getReference("Exercice");
        exerciceList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressBar4 = findViewById(R.id.progressBar4);
        progressBar4.setVisibility(View.GONE);
        gmail = getIntent().getStringExtra("gmail");
    }

    @Override
    public void onStart() {
        progressBar4.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    exerciceList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Exercice Exercice = dataSnapshot1.getValue(Exercice.class);
                        exerciceList.add(Exercice);
                    }
                    exerciceAdapter = new ExerciceAdapter(getApplicationContext(), exerciceList);
                    listView.setAdapter(exerciceAdapter);
                    progressBar4.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Practice Coming Soon !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
