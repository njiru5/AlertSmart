package net.ezra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ezra.ui.SplashScreen
import net.ezra.ui.about.AboutScreen
import net.ezra.ui.auth.LoginScreen
import net.ezra.ui.auth.SignUpScreen
import net.ezra.ui.criminology.CommunitySpaceScreen
import net.ezra.ui.criminology.CreateScreen
import net.ezra.ui.criminology.EnrollScreen
import net.ezra.ui.criminology.ProfileScreen
import net.ezra.ui.criminology.ReportCrimeScreen
import net.ezra.ui.criminology.ViewNewsScreen
import net.ezra.ui.criminology.WantedScreen
import net.ezra.ui.dashboard.DashboardScreen
import net.ezra.ui.home.HomeScreen
import net.ezra.ui.products.AddProductScreen
import net.ezra.ui.products.ProductDetailScreen
import net.ezra.ui.products.ProductListScreen
import net.ezra.ui.students.AddStudents
import net.ezra.ui.students.Search
import net.ezra.ui.students.Students

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(navController)
        }
        composable(ROUTE_ABOUT) {
            AboutScreen(navController)
        }
        composable(ROUTE_ADD_STUDENTS) {
            AddStudents(navController)
        }
        composable(ROUTE_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUTE_VIEW_STUDENTS) {
            Students(navController = navController, viewModel = viewModel())
        }
        composable(ROUTE_SEARCH) {
            Search(navController)
        }
        composable(ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }
        composable(ROUTE_REGISTER) {
            SignUpScreen(navController = navController) {}
        }
        composable("$ROUTE_LOGIN/{destination}") { backStackEntry ->
            val destination = backStackEntry.arguments?.getString("destination")
            LoginScreen(navController = navController, destination = destination)
        }
        composable(ROUTE_ADD_PRODUCT) {
            AddProductScreen(navController = navController) {}
        }
        composable(ROUTE_VIEW_PROD) {
            ProductListScreen(navController = navController, products = listOf())
        }
        composable(ROUTE_REPORT) {
            ReportCrimeScreen(navController = navController)
        }
        composable(ROUTE_SPACE) {
           CommunitySpaceScreen(navController = navController)
        }
        composable(ROUTE_ENROLL) {
            EnrollScreen(navController = navController)
        }
        composable(ROUTE_WANTED) {
            WantedScreen(navController = navController)
        }
        composable(ROUTE_CREATE) {
           CreateScreen(navController = navController)
        }
        composable(ROUTE_PROFILE) {
           ProfileScreen(navController = navController)
        }
        composable(ROUTE_NEWS) {
           ViewNewsScreen(navController = navController)
        }
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController, productId)
        }
    }
}
