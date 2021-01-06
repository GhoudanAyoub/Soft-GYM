package com.exemple.stage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.stage.R;
import com.exemple.stage.modele.Abonner;
import com.exemple.stage.modele.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {

    Abonner abonner;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    private Context mContext;
    private List<User> mUser;
    private int admin = 0;

    public AdminUserAdapter(Context mContext, List<User> mUser) {
        this.mUser = mUser;
        this.mContext = mContext;
    }

    @Override
    public AdminUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_layout, parent, false);
        return new AdminUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User User = mUser.get(position);
        holder.IDCLient.setText(User.getIDUsers());
        holder.NameCLient.setText(User.getName());
        holder.GmailCLient.setText(User.getGmail());
        holder.AbonnerTypeCLient.setText(Integer.toString(User.getDays()));
        holder.DatedeFinCLient.setText("");

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Abonner");

        databaseReference.orderByChild("gmail").equalTo(User.getGmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        abonner = dataSnapshot1.getValue(Abonner.class);
                    }

                    if (abonner.status.matches("approved")) {
                        holder.EtatCLient.setChecked(true);
                    } else {
                        holder.EtatCLient.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        holder.EtatCLient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.EtatCLient.isChecked()) {
                    holder.EtatCLient.setChecked(false);
                    //abonner
                    databaseReference.orderByChild("gmail").equalTo(User.getGmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Abonner abonner = dataSnapshot1.getValue(Abonner.class);
                                    dataSnapshot1.getRef().removeValue();

                                    DatabaseReference newPost2 = databaseReference.push();
                                    newPost2.child("days").setValue(abonner.days);
                                    newPost2.child("gmail").setValue(abonner.gmail);
                                    newPost2.child("status").setValue("");
                                }
                            } else {
                                Toast.makeText(mContext, "Error No Data Found !!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    //Users
                    databaseReference2.orderByChild("gmail").equalTo(User.getGmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    User user = dataSnapshot1.getValue(User.class);

                                    dataSnapshot1.getRef().removeValue();

                                    String key = databaseReference2.push().getKey();
                                    DatabaseReference newPost = databaseReference2.push();
                                    newPost.child("IDUsers").setValue(key);
                                    newPost.child("Phone").setValue(user.getPhone());
                                    newPost.child("Address").setValue(user.getAddress());
                                    newPost.child("name").setValue(user.getGmail());
                                    newPost.child("image").setValue(user.getImage());
                                    newPost.child("gmail").setValue(user.getGmail());
                                    newPost.child("status").setValue("");
                                    newPost.child("Days").setValue(user.getDays());
                                }
                            } else {
                                Toast.makeText(mContext, "Error No Data Found !!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    holder.EtatCLient.setChecked(true);

                    databaseReference.orderByChild("gmail").equalTo(User.getGmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Abonner abonner = dataSnapshot1.getValue(Abonner.class);
                                    dataSnapshot1.getRef().removeValue();

                                    DatabaseReference newPost2 = databaseReference.push();
                                    newPost2.child("days").setValue(abonner.days);
                                    newPost2.child("gmail").setValue(abonner.gmail);
                                    newPost2.child("status").setValue("approved");

                                }
                                databaseReference2.orderByChild("gmail").equalTo(User.getGmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                User user = dataSnapshot1.getValue(User.class);

                                                dataSnapshot1.getRef().removeValue();

                                                String key = databaseReference2.push().getKey();
                                                DatabaseReference newPost = databaseReference2.push();
                                                newPost.child("IDUsers").setValue(key);
                                                newPost.child("Phone").setValue(user.getPhone());
                                                newPost.child("Address").setValue(user.getAddress());
                                                newPost.child("name").setValue(user.getGmail());
                                                newPost.child("image").setValue(user.getImage());
                                                newPost.child("gmail").setValue(user.getGmail());
                                                newPost.child("status").setValue("approved");
                                                newPost.child("Days").setValue(user.getDays());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            } else {
                                DatabaseReference newPost2 = databaseReference.push();
                                newPost2.child("days").setValue(1);
                                newPost2.child("gmail").setValue(User.getGmail());
                                newPost2.child("status").setValue("approved");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView IDCLient, NameCLient, GmailCLient, AbonnerTypeCLient, DatedeFinCLient;
        Switch EtatCLient;

        public ViewHolder(View itemView) {
            super(itemView);
            IDCLient = itemView.findViewById(R.id.IDCLient);
            NameCLient = itemView.findViewById(R.id.NameCLient);
            GmailCLient = itemView.findViewById(R.id.GmailCLient);
            AbonnerTypeCLient = itemView.findViewById(R.id.AbonnerTypeCLient);
            DatedeFinCLient = itemView.findViewById(R.id.DatedeFinCLient);
            EtatCLient = itemView.findViewById(R.id.EtatCLient);
        }
    }
}
