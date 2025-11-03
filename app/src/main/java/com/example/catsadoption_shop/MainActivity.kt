package com.example.catsadoption_shop

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catsadoption_shop.ui.theme.CatsAdoptionShopTheme

/**
 * Main entry point for the app.
 * This version starts with the cat listings instead of the login screen.
 * The user can browse cats freely and choose to log in or register if they want.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatsAdoptionShopTheme {
                // Launch the main composable that controls navigation and app state
                CatsAdoptionShopApp()
            }
        }
    }
}

/**
 * Root composable that manages navigation between all screens
 * and maintains the user's login state.
 */
@Composable
fun CatsAdoptionShopApp() {
    // Create a navigation controller to handle route changes
    val navController = rememberNavController()

    // Track the currently logged-in username; null means the user is logged out
    var loggedInUsername by remember { mutableStateOf<String?>(null) }

    // Determine which screen to show when the app first launches
    // If no one is logged in, start on the listings page
    val startDestination = if (loggedInUsername == null) "listings" else "dashboard"

    // Navigation graph: defines all available screens and how to move between them
    NavHost(navController = navController, startDestination = startDestination) {

        /**
         * 1. Cat Listings Screen
         *    - This is the default starting point.
         *    - Shows available cats for adoption.
         *    - Includes a "Login / Register" button that leads to the login screen.
         */
        composable("listings") {
            val context = LocalContext.current
            CatListingsScreen(
                onLoginClick = {
                    // When the login button is clicked, go to the login screen
                    navController.navigate("login")
                },
                onCatCommunicatorClick = {
                    val intent = Intent(context, PermissionCheckActivity::class.java)
                    context.startActivity(intent)
                },
                onAdminClick = {
                    navController.navigate("admin_dashboard")
                }
            )
        }

        /**
         * 2. Login Screen
         *    - Allows users to log in (or optionally register if implemented).
         *    - On successful login, saves the username and navigates to the dashboard.
         *    - Includes an optional cancel button that returns to listings.
         */
        composable("login") {
            LoginScreen(
                onLoginSuccess = { username ->
                    // Save the logged-in user’s name
                    loggedInUsername = username

                    // Navigate to the dashboard and clear back stack so the user
                    // cannot go back to the login or listings screens
                    navController.navigate("dashboard") {
                        popUpTo("listings") { inclusive = true }
                    }
                }
            )
        }

        /**
         * 3. Dashboard Screen
         *    - Visible only to logged-in users.
         *    - Displays user-specific information (e.g., adopted cats).
         *    - Includes a logout button that clears login state and returns to listings.
         */
        composable("dashboard") {
            val username = loggedInUsername

            if (username != null) {
                // User is logged in, show the dashboard
                UserDashboardScreen(
                    username = username,
                    onLogout = {
                        // Clear the user's session
                        loggedInUsername = null

                        // Return to the listings screen and clear navigation stack
                        navController.navigate("listings") {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            } else {
                // Safety fallback: If the dashboard is accessed without being logged in,
                // redirect immediately back to the listings screen
                navController.navigate("listings") {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }

        /**
         * 4. Admin Dashboard Screen
         *    - A placeholder screen for admin-only functionality.
         */
        composable("admin_dashboard") {
            AdminDashboardScreen()
        }
    }
}


/**
 * Preview composable for Android Studio.
 * Displays the app layout inside the preview window.
 */
@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun PreviewCatsAdoptionShopApp() {
    CatsAdoptionShopTheme {
        CatsAdoptionShopApp()
    }
}
