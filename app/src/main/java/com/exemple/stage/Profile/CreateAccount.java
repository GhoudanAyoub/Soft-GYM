package com.exemple.stage.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.NewStart;
import com.exemple.stage.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class CreateAccount extends AppCompatActivity {


    private TextInputLayout Email, Password, Password2;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Email = findViewById(R.id.EmailSignin);
        Password = findViewById(R.id.PassworSignin);
        Password2 = findViewById(R.id.Password2Signin);

        RxView.clicks(findViewById(R.id.Create))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        createAccount(Objects.requireNonNull(Email.getEditText()).getText().toString(), Objects.requireNonNull(Password.getEditText()).getText().toString(),
                                Objects.requireNonNull(Password2.getEditText()).getText().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Failed", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
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
        try {
            FireBaseClient.getInstance().getFirebaseAuth()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), NewStart.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "createUserWithEmail:failure", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean pass(String password, String password2) {
        boolean valid = true;
        if (!password.equals(password2)) {
            Password2.setError("Did Not mutche.");
            valid = false;
        } else {
            Password2.setError(null);
        }
        return valid;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email1 = Objects.requireNonNull(Email.getEditText()).getText().toString();
        if (TextUtils.isEmpty(email1)) {
            Email.setError("Required.");
            valid = false;
        } else {
            Email.setError(null);
        }

        String password1 = Objects.requireNonNull(Password.getEditText()).getText().toString();
        if (TextUtils.isEmpty(password1)) {
            Password.setError("Required.");
            valid = false;
        } else {
            Password.setError(null);
        }

        String password2 = Objects.requireNonNull(Password2.getEditText()).getText().toString();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
