package com.exemple.stage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.R;
import com.exemple.stage.modele.Exercice;
import com.exemple.stage.modele.Videos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminVideoAdapter extends RecyclerView.Adapter<AdminVideoAdapter.ViewHolder> {

    Exercice exercice;
    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Planning");
    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Exercice");
    private Context mContext;
    private List<Videos> mVideo;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Videos");

    public AdminVideoAdapter(Context mContext, List<Videos> mVideo) {
        this.mVideo = mVideo;
        this.mContext = mContext;
    }

    @Override
    public AdminVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.admin_video_layout, parent, false);
        return new AdminVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Videos videos = mVideo.get(i);
        viewHolder.URlVideo.setText("https://www.youtube.com/watch?v=" + videos.getVideo());
        viewHolder.TitreVideo.setText(videos.getVideoName());
        viewHolder.TypeVideo.setText("");

        databaseReference2.orderByChild("ID").equalTo(videos.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        exercice = dataSnapshot1.getValue(Exercice.class);
                    }
                    viewHolder.TypeVideo.setText(exercice.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ViewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.orderByChild("ID").equalTo(videos.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                databaseReference2.orderByChild("ID").equalTo(videos.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds2 : dataSnapshot.getChildren()) {
                            ds2.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                databaseReference3.orderByChild("videoName").equalTo(videos.getVideoName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds3 : dataSnapshot.getChildren()) {
                            ds3.getRef().removeValue();
                        }
                        Toast.makeText(mContext, "Deleted Successfully !!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        static Button Delete;
        TextView URlVideo, TitreVideo, TypeVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            URlVideo = itemView.findViewById(R.id.URlVideo);
            TitreVideo = itemView.findViewById(R.id.TitreVideo);
            TypeVideo = itemView.findViewById(R.id.TypeVideo);
            Delete = itemView.findViewById(R.id.Delete);
        }
    }
}