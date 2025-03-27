package uk.ac.tees.mad.tuneflow.model.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorites")
data class Track(
    @PrimaryKey
    val id: Long,
    val readable: Boolean,
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("title_version")
    val titleVersion: String,
    val isrc: String,
    val link: String,
    val share: String,
    val duration: Long,
    @SerializedName("track_position")
    val trackPosition: Long,
    @SerializedName("disk_number")
    val diskNumber: Long,
    val rank: Long,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Long,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Long,
    val preview: String,
    val bpm: Double,
    val gain: Double,
    @SerializedName("available_countries")
    val availableCountries: List<String>,
    val contributors: List<Contributor>,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("track_token")
    val trackToken: String,
    val artist: ArtistT,
    val album: AlbumT,
    val type: String,
)

data class Contributor(
    val id: Long,
    val name: String,
    val link: String,
    val share: String,
    val picture: String,
    @SerializedName("picture_small")
    val pictureSmall: String,
    @SerializedName("picture_medium")
    val pictureMedium: String,
    @SerializedName("picture_big")
    val pictureBig: String,
    @SerializedName("picture_xl")
    val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val type: String,
    val role: String,
)

data class ArtistT(
    val id: Long,
    val name: String,
    val link: String,
    val share: String,
    val picture: String,
    @SerializedName("picture_small")
    val pictureSmall: String,
    @SerializedName("picture_medium")
    val pictureMedium: String,
    @SerializedName("picture_big")
    val pictureBig: String,
    @SerializedName("picture_xl")
    val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val type: String,
)

data class AlbumT(
    val id: Long,
    val title: String,
    val link: String,
    val cover: String,
    @SerializedName("cover_small")
    val coverSmall: String,
    @SerializedName("cover_medium")
    val coverMedium: String,
    @SerializedName("cover_big")
    val coverBig: String,
    @SerializedName("cover_xl")
    val coverXl: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val tracklist: String,
    val type: String,
)