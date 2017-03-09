package com.mdb.wingit.wingit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    //UI references
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email, password;
    private ConstraintLayout bg;
    private int screenNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI elements
        email = (EditText) findViewById(R.id.email);
        bg = (ConstraintLayout) findViewById(R.id.screen);
        screenNumber = choosebgScreen();
        password = (EditText) findViewById(R.id.password);

        // TODO: Check the id passed into this shit
        Button login = (Button) findViewById(R.id.email_login_button);
//        TextView register = (TextView) findViewById(R.id.textView);

        //Authenticate user with Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent openMain = new Intent(getApplicationContext(), CategorySelectorActivity.class);
                    startActivity(openMain);

                } else {
                    // User is signed out
                }
            }
        };

        // TODO: Fix and uncomment dis 
        //Direct user to SignUpActivity Activity
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent openRegister = new Intent(getApplicationContext(), SignUpActivity.class);
//                openRegister.putExtra("background", screenNumber);
//                startActivity(openRegister);
//            }
//        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString(), password.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /** Sign in user with Firebase */
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "LoginActivity failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /** Choose random background screen */
    public int choosebgScreen() {
        Random random = new Random();
        int n = random.nextInt(3) + 1;
        switch (n) {
            case 1: {
                bg.setBackgroundResource(R.drawable.sfdawnv2);
            }
            case 2: {
                bg.setBackgroundResource(R.drawable.nycnightv3);
            }
            case 3: {
                bg.setBackgroundResource(R.drawable.honoluluv3);
            }

        }
        return n;
    }
}
