package com.exemple.stage.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.exemple.stage.Adapters.AdminVideoAdapter;
import com.exemple.stage.R;
import com.exemple.stage.modele.Videos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminVideoListActivity extends AppCompatActivity {


    Button Add;
    RecyclerView RecycleViewAdminVideo;
    DatabaseReference databaseReference;
    Videos videos;
    List<Videos> videosList;
    AdminVideoAdapter adminVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_video_list);

        RecycleViewAdminVideo = findViewById(R.id.RecycleViewAdminVideo);
        Add = findViewById(R.id.Add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPractice.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference.keepSynced(true);
        videosList = new ArrayList<>();
        RecycleViewAdminVideo.setHasFixedSize(true);
        RecycleViewAdminVideo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        videos = dataSnapshot1.getValue(Videos.class);
                        videosList.add(videos);
                    }
                    adminVideoAdapter = new AdminVideoAdapter(getApplicationContext(), videosList);
                    RecycleViewAdminVideo.setAdapter(adminVideoAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are You Sure You Want To DELETE");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
