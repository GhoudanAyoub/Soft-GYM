package com.exemple.stage.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.exemple.stage.Adapters.AdminUserAdapter;
import com.exemple.stage.R;
import com.exemple.stage.modele.Abonner;
import com.exemple.stage.modele.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersListActivity extends AppCompatActivity {

    RecyclerView RecyleViewUsers;
    DatabaseReference databaseReference, databaseReference2;
    List<Abonner> abonnerList;
    AdminUserAdapter adminUserAdapter;
    User user;
    Abonner abonner;
    Spinner TypeSpinner;
    String l;
    Button Find;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        RecyleViewUsers = findViewById(R.id.RecyleViewUsers);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);
        userList = new ArrayList<>();
        RecyleViewUsers.setHasFixedSize(true);
        RecyleViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Abonner");
        databaseReference2.keepSynced(true);
        abonnerList = new ArrayList<>();
        TypeSpinner = findViewById(R.id.TypeSpinner);
        Find = findViewById(R.id.Find);

        String[] Tp2 = {"Choose a Type :", "Subscriber", "Not Subscriber"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_dropdown_item, Tp2);
        TypeSpinner.setAdapter(adapter2);
        TypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Choose a Subscriber Or Not Subscriber !! ", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        l = "approved";
                        break;
                    case 2:
                        l = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("status").equalTo(l).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userList.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                user = dataSnapshot1.getValue(User.class);
                                userList.add(user);
                            }
                            adminUserAdapter = new AdminUserAdapter(getApplicationContext(), userList);
                            RecyleViewUsers.setAdapter(adminUserAdapter);

                        } else {
                            Toast.makeText(getApplicationContext(), "No Users For The Moment !!", Toast.LENGTH_LONG).show();
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        user = dataSnapshot1.getValue(User.class);
                        userList.add(user);
                    }
                    adminUserAdapter = new AdminUserAdapter(getApplicationContext(), userList);
                    RecyleViewUsers.setAdapter(adminUserAdapter);


                } else {
                    Toast.makeText(getApplicationContext(), "No Users For The Moment !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        super.onStart();
    }
}
