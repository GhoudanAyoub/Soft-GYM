package com.exemple.stage.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Admin.ADMINside1;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.NewStart;
import com.exemple.stage.R;
import com.exemple.stage.modele.User;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

@SuppressLint("CheckResult")
public class Authentication extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1000;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        // Choose authentication
        providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());
        RxView.clicks(findViewById(R.id.buttonphone))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    findViewById(R.id.buttonphone).setClickable(false);
                    PreBuildLogin();});
    }

    private void checkUserFromDataBase(FirebaseUser user) {
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
                                    Toast.makeText(getApplicationContext(), " مرحبا بعودتك " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), NewStart.class));
                                }
                            }
                        }else {
                            FireBaseClient.getInstance().setFirebaseUser(user);
                            startActivity(new Intent(getApplicationContext(), CreateAccount.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void PreBuildLogin() {
        progressDialog.setMessage("المرجو الانتظار قليلا ⌛️");
        progressDialog.show();
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.activity_authentification)
                .setGoogleButtonId(R.id.buttonphone)
                .build();
// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setAuthMethodPickerLayout(authMethodPickerLayout)
                        .setTheme(R.style.AppTheme)      // Set theme
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == Activity.RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                try {
                    FireBaseClient.getInstance().setFirebaseUser(user);
                    progressDialog.dismiss();
                    checkUserFromDataBase(user);
                    //startActivity(new Intent(getApplicationContext(), Create_Account.class));
                } catch (Exception e) {
                    Log.e("",e.getMessage());
                }
            } else {
                if (response == null) {
                    finish();
                }
                assert response != null;
                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //Show No Internet Notification
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, response.getError().getErrorCode(), Toast.LENGTH_LONG).show();
                    Log.d("",String.valueOf(response.getError().getErrorCode()));
                }
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() { }

}
