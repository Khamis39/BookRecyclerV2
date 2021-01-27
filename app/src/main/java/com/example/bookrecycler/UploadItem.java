package com.example.bookrecycler;

import Model.Books;
import Model.User;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.content.Intent.ACTION_GET_CONTENT;

public class UploadItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1;
    private Button Uplaodbtn;
    private EditText Booktitle,BookDeails,BookPrice;
    private ImageView BookImage;
    private ProgressBar mPrograssBar;
    private DatabaseReference databaseReference;
    private Uri mImageUri,link;
    private Spinner BookcategorySpiner,BookconditonSpiner;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    //owner details
    String userEmail,userPhone,Username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        BookImage = findViewById(R.id.rectangle_5);
        Uplaodbtn = findViewById(R.id.upload_btn);
        Booktitle = findViewById(R.id.title_edittext);
        BookcategorySpiner = findViewById(R.id.category_spinner);
        BookDeails = findViewById(R.id.book_description);
        BookPrice = findViewById(R.id.price_editText);
        BookconditonSpiner = findViewById(R.id.conditon_spinner);

        Uplaodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadImage();

            }
        });
        BookImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });


    }

    private void openFileChooser() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PICK_IMAGE_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Book CoverPage"), PICK_IMAGE_REQUEST);


        }
    } @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                BookImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }











    //UploadBtn onclick action
    private void upLoadImage() {

        final String bookName = Booktitle.getText().toString();
        final   String Category = BookcategorySpiner.getSelectedItem().toString();

        String BookId = "00";

        switch (Category){
            case "Engineering":
                BookId ="01";
                break;
            case "Islamic":
                BookId="02";
                break;
            case "Sciences":
                BookId ="03";
                break;
            case "Computing":
                BookId ="04";
                break;

        }
        if(mImageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Book Categories/").child(Category).child(bookName);
            //UUID.randomUUID()
            final String finalBookId = BookId;
            final String Details = BookDeails.getText().toString();
            final String Price= BookPrice.getText().toString();
            final String Condition = BookconditonSpiner.getSelectedItem().toString();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            String UserId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User OwnerDetail = (User) snapshot.getValue(User.class);
                    Username = OwnerDetail.getName();
                    userPhone = OwnerDetail.getPhoneNumber();
                    userEmail =OwnerDetail.getEmailAddress();

                    final User bookOwner = new User(Username,userPhone,userEmail);

                    ref.putFile(mImageUri)

                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //  final String url =taskSnapshot.getMetadata().getDownloadUrl().toString();


                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url2 = uri.toString().replace("?","_400x400?");
                                            Books books = new Books(bookName, url2, Details+" ", "Yes", finalBookId, Price,bookOwner);
                                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");
                                            String uploadId = databaseReference.push().getKey();
                                            databaseReference.child(uploadId).setValue(books);
                                        }
                                    });


                                    progressDialog.dismiss();
                                    Toast.makeText(UploadItem.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UploadItem.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UploadItem.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });






                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", "onCancelled: Owner Details Failure");
                }
            });




            //getDownloadUrl().getResult().toString();

        }
        else{
            Toast.makeText(this, "Please Choose a Cover Page", Toast.LENGTH_LONG).show();
        }

    }
}