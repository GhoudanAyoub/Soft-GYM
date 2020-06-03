package com.exemple.stage.Adapters;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.R;
import com.exemple.stage.modele.AboutUsItems;

import java.util.List;

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.NewsViewHolder> {

    Context mContext;
    List<AboutUsItems> mData;
    List<AboutUsItems> mDataFiltered;
    boolean isDark = false;


    public AboutUsAdapter(Context mContext, List<AboutUsItems> mData, boolean isDark) {
        this.mContext = mContext;
        this.mData = mData;
        this.isDark = isDark;
        this.mDataFiltered = mData;
    }

    public AboutUsAdapter(Context mContext, List<AboutUsItems> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;

    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_news, viewGroup, false);
        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {

        newsViewHolder.img_user.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        newsViewHolder.img_user.setImageResource(mDataFiltered.get(position).getUserPhoto());
        newsViewHolder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        newsViewHolder.tv_title.setText(mDataFiltered.get(position).getTitle());
        newsViewHolder.tv_content.setText(mDataFiltered.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_content;
        RelativeLayout container;
        ImageView img_user;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.Name);
            tv_content = itemView.findViewById(R.id.tv_description);
            img_user = itemView.findViewById(R.id.img_user);
            if (isDark) {
                setDarkTheme();
            }
        }

        private void setDarkTheme() {

            container.setBackgroundResource(R.drawable.card_bg_dark);
        }

    }

}
