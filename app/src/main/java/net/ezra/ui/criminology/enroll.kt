package net.ezra.ui.criminology


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import net.ezra.R

@Composable
fun EnrollScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf<String?>(null) }

    // Firestore collection reference
    val usersCollection = Firebase.firestore.collection("users")

    // State to manage messages and text field clearing
    var message by remember { mutableStateOf("") }

    // Function to save or update user details
    fun saveOrUpdateUser() {
        if (name.isBlank() || email.isBlank() || password.isBlank() || phone.isBlank()) {
            // Handle empty fields if needed
            message = "All fields are required"
            return
        }

        // Define userData as Map<String, Any>
        val userData: Map<String, Any> = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password,
            "phone" to phone
        )

        // Add new user document
        usersCollection.add(userData)
            .addOnSuccessListener { documentReference ->
                // Handle add success
                userId = documentReference.id
                Log.d("EnrollScreen", "User added successfully with ID: ${documentReference.id}")

                // Navigate to UserDetailsScreen with userId
                navController.navigate("user_details/${documentReference.id}")

                // Update message and clear text fields
                message = "Details saved successfully"
                name = ""
                email = ""
                password = ""
                phone = ""
            }
            .addOnFailureListener { e ->
                // Handle add failure if needed
                Log.e("EnrollScreen", "Error adding user", e)
                message = "Failed to save details"
            }
    }

    // Example list of image URLs
    val imageUrls = listOf(
        "https://i.pinimg.com/564x/cb/72/9b/cb729be91f2e50dcbd819e8249b9c2f2.jpg",
        "https://www.orlando.gov/files/sharedassets/public/v/2/opd/dsc02054.jpg?dimension=pageimagefullwidth&w=992",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSCMKa9QnFamaZBMJk6Kuwir-2zyVluhIkyMA&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_8vr56B62SeCl_muMCABjTxtqnAmjj3d7qQ&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcydWyKbD-hutmF-sRMYEtlJCArqwGZkTqpg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSi6xuihbVdz_Erm5e3xEXW7Rf6Lcdob9UkdQ&s"
    )

    var currentImageIndex by remember { mutableStateOf(0) }
    var scrollPosition by remember { mutableStateOf(0) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(3000)
            scrollPosition = (scrollPosition + 1) % imageUrls.size
            lazyListState.animateScrollToItem(scrollPosition)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color(0xFFBBDEFB)) // Light Blue background color
    ) {
        // Top AppBar
        item {
            TopAppBar(
                title = {
                    Text(
                        text = "Enroll and Join Us Now",
                        textAlign = TextAlign.Center, // Align text to center vertically
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Carousel with images wrapped in LazyRow
        item {
            LazyRow(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Adjust height as per your preference
            ) {
                itemsIndexed(imageUrls) { index, imageUrl ->
                    Card(
                        modifier = Modifier
                            .width(280.dp)
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = 4.dp
                    ) {
                        Image(
                            painter = rememberImagePainter(data = imageUrl),
                            contentDescription = null, // Provide proper content description if needed
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Text between LazyRow and TextField
        item {
            Text(
                text = "Please Input Your Details Below",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Add Details Section
        item {
            RoundedTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                leadingIcon = painterResource(id = R.drawable.person),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item { Spacer(modifier = Modifier.height(4.dp)) }
        item {
            RoundedTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = painterResource(id = R.drawable.email),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item { Spacer(modifier = Modifier.height(4.dp)) }
        item {
            RoundedTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = painterResource(id = R.drawable.lock),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
        }
        item { Spacer(modifier = Modifier.height(4.dp)) }
        item {
            RoundedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                leadingIcon = painterResource(id = R.drawable.phone),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Message Section
        item {
            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = Color.Green,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Save Button Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = {
                        saveOrUpdateUser()
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun RoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation? = null
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp) // Reduced vertical padding for a more compact appearance
            .heightIn(min = 56.dp) // Ensure minimum height for the TextField
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Image(
                painter = leadingIcon,
                contentDescription = null, // Provide proper content description if needed
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            val visualTransform = visualTransformation ?: VisualTransformation.None
            val imeAction = ImeAction.Next
            val keyboardType = KeyboardType.Text
            val autoCorrect = false

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(label) },
                visualTransformation = visualTransform,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )
        }
    }
}
