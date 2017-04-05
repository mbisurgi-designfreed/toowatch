package com.designfreed.toowatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText mNameField;
    private ImageButton mProfilePicBtn;
    private Button mFinishSetupBtn;

    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorageImage;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");

        mStorageImage = FirebaseStorage.getInstance().getReference().child("profile_images");

        mAuth = FirebaseAuth.getInstance();

        mNameField = (EditText) findViewById(R.id.name_field);

        mProfilePicBtn = (ImageButton) findViewById(R.id.profile_pic_btn);
        mProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mFinishSetupBtn = (Button) findViewById(R.id.setup_btn);
        mFinishSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSetup();
            }
        });

        mProgress = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                mProfilePicBtn.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    private void finishSetup() {
        final String name = mNameField.getText().toString();
        final String userId = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(name)) {

            mProgress.setMessage("Seteando cuenta ...");
            mProgress.show();

            if (mImageUri != null) {

                StorageReference filePath = mStorageImage.child(mImageUri.getLastPathSegment());

                filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUri = taskSnapshot.getDownloadUrl().toString();

                        mDatabaseUsers.child(userId).child("name").setValue(name);
                        mDatabaseUsers.child(userId).child("image").setValue(downloadUri);
                    }
                });

            } else {

                mDatabaseUsers.child(userId).child("name").setValue(name);
                mDatabaseUsers.child(userId).child("image").setValue("default");

            }

            mProgress.dismiss();

            Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);

        }

    }
}
