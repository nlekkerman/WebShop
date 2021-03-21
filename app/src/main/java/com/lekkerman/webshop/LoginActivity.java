package com.lekkerman.webshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lekkerman.webshop.model.Users;
import com.lekkerman.webshop.prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhone, iPassword;
    private Button loginButton;

    private ProgressDialog loading;
    private  String parentDbName = "Users";


    private CheckBox rememberMeChk;

    private TextView admin, notAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iPassword = findViewById(R.id.login_password_input);
        inputPhone = findViewById(R.id.login_phone_input);
        loginButton = findViewById(R.id.login_btn);

        rememberMeChk = findViewById(R.id.remember_me_checkbox);

        Paper.init(this);


        admin = findViewById(R.id.admin_txt);
        notAdmin = findViewById(R.id.no_admin_txt);


        loading = new ProgressDialog(this);

        loginButton.setOnClickListener(v -> {
            loginUser();
        });

        admin.setOnClickListener(v-> {

            loginButton.setText(R.string.admin_login);
            admin.setVisibility(View.INVISIBLE);
            notAdmin.setVisibility(View.VISIBLE);

            parentDbName = "Admins";
        });
        notAdmin.setOnClickListener(v->{

            loginButton.setText(R.string.login);
            admin.setVisibility(View.VISIBLE);
            notAdmin.setVisibility(View.INVISIBLE);
            parentDbName = "Users";

        });
    }

    private void loginUser() {

        String phone = inputPhone.getText().toString();
        String password = iPassword.getText().toString();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required!!", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password requires at least 6 digits!! ", Toast.LENGTH_SHORT).show();
        } else {

            loading.setTitle("Logging in");
            loading.setMessage("Please wait while we check your credentials");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            allowAccessToAccount(phone, password);

        }
    }

    private void allowAccessToAccount(final String phone,final String password) {

        if (rememberMeChk.isChecked()){

            Paper.book().write(Prevalent.USER_PHONE_KEY,phone);
            Paper.book().write(Prevalent.USER_PASSWORD_KEY,password);
        }
        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = snapshot.child(parentDbName).child(phone.toString()).getValue(Users.class);

                    assert usersData != null;
                    if (usersData.getPhone().equals(phone)) {


                        if (usersData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")){
                               Toast.makeText(LoginActivity.this, "Welcome dear Admin YOU are Logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }else{

                                Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Wrong password", Toast.LENGTH_SHORT).show();
loading.dismiss();
                            return;
                        }
                    }
                    loading.dismiss();
                } else {
                    loading.dismiss();
                    Toast.makeText(LoginActivity.this, "Account with this number not exist, please create new account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}