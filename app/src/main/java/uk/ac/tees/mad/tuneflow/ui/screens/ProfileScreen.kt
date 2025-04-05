package uk.ac.tees.mad.tuneflow.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.model.dataclass.UserData
import uk.ac.tees.mad.tuneflow.model.dataclass.UserDetails
import uk.ac.tees.mad.tuneflow.view.navigation.SubGraph
import uk.ac.tees.mad.tuneflow.view.utils.LoadingScreen
import uk.ac.tees.mad.tuneflow.viewmodel.ProfileScreenViewModel

val LocalIsDarkMode = staticCompositionLocalOf { mutableStateOf(true) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController, viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    val isDarkMode = LocalIsDarkMode.current
    val userData by viewModel.userData.collectAsStateWithLifecycle()
    val userDetailsResult by viewModel.userDetails.collectAsStateWithLifecycle()
    val updateNameResult by viewModel.updateNameResult.collectAsStateWithLifecycle()
    val updatePhotoResult by viewModel.updatePhotoResult.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Profile") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                AppSettingsSection(isDarkMode, sharedPreferences)
            }
            item {
                PersonalDetailsSection(userDetailsResult, userData, viewModel)
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(8.dp), thickness = 2.dp)
                LogoutSection(navController, viewModel)
            }
        }
    }
}

@Composable
fun AppSettingsSection(isDarkMode: MutableState<Boolean>, sharedPreferences: SharedPreferences) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        SectionHeader(icon = Icons.Default.Settings, title = "App Settings")
        Card(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DarkModeToggle(isDarkMode, sharedPreferences)
            }
        }
    }
}

@Composable
fun DarkModeToggle(isDarkMode: MutableState<Boolean>, sharedPreferences: SharedPreferences) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.DarkMode, contentDescription = "Dark Mode")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")
            Switch(checked = isDarkMode.value, onCheckedChange = {
                isDarkMode.value = it
                sharedPreferences.edit().putBoolean("dark_mode", it).apply()
            })
        }
    }
}

@Composable
fun PersonalDetailsSection(
    userDetailsResult: AuthResult<UserDetails>,
    userData: UserData,
    viewModel: ProfileScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        SectionHeader(icon = Icons.Default.Person, title = "Profile Details")
        Card(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (userDetailsResult) {
                    is AuthResult.Loading -> LoadingScreen()
                    is AuthResult.Success -> {
                        userData.userDetails?.let {
                            ProfileContent(it, viewModel)
                        }
                    }

                    is AuthResult.Error -> {
                        val error = (userDetailsResult as AuthResult.Error).exception
                        Text(
                            text = "Error: ${error.message}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileContent(userDetails: UserDetails, viewModel: ProfileScreenViewModel) {
    var showNameDialog by remember { mutableStateOf(false) }
    var showPhotoDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updatePhoto(it)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Photo
        if (userDetails.photoUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = userDetails.photoUrl),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape).padding(8.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Default Profile Photo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        DetailRow(
            icon = Icons.Default.AccountCircle,
            iconDesc = "User ID",
            label = "User ID",
            value = userDetails.userId
        )
        HorizontalDivider(modifier = Modifier.padding(4.dp), thickness = 2.dp)
        DetailRow(
            icon = Icons.Filled.Email,
            iconDesc = "Email",
            label = "Email",
            value = userDetails.email ?: "Not available"
        )
        HorizontalDivider(modifier = Modifier.padding(4.dp), thickness = 2.dp)
        DetailRow(
            icon = Icons.Filled.Person,
            iconDesc = "Name",
            label = "Name",
            value = userDetails.displayName ?: "Not available",
            endContent = {
                IconButton(onClick = {
                    showNameDialog = true
                    newName = userDetails.displayName ?: ""
                }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Name")
                }
            }
        )
        HorizontalDivider(modifier = Modifier.padding(4.dp), thickness = 2.dp)
        DetailRow(
            icon = Icons.Filled.AccountCircle,
            iconDesc = "Profile Photo",
            label = "Profile Photo",
            value = if (userDetails.photoUrl != null) "Available" else "Not available",
            endContent = {
                IconButton(onClick = { showPhotoDialog = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Photo")
                }
            }
        )

        // Name Update Dialog
        if (showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text("Update Name") },
                text = {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("New Name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.updateDisplayName(newName)
                        showNameDialog = false
                    }) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    Button(onClick = { showNameDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Photo Update Dialog
        if (showPhotoDialog) {
            AlertDialog(
                onDismissRequest = { showPhotoDialog = false },
                title = { Text("Update Photo") },
                text = { Text("Select a new photo from your device.") },
                confirmButton = {
                    Button(onClick = {
                        launcher.launch("image/*")
                        showPhotoDialog = false
                    }) {
                        Text("Select Photo")
                    }
                },
                dismissButton = {
                    Button(onClick = { showPhotoDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun LogoutSection(navController: NavHostController, viewModel: ProfileScreenViewModel) {
    Button(
        onClick = {
            viewModel.signOut()
            navController.navigate(SubGraph.AuthGraph) {
                popUpTo(SubGraph.HomeGraph) {
                    inclusive = true
                }
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Logout")
    }
}

@Composable
fun DetailRow(
    icon: ImageVector,
    iconDesc: String,
    label: String,
    value: String,
    endContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(imageVector = icon, contentDescription = iconDesc)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "$label:", fontWeight = FontWeight.Bold)
            Text(text = value)
        }
        if (endContent != null) {
            endContent()
        }
    }
}

@Composable
fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleSmall
        )
    }
}