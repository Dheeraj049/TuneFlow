package uk.ac.tees.mad.tuneflow.view.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import uk.ac.tees.mad.tuneflow.ui.screens.HomeScreen
import uk.ac.tees.mad.tuneflow.ui.screens.NowPlayingScreen
import uk.ac.tees.mad.tuneflow.ui.screens.PlaylistScreen
import uk.ac.tees.mad.tuneflow.ui.screens.ProfileScreen
import uk.ac.tees.mad.tuneflow.ui.screens.SignInScreen
import uk.ac.tees.mad.tuneflow.ui.screens.SignUpScreen
import uk.ac.tees.mad.tuneflow.ui.screens.SplashScreen

const val CARD_TRANSITION_KEY = "CARD_TRANSITION_KEY"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetupNavGraph(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(
            navController = navController, startDestination = Dest.SplashScreen
        ) {
            composable<Dest.SplashScreen> {
                SplashScreen(navController = navController)
            }
            navigation<SubGraph.AuthGraph>(startDestination = Dest.SignInScreen) {
                composable<Dest.SignInScreen> {
                    SignInScreen(navController = navController, animatedVisibilityScope = this)
                }
                composable<Dest.SignUpScreen> {
                    SignUpScreen(navController = navController, animatedVisibilityScope = this)
                }
            }
            navigation<SubGraph.HomeGraph>(startDestination = Dest.HomeScreen) {
                composable<Dest.HomeScreen> {
                    HomeScreen(navController = navController)
                }
                composable<Dest.NowPlayingScreen> {
                    val args = it.toRoute<Dest.NowPlayingScreen>()
                    NowPlayingScreen(
                        navController = navController,
                        trackId = args.trackId,
                        flag = args.flag
                    )
                }
                composable<Dest.PlaylistScreen> {
                    PlaylistScreen(
                        navController = navController
                    )
                }
                composable<Dest.ProfileScreen> {
                    ProfileScreen(navController = navController)
                }
            }
        }
    }
}