package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Adventure;
import com.mdb.wingit.wingit.modelClasses.User;

/**
 * Sign up using name, email, and password
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //UI elements
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        TextView login = (TextView) findViewById(R.id.login);
        Button signUp = (Button) findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    /** Sign up user with Firebase */
    private void signUp() {
        final String nameText = name.getText().toString();
        final String emailText = email.getText().toString();
        String pwText = password.getText().toString();

        //Check if name, email, and password fields are empty
        if (nameText.length() == 0 || emailText.length() == 0 || pwText.length() == 0) {
            Toast.makeText(SignUpActivity.this, "Sign up failed, please fill in all blanks.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText, pwText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(emailText, nameText);
                            String uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference userRef = mDatabase.child("Users").child(uid);
                            userRef.setValue(user);
                            Intent selectorIntent = new Intent(SignUpActivity.this, CategorySelectorActivity.class);
                            startActivity(selectorIntent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}