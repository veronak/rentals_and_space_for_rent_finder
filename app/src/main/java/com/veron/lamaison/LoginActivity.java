package com.veron.lamaison;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private ProgressBar mProgressBar;
    private TextView mRegisterText;

    //retrieve value for the authentication state
    private FirebaseAuth.AuthStateListener mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = (EditText) findViewById(R.id.etext_mail);
        mPassword = (EditText)findViewById(R.id.etext_pass);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRegisterText = (TextView) findViewById(R.id.txt_register);
 // ...
// Initialize Firebase Auth

        getFireBaseStarted();

        Button sigIn = (Button) findViewById(R.id.btn_login);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())){
                    Log.d("onClick",  "attempting to authenticate");
                    showDialog();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),
                            mPassword.getText().toString()).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideDialog();
                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideDialog();
                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.Check again", Toast.LENGTH_SHORT).show();
                }


            }
        });
        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if(mProgressBar.getVisibility()== View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    //connect to firebase
    //instatiate the AuthstateListener object
    private void getFireBaseStarted(){
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    Toast.makeText(LoginActivity.this, "You have succesful signed in:" +
                            currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed out:");
                }
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuth);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //Remove the AuthStateListener
        if (mAuth != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuth);
        }
    }
}
