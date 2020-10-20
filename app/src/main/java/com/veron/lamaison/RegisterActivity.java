package com.veron.lamaison;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";
    private EditText mRegisterEmail;
    private EditText mPassowrd, mPasswordConfirm;
    private Button mRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegisterEmail = (EditText) findViewById(R.id.etext_register_email);
        mPassowrd = (EditText) findViewById(R.id.etext_reg_password);
        mPasswordConfirm = (EditText) findViewById(R.id.etext_pass_confirm);
        mRegister = (Button) findViewById(R.id.register_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: register user");
                if(!isEmpty(mRegisterEmail.getText().toString())
                && !isEmpty(mPassowrd.getText().toString())
                && !isEmpty(mPasswordConfirm.getText().toString())){
                    if(doStringMatch(mPassowrd.getText().toString(),mPasswordConfirm.getText().toString())) {

                        registerNewUser(mRegisterEmail.getText().toString(), mPassowrd.getText().toString());
                    }else {
                        Toast.makeText(RegisterActivity.this, "Password doesnot match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }

            }
        });
        hideSoftKeyboard();



    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void registerNewUser(final String email, String password) {
        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserwithEmail:onComplete" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            FirebaseAuth.getInstance().signOut();
                            goToLoginScreen();

                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();

                    }
                    });

    }

    private void goToLoginScreen() {
        //Go to the login screen
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void hideDialog() {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private boolean doStringMatch(String string1, String string2) {
        return string1.equals(string2) ;
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }
}

