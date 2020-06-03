package com.exemple.stage.Profile;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.Admin.ADMINside1;
import com.exemple.stage.R;
import com.exemple.stage.ui.NewStart;
import com.google.firebase.FirebaseApp;
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

public class Authentification extends AppCompatActivity {

    MaterialEditText email, password;
    boolean shouldAllowBack = false;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar3);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        progressBar.setVisibility(View.GONE);

        findViewById(R.id.forgot_password).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class)));
        findViewById(R.id.Signin).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CreateAccount.class)));


        RxView.clicks(findViewById(R.id.LogIn))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        progressBar.setVisibility(View.VISIBLE);
                        LoginUser(email.getText().toString(), password.getText().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Done!!!!Welcome", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
    }

    //LogIn Interface
    private void LoginUser(final String email, final String password) {

        if (!validateForm()) {
            return;
        }
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (email.equalsIgnoreCase("contact@mc2020.net") || password.equalsIgnoreCase("contact@mc2020.net")) {
                        startActivity(new Intent(getApplicationContext(), ADMINside1.class));

                    } else {
                        startActivity(new Intent(getApplicationContext(), NewStart.class).putExtra("gmail", email));

                    }
                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Email Or Password", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), " Check Your Connection Network", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email1 = email.getText().toString();
        if (TextUtils.isEmpty(email1)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String password1 = password.getText().toString();
        if (TextUtils.isEmpty(password1)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    @Override
    public void onBackPressed() {
        if (!shouldAllowBack) {
            // doSomething();
        } else {
            Toast.makeText(getApplicationContext(), "You Need To LogIn", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
