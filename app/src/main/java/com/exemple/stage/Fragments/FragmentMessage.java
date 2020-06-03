package com.exemple.stage.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.Adapters.MessageAdapter;
import com.exemple.stage.R;
import com.exemple.stage.modele.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMessage extends Fragment {


    RecyclerView ListeViewMessage;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    DatabaseReference referenceChat;
    Chat chat;

    public FragmentMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_message, container, false);

        ListeViewMessage = view.findViewById(R.id.ListeViewMessage);
        ListeViewMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        ListeViewMessage.setLayoutManager(linearLayoutManager);

        mchat = new ArrayList<>();

        referenceChat = FirebaseDatabase.getInstance().getReference("Chats");

        referenceChat.orderByChild("sender").equalTo("YU24qoMvXdVFyIwju3QdXn36o163").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mchat.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        chat = snapshot.getValue(Chat.class);
                        mchat.add(chat);
                    }
                    if (!chat.isIsseen()) {
                        messageAdapter = new MessageAdapter(getContext(), mchat);
                        ListeViewMessage.setAdapter(messageAdapter);
                    }
                } else {
                    Toast.makeText(getContext(), "No Message Yet !! ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "ERROR !! ", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
