package com.example.flashkart.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flashkart.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import androidx.compose.runtime.getValue

@Composable
fun LoginUi(flashViewModel: FlashViewModel){

    val context = LocalContext.current
    val verificationId by flashViewModel.verificationId.collectAsState()
    val otp by flashViewModel.otp.collectAsState()
    val loading by flashViewModel.loading.collectAsState()
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            flashViewModel.setLoading(false)

            if (credential.smsCode != null) {
                flashViewModel.setOtp(credential.smsCode!!)
            }
            signInWithPhoneAuthCredential(credential, context, flashViewModel)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            flashViewModel.setLoading(false)
            Toast.makeText(context, "Verification Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            flashViewModel.setVerificationId(verificationId)
            Toast.makeText(context, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
            flashViewModel.resetTimer()
            flashViewModel.runTimer()
            flashViewModel.setLoading(false)
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.flashkart),
                contentDescription = "App Icon",
                modifier = Modifier
                    .padding(
                        top = 50.dp,
                        bottom = 10.dp
                    )
                    .size(100.dp)
            )
            if (verificationId.isEmpty()) {
                NumberScreen(flashViewModel = flashViewModel, callbacks = callbacks)
            } else {
                OtpScreen(
                    otp = otp,
                    flashViewModel = flashViewModel,
                    callbacks = callbacks
                )
            }
        }
        if(verificationId.isNotEmpty()){
            IconButton(onClick = {
                flashViewModel.setVerificationId("")
                flashViewModel.setOtp("")
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back")
            }
        }
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = androidx.compose.ui.graphics.Color(0, 0, 0, 100)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = androidx.compose.ui.graphics.Color.White)
                    Text(text = "Sending OTP...", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }
    }
}