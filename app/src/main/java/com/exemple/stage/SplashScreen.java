package com.exemple.stage;
/*
 * Created By GHOUADN AYOUB
 */


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.Commun.UserUtils;
import com.exemple.stage.Profile.Authentication;
import com.exemple.stage.modele.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalshscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth = FirebaseAuth.getInstance();

        Completable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (firebaseAuth.getCurrentUser() != null) {
                        FirebaseInstallations.getInstance()
                                .getId()
                                .addOnFailureListener(Throwable::printStackTrace)
                                .addOnSuccessListener(instanceIdResult -> {
                                    if (instanceIdResult != null)
                                        UserUtils.UpdateToken(this, instanceIdResult);
                                });
                        CheckUserInDataBase(firebaseAuth.getCurrentUser());
                    } else
                        startActivity(new Intent(getApplication(), Authentication.class));
                }, Throwable::printStackTrace);
    }

    private void CheckUserInDataBase(FirebaseUser user) {
        FireBaseClient.getInstance()
                .getFirebaseDatabase()
                .getReference(Commun.User_Class_Name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                User client = dataSnapshot1.getValue(User.class);
                                if (client!=null && (client.getIDUsers().equals(firebaseAuth.getCurrentUser().getUid()) || client.getGmail().equals(user.getEmail()))){
                                    FireBaseClient.getInstance().setFirebaseUser(user);
                                    Toasty.success(getApplicationContext(),getString(R.string.WelcomeBack) + user.getDisplayName(), Toasty.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), NewStart.class));
                                }
                            }
                        }else {
                            FireBaseClient.getInstance().setFirebaseUser(user);
                            startActivity(new Intent(getApplicationContext(), Authentication.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
