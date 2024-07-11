package net.ezra.ui.auth

import android.widget.Toast
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
import com.google.firebase.firestore.FirebaseFirestore
import net.ezra.R
import net.ezra.navigation.ROUTE_LOGIN
import net.ezra.navigation.ROUTE_REGISTER

@Composable
fun SignUpScreen(navController: NavController, onSignUpSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.art), // replace with your background image resource
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
                text = "Sign Up",
                style = MaterialTheme.typography.h4,
                color = Color.Black
            )
            Text(
                text = "Create a new account",
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
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
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
                    onClick = {
                        when {
                            email.isBlank() -> error = "Email is required"
                            password.isBlank() -> error = "Password is required"
                            confirmPassword.isBlank() -> error = "Password Confirmation required"
                            password != confirmPassword -> error = "Passwords do not match"
                            else -> {
                                isLoading = true
                                signUp(email, password, {
                                    isLoading = false
                                    Toast.makeText(context, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                    onSignUpSuccess()
                                }) { errorMessage ->
                                    isLoading = false
                                    error = errorMessage
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }

                Text(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ROUTE_LOGIN) {
                                popUpTo(ROUTE_REGISTER) { inclusive = true }
                            }
                        }
                        .padding(top = 16.dp),
                    text = "Go to login",
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

private fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { signUpTask ->
            if (signUpTask.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    val firestore = FirebaseFirestore.getInstance()
                    val userData = hashMapOf(
                        "uid" to user.uid,
                        "email" to email
                    )
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onError(e.message ?: "Sign-up failed")
                        }
                } ?: run {
                    onError("User is null")
                }
            } else {
                onError("Sign-up failed. Please check your email and try again.")
            }
        }
}
