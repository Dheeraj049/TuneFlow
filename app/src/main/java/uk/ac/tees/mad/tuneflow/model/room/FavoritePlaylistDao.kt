package uk.ac.tees.mad.tuneflow.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.tees.mad.tuneflow.model.dataclass.Track

@Dao
interface FavoritePlaylistDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(track: Track)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<Track>

    @Query("DELETE FROM favorites WHERE id = :trackId")
    suspend fun deleteFavorite(trackId: Long)

    @Query("SELECT * FROM favorites WHERE id = :trackId")
    suspend fun getFavoriteById(trackId: Long): Track?

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE id = :trackId)")
    suspend fun isFavorite(trackId: Long): Boolean
}