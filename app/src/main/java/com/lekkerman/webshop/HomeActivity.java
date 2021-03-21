package com.lekkerman.webshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private Button logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOutBtn = findViewById(R.id.logOut_button);


        logOutBtn.setOnClickListener(v-> {


            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);

        });
    }
}