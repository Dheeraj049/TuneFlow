package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.tuneflow.ui.screens.PlaylistCard
import uk.ac.tees.mad.tuneflow.view.navigation.Dest
import kotlin.collections.plus

// Placeholder data class for a playlist
data class Playlist(val id: Int, val name: String, val songs: List<String>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(navController: NavHostController){
    // Sample playlist data (replace with Room DB data)
    var playlists by remember {
        mutableStateOf(
            listOf(
                Playlist(1, "My Favorites", listOf("Song 1", "Song 2")),
                Playlist(2, "Workout Mix", listOf("Song 3", "Song 4", "Song 5"))
            )
        )
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
            title = {
                Text("Playlist", maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        )}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Your Favorite Playlist",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            HorizontalDivider(
                modifier = Modifier.padding(8.dp),
                thickness = 2.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Display the list of playlists
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistCard(
                        playlist = playlist,
                        onDelete = {
                            // Handle playlist deletion (update the list)
                            playlists = playlists.filter { it.id != playlist.id }
                        }
                    )
                }
            }
        }

    }
}
@Composable
fun PlaylistCard(playlist: Playlist, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
