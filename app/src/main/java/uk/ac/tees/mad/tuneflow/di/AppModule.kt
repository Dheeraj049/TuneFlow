package uk.ac.tees.mad.tuneflow.di

import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import uk.ac.tees.mad.tuneflow.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.viewmodel.SignInScreenViewModel
import uk.ac.tees.mad.tuneflow.viewmodel.SignUpScreenViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { AuthRepository(get()) }

    viewModelOf(::SignInScreenViewModel)
    viewModelOf(::SignUpScreenViewModel)
}