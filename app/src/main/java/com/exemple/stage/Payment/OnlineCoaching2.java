package com.exemple.stage.Payment;
/**
 * Created By GHOUADN AYOUB
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.exemple.stage.R;
import com.exemple.stage.modele.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class OnlineCoaching2 extends AppCompatActivity {
    private static final String TAG = "paymentExample";
    static int key;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId("ATuKpjyRLFj644qCGiGc5luEkb7Ug0EaziwfYns1ZOZ8cWenRdaWriyO73Z_EUJQIlvMujthq2bgTaeL");
    Button i1, i2, i3;
    DatabaseReference databaseReference, reference;
    FirebaseUser fuser;
    String gmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_coaching2);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference("Abonner");
        databaseReference.keepSynced(true);

        gmail = getIntent().getStringExtra("gmail");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        i1 = findViewById(R.id.imageButton3mois);
        i2 = findViewById(R.id.imageButton6mois);
        i3 = findViewById(R.id.imageButton12mois);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal("44.77"), "EUR", "Packet 3 Mois",
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                key = 90;
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 0);
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal("77.40"), "EUR", "Packet 6 Mois",
                        PayPalPayment.PAYMENT_INTENT_SALE);
                key = 180;
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 0);

            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal("118.80"), "EUR", "Packet 12 Mois",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                key = 355;
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 0);

            }
        });
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    String paymentdetail = confirm.toJSONObject().toString(4);
                    final JSONObject jsonObject = new JSONObject(paymentdetail);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference newPost2 = databaseReference.push();
                            try {
                                newPost2.child("days").setValue(key);
                                newPost2.child("gmail").setValue(gmail);
                                newPost2.child("status").setValue(jsonObject.getJSONObject("response").getString("state"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "ERROR ON Subscriber Upload !!! ", Toast.LENGTH_SHORT).show();
                            }

                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference.orderByChild("gmail").equalTo(gmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            User user = snapshot.getValue(User.class);
                                            try {
                                                snapshot.getRef().removeValue();

                                                String key2 = reference.push().getKey();
                                                DatabaseReference newPost = reference.push();
                                                newPost.child("IDUsers").setValue(key2);
                                                newPost.child("City").setValue(user.City);
                                                newPost.child("Zip").setValue(user.Zip);
                                                newPost.child("Country").setValue(user.Country);
                                                newPost.child("Phone").setValue(user.Phone);
                                                newPost.child("Address").setValue(user.Address);
                                                newPost.child("name").setValue(user.name);
                                                newPost.child("image").setValue(user.image);
                                                newPost.child("gmail").setValue(user.gmail);
                                                newPost.child("status").setValue(jsonObject.getJSONObject("response").getString("state"));
                                                newPost.child("Days").setValue(key);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "ERROR ON Update status User!!! ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Payment Canceled!!! ", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(getApplicationContext(), "Invalid Payment ??", Toast.LENGTH_SHORT).show();
        }
    }


}

