package com.exemple.stage.Adapters;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.exemple.stage.R;
import com.exemple.stage.modele.SongObject;

import java.util.ArrayList;

public class MusicAdapter extends ArrayAdapter<SongObject> {
    Context cxt;
    int res;
    ArrayList<SongObject> list;

    public MusicAdapter(Context context, int resource, ArrayList<SongObject> objects) {
        super(context, resource, objects);
        cxt = context;
        res = resource;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        view = LayoutInflater.from(cxt).inflate(res, parent, false);
        TextView fileName = view.findViewById(R.id.textSong);
        SongObject sdOb = list.get(position);
        fileName.setText(sdOb.getFileName());

        return view;
    }
}