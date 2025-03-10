package uk.ac.tees.mad.tuneflow.model.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.tuneflow.model.utils.NetworkConnectivityManager

class NetworkRepository(private val networkConnectivityManager: NetworkConnectivityManager) {
    val isNetworkAvailable: Flow<Boolean> = networkConnectivityManager.observeConnectivity()
}