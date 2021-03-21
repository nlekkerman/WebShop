package com.lekkerman.webshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username,inputPhone,iPassword;
    private Button signUpBtn;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUpBtn = findViewById(R.id.register_btn);
        username = findViewById(R.id.register_username_input);
        inputPhone = findViewById(R.id.register_phone_input);
        iPassword = findViewById(R.id.register_password_input);

        loading = new ProgressDialog(this);


        signUpBtn.setOnClickListener(v->{
            createAccount();
        });
    }

    private void createAccount() {

        String name = username.getText().toString();
        String phone = inputPhone.getText().toString();
        String password= iPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)|| TextUtils.isEmpty(password)){
            Toast.makeText(this, "All fields are required!!", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 6){
            Toast.makeText(this, "Password requires at least 6 digits!! ", Toast.LENGTH_SHORT).show();
        }else {

            loading.setTitle("Creating account");
            loading.setMessage("Please wait while we check your credentials");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            validatePhoneNumber(name,phone,password);


        }


    }

    private void validatePhoneNumber(String name, String phone, String password) {

        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();

        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){

                    HashMap<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("password",password);
                    userDataMap.put("name",name);

                    rootReference.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();

                                        loading.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }else {
                                        loading.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error, try later.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else {

                    Toast.makeText(RegisterActivity.this, "User with this phone number already exists!", Toast.LENGTH_SHORT).show();
                    loading.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}