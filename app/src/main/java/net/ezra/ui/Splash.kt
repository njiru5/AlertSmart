package net.ezra.ui

import android.content.res.Configuration
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import net.ezra.R
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.navigation.ROUTE_HOME
import net.ezra.navigation.ROUTE_LOGIN
import kotlin.random.Random

@Composable
fun SplashScreen(navController: NavHostController) {


    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    // List of color gradients for the splash screen background
    val gradients = listOf(
        Brush.linearGradient(colors = listOf(Color.Red, Color.Yellow)),
        Brush.linearGradient(colors = listOf(Color.Green, Color.Blue)),
        Brush.linearGradient(colors = listOf(Color.Cyan, Color.Magenta)),
        Brush.linearGradient(colors = listOf(Color(0xFFEE7752), Color(0xFFE73C7E), Color(0xFF23A6D5), Color(0xFF23D5AB)))
    )
    val randomGradient = gradients[Random.nextInt(gradients.size)]

    // Animation
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            // tween Animation
            animationSpec = tween(
                durationMillis = 700,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }
            )
        )
        // Customize the delay time
        delay(3000L)
        navController.navigate(ROUTE_HOME)
    }

    // Splash screen content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(randomGradient),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.astrazeneca),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(20.dp)
                .width(100.dp)
                .scale(scale.value)
        )

        Text(
            "Welcome to AlertSmart",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeScreenPreviewLight() {
    SplashScreen(rememberNavController())
}
