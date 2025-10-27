package com.example.catsadoption_shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun UserDashboardScreen(
    username: String,
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var adoptedCats by remember { mutableStateOf<List<CatResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            // Placeholder for the user-specific request (later replace with /user/{id}/cats)
            adoptedCats = CatRepository.fetchCats().take(5)
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Welcome $username 🐾", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Your adopted cats:", style = MaterialTheme.typography.titleMedium)
            }

            // Logout button
            Button(
                onClick = onLogout,
                modifier = Modifier.alignByBaseline()
            ) {
                Text("Logout")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(adoptedCats) { cat ->
                    AdoptedCatItem(cat)
                }
            }
        }
    }
}

@Composable
fun AdoptedCatItem(cat: CatResponse) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = cat.url,
                contentDescription = "Adopted cat",
                modifier = Modifier.size(90.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Cat ID: ${cat.id}", style = MaterialTheme.typography.titleMedium)
                Text("Resolution: ${cat.width ?: "?"}×${cat.height ?: "?"}")
            }
        }
    }
}