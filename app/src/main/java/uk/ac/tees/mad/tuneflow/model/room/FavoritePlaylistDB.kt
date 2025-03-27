package uk.ac.tees.mad.tuneflow.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.tuneflow.model.dataclass.Track

@Database(entities = [Track::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavoritePlaylistDB : RoomDatabase() {
    abstract fun favoritePlaylistDao(): FavoritePlaylistDao
}