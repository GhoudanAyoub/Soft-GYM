package com.exemple.stage.Adapters;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.stage.R;
import com.exemple.stage.Youtube.VideoACtivit;
import com.exemple.stage.modele.Exercice;
import com.exemple.stage.modele.Videos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExerciceAdapter extends RecyclerView.Adapter<ExerciceAdapter.ImageViewHolder2> {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
    Videos videos;
    private Context mContext;
    private List<Exercice> mUploads;

    public ExerciceAdapter(Context context, List<Exercice> uploads) {
        this.mUploads = uploads;
        this.mContext = context;
    }

    @Override
    public ExerciceAdapter.ImageViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.exercice_list, parent, false);
        return new ExerciceAdapter.ImageViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder2 Holder, int position) {
        final Exercice Exercice = mUploads.get(position);
        Holder.t2.setText(Exercice.getName());
        databaseReference.orderByChild("ID").equalTo(Exercice.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        videos = dataSnapshot1.getValue(Videos.class);
                    }
                    Holder.textView14.setText(videos.getVideoName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Holder.buttonv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoACtivit.class);
                intent.putExtra("ID", Exercice.getID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


        // *************Favorie
        //**handle favorites icon turned on or off when entering the product detail
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Set<String> jsonList = mPrefs.getStringSet("LikeVideo1", new HashSet<String>());
        Set<String> jsonList2 = new HashSet<>();
        jsonList2.addAll(jsonList);
        Exercice.setIsAddedAsFav("1");
        Gson gson = new Gson();
        String myJson = gson.toJson(Exercice);
        if (jsonList2.contains(myJson)) {
            Holder.favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
            Holder.ct.setBackgroundResource(R.drawable.cerclebackgroundyello);
        } else {
            Exercice.setIsAddedAsFav("0");
        }


        Holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Exercice.getIsAddedAsFav().equalsIgnoreCase("0")) {
                    Exercice.setIsAddedAsFav("1");
                    Holder.favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String myJson = gson.toJson(Exercice);
                    Set<String> jsonList = mPrefs.getStringSet("LikeVideo1", new HashSet<String>());
                    Set<String> jsonList2 = new HashSet<>();
                    jsonList2.addAll(jsonList);
                    jsonList2.add(myJson);
                    prefsEditor.putStringSet("LikeVideo1", jsonList2);
                    prefsEditor.apply();
                    Holder.ct.setBackgroundResource(R.drawable.cerclebackgroundyello);
                    Toast.makeText(mContext, "Added To favored", Toast.LENGTH_SHORT).show();
                } else {
                    Exercice.setIsAddedAsFav("1");
                    Holder.favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Set<String> jsonList = mPrefs.getStringSet("LikeVideo1", new HashSet<String>());
                    Set<String> jsonList2 = new HashSet<>();
                    jsonList2.addAll(jsonList);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(Exercice);
                    jsonList2.remove(myJson);
                    prefsEditor.putStringSet("LikeVideo1", jsonList2);
                    prefsEditor.apply();
                    Exercice.setIsAddedAsFav("0");
                    Holder.ct.setBackgroundResource(R.drawable.cerclebackgroundpink);
                    Toast.makeText(mContext, "Deleted From Favored", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //*********************** end of favories;**

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public static class ImageViewHolder2 extends RecyclerView.ViewHolder {
        TextView t2, textView14;
        Button favorite, buttonv;
        ConstraintLayout ct;

        public ImageViewHolder2(@NonNull View itemView) {
            super(itemView);
            t2 = itemView.findViewById(R.id.textView32);
            favorite = itemView.findViewById(R.id.favorite);
            textView14 = itemView.findViewById(R.id.textView14);
            ct = itemView.findViewById(R.id.ct);
            buttonv = itemView.findViewById(R.id.buttonv);
        }
    }

}
