package com.exemple.stage;
/**
 * Created By GHOUADN AYOUB
 */


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.exemple.stage.Adapters.EventAdapter;
import com.exemple.stage.modele.eventt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.exemple.stage.R.id.calendarView;

public class Calandar extends AppCompatActivity {

    CalendarView calandare;
    LinearLayout LN12, LN13;
    Animation uptodown, downtoup;
    MaterialEditText testEvent;
    Button AddEvent, choosedays, delete;
    DatabaseReference databaseReference;
    String gmail;
    EventAdapter eventAdapter;
    RecyclerView RecycleCalendar;
    String d;
    private List<eventt> eventtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calandar);

        calandare = findViewById(calendarView);
        calandare.showCurrentMonthPage();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        choosedays = findViewById(R.id.choosedays);
        AddEvent = findViewById(R.id.AddEvent);
        testEvent = findViewById(R.id.testEvent);
        RecycleCalendar = findViewById(R.id.RecycleCalendar);
        RecycleCalendar.setHasFixedSize(true);
        RecycleCalendar.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference("eventt");

        gmail = getIntent().getStringExtra("gmail");
        eventtList = new ArrayList<>();

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        LN12 = findViewById(R.id.LN12);
        LN13 = findViewById(R.id.LN13);
        LN13.setAnimation(downtoup);
        LN12.setAnimation(uptodown);
        calandare.setAnimation(downtoup);

        choosedays.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Calandar.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    d = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        databaseReference.orderByChild("gmail").equalTo(gmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    eventtList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        eventt eventt = dataSnapshot1.getValue(eventt.class);
                        eventtList.add(eventt);
                    }
                    eventAdapter = new EventAdapter(getApplicationContext(), eventtList);
                    RecycleCalendar.setAdapter(eventAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "You Have No Event Yet !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        AddEvent.setOnClickListener(v -> {
            if (!validateForm()) return;
            final String text = testEvent.getText().toString();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference newPost = databaseReference.push();
                    newPost.child("Text").setValue(text);
                    newPost.child("Date").setValue(d);
                    newPost.child("gmail").setValue(gmail);
                    Toast.makeText(getApplicationContext(), "Your Have Added An eventt !!!! ", Toast.LENGTH_LONG).show();
                    testEvent.setText("");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean validateForm() {
        boolean valid = true;

        String name1 = testEvent.getText().toString();
        if (TextUtils.isEmpty(name1)) {
            testEvent.setError("Required.");
            valid = false;
        } else {
            testEvent.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
