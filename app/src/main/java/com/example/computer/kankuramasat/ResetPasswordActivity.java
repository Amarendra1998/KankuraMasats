package com.example.computer.kankuramasat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText mailedit;
    private TextView mlogin;
    private Button mregbtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mailedit = (EditText)findViewById(R.id.atvEmailRes);
        mlogin= (TextView)findViewById(R.id.tvGoBack);
        mregbtn = (Button)findViewById(R.id.btnReset);
        firebaseAuth = FirebaseAuth.getInstance();
        mregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = mailedit.getText().toString().trim();
                if (useremail.equals(""))
                {
                    Toast.makeText(ResetPasswordActivity.this,"Please enter your registered email ID",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful())
                             {
                                 Toast.makeText(ResetPasswordActivity.this,"Password reset email sent",Toast.LENGTH_SHORT).show();
                                 finish();
                                 startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                             }else {
                                 Toast.makeText(ResetPasswordActivity.this,"Error in sending password to email..",Toast.LENGTH_SHORT).show();

                             }
                        }
                    });
                }
            }
        });
    }
}
