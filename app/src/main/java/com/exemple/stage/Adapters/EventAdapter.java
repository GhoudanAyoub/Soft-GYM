package com.exemple.stage.Adapters;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.exemple.stage.R;
import com.exemple.stage.modele.eventt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ImageViewHolder> {
    int t = 0;
    private Context mContext;
    private List<eventt> mUploads;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("eventt");

    public EventAdapter(Context context, List<eventt> uploads) {
        this.mUploads = uploads;
        this.mContext = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.evnt_list_delete, parent, false);
        return new EventAdapter.ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder Holder, final int position) {
        final eventt eventt = mUploads.get(position);
        Holder.t1.setText("Your Event is : " + eventt.getText());
        Holder.t2.setText("Date : " + eventt.getDate());
        Holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.orderByChild("Text").equalTo(eventt.getText()).addListenerForSingleValueEvent(new ValueEventListener() {
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView t1, t2;
        Button delete;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.EventText2);
            t2 = itemView.findViewById(R.id.DateText2);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
