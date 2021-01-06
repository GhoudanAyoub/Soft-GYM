package com.exemple.stage.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.NewStart;
import com.exemple.stage.R;
import com.exemple.stage.modele.User;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

@SuppressLint("CheckResult")
public class CreateAccount extends AppCompatActivity {


    private EditText editTextTextPersonName, editTextPhone, editTextTextEmailAddress;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        _view();
        RxView.clicks(findViewById(R.id.Create))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit ->
                        _SaveUser(editTextTextPersonName.getText().toString(), editTextTextEmailAddress.getText().toString(),
                                editTextPhone.getText().toString()),Throwable::printStackTrace);

    }

    private void _view(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);

        //************* ADS
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private boolean validateForm() {
        boolean valid = true;

        String name1 = editTextTextPersonName.getText().toString();
        if (TextUtils.isEmpty(name1)) {
            editTextTextPersonName.setError("Required.");
            valid = false;
        } else {
            editTextTextPersonName.setError(null);
        }

        String phone1 = editTextPhone.getText().toString();
        if (TextUtils.isEmpty(phone1)) {
            editTextPhone.setError("Required.");
            valid = false;
        } else {
            editTextPhone.setError(null);
        }

        String email1 = editTextTextEmailAddress.getText().toString();
        if (TextUtils.isEmpty(email1)) {
            editTextTextEmailAddress.setError("Required.");
            valid = false;
        } else {
            editTextTextEmailAddress.setError(null);
        }

        return valid;
    }

    private void _SaveUser(String name, String email, String phone) {
        //empty  champ
        if (!validateForm()) return;
        String key = FirebaseDatabase.getInstance().getReference(Commun.User_Class_Name).push().getKey();
        if (key!=null)
            FirebaseDatabase.getInstance()
                    .getReference(Commun.User_Class_Name)
                    .child(key)
                    .setValue(new User(key,name,email,phone))
                    .addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Failed", "Failed Adds");
            }

            @Override
            public void onAdClosed() {
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.e("Failed", "Ad did not load");
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

}
