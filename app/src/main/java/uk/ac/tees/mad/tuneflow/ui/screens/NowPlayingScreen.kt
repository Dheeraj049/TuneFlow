package uk.ac.tees.mad.tuneflow.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.R
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateNowPlaying
import uk.ac.tees.mad.tuneflow.view.utils.shimmerEffect
import uk.ac.tees.mad.tuneflow.viewmodel.NowPlayingScreenViewModel
import java.util.concurrent.TimeUnit

@Composable
fun NowPlayingScreen(
    navController: NavHostController,
    viewModel: NowPlayingScreenViewModel = koinViewModel()
) {
    val uiStateNowPlaying by viewModel.uiStateNowPlaying.collectAsStateWithLifecycle()
    when (uiStateNowPlaying) {
        is UiStateNowPlaying.Error -> {
            Text(text = "Error")
        }
        is UiStateNowPlaying.Loading -> {
            Text(text = "Loading", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        is UiStateNowPlaying.Success -> {
            val track by viewModel.track.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(Uri.parse(track?.preview)))
                    prepare()
                }
            }
            var isPlaying by remember { mutableStateOf(false) }
            var currentPosition by remember { mutableFloatStateOf(0f) }
            var totalDuration by remember { mutableFloatStateOf(0f) }

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            totalDuration = exoPlayer.duration.toFloat()
                        }
                    }
                }

                override fun onIsPlayingChanged(isPlayingValue: Boolean) {
                    isPlaying = isPlayingValue
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    currentPosition = exoPlayer.currentPosition.toFloat()
                }
            })

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    if (track != null) {
                        SubcomposeAsyncImage(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data("https://cdn-images.dzcdn.net/images/cover/be682506145061814eddee648edb7c59/500x500-000000-80-0-0.jpg")
                                .crossfade(true)
                                .build(),
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .size(300.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .shimmerEffect()
                                )
                            },
                            error = {
                                Image(
                                    painter = painterResource(id = R.drawable.placeholder),
                                    contentDescription = "Error loading Image",
                                    modifier = Modifier
                                        .size(300.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.FillBounds
                                )
                            },
                contentDescription = "Album Cover",
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
//                    Image(
//                        painter = painterResource(id = R.drawable.placeholder),
//                        contentDescription = "Error loading Image",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(RoundedCornerShape(8.dp)),
//                        contentScale = ContentScale.FillBounds
//                    )
                    Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "${track?.title}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        track?.artist?.let {
                            Text(
                                text = "${it.name}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDuration(currentPosition.toLong()),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp
                        )
                        Text(
                            text = formatDuration(totalDuration.toLong()),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp
                        )
                    }
                    Slider(
                        value = if (totalDuration > 0) currentPosition else 0f,
                        onValueChange = {
                            currentPosition = it
                            exoPlayer.seekTo(it.toLong())
                        },
                        valueRange = 0f..totalDuration,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (isPlaying) {
                                    exoPlayer.pause()
                                } else {
                                    exoPlayer.play()
                                }
                            },
                            modifier = Modifier.size(72.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(72.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d:%02d", minutes, seconds)
}