package com.example.otpverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTP extends AppCompatActivity {


    Button verify_now_btn;
    EditText otp_ET;
    TextView user_number_TV;
    String verificationId="";
    String phone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);


        verify_now_btn = findViewById(R.id.verify_now_btn);
        otp_ET = findViewById(R.id.otp_ET);
        user_number_TV = findViewById(R.id.user_number_TV);


        phone = getIntent().getStringExtra("phone");
        verificationId = getIntent().getStringExtra("verificationId");
        user_number_TV.setText(phone);



        verify_now_btn.setOnClickListener(v->{

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp_ET.getText().toString().trim());
            FirebaseAuth.getInstance()
                    .signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(),SuccessPage.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Verification Failed \n"+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Verification Failed \n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });



    }
}