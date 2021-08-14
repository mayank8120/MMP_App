package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {


    private static final int CHOOSE_IMAGE = 4;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    Button backbutton;
    TextView usernamepro, numberpro, businesspro, useremailpro, emailverif;
    FloatingActionButton fabutt;
    ProgressDialog pd;
    ImageView profile_image;
    String[] cameraPermissions;
    String[] storagePermissions;
    Uri image_uri;
    String profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        backbutton = findViewById(R.id.backbutton);
        fabutt = findViewById(R.id.fab_edit);
        usernamepro = findViewById(R.id.username);
        useremailpro = findViewById(R.id.email);
        numberpro = findViewById(R.id.phonenumber);
        businesspro = findViewById(R.id.businesstype);
        emailverif = findViewById(R.id.verificationtext);
        profile_image = findViewById(R.id.profilepic);


        pd = new ProgressDialog(this);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        fabutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditprofiledialog();
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    private void showEditprofiledialog() {


        String[] options = {"Change profile picture", "Edit Name", "Change Email", "Change phone number"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CHOOSE ACTION");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInter, int which) {

                if (which == 0) {

                    pd.setMessage("updating user pic");
                    profilePhoto = "image";
                    showImagepicDialog();

                } else if (which == 1) {

                    pd.setMessage("updating user name");
                    showNamePhoneEmailBusinessUpdateDialog("name");

                } else if (which == 2) {

                    pd.setMessage("updating user email");
                    showNamePhoneEmailBusinessUpdateDialog("email");

                } else if (which == 3) {

                    pd.setMessage("updating user phone number");
                    showNamePhoneEmailBusinessUpdateDialog("phone number");

                }

            }
        });
        builder.create().show();
    }

    private void showNamePhoneEmailBusinessUpdateDialog(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update " + key);

       /* LinearLayout linearLayout =new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);*/
        final EditText input = new EditText(this);
        input.setHint("Enter " + key);

        builder.setView(input);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String value = input.getText().toString().trim();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();


    }


    private void showImagepicDialog() {

        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInter, int which) {

                if (which == 0) {
                    if (!checkcamerapermissions()) {
                        requestcamerapermission();
                    } else {
                        pickfromcamera();
                    }
                } else if (which == 1) {
                    pd.setMessage("Updating user name");
                    if (!checkStoragepermissions()) {
                        requeststoragepermission();
                    } else {
                        pickfromcGallery();
                    }

                }

            }
        });
        builder.create().show();
    }


    private void requeststoragepermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }


    private boolean checkStoragepermissions() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void requestcamerapermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    private boolean checkcamerapermissions() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    private void pickfromcamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }


    private void pickfromcGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {


            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                uploadProfilePic(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image_uri = data.getData();
                uploadProfilePic(image_uri);

            }


            uploadProfilePic(image_uri);

        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadProfilePic(Uri uri) {

        pd.show();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writestorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writestorageAccepted) {
                        pickfromcamera();
                    } else {
                        Toast.makeText(this, "enable permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    boolean writestorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writestorageAccepted) {
                        pickfromcGallery();
                    } else {
                        Toast.makeText(this, "enable permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }


}