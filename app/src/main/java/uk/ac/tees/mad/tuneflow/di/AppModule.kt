package uk.ac.tees.mad.tuneflow.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.model.repository.DeezerRepository
import uk.ac.tees.mad.tuneflow.model.repository.FavoritePlaylistRepository
import uk.ac.tees.mad.tuneflow.model.repository.NetworkRepository
import uk.ac.tees.mad.tuneflow.model.retrofit.DeezerRetrofitInstance
import uk.ac.tees.mad.tuneflow.model.room.Converters
import uk.ac.tees.mad.tuneflow.model.room.FavoritePlaylistDB
import uk.ac.tees.mad.tuneflow.model.serviceapi.DeezerApiService
import uk.ac.tees.mad.tuneflow.model.utils.NetworkConnectivityManager
import uk.ac.tees.mad.tuneflow.viewmodel.HomeScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.NowPlayingScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.PlaylistScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.ProfileScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.SignInScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.SignUpScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.SplashScreenViewModel

val appModule = module {
    single { NetworkConnectivityManager(androidContext()) }
    single { NetworkRepository(get()) }

    single<DeezerApiService> { DeezerRetrofitInstance.create() }

    // FavoritePlaylist database
    singleOf(::Converters)
    single {
        Room.databaseBuilder(
            androidApplication(), FavoritePlaylistDB::class.java, "favorite_playlist"
        ).addTypeConverter(Converters()).build()
    }
    single {
        val database = get<FavoritePlaylistDB>()
        database.favoritePlaylistDao()
    }

    single { FirebaseAuth.getInstance() }
    single { AuthRepository(get()) }

    // Repository
    single { DeezerRepository(get()) }
    single { FavoritePlaylistRepository(get()) }

    // ViewModels
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::SignInScreenViewModel)
    viewModelOf(::SignUpScreenViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::NowPlayingScreenViewModel)
    viewModelOf(::PlaylistScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
}