package com.mdb.wingit.wingit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import static android.Manifest.permission.READ_CONTACTS;

public class SignUp extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView, name;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        signUpButton = (Button) findViewById(R.id.email_sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


    }

    private void signup() {
        final String n = name.getText().toString();
        final String em = mEmailView.getText().toString();
        String pass = mPasswordView.getText().toString();
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
                            DatabaseReference userdb = mDatabase.child("Users").push();
                            userdb.setValue(user);
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                        } else if (!(task.isSuccessful())) {
                            Toast.makeText(SignUp.this, "Sign up problem",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


}