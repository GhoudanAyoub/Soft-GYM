package com.exemple.stage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.exemple.stage.Company.ActivitySetting;
import com.exemple.stage.Payment.OnlineCoaching;
import com.exemple.stage.Profile.Authentification;
import com.exemple.stage.Profile.profile;
import com.exemple.stage.Youtube.Home;
import com.exemple.stage.modele.Abonner;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class start extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Abonner abonner;
    String gmail;
    DatabaseReference database;
    ConstraintLayout cctt;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<Abonner> AbonnerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gmail = getIntent().getStringExtra("gmail");

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RxView.clicks(findViewById(R.id.cctt))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        drawer.openDrawer(GravityCompat.START);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        database = FirebaseDatabase.getInstance().getReference("Abonner");

        database.keepSynced(true);
        AbonnerList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), start.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_planning) {
            database.orderByChild("gmail").equalTo(gmail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            abonner = dataSnapshot1.getValue(Abonner.class);
                            AbonnerList.add(abonner);
                        }
                        if (abonner.status.matches("")) {
                            Toast.makeText(getApplicationContext(), "Your Should Think About Subscribing To Start Your Challenge !!! ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), OnlineCoaching.class);
                            startActivity(intent);
                        } else {
                            if (abonner.days == 0) {
                                Toast.makeText(getApplicationContext(), "Your Subscription Have Been Expired Think About Subscribing Again  !!! ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), OnlineCoaching.class);
                                startActivity(intent);
                            } else if (abonner.days <= 5) {
                                Toast.makeText(getApplicationContext(), "You Have Less Then 5 Days Left In Your Subscription !!! ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ExercicesActivity.class);
                                intent.putExtra("gmail", gmail);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(getApplicationContext(), ExercicesActivity.class);
                                intent.putExtra("gmail", gmail);
                                startActivity(intent);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Your Have To Subsribe First !!! ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), OnlineCoaching.class);
                        intent.putExtra("gmail", gmail);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else if (id == R.id.nav_practice) {
            Intent intent = new Intent(getApplicationContext(), PlanningActivity.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(getApplicationContext(), Calandar.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_Assisance) {
            Intent intent = new Intent(getApplicationContext(), message.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_Music) {
            Intent intent = new Intent(getApplicationContext(), ListOfSongsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(getApplicationContext(), RS.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_moreInfo) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), profile.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext(), ActivitySetting.class);
            startActivity(intent);
        } else if (id == R.id.nav_deconnecter) {
            Intent intent = new Intent(getApplicationContext(), Authentification.class);
            startActivity(intent);
        } else if (id == R.id.nav_pay) {
            Intent intent = new Intent(getApplicationContext(), OnlineCoaching.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
