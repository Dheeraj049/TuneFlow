package uk.ac.tees.mad.tuneflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()), contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Authentication Icon",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Please login in to continue.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

//                    OutlinedTextField(
//                        value = email,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .focusRequester(focusRequesterEmail),
//                        onValueChange = {
//                            viewModel.updateEmail(it)
//                        },
//                        label = { Text("Email") },
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions(onNext = {
//                            focusRequesterPassword.requestFocus()
//                        }),
//                        shape = RoundedCornerShape(8.dp),
//                        singleLine = true
//                    )
//
//                    OutlinedTextField(value = password,
//                        onValueChange = {
//                            viewModel.updatePassword(it)
//                        },
//                        label = { Text("Password") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .focusRequester(focusRequesterPassword),
//                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
//                        ),
//                        keyboardActions = KeyboardActions(onDone = {
//                            focusManager.clearFocus()
//                        }),
//                        shape = RoundedCornerShape(8.dp),
//                        singleLine = true,
//                        trailingIcon = {
//                            IconButton(onClick = {
//                                viewModel.togglePasswordVisibility()
//                            }) {
//                                Icon(
//                                    imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
//                                    contentDescription = "Toggle Password Visibility"
//                                )
//                            }
//                        })
//
//                    Button(
//                        enabled = email.isNotBlank() && password.isNotBlank(),
//                        onClick = {
//                            // Placeholder for login logic
//                            println("Login button clicked with email: $email, password: $password")
//
//                            viewModel.logIn(email, password)
//                            viewModel.switchLoginMode()
//
//                            //onLoginSuccess()
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text("Login")
//                    }
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = "Don't have an account?",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                        TextButton(onClick = {
//                            viewModel.updateEmail("")
//                            viewModel.updatePassword("")
//                            navController.navigate(Dest.SignUpScreen)
//                        }) {
//                            Text("Sign Up")
//                        }
//                    }
                }
            }
        }
    }
}