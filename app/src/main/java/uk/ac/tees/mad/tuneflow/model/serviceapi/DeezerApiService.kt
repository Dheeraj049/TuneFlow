package uk.ac.tees.mad.tuneflow.model.serviceapi

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import uk.ac.tees.mad.tuneflow.model.dataclass.ApiPlaylistResponse
import uk.ac.tees.mad.tuneflow.model.dataclass.ApiSearchResponse
import uk.ac.tees.mad.tuneflow.model.dataclass.Track

interface DeezerApiService {
    @Headers(
        "x-rapidapi-key: 4b34c8ea80msh39ddfee03472a65p1af7aejsn0da7ae025973",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )
    @GET("search")
    suspend fun search(@Query("q") query: String): ApiSearchResponse

    @Headers(
        "x-rapidapi-key: 4b34c8ea80msh39ddfee03472a65p1af7aejsn0da7ae025973",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )
    @GET("playlist/3155776842")
    suspend fun topWorldwide(): ApiPlaylistResponse

    @Headers(
        "x-rapidapi-key: 4b34c8ea80msh39ddfee03472a65p1af7aejsn0da7ae025973",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )
    @GET("playlist/10496059222")
    suspend fun topSongs(): ApiPlaylistResponse

    @Headers(
        "x-rapidapi-key: 4b34c8ea80msh39ddfee03472a65p1af7aejsn0da7ae025973",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )
    @GET("track/6461440")
    suspend fun getTrack(): Track
}