package com.exemple.stage.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.exemple.stage.Commun.Commun;
import com.exemple.stage.modele.Videos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeModelView extends ViewModel {

    private List<Videos> videosList = new ArrayList<>();
    private MutableLiveData<List<Videos>> VideosLiveData = null;

    public MutableLiveData<List<Videos>> getVideosLiveData() {
        return VideosLiveData;
    }

    //Planning Data
    public void GetVideosData(){
        FirebaseDatabase.getInstance()
                .getReference(Commun.Videos_Class_Name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Videos videos = dataSnapshot.getValue(Videos.class);
                                videosList.add(videos);
                            }
                            VideosLiveData.setValue(videosList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
