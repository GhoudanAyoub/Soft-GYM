package com.exemple.stage.Profile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.R;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText send_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        send_email = findViewById(R.id.send_email);
        findViewById(R.id.btn_reset).setOnClickListener(view -> {
            String email = send_email.getText().toString();
            try {
                if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    FireBaseClient.getInstance().getFirebaseAuth()
                            .sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "Please check you Email", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Authentification.class));
                                }
                            });
                }
            } catch (Throwable e) {
                Log.e("Field", "ResetPassword: "+e.getMessage());
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
