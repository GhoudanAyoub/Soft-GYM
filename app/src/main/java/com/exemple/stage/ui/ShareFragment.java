package com.exemple.stage.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exemple.stage.Company.ContactUs;
import com.exemple.stage.R;

public class ShareFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragment, container, false);

        view.findViewById(R.id.Facebook).setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/ayoub_ghoudan")).setPackage("com.facebook.appmanager"));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com")).setPackage("com.facebook.appmanager"));
            }
        });
        view.findViewById(R.id.Instagram).setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/ayoub_ghoudan")).setPackage("com.instagram.android"));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com")).setPackage("com.instagram.android"));
            }
        });
        view.findViewById(R.id.Gmail).setOnClickListener(v -> startActivity(new Intent(requireActivity(), ContactUs.class)));

        return view;
    }

}
