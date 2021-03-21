package com.lekkerman.webshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lekkerman.webshop.model.Users;
import com.lekkerman.webshop.prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
private Button login, signUp;

    private ProgressDialog loading;
    private final String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.main_login_btn);
        signUp = findViewById(R.id.main_signup_btn);


        loading = new ProgressDialog(this);


        Paper.init(this);


        login.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        String USER_PHONE_KEY = Paper.book().read(Prevalent.USER_PHONE_KEY);
        String USER_PASSWORD_KEY = Paper.book().read(Prevalent.USER_PASSWORD_KEY);

        if (!(USER_PHONE_KEY == "") && !(USER_PASSWORD_KEY == "")){


            if (!TextUtils.isEmpty(USER_PHONE_KEY) && !TextUtils.isEmpty(USER_PASSWORD_KEY)){
                allowAccess(USER_PHONE_KEY,USER_PASSWORD_KEY);

                loading.setTitle("Already Logged in");
                loading.setMessage("Please wait...");
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }
        }
    }

    private void allowAccess(String phone, String password) {

        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {

                            return;
                        }
                    }
                    loading.dismiss();
                } else {
                    loading.dismiss();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}