package com.exemple.stage.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.exemple.stage.ExercicesActivity;
import com.exemple.stage.PlanningActivity;
import com.exemple.stage.Profile.Authentification;
import com.exemple.stage.R;
import com.exemple.stage.message;
import com.exemple.stage.modele.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ADMINside1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String gmail;
    boolean shouldAllowBack = false;
    BarData data;
    DatabaseReference databaseReference;
    User user;
    private FirebaseAuth mAuth;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminside1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        gmail = getIntent().getStringExtra("gmail");
        mAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userList = new ArrayList<>();

        final ArrayList arrayList = new ArrayList<>();
        int i = 0;
        int j = 1, k = 0;
        do {
            arrayList.add(new Entry(j, i));
            i++;
            j++;
            k++;
        } while (k < 3);
        drawChart(arrayList);
   /*     databaseReference.orderByChild("days").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        user = dataSnapshot1.getValue(User.class);
                        if (user.Days == 355 || user.Days == 180 || user.Days == 90) {

                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Users For The Moment !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
*/

        BarChart chart = findViewById(R.id.chart);


        data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.animateXY(2500, 2500);
        chart.invalidate();

    }

    private void drawChart(ArrayList arrayList) {
        PieChart pieChart = findViewById(R.id.chart2);
        pieChart.setUsePercentValues(true);

        PieDataSet dataSet = new PieDataSet(arrayList, "Subsrcibers By Package Type");
        ArrayList year = new ArrayList();
        year.add("12 month Pack");
        year.add("6 month Pack");
        year.add("3 month Pack");
        try {
            PieData data = new PieData(year, dataSet);
            pieChart.setData(data);
        } catch (Exception e) {
        }
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(3000, 3000);

    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(0, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(0, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(0, 2);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(2, 3);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(1, 4);
        valueSet1.add(v1e5);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Number Of Subscribers");
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!shouldAllowBack) {
                // doSomething();
            } else {
                Toast.makeText(getApplicationContext(), "You Need To LogIn", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_videos) {
            Intent intent = new Intent(getApplicationContext(), AdminVideoListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_users) {
            Intent intent = new Intent(getApplicationContext(), AdminUsersListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat) {
            Intent intent = new Intent(getApplicationContext(), message.class);
            intent.putExtra("gmail", gmail);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            mAuth.signOut();
            Intent intent = new Intent(ADMINside1.this, Authentification.class);
            startActivity(intent);
        } else if (id == R.id.nav_videoList) {
            Intent intent = new Intent(ADMINside1.this, ExercicesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Planning) {
            Intent intent = new Intent(ADMINside1.this, PlanningActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
