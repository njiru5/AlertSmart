package net.ezra.ui.auth

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import net.ezra.R
import net.ezra.navigation.ROUTE_CREATE
import net.ezra.navigation.ROUTE_LOGIN
import net.ezra.navigation.ROUTE_NEWS
import net.ezra.navigation.ROUTE_REGISTER

@Composable
fun LoginScreen(navController: NavController, destination: String?, onLoginSuccess: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    fun loginUser() {
        if (email.isEmpty() || password.isEmpty()) {
            error = "All fields are required"
            return
        }

        isLoading = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    destination?.let {
                        navController.navigate(it) {
                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                        }
                    } ?: navController.navigate(ROUTE_NEWS) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                    }
                    onLoginSuccess()
                } else {
                    error = task.exception?.message
                }
            }
    }

    BackHandler {
        navController.popBackStack()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.art),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Get started",
                style = MaterialTheme.typography.h4,
                color = Color.Black
            )
            Text(
                text = "Already have an account? Sign in",
                style = MaterialTheme.typography.body1,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else {
                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xff4A235A)),
                    onClick = { loginUser() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Text(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ROUTE_REGISTER) {
                                popUpTo(ROUTE_LOGIN) { inclusive = true }
                            }
                        }
                        .padding(top = 16.dp),
                    text = "Go to register",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
            }

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
