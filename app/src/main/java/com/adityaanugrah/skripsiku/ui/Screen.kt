package com.adityaanugrah.skripsiku.ui

import androidx.annotation.StringRes
import com.adityaanugrah.skripsiku.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Beranda : Screen("Beranda", R.string.beranda)
    object Pencarian : Screen("Search", R.string.pencarian)
    object Favorit : Screen("Favorit", R.string.favorit)
    object Profile : Screen("Profile", R.string.profile)
    object Login : Screen("Login", R.string.login)
    object Register : Screen("Register", R.string.register)

    companion object {
        const val homeRoot = "home-root"
    }
}