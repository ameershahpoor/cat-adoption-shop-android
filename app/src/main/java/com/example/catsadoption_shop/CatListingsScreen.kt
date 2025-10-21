package com.example.catsadoption_shop


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.catsadoption_shop.CatRepository
import com.example.catsadoption_shop.CatResponse

@Composable
fun CatListingsScreen(onLoginClick: () -> Unit) {
    val scope = rememberCoroutineScope()
    var cats by remember { mutableStateOf<List<CatResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                cats = CatRepository.fetchCats()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error fetching cats"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Welcome to CatsAdoption Shop 🐱",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text("Error: $errorMessage")
            }
            else -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cats) { cat ->
                        CatItem(cat)
                    }
                }
            }
        }

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Login to Your Dashboard")
        }
    }
}