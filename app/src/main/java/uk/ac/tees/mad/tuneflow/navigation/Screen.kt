package uk.ac.tees.mad.tuneflow.navigation

import kotlinx.serialization.Serializable

sealed class SubGraph {
    @Serializable
    data object AuthGraph : SubGraph()

    @Serializable
    data object HomeGraph : SubGraph()
}

sealed class Dest {
    @Serializable
    data object SplashScreen : Dest()

    @Serializable
    data object SignInScreen : Dest()

    @Serializable
    data object SignUpScreen : Dest()

    @Serializable
    data object HomeScreen : Dest()

    @Serializable
    data object NowPlayingScreen : Dest()

    @Serializable
    data object PlaylistScreen : Dest()

    @Serializable
    data object ProfileScreen : Dest()

}