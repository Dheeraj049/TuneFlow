package uk.ac.tees.mad.tuneflow.model.dataclass

import com.google.gson.annotations.SerializedName

data class ApiPlaylistResponse(
    val id: Long,
    val title: String,
    val description: String,
    val duration: Long,
    val public: Boolean,
    @SerializedName("is_loved_track")
    val isLovedTrack: Boolean,
    val collaborative: Boolean,
    @SerializedName("nb_tracks")
    val nbTracks: Long,
    val fans: Long,
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
    val checksum: String,
    val tracklist: String,
    @SerializedName("creation_date")
    val creationDate: String,
    @SerializedName("add_date")
    val addDate: String,
    @SerializedName("mod_date")
    val modDate: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("picture_type")
    val pictureType: String,
    val creator: Creator,
    val type: String,
    val tracks: Tracks,
)

data class Creator(
    val id: Long,
    val name: String,
    val tracklist: String,
    val type: String,
)

data class Tracks(
    val data: List<DaumP>,
    val checksum: String,
)

data class DaumP(
    val id: Long,
    val readable: Boolean,
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("title_version")
    val titleVersion: String,
    val isrc: String,
    val link: String,
    val duration: Long,
    val rank: Long,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Long,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Long,
    val preview: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("time_add")
    val timeAdd: Long,
    val artist: ArtistP,
    val album: AlbumP,
    val type: String,
)

data class ArtistP(
    val id: Long,
    val name: String,
    val link: String,
    val tracklist: String,
    val type: String,
)

data class AlbumP(
    val id: Long,
    val title: String,
    val upc: String,
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
    val tracklist: String,
    val type: String,
)
