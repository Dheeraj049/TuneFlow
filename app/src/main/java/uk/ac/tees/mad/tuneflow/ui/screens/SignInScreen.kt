package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.view.navigation.CARD_TRANSITION_KEY
import uk.ac.tees.mad.tuneflow.view.navigation.Dest
import uk.ac.tees.mad.tuneflow.view.navigation.SubGraph
import uk.ac.tees.mad.tuneflow.view.utils.WaveAnimation
import uk.ac.tees.mad.tuneflow.view.utils.WaveLine
import uk.ac.tees.mad.tuneflow.view.utils.pulsateEffect
import uk.ac.tees.mad.tuneflow.viewmodel.SignInScreenViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SignInScreen(
    navController: NavHostController,
    viewModel: SignInScreenViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsStateWithLifecycle()
    val isSignInMode by viewModel.isSignInMode.collectAsStateWithLifecycle()
    val signInResult by viewModel.signInResult.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        // Decorative background music waves
        repeat(3) { index ->
            WaveLine(
                index = index, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent
        ) { innerPadding ->
            when (isSignInMode) {
                true -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(16.dp)
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = CARD_TRANSITION_KEY),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .clip(RoundedCornerShape(24.dp)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .pulsateEffect()
                                        .border(
                                            width = 4.dp,
                                            color = Color.Black,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .background(
                                            color = Color.White, shape = MaterialTheme.shapes.medium
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // Animated sound wave
                                    WaveAnimation(numWaves = 6, color = Color.Black)
                                }


                                Text(
                                    text = "Welcome Back",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                OutlinedTextField(
                                    value = email,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequesterEmail),
                                    onValueChange = {
                                        viewModel.updateEmail(it)
                                    },
                                    label = { Text("Email") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Email,
                                            contentDescription = "Email",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusRequesterPassword.requestFocus()
                                    }),
                                    shape = RoundedCornerShape(16.dp),
                                    singleLine = true
                                )

                                OutlinedTextField(value = password,
                                    onValueChange = {
                                        viewModel.updatePassword(it)
                                    },
                                    label = { Text("Password") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Lock,
                                            contentDescription = "Password",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequesterPassword),
                                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        focusManager.clearFocus()
                                    }),
                                    shape = RoundedCornerShape(16.dp),
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            viewModel.togglePasswordVisibility()
                                        }) {
                                            Icon(
                                                imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                                contentDescription = "Toggle Password Visibility"
                                            )
                                        }
                                    })

                                Button(
                                    enabled = email.isNotBlank() && password.isNotBlank(),
                                    onClick = {
                                        // Placeholder for sign in logic
                                        println("Sign in button clicked with email: $email, password: $password")

                                        viewModel.signIn(email, password)
                                        viewModel.switchSignInMode()

                                        //onSignInSuccess()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        "Sign In", style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "First time dropping by?",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    TextButton(onClick = {
                                        viewModel.updateEmail("")
                                        viewModel.updatePassword("")
                                        navController.navigate(Dest.SignUpScreen)
                                    }) {
                                        Text("Sign Up")
                                    }
                                }
                            }
                        }
                    }
                }

                false -> {
                    when (val result = signInResult) {
                        is AuthResult.Loading -> {
                            AlertDialog(onDismissRequest = {
                                viewModel.switchSignInMode()
                            }, icon = {
                                Icon(
                                    Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                            }, title = { Text("Signing In") }, text = {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }, confirmButton = { })
                        }

                        is AuthResult.Success -> {
                            // Handle successful sign-up
                            navController.navigate(SubGraph.HomeGraph) {
                                popUpTo(SubGraph.AuthGraph) {
                                    inclusive = true
                                }
                            }

                        }

                        is AuthResult.Error -> {
                            // Handle sign-up error
                            AlertDialog(icon = {
                                Icon(
                                    Icons.Default.Error,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                            }, title = { Text("Error") }, text = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(result.exception.message.toString())
                                }
                            }, confirmButton = {
                                TextButton(onClick = {
                                    viewModel.switchSignInMode()
                                }) {
                                    Text("Retry?")
                                }
                            }, onDismissRequest = {
                                viewModel.switchSignInMode()
                            })
                        }
                    }
                }
            }
        }
    }
}



