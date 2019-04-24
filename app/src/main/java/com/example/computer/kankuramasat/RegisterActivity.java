package com.example.computer.kankuramasat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private EditText mailedit,passedit,mname,mcity;
    private TextView mlogin;
    private Button mregbtn;
    private FirebaseAuth mauth;
    private DatabaseReference mRoot;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mauth = FirebaseAuth.getInstance();
        initializefields();
        mRoot = FirebaseDatabase.getInstance().getReference();
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        mregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });
    }
    private void createaccount() {
        final String email = mailedit.getText().toString();
        final String password = passedit.getText().toString();
        final String name = mname.getText().toString();
        final String city = mcity.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please provide email...",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please provide password...",Toast.LENGTH_LONG).show();
        } if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please provide name...",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(city)){
            Toast.makeText(this,"Please provide city name...",Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.setTitle("Creating new account");
            progressDialog.setMessage("Please wait, process is under construction...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.show();
                       // String devicetoken = FirebaseInstanceId.getInstance().getToken();
                        String currentId = mauth.getCurrentUser().getUid();
                        Map<String,Object> profilemap =new HashMap<>();
                        profilemap.put("uid",currentId);
                        profilemap.put("email",email);
                        profilemap.put("name",name);
                        profilemap.put("city",city);
                        profilemap.put("password",password);
                        mRoot.child("Users").child(currentId).setValue(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    SendUserToMainActivity();
                                    Toast.makeText(RegisterActivity.this,"Sighed up successfully",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"Sighed up failed",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });

                    }else {
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this,"Error:"+message,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
    private void SendUserToMainActivity() {
        Intent loginInt = new Intent(RegisterActivity.this,Home.class);
        loginInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginInt);
        finish();
    }

    private void initializefields() {
        mailedit = (EditText)findViewById(R.id.atvEmailReg);
        passedit = (EditText)findViewById(R.id.atvPasswordReg);
        mname = (EditText)findViewById(R.id.atvUsernameReg);
        mcity = (EditText)findViewById(R.id.address);
        mlogin= (TextView)findViewById(R.id.tvSignIn);
        mregbtn = (Button)findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);
    }
    private void SendUserToLoginActivity() {
        Intent loginInt = new Intent(RegisterActivity.this,LoginActivity.class);
        loginInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginInt);
    }
}
