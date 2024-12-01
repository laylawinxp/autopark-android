package com.example.autopark.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.autopark.dto.UsersDto
import com.example.autopark.navigation.Screen
import com.example.autopark.viewModel.UsersViewModel
import org.mindrot.jbcrypt.BCrypt

var isAdmin = false

@Composable
fun LoginScreen(
    navController: NavHostController,
    usersViewModel: UsersViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoginScreen by remember { mutableStateOf(true) }

    val isValid = email.isNotEmpty() && password.isNotEmpty() && (isLoginScreen || password == confirmPassword)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isLoginScreen) "Login" else "Sign Up",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )

        if (!isLoginScreen) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (isValid) {
                    isLoading = true

                    if (isLoginScreen) {
                        val user = usersViewModel.users.value.find { it.email == email }
                        if (user != null) {
                            isAdmin = user.isAdmin
                        }
                        isLoading = false

                        if (user != null && BCrypt.checkpw(password, user.password)) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid email or password"
                        }
                    } else {
                        val userExists = usersViewModel.users.value.any { it.email == email }
                        if (userExists) {
                            errorMessage = "Email already registered"
                        } else {
                            val newUser = UsersDto(
                                id = 0,
                                username = username,
                                email = email,
                                password = BCrypt.hashpw(password, BCrypt.gensalt()),
                                isAdmin = false
                            )
                            usersViewModel.addUser(newUser)
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
                } else {
                    errorMessage = "Please fill in all fields correctly"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isValid && !isLoading
        ) {
            Text(text = if (isLoading) "Loading..." else if (isLoginScreen) "Login" else "Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoginScreen = !isLoginScreen
                errorMessage = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLoginScreen) "Don't have an account? Register here" else "Already have an account? Log in")
        }
    }
}