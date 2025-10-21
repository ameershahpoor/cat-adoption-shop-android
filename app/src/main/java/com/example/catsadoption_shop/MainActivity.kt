package com.example.catsadoption_shop

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catsadoption_shop.ui.theme.CatsAdoptionShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatsAdoptionShopTheme {
                CatsAdoptionShopApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatsAdoptionShopApp() {
    val navController = rememberNavController()
    Scaffold {
        NavigationGraph(navController)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "cats") {
        composable("cats") {
            CatListingsScreen(onLoginClick = { navController.navigate("dashboard") })
        }
        //composable("dashboard") { UserDashboardScreen() } //TODO later
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatsAdoptionShopTheme {
        CatsAdoptionShopApp()
    }
}