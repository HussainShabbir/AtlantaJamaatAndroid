package com.atlantajamaat.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atlantajamaat.app.models.LoginState

sealed class Screen(val route: String) {
    object ValidateITS: Screen("validateITS")
    object GuestLogin: Screen("guestLogin")
    object MemberLogin: Screen("memberLogin")
    object ForgotPassword: Screen("forgotPassword")
    object HomeScreen: Screen("homeScreen")
}
// AppNavigation.kt
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.ValidateITS.route) {
        composable(route = Screen.ValidateITS.route) {
            NumberInputApiScreen(onNavigate = { loginState ->
                if (loginState is LoginState.NavigateToGuestLogin) {
                    navController.navigate(Screen.GuestLogin.route)
                } else if (loginState is LoginState.NavigateToMemberLogin) {
                    navController.navigate(Screen.MemberLogin.route)
                }
            })
        }
        // Member Login Route
        composable(route = Screen.MemberLogin.route) {
            MemberLoginScreen(onNavigateToHome = {
                navController.navigate(Screen.HomeScreen.route)
            })
        }

        // Guest Login Route
        composable(route = Screen.GuestLogin.route) {
            GuestLoginScreen(onLoginClick = {}, onForgotPasswordClick = {
                navController.navigate(Screen.ForgotPassword.route)
            })
        }

        // Forgot Password Route
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(onSendEmailClick = {}, onBackClick = {
                navController.popBackStack()})
        }

        composable(route = Screen.HomeScreen.route) {
            Home()
        }
    }
}