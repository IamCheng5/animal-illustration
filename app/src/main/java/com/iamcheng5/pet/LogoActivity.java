package com.iamcheng5.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white);
        new DatabaseHelper(this).createDatabase();
        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Intent intent;
            if (currentUser != null){
                intent = new Intent(LogoActivity.this,MainActivity.class);
            }else{
                intent = new Intent(LogoActivity.this,LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}