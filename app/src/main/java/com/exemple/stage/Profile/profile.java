package com.exemple.stage.Profile;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exemple.stage.Company.ActivitySetting;
import com.exemple.stage.Company.ContactUs;
import com.exemple.stage.Fragments.FragmentFavories;
import com.exemple.stage.Fragments.FragmentMessage;
import com.exemple.stage.Payment.OnlineCoaching;
import com.exemple.stage.R;
import com.exemple.stage.Youtube.Home;
import com.exemple.stage.modele.Abonner;
import com.exemple.stage.modele.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static FragmentManager fragmentManager;
    TextView ClientName, Abonnerdate, Status;
    ImageView photo;
    List<com.exemple.stage.modele.Abonner> abonnerList;
    User CLassUser;
    Abonner Abonner;
    String gmaill;
    Button button5;
    Fragment fragment;
    private DatabaseReference userdata, abonnerdata;
    private FirebaseUser currentuser;
    private FirebaseAuth mAuth;
    private List<User> Userlist;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    fragment = new FragmentMessage();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_fav:

                    abonnerdata = FirebaseDatabase.getInstance().getReference("Abonner");
                    tsr(abonnerdata);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_fav);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        gmaill = getIntent().getStringExtra("gmail");
        /**Profile side  getting data**/

        ClientName = findViewById(R.id.ClientName);
        photo = findViewById(R.id.photo);
        button5 = findViewById(R.id.button5);
        Abonnerdate = findViewById(R.id.dateAbonner);
        Status = findViewById(R.id.Status);

        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        userdata = FirebaseDatabase.getInstance().getReference("Users");
        userdata.keepSynced(true);
        Userlist = new ArrayList<>();
        abonnerList = new ArrayList<>();

        abonnerdata = FirebaseDatabase.getInstance().getReference("Abonner");
        abonnerdata.keepSynced(true);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                intent.putExtra("gmail", gmaill);
                startActivity(intent);
            }
        });

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragmentProfile) != null) {
            if (savedInstanceState != null) {
                return;
            }


            abonnerdata.orderByChild("gmail").equalTo(gmaill).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Abonner = dataSnapshot1.getValue(Abonner.class);
                            abonnerList.add(Abonner);
                        }
                        if (Abonner.status.matches("approved")) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentFavories fragmentFavories = new FragmentFavories();
                            fragmentTransaction.add(R.id.fragmentProfile, fragmentFavories, null);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getApplicationContext(), "You Have To Subscribe first !!! ", Toast.LENGTH_LONG).show();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentMessage fragmentMessage = new FragmentMessage();
                            fragmentTransaction.add(R.id.fragmentProfile, fragmentMessage, null);
                            fragmentTransaction.commit();
                        }

                    } else {
                        Abonnerdate.setText("You Are Not Subscribed");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void tsr(DatabaseReference abonnerdata) {

        abonnerdata.orderByChild("gmail").equalTo(gmaill).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Abonner = dataSnapshot1.getValue(Abonner.class);
                        abonnerList.add(Abonner);
                    }
                    if (Abonner.status.matches("approved")) {
                        fragment = new FragmentFavories();
                        loadFragment(fragment);
                    } else {
                        Toast.makeText(getApplicationContext(), "You Have To Subscribe first !!! ", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Abonnerdate.setText("You Are Not Subscribed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onStart() {

        userdata.orderByChild("gmail").equalTo(gmaill).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        CLassUser = dataSnapshot1.getValue(User.class);
                        Userlist.add(CLassUser);
                    }
                    ClientName.setText(CLassUser.name);
                    try {
                        Glide.with(getApplicationContext()).load(CLassUser.image).centerCrop().placeholder(R.drawable.no_image_available).into(photo);
                    } catch (Exception e) {
                    }
                    //$$$$$$$$$$$$$$$$$$$$$getting subscriber data
                    abonnerdata.orderByChild("gmail").equalTo(gmaill).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Abonner = dataSnapshot1.getValue(Abonner.class);
                                    abonnerList.add(Abonner);
                                }
                                int r = Abonner.days;
                                Abonnerdate.setText(Integer.toString(r));
                                Status.setText(Abonner.status);
                            } else {
                                Abonnerdate.setText("You Are Not Subscribed");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    //$$$$$$$$$$$$$$$$$$$$ End of getting subscribers data
                } else {
                    ClientName.setText("WELCOME PLEASE EDIT YOUR PROFILE");
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), ActivitySetting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_online) {
            Intent intent = new Intent(getApplicationContext(), OnlineCoaching.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(getApplicationContext(), ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_disconnect) {
            Intent intent = new Intent(getApplicationContext(), Authentification.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentProfile, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
