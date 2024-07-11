package net.ezra.ui.criminology

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import net.ezra.navigation.ROUTE_ENROLL
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ViewNewsScreen(navController: NavController) {
    var currentTime by remember { mutableStateOf("") }
    var newsArticles by remember { mutableStateOf<List<NewsArticle>>(emptyList()) }

    // Function to update time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }

    // Function to fetch news articles from Firestore
    LaunchedEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val newsSnapshot = firestore.collection("news")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        newsArticles = newsSnapshot.documents.map { document ->
            NewsArticle(
                name = document.getString("name") ?: "",
                description = document.getString("description") ?: "",
                imageUrl = document.getString("imageUrl"),
                time = document.getString("time") ?: "",
                location = document.getString("location") ?: "",
                timestamp = document.getLong("timestamp") ?: 0L
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "News", color = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = currentTime, color = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Enroll",
                            color = Color.White,
                            modifier = Modifier.clickable {
                                navController.navigate(ROUTE_ENROLL)
                            }
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            if (newsArticles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No news available",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(newsArticles) { article ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = Color(0xFFF1F1F1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = article.name, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = article.description)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Location: ${article.location}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Published at: ${article.time}")
                                Spacer(modifier = Modifier.height(8.dp))
                                article.imageUrl?.let {
                                    Image(
                                        painter = rememberImagePainter(it),
                                        contentDescription = "Article Image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 200.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

// Data class for news articles
data class NewsArticle(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val time: String,
    val location: String,
    val timestamp: Long
)
