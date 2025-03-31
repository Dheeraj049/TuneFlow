package uk.ac.tees.mad.tuneflow.ui.screens

import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.R
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateNowPlaying
import uk.ac.tees.mad.tuneflow.view.navigation.Dest
import uk.ac.tees.mad.tuneflow.view.utils.shimmerEffect
import uk.ac.tees.mad.tuneflow.viewmodel.NowPlayingScreenViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    navController: NavHostController,
    trackId: String,
    flag: Boolean,
    viewmodel: NowPlayingScreenViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewmodel.getTrack(id = trackId)
        viewmodel.fetchDataFromDB()
        if (flag) {
            viewmodel.updateTrackIndex(-1)
        } else {
            viewmodel.updateTrackIndex(0)
        }
    }

    val uiStateNowPlaying by viewmodel.uiStateNowPlaying.collectAsStateWithLifecycle()
    when (uiStateNowPlaying) {
        is UiStateNowPlaying.Error -> {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar(title = {
                    Text("Now Playing", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }, navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                })
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error")
                }
            }
        }

        is UiStateNowPlaying.Loading -> {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar(title = {
                    Text("Now Playing", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }, navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                })
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is UiStateNowPlaying.Success -> {
            val track by viewmodel.track.collectAsStateWithLifecycle()
            val trackIndex by viewmodel.trackIndex.collectAsStateWithLifecycle()
            val favoriteTracks by viewmodel.favoriteTracks.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(Uri.parse(track?.preview)))
                    prepare()
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }

            LaunchedEffect(track?.preview) {
                track?.preview?.let { previewUrl ->
                    val mediaItem = MediaItem.fromUri(previewUrl)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
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
                    oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int
                ) {
                    currentPosition = exoPlayer.currentPosition.toFloat()
                }
            })

            LaunchedEffect(isPlaying) {
                while (isPlaying) {
                    currentPosition = exoPlayer.currentPosition.toFloat()
                    delay(100) // Update every 100ms, adjust as needed
                }
            }

            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar(title = {
                    Text("Now Playing", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }, navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Dest.HomeScreen) {
                            popUpTo(Dest.HomeScreen) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }, actions = {
                    IconButton(onClick = {
                        navController.navigate(Dest.PlaylistScreen)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                            contentDescription = "Localized description"
                        )
                    }
                })
            }) { innerPadding ->
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
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(track!!.album.coverBig.toString()).crossfade(true).build(),
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewmodel.onPrev()
                            }, modifier = Modifier.size(72.dp), enabled = trackIndex >= 1
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SkipPrevious,
                                contentDescription = "Previous",
                                modifier = Modifier.size(72.dp),
                                //tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(
                            onClick = {
                                if (isPlaying) {
                                    exoPlayer.pause()
                                } else {
                                    exoPlayer.play()
                                }
                            }, modifier = Modifier.size(72.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(72.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(
                            onClick = {
                                viewmodel.onNext()
                            },
                            modifier = Modifier.size(72.dp),
                            enabled = trackIndex <= favoriteTracks.size - 2
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SkipNext,
                                contentDescription = "Next",
                                modifier = Modifier.size(72.dp),
                                //tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var check by remember { mutableStateOf(viewmodel.checkIsFavorite(track?.id.toString())) }
                        if (!check) {
                            IconButton(
                                onClick = {

                                    check = true
                                    viewmodel.addFavoriteTrack(track?.id.toString())


                                }, modifier = Modifier.size(72.dp)
                            ) {
                                if (!check) {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Add to favorite",
                                        modifier = Modifier.size(72.dp)
                                    )
                                }
                            }
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