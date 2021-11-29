package com.example.otpverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterMobileNumberScreen extends AppCompatActivity {

    Button send_otp_btn;
    EditText number_ET;
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number_screen);

        send_otp_btn = findViewById(R.id.send_otp_btn);
        number_ET = findViewById(R.id.number_ET);

        mAuth = FirebaseAuth.getInstance();



        send_otp_btn.setOnClickListener(v->{


            if(number_ET.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
            }
            else if(number_ET.getText().toString().trim().length()<12){
                Toast.makeText(getApplicationContext(), "Please enter correct mobile number", Toast.LENGTH_SHORT).show();
            }
            else{

                send_otp_btn.setEnabled(false);


                otpSend();


                /*Intent intent = new Intent(getApplicationContext(),VerifyOTP.class);
                intent.putExtra("mobile",number_ET.getText().toString());
                startActivity(intent);*/
            }

        });


    }

    void otpSend(){

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                send_otp_btn.setEnabled(true);
                Toast.makeText(getApplicationContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Intent intent  = new Intent(getApplicationContext(),VerifyOTP.class);
                intent.putExtra("phone",number_ET.getText().toString().trim());
                intent.putExtra("verificationId",verificationId);

                startActivity(intent);

            }
        };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number_ET.getText().toString().trim())       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),SuccessPage.class));
        }
    }
}