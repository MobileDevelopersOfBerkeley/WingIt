package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mdb.wingit.wingit.R;

/**
 * Login using email and password
 */

public class LoginActivity extends AppCompatActivity implements OnMapReadyCallback {

    //UI references
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI elements
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login_button);
        TextView signUp = (TextView) findViewById(R.id.signup);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Authenticate user with Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent openSelector = new Intent(getApplicationContext(), CategorySelectorActivity.class);
                    startActivity(openSelector);
                }
            }
        };

        //Direct user to SignUpActivity Activity
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(openSignUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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
    private void signIn(){
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        //Check if email and password fields are empty
        if(emailText.length() == 0 || passwordText.length() == 0) {
            Toast.makeText(LoginActivity.this, "Please enter an email and password",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email or password is incorrect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /** Center background map on current location */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }
}
