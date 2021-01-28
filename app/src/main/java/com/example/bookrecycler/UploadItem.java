package com.example.bookrecycler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.Intent.ACTION_GET_CONTENT;

public class UploadItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button Uplaodbtn;
    private EditText Booktitle, BookDeails, BookPrice;
    private ImageButton BookImage;
    private ProgressBar mPrograssBar;
    // private DatabaseReference databaseReference;
    private Uri mImageUri, link;
    private Spinner BookcategorySpiner, BookconditonSpiner;
    FirebaseStorage storage;
    StorageReference storageReference;
    // FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    //owner details
    String userEmail, userPhone, Username;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        BookImage = findViewById(R.id.rectangle_5);
        Uplaodbtn = findViewById(R.id.upload_btn);
        Booktitle = findViewById(R.id.title_edittext);
        BookcategorySpiner = findViewById(R.id.category_spinner);
        BookDeails = findViewById(R.id.book_description);
        BookPrice = findViewById(R.id.price_editText);
        BookconditonSpiner = findViewById(R.id.conditon_spinner);
        // Create a reference to "mountains.jpg"
        //StorageReference mountainsRef = storageReference.child("mountains.jpg");

        Uplaodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadImage2(Booktitle, BookDeails, BookPrice, BookImage, BookcategorySpiner);

            }
        });
//        BookImage.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//
//                openFileChooser();
//
//            }
//        });
        BookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void upLoadImage2(EditText booktitle, EditText bookDeails, EditText bookPrice, ImageView bookImage, Spinner bookcategorySpiner) {

        // Create a new user with a first and last name
        Map<String, Object> Book = new HashMap<>();
        Book.put("Name", booktitle);
        Book.put("Price", bookPrice);
        Book.put("Category", bookcategorySpiner);
        Book.put("Details", bookDeails);
        StorageReference mountainImagesRef = storageReference.child("images/" + bookcategorySpiner + "/" + bookImage + ".jpg");

// Add a new document with a generated ID
        db.collection("Books")
                .add(Book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    public void openFileChooser() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "I am here", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                BookImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
