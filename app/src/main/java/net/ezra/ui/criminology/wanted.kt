package net.ezra.ui.criminology

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import net.ezra.R
import java.util.*


// Data class for wanted persons
data class WantedPerson(
    val name: String,
    val description: String,
    val imageUrl: String
)

// Function to fetch wanted persons data
suspend fun fetchWantedPersons(): List<WantedPerson> {
    return listOf(
        WantedPerson("", "", "https://mountkenyatimes.co.ke/wp-content/uploads/2023/02/Wanted.-450x553.jpg"),
        WantedPerson("", "", "https://mountkenyatimes.co.ke/wp-content/uploads/2023/02/Wanted-450x553.jpg"),
        WantedPerson("", "", "https://mountkenyatimes.co.ke/wp-content/uploads/2023/02/Wanted.-1-450x553.jpg"),
        WantedPerson("", "", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbZ34Zahbw_Zn3JzypV1O5sGz5I6Un0sKIUQ&usqp=CAU"),
        WantedPerson("", "", "https://mountkenyatimes.co.ke/wp-content/uploads/2023/02/Wanted..-450x553.jpg"),
        WantedPerson("", "", "https://www.capitalfm.co.ke/news/files/2022/08/Second-Fugitive-592x576.jpg"),
        WantedPerson("", "", "https://lh3.googleusercontent.com/qJuuJF-RzBhCTc7PtN8y5_E5i6oW4eeZT6WPZ5Uj0rUSBQFckTu_gF6XZm-4YMZ2C7TTvVZBlDLAUVkycjxNXe7UwrbgH-Gn_-UM4mWL=s1000"),
        WantedPerson("", "", "https://panafricanvisions.com/wp-content/uploads/2021/11/wanted-b-og_image-768x593.jpg")
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun WantedScreen(navController: NavHostController) {
    var wantedPersons by remember { mutableStateOf<List<WantedPerson>>(emptyList()) }

    LaunchedEffect(Unit) {
        wantedPersons = fetchWantedPersons()
    }

    // Manually add images to the carousel
    val carouselImages = listOf(
        "https://i.pinimg.com/474x/ff/4a/ef/ff4aef8c414073d949f74e386266e1b4.jpg",
        "https://i.pinimg.com/474x/4e/1c/a9/4e1ca9791b8789385a3e35b4f0891faa.jpg",
        "https://i.pinimg.com/474x/96/d6/0d/96d60d8fbc976037e61925b4643aec68.jpg",
        "https://i.pinimg.com/736x/e3/fa/64/e3fa64f50887633317e482fb81086078.jpg",
        "https://i.pinimg.com/474x/bd/c5/a6/bdc5a6fbedfbdd572682b665ebfc35d8.jpg"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Report Anybody From The Posters Below",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Note that they are armed and dangerous",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF800080), // Use purple color for top bar
                    titleContentColor = Color.White,
                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                item {
                    Carousel(images = carouselImages)
                }
                item {
                    Text(
                        text = "Be vigilant and call 911 incase you spot anybody below",
                        color = Color.Cyan,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 16.dp)

                    )
                }
                item {
                    Text(
                        text = "Wanted Persons",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                    )
                }

                items(wantedPersons) { person ->
                    WantedPersonCard(person)
                }
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Carousel(images: List<String>) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        while (true) {
            yield() // Ensure the effect isn't run continuously without yielding
            delay(2500) // Delay for 3 seconds
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % images.size
            )
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Image(
                painter = rememberImagePainter(data = images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
fun WantedPersonCard(person: WantedPerson) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Load image using Coil
            Image(
                painter = rememberImagePainter(data = person.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = person.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
