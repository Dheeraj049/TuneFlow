package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.view.navigation.SubGraph
import uk.ac.tees.mad.tuneflow.viewmodel.HomeScreenViewModel
import uk.ac.tees.mad.tuneflow.R
import coil3.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.tuneflow.model.dataclass.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController, viewmodel: HomeScreenViewModel = koinViewModel()
) {
    val userData by viewmodel.userData.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showSearchBar by remember { mutableStateOf(false) }

    var expanded by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    val trendingSongsAndAlbumListState1 = rememberLazyListState()
    val trendingSongsAndAlbumListState2 = rememberLazyListState()
    Scaffold(modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Image(
                            painter = painterResource(id= R.drawable.tuneflow_logo),
                            contentDescription = "TuneFlow Logo",
                            colorFilter = if(isSystemInDarkTheme()) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black)
                        )
                    Text(
                        text = "TuneFlow",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    }
                        },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSearchBar = !showSearchBar
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
                ChipGroupSingleLine()
                AnimatedVisibility(showSearchBar) {
                    DockedSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        inputField = {
                            SearchBarDefaults.InputField(modifier = Modifier.fillMaxWidth(),
                                query = query,
                                onQueryChange = { newQuery ->
                                    query = newQuery
                                    //viewModel.searchProducts(newQuery)
                                },
                                onSearch = { newQuery ->
                                    expanded = false
//                                    query = newQuery
//                                    viewModel.searchProducts(newQuery)
                                },
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                placeholder = { Text("Search songs, artists, or albums...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search, contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if(expanded){
                                        IconButton(onClick = {
                                            if (query.isNotBlank()) {
                                                query = ""
                                                //viewModel.searchProducts(query)
                                            }
                                            expanded = false


                                        }) {
                                            Icon(Icons.Default.Close, contentDescription = null)
                                        }}

                                })
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        if (query.isNotBlank()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.4f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Start Typing to Search")
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item{
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
            Text(text = "Home Screen")
            Text("Welcome")
            Text(userData.userDetails?.email.toString())
            Button(onClick = {
                viewmodel.signOut()
                navController.navigate(SubGraph.AuthGraph) {
                    popUpTo(SubGraph.HomeGraph) {
                        inclusive = true
                    }
                }
            }) {
                Text("Sign Out")
            }}
            }
            item{
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ){
                    when(uiState){
                        is UiState.Error -> Text(text = "Error")
                        is UiState.Loading -> Text(text = "Loading")
                        is UiState.Success -> {
                            val trendingAlbums by viewmodel.trendingAlbums.collectAsStateWithLifecycle()
                            val dataA = trendingAlbums?.tracks?.data!!
                            Text(text = "Trending Albums")
                            LazyRow(
                                state = trendingSongsAndAlbumListState1,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                items(dataA) { data ->
                                    Column {
                                        ElevatedCard(modifier = Modifier,
                                            onClick = {
                                                //TODO: Navigate to detail page
                                            }) {
                                            Column(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(
                                                        model = data.album.cover,
                                                        placeholder = painterResource(id = R.drawable.placeholder)
                                                    ),
                                                    contentDescription = "",
                                                    contentScale = ContentScale.FillWidth,
                                                    modifier = Modifier
                                                        .height(128.dp)
                                                        .fillMaxWidth()
                                                        .aspectRatio(1f)
                                                        .padding(4.dp)
                                                        .clip(MaterialTheme.shapes.medium)
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))

                                                Text(
                                                    text = data.album.title,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(4.dp),
                                                    softWrap = true,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = data.artist.name,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    modifier = Modifier.padding(4.dp),
                                                    softWrap = true,
                                                    overflow = TextOverflow.Ellipsis
                                                )

                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            val trendingSongs by viewmodel.trendingSongs.collectAsStateWithLifecycle()
                            val dataB = trendingSongs?.tracks?.data!!
                            Text(text = "Trending Songs")
                            LazyRow(
                                state = trendingSongsAndAlbumListState2,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                items(dataB) { data ->
                                    Column {
                                        ElevatedCard(modifier = Modifier,
                                            onClick = {
                                                //TODO: Navigate to detail page
                                            }) {
                                            Column(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(
                                                        model = data.album.cover,
                                                        placeholder = painterResource(id = R.drawable.placeholder)
                                                    ),
                                                    contentDescription = "",
                                                    contentScale = ContentScale.FillWidth,
                                                    modifier = Modifier
                                                        .height(128.dp)
                                                        .fillMaxWidth()
                                                        .aspectRatio(1f)
                                                        .padding(4.dp)
                                                        .clip(MaterialTheme.shapes.medium)
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))

                                                Text(
                                                    text = data.title,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(4.dp),
                                                    softWrap = true,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = data.artist.name,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    modifier = Modifier.padding(4.dp),
                                                    softWrap = true,
                                                    overflow = TextOverflow.Ellipsis
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

        }

    }
}

@Composable
fun ChipGroupSingleLine() {
    val chipTexts = listOf(
        "Metal", "R&B", "Rap", "Folk", "Reaggaeton", "Pop",
        "Latin music", "Jazz", "Dance & EDM", "Afro", "Rock", "Classical", "Blues"
    )

    // Use a map to store the selected state for each chip
    var selectedChip by remember { mutableStateOf<String?>(null) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            chipTexts.forEach { chipText ->
                ElevatedFilterChip(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    selected = (selectedChip == chipText), // Check if this chip is selected
                    onClick = {
                        selectedChip = if (selectedChip == chipText) {
                            null // Deselect if already selected
                        } else {
                            chipText // Select this chip
                        }
                    },
                    label = { Text(chipText) }
                )
            }
        }
    }
}