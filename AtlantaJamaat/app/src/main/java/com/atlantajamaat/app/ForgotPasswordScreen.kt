package com.atlantajamaat.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlantajamaat.app.models.LoginState
import com.atlantajamaat.app.network.LoginRepository
import com.atlantajamaat.app.ui.theme.AppTopBar
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightGreyBg
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onSendEmailClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var itsId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isButtonEnabled = itsId.isNotBlank() && email.isNotBlank() && !isLoading
    val repository = remember { LoginRepository() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Forgot Password",
                onBackClick = onBackClick
            )
        },
        containerColor = LightGreyBg
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = LightGreyBg
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Reset Password",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Enter your ITS ID and registered email to receive reset instructions.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // ITS ID Input Field
                        OutlinedTextField(
                            value = itsId,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() }) {
                                    itsId = input
                                }
                            },
                            label = { Text("ITS ID") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "ITS ID Icon"
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email Input Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email Icon"
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Send Email Button
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    val result = repository.forgotPassword(itsId, email)
                                    isLoading = false
                                    when (result) {
                                        is LoginState.Success -> onSendEmailClick()
                                        is LoginState.Error -> errorMessage = result.message
                                        else -> errorMessage = "Something went wrong"
                                    }
                                }
                            },
                            enabled = isButtonEnabled,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkBlue,
                                disabledContainerColor = Color(0xFF94A3B8)
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = "Send Email",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Back to Login Link
                        TextButton(
                            onClick = onBackClick
                        ) {
                            Text(
                                text = "Back to Login",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkBlue
                            )
                        }

                        errorMessage?.let { errorText ->
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = errorText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}