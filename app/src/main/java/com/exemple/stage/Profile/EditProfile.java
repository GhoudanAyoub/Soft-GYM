package com.exemple.stage.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.exemple.stage.API.FireBaseClient;
import com.exemple.stage.Commun.Commun;
import com.exemple.stage.NewStart;
import com.exemple.stage.R;
import com.exemple.stage.modele.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private final static int GALLERY_PIC = 1;
    private MaterialEditText Name, Gmail, Phone, Address, Country, City, Zip;
    private ImageView imageView;
    private Uri imageUri = null;
    private String key;
    private ProgressDialog mProgress;
    private String City1, Zip1, Country1, tel1, adress1, name1, gmail121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        GetView();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Uploading ....");
/*
        findViewById(R.id.textView54).setOnClickListener(v -> openImageFile());
        findViewById(R.id.imageView8).setOnClickListener(v -> openImageFile());
        findViewById(R.id.check).setOnClickListener(v -> startFinish());
        findViewById(R.id.Exit).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewStart.class)));

        FireBaseClient.getInstance().getFirebaseDatabase()
                .getReference("Users")
                .orderByChild("gmail")
                .equalTo(Commun.Email_User)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                SetData(Objects.requireNonNull(dataSnapshot1.getValue(User.class)));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Complete Your Authentication !!! ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

 */
    }

    private void GetView() {
        Name = findViewById(R.id.name);
        Gmail = findViewById(R.id.Gmail);
        Phone = findViewById(R.id.editText3);
        Address = findViewById(R.id.editText4);
        Country = findViewById(R.id.editText5);
        City = findViewById(R.id.editText6);
        Zip = findViewById(R.id.editText7);
        imageView = findViewById(R.id.imageView8);
    }
    /*

    private void SetData(User CLassUser) {
        Name.setText(CLassUser.name);
        Phone.setText(CLassUser.Phone);
        Address.setText(CLassUser.Address);
        City.setText(CLassUser.City);
        Zip.setText(CLassUser.Zip);
        Country.setText(CLassUser.Country);
        Gmail.setText(Commun.Email_User);
        Glide.with(getApplicationContext())
                .load(CLassUser.image)
                .centerCrop()
                .placeholder(R.drawable.no_image_available)
                .into(imageView);
    }

    private void GetData() {
        City1 = Objects.requireNonNull(City.getText()).toString();
        Zip1 = Objects.requireNonNull(Zip.getText()).toString();
        Country1 = Objects.requireNonNull(Country.getText()).toString();
        tel1 = Objects.requireNonNull(Phone.getText()).toString();
        adress1 = Objects.requireNonNull(Address.getText()).toString();
        name1 = Objects.requireNonNull(Name.getText()).toString();
        gmail121 = Objects.requireNonNull(Gmail.getText()).toString();
    }

    private void openImageFile() {
        Intent glinted = new Intent();
        glinted.setType("image/*");
        glinted.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(glinted, "SELECT IMAGE"), GALLERY_PIC);
    }

    private void AddData(User newUser) {
        try {
            FireBaseClient.getInstance()
                    .getFirebaseDatabase()
                    .getReference("Users")
                    .push()
                    .setValue(newUser);
        } catch (Throwable e) {
            Log.e("Field", "AddData: " + e.getMessage());
        }
    }

    public void startFinish() {
        if (!validateForm()) return;
        mProgress.show();
        GetData();
        try {
            FireBaseClient.getInstance().getStorageReference()
                    .child("profile_images")
                    .child(Objects.requireNonNull(imageUri.getLastPathSegment()))
                    .putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                final Task<Uri> download1 = taskSnapshot.getStorage().getDownloadUrl();

                key = FireBaseClient.getInstance().getFirebaseDatabase()
                        .getReference("Users").push().getKey();

                FireBaseClient.getInstance().getFirebaseDatabase()
                        .getReference("Users")
                        .orderByChild("gmail")
                        .equalTo(Commun.Email_User)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        User user = dataSnapshot1.getValue(User.class);
                                        dataSnapshot1.getRef().removeValue();

                                        assert user != null;
                                        User newUser = new User(City1, tel1, adress1, name1,
                                                Country1, Zip1, download1.toString(), gmail121,
                                                user.Days, key, user.getStatus());
                                        AddData(newUser);
                                    }
                                } else {
                                    User newUser2 = new User(City1, tel1, adress1, name1,
                                            Country1, Zip1, download1.toString(), gmail121,
                                            0, key, "");
                                    AddData(newUser2);
                                }
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), "Updated Successfully  ", Toast.LENGTH_LONG).show();
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Faced !! Try To Change Your Image To Avoid That ??", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PIC && resultCode == RESULT_OK) {
            assert data != null;
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String name1 = Objects.requireNonNull(Name.getText()).toString();
        if (TextUtils.isEmpty(name1)) {
            Name.setError("Required.");
            valid = false;
        } else {
            Name.setError(null);
        }


        String tel1 = Objects.requireNonNull(Phone.getText()).toString();
        if (TextUtils.isEmpty(tel1)) {
            Phone.setError("Required.");
            valid = false;
        } else {
            Phone.setError(null);
        }

        String adress11 = Objects.requireNonNull(Address.getText()).toString();
        if (TextUtils.isEmpty(adress11)) {
            Address.setError("Required.");
            valid = false;
        } else {
            Address.setError(null);
        }

        String gmail1 = Objects.requireNonNull(City.getText()).toString();
        if (TextUtils.isEmpty(gmail1)) {
            City.setError("Required.");
            valid = false;
        } else {
            City.setError(null);
        }

        String Zip1 = Objects.requireNonNull(Zip.getText()).toString();
        if (TextUtils.isEmpty(Zip1)) {
            Zip.setError("Required.");
            valid = false;
        } else {
            Zip.setError(null);
        }
        String Country1 = Objects.requireNonNull(Country.getText()).toString();
        if (TextUtils.isEmpty(Country1)) {
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

     */

}
