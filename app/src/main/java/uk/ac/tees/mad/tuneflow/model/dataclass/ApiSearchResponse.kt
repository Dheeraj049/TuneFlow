package uk.ac.tees.mad.tuneflow.model.dataclass

import com.google.gson.annotations.SerializedName

data class ApiSearchResponse(
    val data: List<Daum>,
    val total: Long,
    val next: String,
)

data class Daum(
    val id: Long,
    val readable: Boolean,
    val title: String,
    @SerializedName("title_short") val titleShort: String,
    @SerializedName("title_version") val titleVersion: String,
    val link: String,
    val duration: Long,
    val rank: Long,
    @SerializedName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics") val explicitContentLyrics: Long,
    @SerializedName("explicit_content_cover") val explicitContentCover: Long,
    val preview: String,
    @SerializedName("md5_image") val md5Image: String,
    val artist: Artist,
    val album: Album,
    val type: String,
)

data class Artist(
    val id: Long,
    val name: String,
    val link: String,
    val picture: String,
    @SerializedName("picture_small") val pictureSmall: String,
    @SerializedName("picture_medium") val pictureMedium: String,
    @SerializedName("picture_big") val pictureBig: String,
    @SerializedName("picture_xl") val pictureXl: String,
    val tracklist: String,
    val type: String,
)

data class Album(
    val id: Long,
    val title: String,
    val cover: String,
    @SerializedName("cover_small") val coverSmall: String,
    @SerializedName("cover_medium") val coverMedium: String,
    @SerializedName("cover_big") val coverBig: String,
    @SerializedName("cover_xl") val coverXl: String,
    @SerializedName("md5_image") val md5Image: String,
    val tracklist: String,
    val type: String,
)
