package net.ezra.ui.criminology

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import net.ezra.navigation.ROUTE_PROFILE
import net.ezra.ui.criminology.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunitySpaceScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    val name by profileViewModel.name.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()
    var message by remember { mutableStateOf("") }
    var chatMessages by remember { mutableStateOf(listOf<String>()) }
    var backgroundColor by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Community Space",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            actions = {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                            navController.navigate(ROUTE_PROFILE) // Adjust this route accordingly
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
                .background(Color.LightGray)
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            items(chatMessages) { chatMessage ->
                Text(
                    text = chatMessage,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Divider(color = Color.Gray, thickness = 5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Type a message") },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
            )
            IconButton(onClick = {
                if (message.isNotEmpty()) {
                    chatMessages = chatMessages + message
                    message = ""
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
            IconButton(onClick = {
                // Implement the color palette functionality
                backgroundColor = Color(
                    (0..255).random(),
                    (0..255).random(),
                    (0..255).random()
                )
            }) {
                LoadImageIcon(
                    url = "https://img.icons8.com/?size=128&id=56361&format=png&color=1A6DFF,C822FF",
                    contentDescription = "Change Background Color Icon"
                )
            }
        }
    }
}

@Composable
fun LoadImageIcon(url: String, contentDescription: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.size(24.dp)
    )
}
