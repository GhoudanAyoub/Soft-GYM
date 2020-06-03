package com.exemple.stage.Adapters;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.R;
import com.exemple.stage.modele.Planning;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PlanningAdapter extends RecyclerView.Adapter<PlanningAdapter.ImageViewHolder2> {
    DatabaseReference databaseReference;
    String gmail;
    private Context mContext;
    private List<Planning> mUploads;

    public PlanningAdapter(Context context, List<Planning> uploads) {
        this.mUploads = uploads;
        this.mContext = context;
    }

    @Override
    public PlanningAdapter.ImageViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.plannin_layout, parent, false);
        return new PlanningAdapter.ImageViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder2 Holder, int position) {

        final Planning planning = mUploads.get(position);
        Holder.Name.setText(planning.getName());
        Holder.duree.setText(planning.getDuree());
        Holder.Chapter1.setText(planning.getChapter1());
        Holder.Chapter2.setText(planning.getChapter2());
        Holder.Chapter3.setText(planning.getChapter3());
        Holder.videoName.setText(planning.getVideoName());
        Holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public static class ImageViewHolder2 extends RecyclerView.ViewHolder {
        TextView videoName, Name, duree, Chapter1, Chapter2, Chapter3;
        RelativeLayout container;

        public ImageViewHolder2(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            videoName = itemView.findViewById(R.id.videoName);
            Name = itemView.findViewById(R.id.Name);
            duree = itemView.findViewById(R.id.duree);
            Chapter1 = itemView.findViewById(R.id.Chapter1);
            Chapter2 = itemView.findViewById(R.id.Chapter2);
            Chapter3 = itemView.findViewById(R.id.Chapter3);
        }
    }
}
