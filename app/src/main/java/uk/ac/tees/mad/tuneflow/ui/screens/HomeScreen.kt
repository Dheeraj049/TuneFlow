package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.room.util.TableInfo
import coil3.compose.SubcomposeAsyncImage
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.view.navigation.SubGraph
import uk.ac.tees.mad.tuneflow.viewmodel.HomeScreenViewModel
import uk.ac.tees.mad.tuneflow.R
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import uk.ac.tees.mad.tuneflow.model.dataclass.DaumP
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateTrendingAlbums
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateTrendingSongs
import uk.ac.tees.mad.tuneflow.view.navigation.Dest
import uk.ac.tees.mad.tuneflow.view.utils.shimmerEffect

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

    val uiStateTrendingAlbums by viewmodel.uiStateTrendingAlbums.collectAsStateWithLifecycle()
    val uiStateTrendingSongs by viewmodel.uiStateTrendingSongs.collectAsStateWithLifecycle()
    val uiStateSearch by viewmodel.uiStateSearch.collectAsStateWithLifecycle()
    val searchResult by viewmodel.searchResult.collectAsStateWithLifecycle()



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
                        AnimatedVisibility(!showSearchBar) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )}
                        AnimatedVisibility(showSearchBar) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
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
                                    viewmodel.updateSearchText(newQuery)
                                },
                                onSearch = { newQuery ->
                                    //expanded = false
//                                    query = newQuery
                                    viewmodel.search(newQuery)
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
                        if (query.isNotBlank() && searchResult!=null) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(searchResult!!.data){result->
                                    ListItem(headlineContent = { Text(result.title) },
                                        colors = ListItemDefaults.colors(
                                            MaterialTheme.colorScheme.surfaceContainerHigh
                                        ),
                                        modifier = Modifier.clickable {
                                            viewmodel.updateClickedTrackId(result.id.toString())
                                            navController.navigate(Dest.NowPlayingScreen(
                                                trackId = viewmodel.clickedTrackId.value
                                            ))
                                        },
                                        leadingContent = {
                                            Icon(
                                                Icons.Filled.Search, contentDescription = null
                                            )
                                        },
                                        supportingContent = {
                                            Text("${result.album.title} - ${result.artist.name}")
                                        })

                                }
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
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        onClick = {
                            navController.navigate(Dest.NowPlayingScreen(
                                trackId = viewmodel.clickedTrackId.value
                            ))
                        },
                        enabled = if(viewmodel.clickedTrackId.value.isNotBlank()) true else false
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column{
                        Text("Go to Now Playing Screen")
                            Text("Track ID: ${viewmodel.clickedTrackId.value}")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item{
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ){
                    if(uiStateTrendingAlbums is UiStateTrendingAlbums.Error || uiStateTrendingSongs is UiStateTrendingSongs.Error){
                        Text(text = "Error")
                    }
                    else if(uiStateTrendingAlbums is UiStateTrendingAlbums.Loading || uiStateTrendingSongs is UiStateTrendingSongs.Loading){
                        Text(text = "Loading", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                    if(uiStateTrendingAlbums is UiStateTrendingAlbums.Success && uiStateTrendingSongs is UiStateTrendingSongs.Success) {
                        Text(text = "Trending Albums",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold)
                        TrendingAlbums(viewmodel = viewmodel, navController = navController)
                        HorizontalDivider(
                            modifier = Modifier.padding(8.dp),
                            thickness = 2.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Trending Songs",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold)
                        TrendingSongs(viewmodel = viewmodel, navController = navController)
                        HorizontalDivider(
                            modifier = Modifier.padding(8.dp),
                            thickness = 2.dp
                        )
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

        }

    }
}

@Composable
fun TrendingAlbums(viewmodel: HomeScreenViewModel, navController: NavHostController){
    val uiStateTrendingAlbums by viewmodel.uiStateTrendingAlbums.collectAsStateWithLifecycle()
    val trendingAlbumListState =  rememberLazyGridState()
    when (uiStateTrendingAlbums) {
        is UiStateTrendingAlbums.Error -> {
            Text(text = "Error")
        }
        UiStateTrendingAlbums.Loading -> {
            Text(text = "Loading", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        is UiStateTrendingAlbums.Success -> {
            val trendingAlbums by viewmodel.trendingAlbums.collectAsStateWithLifecycle()
            val trendingAlbumsData = trendingAlbums?.tracks?.data!!
            LazyHorizontalGrid(
                rows = GridCells.Fixed(5),
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(532.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = trendingAlbumListState
            ) {
                items(trendingAlbumsData) { data ->
                    TrendingAlbumsItem(data, viewmodel, navController)
                }
            }

        }
    }
}

@Composable
fun TrendingAlbumsItem(albums: DaumP, viewmodel: HomeScreenViewModel, navController: NavHostController){
    ElevatedCard(modifier = Modifier.width(300.dp),
        colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.surfaceContainerHighest),
        onClick = {
            //TODO: Navigate to detail page
            viewmodel.updateClickedTrackId(albums.id.toString())
            navController.navigate(Dest.NowPlayingScreen(
                trackId = viewmodel.clickedTrackId.value
            ))
        }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(albums.album.cover).crossfade(true).size(100)
                    .build(),
                contentDescription = "Album Cover",
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
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = albums.album.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp),
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
            Text(
                text = albums.artist.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp),
                softWrap = true,
                overflow = TextOverflow.Ellipsis
            )
            }
            IconButton(
                onClick = {
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    }
}

@Composable
fun TrendingSongs(viewmodel: HomeScreenViewModel, navController: NavHostController){
    val uiStateTrendingSongs by viewmodel.uiStateTrendingSongs.collectAsStateWithLifecycle()
    val trendingSongsListState =  rememberLazyGridState()
    when (uiStateTrendingSongs) {
        is UiStateTrendingSongs.Error -> {
            Text(text = "Error")
        }
        UiStateTrendingSongs.Loading -> {
            Text(text = "Loading", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        is UiStateTrendingSongs.Success ->{
            val trendingSongs by viewmodel.trendingSongs.collectAsStateWithLifecycle()
            val trendingSongsData = trendingSongs?.tracks?.data!!
            LazyHorizontalGrid(
                rows = GridCells.Fixed(5),
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(532.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = trendingSongsListState
            ) {
                items(trendingSongsData) { data ->
                    TrendingSongsItem(data, viewmodel, navController)
                }
            }


        }
    }
}

@Composable
fun TrendingSongsItem(songs: DaumP, viewmodel: HomeScreenViewModel, navController: NavHostController){
    ElevatedCard(modifier = Modifier.width(300.dp),
        colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.surfaceContainerHighest),
        onClick = {
            //TODO: Navigate to detail page
            viewmodel.updateClickedTrackId(songs.id.toString())
            navController.navigate(Dest.NowPlayingScreen(
                trackId = viewmodel.clickedTrackId.value
            ))
        }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(songs.album.cover).crossfade(true).size(100)
                    .build(),
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
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = songs.titleShort,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp),
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = songs.artist.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(4.dp),
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    }
}