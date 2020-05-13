package com.exemple.stage.Profile;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.exemple.stage.R;
import com.exemple.stage.start;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.rxbinding3.view.RxView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class CreateAccount extends AppCompatActivity {


    MaterialEditText Email, Password, Password2;
    Button Create;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Create = findViewById(R.id.Create);
        Email = findViewById(R.id.EmailSignin);
        Password = findViewById(R.id.PassworSignin);
        Password2 = findViewById(R.id.Password2Signin);

        RxView.clicks(Create)
                .throttleFirst(5, TimeUnit.SECONDS)
                .takeWhile(unit -> false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        createAccount(Email.getText().toString(), Password.getText().toString(), Password2.getText().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Check Your Internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Welcome Mr" + mUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    }
                });

        //************* ADS
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void createAccount(String email, String password, String password2) {
        //empty  champ
        if (!validateForm()) return;
        if (!pass(password, password2)) return;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                mUser = mAuth.getCurrentUser();
                startActivity(new Intent(getApplicationContext(), start.class).putExtra("gmail", email));
            } else {
                Toast.makeText(getApplicationContext(), "createUserWithEmail:failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean pass(String password, String password2) {
        boolean valid = true;
        if (!password.matches(password2)) {
            Password2.setError("Did Not mutche.");
            valid = false;
        } else {
            Password2.setError(null);
        }
        return valid;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email1 = Email.getText().toString();
        if (TextUtils.isEmpty(email1)) {
            Email.setError("Required.");
            valid = false;
        } else {
            Email.setError(null);
        }

        String password1 = Password.getText().toString();
        if (TextUtils.isEmpty(password1)) {
            Password.setError("Required.");
            valid = false;
        } else {
            Password.setError(null);
        }

        String password2 = Password2.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            Password2.setError("Required.");
            valid = false;
        } else {
            Password2.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitial();
        compositeDisposable.clear();
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
                Toast.makeText(getApplicationContext(), "Faild", Toast.LENGTH_SHORT);
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
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT);
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
