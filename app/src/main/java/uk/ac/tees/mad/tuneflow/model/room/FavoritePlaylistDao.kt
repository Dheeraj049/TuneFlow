package uk.ac.tees.mad.tuneflow.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.tees.mad.tuneflow.model.dataclass.Track

@Dao
interface FavoritePlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(track: Track)

    @Query("UPDATE favorites SET user = :user WHERE user IS NULL")
    suspend fun updateUsers(user: String)

    @Query("SELECT * FROM favorites WHERE user = :user")
    suspend fun getAllFavorites(user: String): List<Track>

    @Query("DELETE FROM favorites WHERE id = :trackId AND user = :user")
    suspend fun deleteFavorite(trackId: Long, user: String)

    @Query("SELECT * FROM favorites WHERE id = :trackId AND user = :user")
    suspend fun getFavoriteById(trackId: Long, user: String): Track?

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE id = :trackId AND user = :user)")
    suspend fun isFavorite(trackId: Long, user: String): Boolean
}