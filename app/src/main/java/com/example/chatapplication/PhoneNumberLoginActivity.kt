package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneNumberLoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var number: EditText
    private lateinit var BtnNum: Button
    private lateinit var otp: EditText
    private lateinit var BtnOtp: Button
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private  lateinit var name:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //supportActionBar!!.hide()

        setContentView(R.layout.activity_phone_number_login)
        number = findViewById(R.id.ETNumber)
        otp = findViewById(R.id.ETOtp)
        BtnNum = findViewById(R.id.btn_login)
        BtnOtp = findViewById(R.id.Verify)
        name=findViewById(R.id.Name)

        auth = FirebaseAuth.getInstance()

        val text=name.text




        BtnNum.setOnClickListener {
            val phoneNumber = number.text.toString().trim()
            if (!phoneNumber.isEmpty()) {
                sendVerificationCode("+91$phoneNumber")
            } else {
                Toast.makeText(this, "Enter phoneNumber first", Toast.LENGTH_LONG).show()
            }
        }

        BtnOtp.setOnClickListener {
            val otp = otp.text.toString().trim()
            if (otp.isNotEmpty()) {
                verifyVerificationCode(otp)
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code:String?=credential.smsCode
                if(code!=null){
                    otp.setText(code)
                }

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@PhoneNumberLoginActivity, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {

                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            this,
            callbacks)
    }

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show()
                    val oi = Intent(this, MainActivity::class.java)
                    startActivity(oi)
                    finish()
                } else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    otp.setText("")
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser
        if(currentUser!=null){
            val currentIntent=Intent(this@PhoneNumberLoginActivity,MainActivity::class.java)
            startActivity(currentIntent)
        }
    }

}
