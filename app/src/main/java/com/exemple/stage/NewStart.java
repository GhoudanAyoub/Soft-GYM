package com.exemple.stage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Company.ActivitySetting;
import com.exemple.stage.Profile.Authentication;
import com.exemple.stage.Profile.profile;
import com.exemple.stage.Youtube.Home;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class NewStart extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_start);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_planning, R.id.nav_practice, R.id.nav_calendar
                , R.id.nav_pay)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);


        RxView.clicks(findViewById(R.id.nav_host_fragment))
                .throttleFirst(3, TimeUnit.SECONDS)
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

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.nav_home);
                    break;
                case R.id.nav_planning:
                    navController.navigate(R.id.nav_planning);
                    break;
                case R.id.nav_practice:
                    navController.navigate(R.id.nav_practice);
                    break;
                case R.id.nav_calendar:
                    navController.navigate(R.id.nav_calendar);
                    break;
                case R.id.nav_pay:
                    navController.navigate(R.id.nav_pay);
                    break;
                case R.id.nav_Assisance:
                    startActivity(new Intent(getApplicationContext(), message.class));
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(), profile.class));
                    break;
                case R.id.nav_Music:
                    startActivity(new Intent(getApplicationContext(), ListOfSongsActivity.class));
                    break;
                case R.id.nav_share:
                    navController.navigate(R.id.nav_share);
                    break;
                case R.id.nav_moreInfo:
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    break;
                case R.id.nav_setting:
                    startActivity(new Intent(getApplicationContext(), ActivitySetting.class));
                    break;
                case R.id.nav_deconnecter:
                    FireBaseClient.getInstance().getFirebaseAuth().signOut();
                    startActivity(new Intent(getApplicationContext(), Authentication.class));
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_start, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
