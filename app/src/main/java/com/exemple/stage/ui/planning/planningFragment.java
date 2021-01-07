package com.exemple.stage.ui.planning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.Adapters.PlanningAd;
import com.exemple.stage.R;
import com.exemple.stage.modele.Videos;
import com.exemple.stage.ui.HomeModelView;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class planningFragment extends Fragment {

    private HomeModelView homeModelView;
    private PlanningAd planningAd;
    private RecyclerView recyclerView;
    @BindView(R.id.spinnerDif)
    Spinner spinnerDif;


    @OnClick(R.id.FIND)
    void FIND(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.planning_fragment, container, false);
        _View(view);
        return view;
    }

    private void _View(View view) {
        planningAd = new PlanningAd(requireActivity());
        homeModelView = new ViewModelProvider(requireActivity()).get(HomeModelView.class);
        homeModelView.GetVideosData();
        recyclerView = view.findViewById(R.id.PlanningRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(planningAd);

        /*
        String[] Tp2 = {"Beginner", "Advanced"};
        spinnerDif.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, Tp2));
        spinnerDif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //l = "Beginner";
                        break;
                    case 1:
                        //l = "Advanced";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
        homeModelView.getVideosLiveData().observe(requireActivity(),videos -> planningAd.setList(videos));
    }
}
