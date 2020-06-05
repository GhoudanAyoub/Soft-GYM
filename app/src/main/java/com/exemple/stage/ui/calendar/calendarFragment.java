package com.exemple.stage.ui.calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Adapters.EventAdapter;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.R;
import com.exemple.stage.modele.eventt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.exemple.stage.R.id.calendarView;

public class calendarFragment extends Fragment {

    private MaterialEditText testEvent;
    private EventAdapter eventAdapter;
    private RecyclerView RecycleCalendar;
    private String d;
    private List<eventt> eventtList  = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        CalendarView calendar = view.findViewById(calendarView);
        testEvent = view.findViewById(R.id.testEvent);
        RecycleCalendar = view.findViewById(R.id.RecycleCalendar);
        RecycleCalendar.setHasFixedSize(true);
        RecycleCalendar.setLayoutManager(new LinearLayoutManager(requireActivity()));

        calendar.showCurrentMonthPage();
        calendar.setAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.downtoup));
        view.findViewById(R.id.LN13).setAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.downtoup));
        view.findViewById(R.id.LN12).setAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.uptodown));
        view.findViewById(R.id.AddEvent).setOnClickListener(v -> AddEvent());
        view.findViewById(R.id.choosedays).setOnClickListener(v -> PickDate());
        Init();
        return  view;
    }
    private void Init(){
        FireBaseClient.getInstance().getFirebaseDatabase()
                .getReference("eventt")
                .orderByChild("gmail")
                .equalTo(Commun.Email_User)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            eventtList.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                eventt eventt = dataSnapshot1.getValue(eventt.class);
                                eventtList.add(eventt);
                            }
                            eventAdapter = new EventAdapter(requireActivity(), eventtList);
                            RecycleCalendar.setAdapter(eventAdapter);
                        } else {
                            Toast.makeText(requireActivity(), "You Have No Event Yet !!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) { }
                });
    }
    private void PickDate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                (view1, year, monthOfYear, dayOfMonth) ->
                        d = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void AddEvent(){
        if (!validateForm()) return;
        final String text = Objects.requireNonNull(testEvent.getText()).toString();
        FireBaseClient.getInstance().getFirebaseDatabase()
                .getReference("eventt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        FireBaseClient.getInstance()
                                .getFirebaseDatabase()
                                .getReference("eventt").push()
                                .setValue(new eventt(text,d,Commun.Email_User));
                        Toast.makeText(requireActivity(), "Your Have Added An Event !!!! ", Toast.LENGTH_LONG).show();
                        testEvent.setText("");
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) { }
                });
    }
    private boolean validateForm() {
        boolean valid = true;
        String name1 = Objects.requireNonNull(testEvent.getText()).toString();
        if (TextUtils.isEmpty(name1)) {
            testEvent.setError("Required.");
            valid = false;
        } else {
            testEvent.setError(null);
        }
        return valid;
    }

}
