package com.atlantajamaat.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlantajamaat.app.models.LoginState
import com.atlantajamaat.app.network.LoginRepository
import com.atlantajamaat.app.platform.platform
import com.atlantajamaat.app.ui.theme.AppTopBar
import com.example.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NumberInputApiScreen() {
    var itsNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var apiResponse by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    val loginRepository = LoginRepository()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Enter ITS Number"
            )
        },
        containerColor = LightGreyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter Details",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter your ITS number to proceed",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Phone Input Field
                    OutlinedTextField(
                        value = itsNumber,
                        onValueChange = { input ->
                            // Allow only digits
                            if (input.all { it.isDigit() }) {
                                itsNumber = input
                                isError = false
                            }
                        },
                        label = { Text("ITS Number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = DarkBlue
                            )
                        },
                        singleLine = true,
                        isError = isError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkBlue,
                            focusedLabelColor = DarkBlue
                        )
                    )

                    if (isError) {
                        Text(
                            text = "Please enter a valid ITS number",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (itsNumber.isBlank() || itsNumber.length < 8) {
                                isError = true
                            } else {
                                isError = false
                                isLoading = true
                                apiResponse = null

                                // Trigger API Call inside Coroutine Scope
                                coroutineScope.launch {
                                    apiResponse = validateITS(loginRepository, itsNumber,
                                        platform()
                                    )
                                    isLoading = false
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkBlue,
                            contentColor = Color.White
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
                                text = "Submit",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Display API Response State
                    apiResponse?.let { result ->
                        Spacer(modifier = Modifier.height(20.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = result,
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFF2E7D32),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// Function Signature Update
suspend fun validateITS(loginRepository: LoginRepository, itsId: String, userAgent: String): String {
    return when (val state = loginRepository.performLogin(itsId, userAgent)) {
        is LoginState.NavigateToGuestLogin -> "Success: Navigate to Welcome"
        is LoginState.NavigateToMemberLogin -> "Success: Navigate to PreLogin"
        is LoginState.Error -> "Error: ${state.message}"
        else -> "Unknown State"
    }
}