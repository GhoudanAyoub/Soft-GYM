package com.exemple.stage.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.Company.ActivitySetting;
import com.exemple.stage.Company.ContactUs;
import com.exemple.stage.ListOfSongsActivity;
import com.exemple.stage.NewStart;
import com.exemple.stage.R;
import com.exemple.stage.Youtube.Home;
import com.exemple.stage.modele.Abonner;
import com.exemple.stage.modele.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


@SuppressLint("SetTextI18n")
public class profile extends AppCompatActivity {


    TextView ClientName, Abonnerdate, Status;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SetView();
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chat, R.id.navigation_fav)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigation, navController);

        findViewById(R.id.floatingActionButton).setOnClickListener(l1 -> {
        });
        findViewById(R.id.floatingActionButton2).setOnClickListener(l2 -> startActivity(new Intent(getApplicationContext(), NewStart.class)));
        findViewById(R.id.floatingActionButton3).setOnClickListener(l3 -> {
            FireBaseClient.getInstance().getFirebaseAuth().signOut();
            startActivity(new Intent(getApplicationContext(), Authentication.class));
        });
        findViewById(R.id.floatingActionButton4).setOnClickListener(l4 -> startActivity(new Intent(getApplicationContext(), ContactUs.class)));
        findViewById(R.id.floatingActionButton5).setOnClickListener(l5 -> startActivity(new Intent(getApplicationContext(), Home.class)));
        findViewById(R.id.floatingActionButton6).setOnClickListener(l6 -> startActivity(new Intent(getApplicationContext(), ListOfSongsActivity.class)));
        findViewById(R.id.floatingActionButton7).setOnClickListener(l7 -> startActivity(new Intent(getApplicationContext(), ActivitySetting.class)));
        findViewById(R.id.button5).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EditProfile.class)));
    }

    private void SetView() {
        ClientName = findViewById(R.id.ClientName);
        photo = findViewById(R.id.photo);
        Abonnerdate = findViewById(R.id.dateAbonner);
        Status = findViewById(R.id.Status);
    }

    @Override
    public void onStart() {
        super.onStart();
        GetUserData();
    }

    private void GetUserData() {
        FireBaseClient.getInstance().getFirebaseDatabase()
                .getReference("Users")
                .orderByChild("gmail")
                .equalTo(Commun.Current_Client_Gmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                try {
                                    ClientName.setText(Objects.requireNonNull(dataSnapshot1.getValue(User.class)).getName());
                                    Glide.with(getApplicationContext())
                                            .load(Objects.requireNonNull(dataSnapshot1.getValue(User.class)).getImage())
                                            .centerCrop()
                                            .placeholder(R.drawable.no_image_available)
                                            .into(photo);
                                } catch (Exception e) {
                                    Log.e("failed", "onDataChangeProfile: " + e.getMessage());
                                }
                            }
                            GetSubscribedData();
                        } else {
                            ClientName.setText("WELCOME PLEASE EDIT YOUR PROFILE");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void GetSubscribedData() {
        FireBaseClient.getInstance().getFirebaseDatabase()
                .getReference("Abonner")
                .orderByChild("gmail")
                .equalTo(Commun.Current_Client_Gmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Abonnerdate.setText(Integer.toString(Objects.requireNonNull(dataSnapshot1.getValue(Abonner.class)).days));
                                    Status.setText(Objects.requireNonNull(dataSnapshot1.getValue(Abonner.class)).status);
                                }
                            } else {
                                Abonnerdate.setText("You Are Not Subscribed");
                            }
                        } catch (Throwable e) {
                            Log.e("failed", "onDataChangeProfile : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
