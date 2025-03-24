package uk.ac.tees.mad.tuneflow.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import uk.ac.tees.mad.tuneflow.model.dataclass.ApiPlaylistResponse
import uk.ac.tees.mad.tuneflow.model.dataclass.ApiSearchResponse
import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.serviceapi.DeezerApiService
import java.io.IOException

class DeezerRepository(private val apiService:DeezerApiService ) {
    suspend fun search(query: String): Result<ApiSearchResponse>{
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.search(query)
                // Assign a unique ID to each item based on its index in the list.
                Result.success(response)
            } catch (e: IOException) {
                // Handle network errors
                e.printStackTrace()
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                // Handle HTTP errors (e.g., 404 Not Found, 500 Internal Server Error)
                e.printStackTrace()
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message}"))
            } catch (e: Exception) {
                // Handle other errors
                e.printStackTrace()
                Result.failure(Exception("An unexpected error occurred: ${e.message}"))
            }
        }
    }

    suspend fun topWorldwide(): Result<ApiPlaylistResponse>{
            return withContext(Dispatchers.IO) {
                try {
                    val response = apiService.topWorldwide()
                    // Assign a unique ID to each item based on its index in the list.
                    Result.success(response)
                } catch (e: IOException) {
                    // Handle network errors
                    e.printStackTrace()
                    Result.failure(Exception("Network error: ${e.message}"))
                } catch (e: HttpException) {
                    // Handle HTTP errors (e.g., 404 Not Found, 500 Internal Server Error)
                    e.printStackTrace()
                    Result.failure(Exception("HTTP error: ${e.code()} - ${e.message}"))
                } catch (e: Exception) {
                    // Handle other errors
                    e.printStackTrace()
                    Result.failure(Exception("An unexpected error occurred: ${e.message}"))
                }
            }
    }

    suspend fun topSongs(): Result<ApiPlaylistResponse>{
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.topSongs()
                // Assign a unique ID to each item based on its index in the list.
                Result.success(response)
            } catch (e: IOException) {
                // Handle network errors
                e.printStackTrace()
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                // Handle HTTP errors (e.g., 404 Not Found, 500 Internal Server Error)
                e.printStackTrace()
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message}"))
            } catch (e: Exception) {
                // Handle other errors
                e.printStackTrace()
                Result.failure(Exception("An unexpected error occurred: ${e.message}"))
            }
        }
    }

    suspend fun getTrack(id: String): Result<Track> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTrack(id)
                // Assign a unique ID to each item based on its index in the list.
                Result.success(response)
            } catch (e: IOException) {
                // Handle network errors
                e.printStackTrace()
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: HttpException) {
                // Handle HTTP errors (e.g., 404 Not Found, 500 Internal Server Error)
                e.printStackTrace()
                Result.failure(Exception("HTTP error: ${e.code()} - ${e.message}"))
            } catch (e: Exception) {
                // Handle other errors
                e.printStackTrace()
                Result.failure(Exception("An unexpected error occurred: ${e.message}"))
            }
        }
    }

}