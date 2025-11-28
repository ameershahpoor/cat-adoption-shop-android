package com.example.catsadoption_shop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.catsadoption_shop.data.AppDatabase
import com.example.catsadoption_shop.data.User
import com.example.catsadoption_shop.data.UserRepository
import com.example.catsadoption_shop.security.WeakEncryption
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Local DB
    val userRepository = remember {
        val userDao = AppDatabase.getDatabase(context).userDao()
        UserRepository(userDao)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("User Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            val color = if (message.contains("successful", ignoreCase = true))
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error

            Text(message, color = color)
            Spacer(Modifier.height(8.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            // LOGIN
            Button(onClick = {
                coroutineScope.launch {
                    if (username.isBlank() || password.isBlank()) {
                        message = "Username and password cannot be empty."
                        return@launch
                    }

                    val user = userRepository.getUserByUsername(username)

                    if (user != null) {
                        val decrypted = WeakEncryption.decrypt(user.encryptedPassword)
                        if (decrypted == password) {
                            message = "Login successful!"
                            onLoginSuccess(username)
                        } else {
                            message = "Invalid username or password."
                        }
                    } else {
                        message = "Invalid username or password."
                    }
                }
            }) {
                Text("Login")
            }

            // REGISTER
            Button(onClick = {
                coroutineScope.launch {
                    if (username.isBlank() || password.isBlank()) {
                        message = "Username and password are required for registration."
                        return@launch
                    }

                    val existing = userRepository.getUserByUsername(username)
                    if (existing != null) {
                        message = "Username already taken."
                        return@launch
                    }

                    val encryptedPassword = WeakEncryption.encrypt(password)
                    val newUser = User(username = username, encryptedPassword = encryptedPassword)
                    userRepository.registerUser(newUser)

                    message = "Registration successful! Please log in."
                }
            }) {
                Text("Register")
            }
        }
    }
}
