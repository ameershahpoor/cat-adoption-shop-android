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
fun UserDashboardScreen(username: String) {
    val scope = rememberCoroutineScope()
    var adoptedCats by remember { mutableStateOf<List<CatResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            // This simulates an IDOR-vulnerable request
            // Later I'll swap to: GET /user/{id}/cats
            adoptedCats = CatRepository.fetchCats().take(5)
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Welcome $username 🐾", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Your adopted cats:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
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