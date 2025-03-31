package uk.ac.tees.mad.tuneflow.model.repository

import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.room.FavoritePlaylistDao

class FavoritePlaylistRepository(private val favoritePlaylistDao: FavoritePlaylistDao) {

    suspend fun insertFavorite(track: Track, user: String) {
        favoritePlaylistDao.insertFavorite(track)
        favoritePlaylistDao.updateUsers(user)
    }

    suspend fun getAllFavorites(user: String): List<Track> {
        return favoritePlaylistDao.getAllFavorites(user)
    }

    suspend fun deleteFavorite(trackId: Long, user: String) {
        favoritePlaylistDao.deleteFavorite(trackId, user)
    }

    suspend fun getFavoriteById(trackId: Long, user: String): Track? {
        return favoritePlaylistDao.getFavoriteById(trackId, user)
    }

    suspend fun isFavorite(trackId: Long, user: String): Boolean {
        return favoritePlaylistDao.isFavorite(trackId, user)
    }
}