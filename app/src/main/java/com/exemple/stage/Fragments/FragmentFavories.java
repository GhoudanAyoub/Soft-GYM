package com.exemple.stage.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.Adapters.FavorieAdapter;
import com.exemple.stage.R;
import com.exemple.stage.modele.Exercice;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavories extends Fragment {


    FavorieAdapter exerciceAdapter;
    private List<Exercice> exerciceList;
    private RecyclerView ListeViewNotification;
    private TextView txtEmptyFavoritesList;

    public FragmentFavories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_favories, container, false);

        exerciceList = new ArrayList<>();
        exerciceAdapter = new FavorieAdapter(getContext(), exerciceList);

        //favori shit
        ListeViewNotification = v.findViewById(R.id.ListeViewNotification);
        txtEmptyFavoritesList = v.findViewById(R.id.content_favorite_emptylist);
        ListeViewNotification.setHasFixedSize(true);
        ListeViewNotification.setLayoutManager(new GridLayoutManager(getContext(), 3));
        //getting back data from shared preferences
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson gson = new Gson();

        //getting back favorites
        Set<String> myJson = mPrefs.getStringSet("LikeVideo1", new HashSet<String>());
        if (myJson.isEmpty() && exerciceList.isEmpty()) {
            ListeViewNotification.setAdapter(null);
            txtEmptyFavoritesList.setText("You Don't Have Any Favorites Now!!!");
            txtEmptyFavoritesList.setVisibility(View.VISIBLE);
        } else if (myJson.isEmpty() && exerciceAdapter != null) {
            exerciceAdapter.notifyDataSetChanged();
            exerciceAdapter = new FavorieAdapter(getContext(), exerciceList);
            ListeViewNotification.setAdapter(exerciceAdapter);
            txtEmptyFavoritesList.setVisibility(View.GONE);
        } else {
            for (String id : myJson) {
                Exercice savedProduct = gson.fromJson(id, Exercice.class);
                exerciceList.add(savedProduct);
            }
            exerciceAdapter = new FavorieAdapter(getContext(), exerciceList);
            ListeViewNotification.setAdapter(exerciceAdapter);
        }

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
