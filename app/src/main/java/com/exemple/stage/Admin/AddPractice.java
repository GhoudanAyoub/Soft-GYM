package com.exemple.stage.Admin;
/**
 * Created By GHOUADN AYOUB
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.exemple.stage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class AddPractice extends AppCompatActivity {


    private final static int GALLERI_PIK = 1;
    private final static int PICK_VIDEO_REQUEST = 2;
    private static final String VIDEO_DIRECTORY = "video/*";
    public static String key;
    DatabaseReference databaseReference, databaseReference2, DatabaseReference3;
    StorageReference mStorage;
    ImageView imageView6;
    Button UPLOAD, AddPractices;
    VideoView vv;
    Uri videouri = null;
    MaterialEditText VideoName, Duration, Chapter1, Chapter2, Chapter3, Others, videoUri;
    Uri imageUri = null;
    ProgressBar progressBar;
    Task<Uri> download1;
    Spinner PracticeName;
    String l;
    private MediaController mc;
    private ProgressDialog mProgress;
    private String Videonamee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practice);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imageView6 = findViewById(R.id.imageView6);
        //UPLOAD = findViewById(R.id.UPLOAD);
        AddPractices = findViewById(R.id.AddPractices);
        //vv = findViewById(R.id.videoadc);
        PracticeName = findViewById(R.id.PracticeName);
        VideoName = findViewById(R.id.VideoName);
        Duration = findViewById(R.id.Duration);
        Chapter1 = findViewById(R.id.Chapter1);
        Chapter2 = findViewById(R.id.Chapter2);
        Chapter3 = findViewById(R.id.Chapter3);
        Others = findViewById(R.id.Others);
        videoUri = findViewById(R.id.videoUri);
        progressBar = findViewById(R.id.progressBar2);
        mProgress = new ProgressDialog(AddPractice.this);


        String[] Tp2 = {"Choose One :", "Beginner", "Advanced"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_dropdown_item, Tp2);
        PracticeName.setAdapter(adapter2);
        PracticeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Choose a Beginner Or Advanced !! ", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        l = "Beginner";
                        break;
                    case 2:
                        l = "Advanced";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mStorage = FirebaseStorage.getInstance().getReference();
        /**
         //**********************videoset
         vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override public void onPrepared(MediaPlayer mp) {
        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
        @Override public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mc = new MediaController(getApplicationContext());
        vv.setMediaController(mc);
        mc.setAnchorView(vv);
        }
        });
        }
        });
         vv.start();**/
        //*************************************BUTTON ACT*
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFile();
            }
        });
        AddPractices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mProgress.setMessage("Working On It !!");
                mProgress.show();
                AddPractices();
                mProgress.dismiss();
                clair();
            }
        });
        /**
         UPLOAD.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        uploadvideo();
        }
        });**/
        //************************* Exercice
        databaseReference = FirebaseDatabase.getInstance().getReference("Exercice");
        databaseReference.keepSynced(true);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference2.keepSynced(true);
        DatabaseReference3 = FirebaseDatabase.getInstance().getReference("Planning");
        DatabaseReference3.keepSynced(true);
    }


    public void openImageFile() {
        Intent galinten = new Intent();
        galinten.setType("image/*");
        galinten.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galinten, "SELECT IMAGE"), GALLERI_PIK);
    }

    public void uploadvideo() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(i, "Select a Video"), PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERI_PIK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

            Glide.with(this).load(imageUri).into(imageView6);
        }


        Log.d("result", "" + resultCode);
        if (resultCode == RESULT_CANCELED) {
            Log.d("what", "cancle");
            return;
        }
        if (requestCode == PICK_VIDEO_REQUEST) {
            Log.d("what", "gale");
            if (data != null) {
                videouri = data.getData();

                String selectedVideoPath = getFileName(videouri);
                Log.d("path", selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                vv.setVideoURI(videouri);
                vv.requestFocus();
                vv.start();

            }

        } else if (requestCode == PICK_VIDEO_REQUEST) {
            videouri = data.getData();
            String recordedVideoPath = getFileName(videouri);
            Log.d("frrr", recordedVideoPath);
            saveVideoToInternalStorage(recordedVideoPath);
            vv.setVideoURI(videouri);
            vv.requestFocus();
            vv.start();
        }
    }

    private void saveVideoToInternalStorage(String filePath) {

        File newfile;

        try {

            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            if (currentFile.exists()) {

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void AddPractices() {

        if (!validateForm()) return;
        //**********************************************OWNING DATA FROM MATERIAL EDITTEXT
        final String VideoName1 = VideoName.getText().toString();
        final String Duration1 = Duration.getText().toString();
        final String Chapter11 = Chapter1.getText().toString();
        final String Chapter21 = Chapter2.getText().toString();
        final String Chapter31 = Chapter3.getText().toString();
        final String Others1 = Others.getText().toString();
        final String VideoUri = videoUri.getText().toString();

        //*************Spliting Video Uri
        String string = VideoUri;
        String[] parts = string.split("(?<==)");
        String part1 = parts[0];
        final String part2 = parts[1];

        //***********************Exercice Add
        StorageReference filePath = mStorage.child("profile_images").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                download1 = taskSnapshot.getStorage().getDownloadUrl();

                DatabaseReference newPost = databaseReference.push();
                key = databaseReference.push().getKey();
                newPost.child("ID").setValue(key);
                newPost.child("Name").setValue(l);
                newPost.child("image").setValue(download1.toString());
                Toast.makeText(getApplicationContext(), "Uploaded Exercice Successfully  ", Toast.LENGTH_SHORT).show();

                //**********************************VideoAdd
                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        DatabaseReference newPost2 = databaseReference2.push();
                        newPost2.child("ID").setValue(key);
                        newPost2.child("video").setValue(part2);
                        newPost2.child("videoName").setValue(VideoName1);
                        newPost2.child("duree").setValue(Duration1);
                        newPost2.child("Chapter1").setValue(Chapter11);
                        newPost2.child("Chapter2").setValue(Chapter21);
                        newPost2.child("Chapter3").setValue(Chapter31);
                        newPost2.child("Other").setValue(Others1);
                        Toast.makeText(getApplicationContext(), "Uploaderd video Successfully  ", Toast.LENGTH_SHORT).show();

                        //******************************Practice
                        StorageReference PLAN = mStorage.child("profile_images").child(imageUri.getLastPathSegment());
                        PLAN.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uri2 = taskSnapshot.getStorage().getDownloadUrl();
                                DatabaseReference newPost3 = DatabaseReference3.push();
                                newPost3.child("Name").setValue(l);
                                newPost3.child("image").setValue(uri2.toString());
                                newPost3.child("videoName").setValue(VideoName1);
                                newPost3.child("duree").setValue(Duration1);
                                newPost3.child("Chapter1").setValue(Chapter11);
                                newPost3.child("Chapter2").setValue(Chapter21);
                                newPost3.child("Chapter3").setValue(Chapter31);
                                newPost3.child("Other").setValue(Others1);
                                Toast.makeText(getApplicationContext(), "Uploaded Planning Successfully  ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "ERROR ON Planning Upload !!! ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Video Not Uploaded Successfully  ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void clair() {

        VideoName.setText("");
        Duration.setText("");
        Chapter1.setText("");
        Chapter2.setText("");
        Chapter3.setText("");
        Others.setText("");
        videoUri.setText("");
        Glide.with(this).load(R.drawable.ic_add_a_photo_black_24dp).into(imageView6);
    }

    public boolean validateForm() {
        boolean valid = true;

        String tel1 = VideoName.getText().toString();
        if (TextUtils.isEmpty(tel1)) {
            VideoName.setError("Required.");
            valid = false;
        } else {
            VideoName.setError(null);
        }

        String adress11 = Duration.getText().toString();
        if (TextUtils.isEmpty(adress11)) {
            Duration.setError("Required.");
            valid = false;
        } else {
            Duration.setError(null);
        }

        String gmail1 = Chapter1.getText().toString();
        if (TextUtils.isEmpty(gmail1)) {
            Chapter1.setError("Required.");
            valid = false;
        } else {
            Chapter1.setError(null);
        }

        String Zip1 = Chapter2.getText().toString();
        if (TextUtils.isEmpty(Zip1)) {
            Chapter2.setError("Required.");
            valid = false;
        } else {
            Chapter2.setError(null);
        }
        String Country1 = Chapter3.getText().toString();
        if (TextUtils.isEmpty(Country1)) {
            Chapter3.setError("Required.");
            valid = false;
        } else {
            Chapter3.setError(null);
        }

        String Country2 = videoUri.getText().toString();
        if (TextUtils.isEmpty(Country2)) {
            videoUri.setError("Required.");
            valid = false;
        } else {
            videoUri.setError(null);
        }

        return valid;
    }
}
