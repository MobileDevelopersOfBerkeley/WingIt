package com.mdb.wingit.wingit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password;
    private Button signup;
    private TextView login;
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
        login = (TextView) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(openLogin);
            }
        });
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    /** Sign up user with Firebase */
    private void signup() {
        final String nameText = name.getText().toString();
        final String emailText = email.getText().toString();
        String pwText = password.getText().toString();

        //If fields are empty, sign up fails
        if (nameText.length() == 0 || emailText.length() == 0 || pwText.length() == 0) {
            Toast.makeText(SignUpActivity.this, "Sign up failed, please fill in all blanks.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        final User user = new User(emailText, nameText);
        mAuth.createUserWithEmailAndPassword(emailText, pwText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference userRef = mDatabase.child("Users").child(uid);
                            userRef.setValue(user);
                            Intent openSelector = new Intent(SignUpActivity.this, CategorySelectorActivity.class);
                            startActivity(openSelector);
                        } else if (!(task.isSuccessful())) {
                            Toast.makeText(SignUpActivity.this, "Sign up problem",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}