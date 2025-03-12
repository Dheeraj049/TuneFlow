package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.model.dataclass.LoadingState
import uk.ac.tees.mad.tuneflow.view.navigation.Dest
import uk.ac.tees.mad.tuneflow.view.navigation.SubGraph
import uk.ac.tees.mad.tuneflow.view.utils.LoadingErrorScreen
import uk.ac.tees.mad.tuneflow.view.utils.LoadingScreen
import uk.ac.tees.mad.tuneflow.viewmodel.SplashScreenViewModel

@Composable
fun SplashScreen(
    navController: NavHostController, viewModel: SplashScreenViewModel = koinViewModel()
) {
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = loadingState,
                animationSpec = tween(durationMillis = 1000),
                label = "splashScreen"
            ) { state ->
                when (state) {
                    is LoadingState.Loading -> {
                        AnimatedVisibility(state == LoadingState.Loading) {
                            LoadingScreen()
                        }
                    }

                    is LoadingState.Error -> {
                        LoadingErrorScreen(
                            errorMessage = state.message,
                            onRetry = { viewModel.startLoading() })
                    }

                    is LoadingState.Success -> {
                        val isUserLoggedIn = viewModel.isLoggedIn()
                        LaunchedEffect(key1 = Unit) {
                            if (!isUserLoggedIn) {
                                navController.navigate(SubGraph.AuthGraph) {
                                    popUpTo(Dest.SplashScreen) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                navController.navigate(SubGraph.HomeGraph) {
                                    popUpTo(Dest.SplashScreen) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


//@Composable
//fun SplashScreen(
//    navController: NavHostController, viewModel: SplashScreenViewModel = koinViewModel()
//) {
//    var isVisible by remember { mutableStateOf(true) }
//
//    // Title scale animation
//    val scale = remember { Animatable(0.3f) }
//
//    LaunchedEffect(key1 = true) {
//        // Animate title scale
//        scale.animateTo(
//            targetValue = 1f, animationSpec = tween(
//                durationMillis = 1000, easing = EaseOutBack
//            )
//        )
//
//        // Preload user preferences and cached songs here
//        delay(2000) // Simulated loading time
//
//        // Fade out animation
//        isVisible = false
//        delay(500)
//        //onSplashComplete()
//
//
//        if (viewModel.isLoggedIn()) {
//            navController.navigate(SubGraph.HomeGraph) {
//                popUpTo(Dest.SplashScreen) {
//                    inclusive = true
//                }
//            }
//        } else {
//            navController.navigate(SubGraph.AuthGraph) {
//                popUpTo(Dest.SplashScreen) {
//                    inclusive = true
//                }
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.scale(scale.value)
//        ) {
//            // App Name
//            Text(
//                text = "TuneFlow", style = MaterialTheme.typography.headlineLarge.copy(
//                    fontWeight = FontWeight.Bold, fontSize = 48.sp
//                ), color = MaterialTheme.colorScheme.primary
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Animated sound wave
//            WaveAnimation(numWaves = 24)
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Tagline
//            Text(
//                text = "Feel the music, flow with the rhythm!",
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
//                fontStyle = FontStyle.Italic
//            )
//        }
//    }
//}