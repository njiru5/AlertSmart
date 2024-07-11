package net.ezra.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay
import net.ezra.R
import net.ezra.navigation.ROUTE_CREATE
import net.ezra.navigation.ROUTE_LOGIN
import net.ezra.navigation.ROUTE_NEWS
import net.ezra.navigation.ROUTE_PROFILE
import net.ezra.navigation.ROUTE_REPORT
import net.ezra.navigation.ROUTE_SPACE
import net.ezra.navigation.ROUTE_WANTED
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    var currentDate by remember { mutableStateOf(getCurrentDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            delay(1000L)
        }
    }
    //  $ROUTE_LOGIN/$ROUTE_NEWS
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Welcome Back!!", color = Color.White, fontSize = 30.sp)
                                Text(text = currentTime, color = Color.White, fontSize = 18.sp)
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.add),
                                contentDescription = "Icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                       // navController.navigate("$ROUTE_LOGIN/$ROUTE_CREATE")
                                        navController.navigate(ROUTE_CREATE)
                                    }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF322C6F), // Use purple color for top bar
                        titleContentColor = Color.White,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp)) // Added padding between top bars
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = currentDate, color = Color.White, fontSize = 18.sp)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF322C6F), // Use purple color for top bar
                        titleContentColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // Add padding to left and right
                )
            }
        },
        content = { paddingValues ->
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(Color.White, Color(0xFF322C6F)) // White to purple gradient
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Your daily updates and get the chance" +
                                "to enroll with us",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                item {
                    val scrollState = rememberScrollState()
                    var currentIndex by remember { mutableStateOf(0) }

                    LaunchedEffect(scrollState) {
                        while (true) {
                            delay(3000L)
                            currentIndex = (currentIndex + 1) % 5
                            scrollState.animateScrollTo(currentIndex * 300)
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(state = scrollState, orientation = Orientation.Horizontal)
                    ) {
                        items(listOf(
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJT6vRfWMViFPULhYkZlmgqevMcaFRYcB6ZQ&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeDLUgPm6eqlc3xZzykaaMRKvUUlMVaaiUlA&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4J3SVuvfQ1zJbe3CiTHVsR42-kuLYR1fem7KeTTI1JVsnMONQPISWbL-1UsyCBL_b9lM&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFWL3IUkZztcASctNoanXhc9sWkYgYwNfOJg&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5BiIMU-ESAt2lycI_RmlMdU06_ixipriOmw&usqp=CAU"
                        )) { imageUrl ->
                            Image(
                                painter = rememberImagePainter(data = imageUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(300.dp) // Adjusted width for visibility
                                    .height(200.dp)
                                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { navController.navigate( ROUTE_NEWS) },
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            colors = ButtonDefaults.buttonColors(Color(0xFF322C6F))
                        ) {
                            Text(
                                text = "View All",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DashboardCard(
                            imageRes = R.drawable.person,
                            title = "Wanted Persons",
                            onClick = { navController.navigate(ROUTE_WANTED) },
                            modifier = Modifier.weight(1f)
                        )
                        DashboardCard(
                            imageRes = R.drawable.face,
                            title = "Community Space",
                            onClick = { navController.navigate(ROUTE_SPACE) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DashboardCard(
                            imageRes = R.drawable.phone,
                            title = "Report Issue",
                            onClick = { navController.navigate(ROUTE_REPORT) },
                            modifier = Modifier.weight(1f)
                        )
                        DashboardCard(
                            imageRes = R.drawable.profile, // replace with your image resource
                            title = "Profile",
                            onClick = { navController.navigate(ROUTE_PROFILE)  },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DashboardCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("hh:mm:ss a")
    return sdf.format(Date())
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM d")
    return sdf.format(Date())
}
