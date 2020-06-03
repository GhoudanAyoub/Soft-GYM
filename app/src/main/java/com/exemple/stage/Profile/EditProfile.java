package com.exemple.stage.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.exemple.stage.R;
import com.exemple.stage.modele.User;
import com.exemple.stage.ui.NewStart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    private final static int GALLERI_PIK = 1;
    MaterialEditText Name, Gmail, Phone, Address, Country, City, Zip;
    ImageView imageView;
    TextView textView54;
    Button check, exit;
    DatabaseReference database;
    User CLassUser;
    Uri imageUri = null;
    StorageReference mStorage;
    String email, key;
    private FirebaseUser currentuser;
    private List<User> Userlist;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Name = findViewById(R.id.name);
        Gmail = findViewById(R.id.Gmail);
        Phone = findViewById(R.id.editText3);
        Address = findViewById(R.id.editText4);
        Country = findViewById(R.id.editText5);
        City = findViewById(R.id.editText6);
        Zip = findViewById(R.id.editText7);
        imageView = findViewById(R.id.imageView8);
        textView54 = findViewById(R.id.textView54);
        check = findViewById(R.id.check);
        exit = findViewById(R.id.Exit);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        Userlist = new ArrayList<>();

        textView54.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFile();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFile();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFinish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewStart.class);
                intent.putExtra("gmail", email);
                startActivity(intent);
            }
        });


        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users");
        database.keepSynced(true);

        email = getIntent().getStringExtra("gmail");

        database.orderByChild("gmail").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        CLassUser = dataSnapshot1.getValue(User.class);
                        Userlist.add(CLassUser);
                    }
                    Name.setText(CLassUser.name);
                    Phone.setText(CLassUser.Phone);
                    Address.setText(CLassUser.Address);
                    City.setText(CLassUser.City);
                    Zip.setText(CLassUser.Zip);
                    Country.setText(CLassUser.Country);
                    Gmail.setText(email);
                    Glide.with(getApplicationContext()).load(CLassUser.image).centerCrop().placeholder(R.drawable.no_image_available).into(imageView);

                } else {
                    Toast.makeText(getApplicationContext(), "Complate Your Authentification !!! ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }


    private void openImageFile() {
        Intent galinten = new Intent();
        galinten.setType("image/*");
        galinten.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galinten, "SELECT IMAGE"), GALLERI_PIK);
    }

    public void startFinish() {
        if (!validateForm()) return;
        mProgress.setMessage("Working On It !!");
        mProgress.show();
        final String City1 = City.getText().toString();
        final String Zip1 = Zip.getText().toString();
        final String Country1 = Country.getText().toString();
        final String tel1 = Phone.getText().toString();
        final String adress1 = Address.getText().toString();
        final String name1 = Name.getText().toString();
        final String gmail121 = Gmail.getText().toString();

        try {
            StorageReference filePath = mStorage.child("profile_images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Task<Uri> download1 = taskSnapshot.getStorage().getDownloadUrl();
                    final DatabaseReference newPost = database.push();
                    key = database.push().getKey();

                    database.orderByChild("gmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    User user = dataSnapshot1.getValue(User.class);
                                    dataSnapshot1.getRef().removeValue();

                                    newPost.child("IDUsers").setValue(key);
                                    newPost.child("City").setValue(City1);
                                    newPost.child("Zip").setValue(Zip1);
                                    newPost.child("Country").setValue(Country1);
                                    newPost.child("Phone").setValue(tel1);
                                    newPost.child("Address").setValue(adress1);
                                    newPost.child("name").setValue(name1);
                                    newPost.child("image").setValue(download1.toString());
                                    newPost.child("gmail").setValue(gmail121);
                                    newPost.child("status").setValue(user.getStatus());
                                    newPost.child("Days").setValue(user.Days);

                                }
                            } else {
                                newPost.child("IDUsers").setValue(key);
                                newPost.child("City").setValue(City1);
                                newPost.child("Zip").setValue(Zip1);
                                newPost.child("Country").setValue(Country1);
                                newPost.child("Phone").setValue(tel1);
                                newPost.child("Address").setValue(adress1);
                                newPost.child("name").setValue(name1);
                                newPost.child("image").setValue(download1.toString());
                                newPost.child("gmail").setValue(gmail121);
                                newPost.child("status").setValue("");
                                newPost.child("Days").setValue(0);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Updated Successfully  ", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Faced !! Try To Change Your Image To Avoid That ??", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERI_PIK && resultCode == RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String name1 = Name.getText().toString();
        if (TextUtils.isEmpty(name1)) {
            Name.setError("Required.");
            valid = false;
        } else {
            Name.setError(null);
        }


        String tel1 = Phone.getText().toString();
        if (TextUtils.isEmpty(tel1)) {
            Phone.setError("Required.");
            valid = false;
        } else {
            Phone.setError(null);
        }

        String adress11 = Address.getText().toString();
        if (TextUtils.isEmpty(adress11)) {
            Address.setError("Required.");
            valid = false;
        } else {
            Address.setError(null);
        }

        String gmail1 = City.getText().toString();
        if (TextUtils.isEmpty(gmail1)) {
            City.setError("Required.");
            valid = false;
        } else {
            City.setError(null);
        }

        String Zip1 = Zip.getText().toString();
        if (TextUtils.isEmpty(Zip1)) {
            Zip.setError("Required.");
            valid = false;
        } else {
            Zip.setError(null);
        }
        String Country1 = Country.getText().toString();
        if (TextUtils.isEmpty(gmail1)) {
            Country.setError("Required.");
            valid = false;
        } else {
            Country.setError(null);
        }

        return valid;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
