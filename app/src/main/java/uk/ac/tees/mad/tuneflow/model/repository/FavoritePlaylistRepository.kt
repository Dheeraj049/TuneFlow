package uk.ac.tees.mad.tuneflow.model.repository

import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.room.FavoritePlaylistDao

class FavoritePlaylistRepository (private val favoritePlaylistDao: FavoritePlaylistDao){

    suspend fun insertFavorite(track: Track) {
        favoritePlaylistDao.insertFavorite(track)
    }

    suspend fun getAllFavorites(): List<Track> {
        return favoritePlaylistDao.getAllFavorites()
    }

    suspend fun deleteFavorite(trackId: Long) {
        favoritePlaylistDao.deleteFavorite(trackId)
    }

    suspend fun getFavoriteById(trackId: Long): Track? {
        return favoritePlaylistDao.getFavoriteById(trackId)
    }

    suspend fun isFavorite(trackId: Long): Boolean {
        return favoritePlaylistDao.isFavorite(trackId)
    }
}