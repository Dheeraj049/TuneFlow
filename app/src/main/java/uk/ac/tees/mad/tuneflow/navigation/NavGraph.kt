package uk.ac.tees.mad.tuneflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import uk.ac.tees.mad.tuneflow.ui.screens.HomeScreen
import uk.ac.tees.mad.tuneflow.ui.screens.SignInScreen
import uk.ac.tees.mad.tuneflow.ui.screens.SignUpScreen
import uk.ac.tees.mad.tuneflow.ui.screens.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Dest.SplashScreen
    ) {
        composable<Dest.SplashScreen> {
            SplashScreen(navController = navController)
        }
        navigation<SubGraph.AuthGraph>(startDestination = Dest.SignInScreen) {
            composable<Dest.SignInScreen> {
                SignInScreen(navController = navController)
            }
            composable<Dest.SignUpScreen> {
                SignUpScreen(navController = navController)
            }
        }
        navigation<SubGraph.HomeGraph>(startDestination = Dest.HomeScreen) {
            composable<Dest.HomeScreen> {
                HomeScreen(navController = navController)
            }
//            composable<Dest.NowPlayingScreen> {
//                NowPlayingScreen(navController = navController)
//            }
//            composable<Dest.PlaylistScreen> {
//                PlaylistScreen(
//                    navController = navController
//                )
//            }
//            composable<Dest.ProfileScreen> {
//                ProfileScreen(navController = navController)
//            }
        }
    }
}