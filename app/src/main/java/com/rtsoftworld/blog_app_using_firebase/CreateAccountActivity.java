package com.rtsoftworld.blog_app_using_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText passwordAcc;
    private Button createAccountBtn;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        firstName = findViewById(R.id.firstNameAc);
        lastName = findViewById(R.id.lastNameAcc);
        email = findViewById(R.id.emailAcc);
        passwordAcc = findViewById(R.id.passwordAct);
        createAccountBtn = findViewById(R.id.createAccountButtonAct);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String em = email.getText().toString();
        String pwd = passwordAcc.getText().toString();

        if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName) & !TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)){
            mProgressDialog.setMessage("Creating Account ...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDB = mDatabaseReference.child(userId);
                        currentUserDB.child("firstname").setValue(fName);
                        currentUserDB.child("lastname").setValue(lName);
                        currentUserDB.child("email").setValue(em);
                        currentUserDB.child("pwd").setValue(pwd);
                        currentUserDB.child("image").setValue("none");

                        mProgressDialog.dismiss();

                        // send user to postListActivity
                        Intent intent = new Intent(CreateAccountActivity.this,PostListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                }
            });

        }
    }
}