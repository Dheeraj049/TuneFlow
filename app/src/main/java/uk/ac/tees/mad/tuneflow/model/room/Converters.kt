package uk.ac.tees.mad.tuneflow.model.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.ac.tees.mad.tuneflow.model.dataclass.AlbumT
import uk.ac.tees.mad.tuneflow.model.dataclass.ArtistT
import uk.ac.tees.mad.tuneflow.model.dataclass.Contributor

@ProvidedTypeConverter
class Converters {

    private val gson = Gson()

    // --- List<String> Converter ---
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        if (value == null) {
            return null
        }
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    // --- Contributor Converter ---
    @TypeConverter
    fun fromContributorList(value: List<Contributor>?): String? {
        if (value == null) {
            return null
        }
        return gson.toJson(value)
    }

    @TypeConverter
    fun toContributorList(value: String?): List<Contributor>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<Contributor>>() {}.type
        return gson.fromJson(value, listType)
    }

    // --- ArtistT Converter ---
    @TypeConverter
    fun fromArtistT(value: ArtistT?): String? {
        if (value == null) {
            return null
        }
        return gson.toJson(value)
    }

    @TypeConverter
    fun toArtistT(value: String?): ArtistT? {
        if (value == null) {
            return null
        }
        return gson.fromJson(value, ArtistT::class.java)
    }

    // --- AlbumT Converter ---
    @TypeConverter
    fun fromAlbumT(value: AlbumT?): String? {
        if (value == null) {
            return null
        }
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAlbumT(value: String?): AlbumT? {
        if (value == null) {
            return null
        }
        return gson.fromJson(value, AlbumT::class.java)
    }
}