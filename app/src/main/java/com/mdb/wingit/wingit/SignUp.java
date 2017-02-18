package com.mdb.wingit.wingit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.Manifest.permission.READ_CONTACTS;

public class SignUp extends AppCompatActivity {

    //UI references
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView, name;
    private Button signUpButton;
    private ConstraintLayout bg;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //UI elements
        mEmailView = (TextInputEditText) findViewById(R.id.email);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        name = (TextInputEditText) findViewById(R.id.name);
        signUpButton = (Button) findViewById(R.id.email_sign_in_button);
        bg = (ConstraintLayout) findViewById(R.id.screen);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        choosebgScreen();
    }

    /** Sign up user with Firebase */
    private void signup() {
        final String n = name.getText().toString();
        final String em = mEmailView.getText().toString();
        String pass = mPasswordView.getText().toString();

        //If fields are empty, sign up fails
        if(n.length()==0 || em.length()==0 || pass.length()==0){
            Toast.makeText(SignUp.this, "Sign up failed, please fill in all blanks.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        final User user = new User(em, n);
        mAuth.createUserWithEmailAndPassword(em, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference userdb = mDatabase.child("Users").child(uid);
                            userdb.setValue(user);
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                        } else if (!(task.isSuccessful())) {
                            Toast.makeText(SignUp.this, "Sign up problem",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    /** Get background screen from Login Activity */
    public void choosebgScreen() {
        int n = getIntent().getIntExtra("background", 1);
        switch (n) {
            case 1: {
                bg.setBackground(getResources().getDrawable(R.drawable.sfdawnv2));
            }
            case 2: {
                bg.setBackground(getResources().getDrawable(R.drawable.nycnightv3));
            }
            case 3: {
                bg.setBackground(getResources().getDrawable(R.drawable.honoluluv3));
            }
        }
    }
}