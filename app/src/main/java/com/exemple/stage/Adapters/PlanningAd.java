package com.exemple.stage.Adapters;

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
import com.exemple.stage.modele.Videos;

import java.util.ArrayList;
import java.util.List;

public class PlanningAd extends RecyclerView.Adapter<PlanningAd.PlanningAdHolder> {

    private List<Videos> VideosList = new ArrayList<>();
    private Context mContext;

    public PlanningAd(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlanningAdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanningAdHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plannin_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull PlanningAdHolder Holder, int position) {
        final Videos planning = VideosList.get(position);
        Holder.Name.setText(planning.getName());
        Holder.Time.setText(planning.getTime());
        Holder.videoName.setText(planning.getVideoName());

        Holder.Chapters.setText(planning.getChapter().get(position));
        Holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return VideosList.size();
    }

    public void setList(List<Videos> VideosList) {
        this.VideosList = VideosList;
        notifyDataSetChanged();
    }

    public class PlanningAdHolder extends RecyclerView.ViewHolder {

        TextView videoName, Name, Time, Chapters;
        RelativeLayout container;
        public PlanningAdHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            videoName = itemView.findViewById(R.id.videoName);
            Name = itemView.findViewById(R.id.Name);
            Time = itemView.findViewById(R.id.duree);
            Chapters = itemView.findViewById(R.id.Chapter1);
        }
    }
}