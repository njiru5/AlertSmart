package net.ezra.ui.criminology

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.ezra.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(navController: NavController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Firestore collection reference
    val newsCollection = Firebase.firestore.collection("news")

    // Function to save news article to Firestore
    fun saveNewsArticle() {
        val newsArticle = hashMapOf(
            "name" to name,
            "description" to description,
            "imageUrl" to imageUrl,
            "time" to time,
            "location" to location,
            "timestamp" to System.currentTimeMillis()
        )

        // Add news article to Firestore
        newsCollection.add(newsArticle)
            .addOnSuccessListener {
                Toast.makeText(context, "News article saved", Toast.LENGTH_SHORT).show()
                navController.popBackStack()  // Navigate back to the previous screen
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save news article", Toast.LENGTH_SHORT).show()
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)  // Set background to white
    ) {
        item {
            Text(
                text = "Create your own news Article",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        item {
            Text(
                text = "Note that details you are about to add should be true" +
                        " and you will be fined if found otherwise",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        item {
            Text(
                text = "Fill in the form to create your article.",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon"
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(onClick = { expanded = true })
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (time.isEmpty()) "Select Time" else time,
                    color = Color.Gray
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for (hour in 0..23) {
                        val formattedHour = String.format("%02d:00", hour)
                        DropdownMenuItem(
                            text = { Text(text = formattedHour) },
                            onClick = {
                                time = formattedHour
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL (optional)") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { saveNewsArticle() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff4A235A) // Adjust button color
                )
            ) {
                Text(
                    text = "Submit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
