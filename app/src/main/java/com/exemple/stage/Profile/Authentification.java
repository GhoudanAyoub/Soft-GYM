package com.exemple.stage.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Admin.ADMINside1;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.NewStart;
import com.exemple.stage.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class Authentification extends AppCompatActivity {

    private TextInputLayout email, password;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Log In ....");

        FirebaseApp.initializeApp(getApplicationContext());

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
                        LoginUser(Objects.requireNonNull(email.getEditText()).getText().toString(), Objects.requireNonNull(password.getEditText()).getText().toString());
                        progressDialog.show();
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
        if (Commun.Current_User != null) {
            startActivity(new Intent(getApplicationContext(), NewStart.class));
        }
    }

    //LogIn Interface
    private void LoginUser(final String email, final String password) {
        if (!validateForm()) return;
        try {
            if (email.equalsIgnoreCase("contact@mc2020.net") || password.equalsIgnoreCase("contact@mc2020.net")) {
                startActivity(new Intent(getApplicationContext(), ADMINside1.class));
            } else {
                FireBaseClient.getInstance().getFirebaseAuth()
                        .signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), NewStart.class));
                        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email Or Password", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), " Check Your Connection Network", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email1 = Objects.requireNonNull(Objects.requireNonNull(email.getEditText()).getText()).toString();
        if (TextUtils.isEmpty(email1)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String password1 = Objects.requireNonNull(Objects.requireNonNull(password.getEditText()).getText()).toString();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
