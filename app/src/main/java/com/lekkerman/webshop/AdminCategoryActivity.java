package com.lekkerman.webshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tShirts, sportTShirts, femaleDresses, sweaters;
    private ImageView glasses, purses, hats, shoes;
    private ImageView headphones, watches, laptops, mobilePhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

       // Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();


        tShirts = findViewById(R.id.t_shirts_iv);
        sportTShirts = findViewById(R.id.sports_shirts_iv);
        femaleDresses = findViewById(R.id.t_dresses_iv);
        sweaters = findViewById(R.id.sweaters_iv);

        glasses = findViewById(R.id.glasses_iv);
        purses = findViewById(R.id.pursesETC_iv);
        hats = findViewById(R.id.hats_iv);
        shoes = findViewById(R.id.shoes_iv);

        headphones = findViewById(R.id.headphones_iv);
        watches = findViewById(R.id.watches_iv);
        laptops = findViewById(R.id.laptops_iv);
        mobilePhones = findViewById(R.id.mobilePhones_iv);

        tShirts.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "tShirts");
            startActivity(intent);
        });

        sportTShirts.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "sportTShirts");
            startActivity(intent);
        });

        femaleDresses.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "femaleDresses");
            startActivity(intent);
        });

        sweaters.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "Sweaters");
            startActivity(intent);
        });

        glasses.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "glasses");
            startActivity(intent);
        });

        hats.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "hats");
            startActivity(intent);
        });

       purses.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "purses");
            startActivity(intent);
        });

        shoes.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "shoes");
            startActivity(intent);
        });

        headphones.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "headphones");
            startActivity(intent);
        });

        laptops.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "laptops");
            startActivity(intent);
        });

        watches.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "watches");
            startActivity(intent);
        });
        mobilePhones.setOnClickListener(v->{
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category", "mobilePhones");
            startActivity(intent);
        });

    }
}