package com.iamcheng5.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private boolean btnLock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.activity_bg_gray);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.loginAct_et_email);
        etPassword = findViewById(R.id.loginAct_et_password);
        findViewById(R.id.loginAct_btn_sign_up).setOnClickListener(this);
        findViewById(R.id.loginAct_btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!btnLock)
            return;
        btnLock = false;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(email.trim()) || TextUtils.isEmpty(password.trim())) {
            new CustomDialog1(LoginActivity.this, getString(R.string.dialog_message_field_empty), getString(R.string.dialog_message_ok_text), CODE -> {
            }).show();
            btnLock = true;
            return;
        }
        if (v.getId() == R.id.loginAct_btn_login) {
            login(email,password);
        } else if (v.getId() == R.id.loginAct_btn_sign_up) {
            signUp(email,password);

        }

    }

    private void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
            if (!task.isSuccessful()) {
                new CustomDialog1(LoginActivity.this, getString(R.string.dialog_message_login_failed_check_field), getString(R.string.dialog_message_ok_text), CODE -> {
                }).show();
                btnLock = true;
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void signUp(String email,String password){
        if (password.trim().length() < 8) {
            new CustomDialog1(LoginActivity.this, getString(R.string.dialog_message_password_length_error), getString(R.string.dialog_message_ok_text), CODE -> {
            }).show();
            btnLock = true;
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) { //create account failed
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        new CustomDialog1(LoginActivity.this, getString(R.string.dialog_message_firebaseAuthUserCollisionException), getString(R.string.dialog_message_ok_text), CODE -> {
                        }).show();
                    } else {
                        new CustomDialog1(LoginActivity.this, getString(R.string.dialog_message_sign_up_failed), getString(R.string.dialog_message_ok_text), CODE -> {
                        }).show();
                    }
                    btnLock = true;
                } else {
                    mAuth.signOut();//註冊後不登入
                    new CustomDialog1(LoginActivity.this, getString(R.string.dialog_message_sign_up_succeed), getString(R.string.dialog_message_ok_text), CODE -> {
                    }).show();
                    btnLock = true;
                    etPassword.setText("");
                    etEmail.setText("");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new CustomDialog2(this,
                getString(R.string.dialog_message_quit_message),
                getString(R.string.dialog_message_yes_text),
                getString(R.string.dialog_message_no_text),
                CODE -> {
                    if (CODE == CustomDialog2.POSITIVE_BUTTON_CLICK) {
                        finish();
                        System.exit(0);
                    }

                }).show();
    }

}