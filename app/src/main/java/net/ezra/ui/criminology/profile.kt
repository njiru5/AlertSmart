package net.ezra.ui.criminology

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import net.ezra.ui.criminology.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    val currentTime = remember { mutableStateOf("") }
    val name by profileViewModel.name.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()
    var newName by remember { mutableStateOf(name) }
    var newAvatarUrl by remember { mutableStateOf(avatarUrl) }

    // Function to update current time
    fun updateCurrentTime() {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        currentTime.value = sdf.format(Date())
    }

    // Initial update of current time
    updateCurrentTime()

    val avatars = listOf(
        "https://cdn3.vectorstock.com/i/1000x1000/96/12/young-man-avatar-cartoon-character-profile-picture-vector-25739612.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQGtEolGdo2azvBIZT3BEBbP4nClhnBedEWA&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTw4xIzlTTRJKIQB1tq1Jbs5Rfj7hU6h1UtPg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzohUBll0qs0ul3EGZ3K_-DaP_x15siN_Mng&s",
        "https://cdn.vectorstock.com/i/500p/23/61/arab-man-wearing-turban-vector-45842361.avif",
        "https://cdn.vectorstock.com/i/500p/86/77/portrait-of-a-young-man-with-beard-and-hair-style-vector-23848677.avif",
        "https://cdn.vectorstock.com/i/500p/26/98/young-man-jumping-over-city-vector-782698.avif",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZO8GPm0phbELvMAA6QsEOYilHs0Fnmq3k2w&s",
        "https://st3.depositphotos.com/15648834/17930/v/450/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn9zilY2Yu2hc19pDZFxgWDTUDy5DId7ITqA&s"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Your Own Profile",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = currentTime.value,
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        // Handle avatar click (e.g., open image picker)
                    }
            ) {
                Image(
                    painter = rememberImagePainter(newAvatarUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = {
                        profileViewModel.updateName(newName)
                        profileViewModel.updateAvatarUrl(newAvatarUrl)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Update")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "@marco_asensio",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                avatars.chunked(2).forEach { rowAvatars ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowAvatars.forEach { avatarUrl ->
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .clickable {
                                        newAvatarUrl = avatarUrl
                                    }
                            ) {
                                Image(
                                    painter = rememberImagePainter(avatarUrl),
                                    contentDescription = "Avatar",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
