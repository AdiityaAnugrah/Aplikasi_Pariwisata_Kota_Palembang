package com.adityaanugrah.skripsiku.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.adityaanugrah.skripsiku.ui.BerandaScreen
import com.adityaanugrah.skripsiku.ui.FavoritScreen
import com.adityaanugrah.skripsiku.ui.LoginScreen
import com.adityaanugrah.skripsiku.ui.NavigationItem
import com.adityaanugrah.skripsiku.ui.PencarianScreen
import com.adityaanugrah.skripsiku.ui.ProfileScreen
import com.adityaanugrah.skripsiku.ui.RegisterScreen
import com.adityaanugrah.skripsiku.ui.Screen
import com.adityaanugrah.skripsiku.ui.theme.Purple40
import com.adityaanugrah.skripsiku.ui.theme.PurpleGrey80
import com.adityaanugrah.skripsiku.ui.theme.SkripsikuTheme

class EntryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SkripsikuTheme {
                EntryScreen()
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = Screen.Beranda.route, icon = Icons.Default.Home, screen = Screen.Beranda
            ),
            NavigationItem(
                title = Screen.Pencarian.route,
                icon = Icons.Default.Search,
                screen = Screen.Pencarian
            ),
            NavigationItem(
                title = Screen.Favorit.route, icon = Icons.Default.Favorite, screen = Screen.Favorit
            ),
            NavigationItem(
                title = Screen.Profile.route, icon = Icons.Default.Person, screen = Screen.Profile
            ),
        )

        BottomNavigation(
            backgroundColor = Purple40,
            contentColor = PurpleGrey80,
        ) {
            navigationItems.map { item ->
                BottomNavigationItem(icon = {
                    Icon(
                        imageVector = item.icon, contentDescription = item.title
                    )
                },
                    unselectedContentColor = Color.White,
                    selectedContentColor = Color.Yellow,
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    })
            }
        }
    }
}

@Composable
fun EntryScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(bottomBar = {
        when (currentRoute) {
            Screen.Beranda.route,
            Screen.Pencarian.route,
            Screen.Favorit.route,
            Screen.Profile.route -> {
                BottomBar(navController = navController)
            }
        }
    }) { innerPadding ->
        NavHost(
            navController, startDestination = Screen.homeRoot, Modifier.padding(innerPadding)
        ) {
            navigation(Screen.Beranda.route, Screen.homeRoot) {
                composable(Screen.Beranda.route) { BerandaScreen() }
                composable(Screen.Pencarian.route) { PencarianScreen() }
                composable(Screen.Favorit.route) {
                    FavoritScreen {
                        navController.navigate(Screen.Login.route)
                    }
                }
                composable(Screen.Profile.route) {
                    ProfileScreen {
                        navController.navigate(Screen.Login.route)
                    }
                }
            }

            composable(Screen.Login.route) {
                LoginScreen(goToBeranda = {
                    navController.popBackStack()
                }, goToRegister = {
                    navController.navigate(Screen.Register.route)
                })
            }

            composable(Screen.Register.route) {
                RegisterScreen(navigator = {
                    navController.popBackStack()
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EntryPreview() {
    SkripsikuTheme {
        EntryScreen()
    }
}