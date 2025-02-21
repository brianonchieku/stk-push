package com.example.stkpush

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.stkpush.viewmodel.AuthViewModel
import com.example.stkpush.viewmodel.StkPushViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthScreen(viewModel: StkPushViewModel) {

    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (phoneNumber.isNotEmpty() && amount.isNotEmpty()) {
                viewModel.fetchAccessToken { isSuccess ->
                    if (isSuccess) {
                        viewModel.initiateStkPush(phoneNumber, amount) { response ->
                            if (response != null) {
                                Toast.makeText(context, "Payment Initiated", Toast.LENGTH_LONG).show()
                                Log.d("STK", "STK Push successful: ${response.CheckoutRequestID}")
                            } else {
                                Toast.makeText(context, "Payment Failed", Toast.LENGTH_LONG).show()
                                Log.e("STK", "STK Push failed")
                            }
                        }
                    } else {
                        Toast.makeText(context, "Failed to fetch access token", Toast.LENGTH_LONG).show()
                        Log.e("STK", "Failed to fetch access token")
                    }
                }

            }
        }) {
            Text("Pay Now")
        }
    }
}