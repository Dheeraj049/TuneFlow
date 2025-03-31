package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.R
import uk.ac.tees.mad.tuneflow.view.utils.shimmerEffect
import uk.ac.tees.mad.tuneflow.viewmodel.PlaylistScreenViewModel

// Placeholder data class for a playlist
data class Playlist(val id: Int, val name: String, val songs: List<String>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    navController: NavHostController,
    viewmodel: PlaylistScreenViewModel = koinViewModel()
) {
    val favoriteTracks by viewmodel.favoriteTracks.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text("Playlist", maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Your Favorite Playlist",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(
                modifier = Modifier.padding(8.dp), thickness = 2.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (favoriteTracks.isEmpty()) {
                Text(
                    text = "No favorite tracks found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else {

                // Display the list of playlists
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(favoriteTracks) { track ->
                        ListItem(headlineContent = { Text(track.album.title) },
                            colors = ListItemDefaults.colors(
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                            leadingContent = {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(track.album.cover).crossfade(true).size(100).build(),
                                    contentDescription = "Song Cover",
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .shimmerEffect()
                                        )
                                    },
                                    error = {
                                        Image(
                                            painter = painterResource(id = R.drawable.placeholder),
                                            contentDescription = "Error loading Image",
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.FillBounds
                                        )
                                    },
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.FillBounds
                                )
                            },
                            supportingContent = {
                                Text("${track.artist.name}")
                            },
                            trailingContent = {
                                IconButton(onClick = {
                                    viewmodel.removeFavoriteTrack(track.id.toString())
                                }) {
                                    Icon(
                                        Icons.Filled.Delete, contentDescription = null
                                    )
                                }
                            })

                    }
                }
            }
        }

    }
}

@Composable
fun PlaylistCard(playlist: Playlist, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Song 1", fontWeight = FontWeight.Bold)
                Text(text = "Artist1", color = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, "Delete Playlist", modifier = Modifier.size(24.dp))
            }
        }
    }
}
