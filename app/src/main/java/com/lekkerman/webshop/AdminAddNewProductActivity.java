package com.lekkerman.webshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private ImageView inputProductImage;
    private EditText inputProductName, inputProductPrice, inputProductDesc;
    private Button addNewProductBtn;


    private String categoryName, pDescription, pName, pPrice, saveCurrentDate, saveCurrentTime;
    private static final int galleryPick = 1;

    private Uri imageUri;
    private String PRODUCT_RANDOM_KEY, DOWNLOAD_IMAGE_URL;

    private StorageReference productImageRef;
    private DatabaseReference productRef;

    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        categoryName = getIntent().getExtras().get("category").toString();

        inputProductImage = findViewById(R.id.select_product_imageView);
        inputProductName = findViewById(R.id.product_name);
        inputProductPrice = findViewById(R.id.product_price);
        inputProductDesc = findViewById(R.id.product_description);
        addNewProductBtn = findViewById(R.id.add_productBtn);

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loading = new ProgressDialog(this);


        addNewProductBtn.setOnClickListener(v -> {


            validateProductData();
        });

        inputProductImage.setOnClickListener(v -> {
            Toast.makeText(this, "KLicKed", Toast.LENGTH_SHORT).show();

            openGallery();
        });


    }

    private void validateProductData() {

        pName = inputProductName.getText().toString();
        pDescription = inputProductDesc.getText().toString();
        pPrice = inputProductPrice.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Must Ad product Image", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(pName) || TextUtils.isEmpty(pPrice) || TextUtils.isEmpty(pDescription)) {

            Toast.makeText(this, "All fields ARE required", Toast.LENGTH_SHORT).show();

        } else {
            storeProductInformation();
        }

    }

    private void storeProductInformation() {

        loading.setTitle("Uploading new Product");
        loading.setMessage("Uploading......");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        saveCurrentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = timeFormat.format(calendar.getTime());

        PRODUCT_RANDOM_KEY = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + PRODUCT_RANDOM_KEY + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                Toast.makeText(AdminAddNewProductActivity.this, e + "", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                loading.dismiss();
                Toast.makeText(AdminAddNewProductActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());//moguc problem
                        }

                        DOWNLOAD_IMAGE_URL = filePath.getDownloadUrl().toString();

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DOWNLOAD_IMAGE_URL = Objects.requireNonNull(task.getResult()).toString();//moguc problem
                        loading.dismiss();
                        Toast.makeText(AdminAddNewProductActivity.this, " Getting ImageUrl successfully", Toast.LENGTH_SHORT).show();
                        saveProductInfoToDatabase();
                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", PRODUCT_RANDOM_KEY);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", pDescription);
        productMap.put("image", DOWNLOAD_IMAGE_URL);
        productMap.put("category", categoryName);
        productMap.put("price", pPrice);
        productMap.put("pname", pName);

        productRef.child(PRODUCT_RANDOM_KEY).updateChildren(productMap)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        loading.dismiss();
                        Toast.makeText(AdminAddNewProductActivity.this, " Product is added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        loading.dismiss();
                        Toast.makeText(AdminAddNewProductActivity.this, task.getException() + "", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void openGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }
}