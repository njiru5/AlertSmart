package net.ezra.ui.criminology

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.ezra.R

@Composable
fun ReportCrimeScreen(navController: NavHostController) {
    var location by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var isNight by remember { mutableStateOf(true) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var timeDropdownExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var isReportSaved by remember { mutableStateOf(false) }

    val counties = listOf("Nairobi", "Embu", "Nakuru", "Meru", "Kajiado", "Kisii", "Machakos",
        "Kiambu", "Kisumu", "Busia", "Kitui", "Isiolo", "Nyeri", "Murang'a", "Kirinyaga") // Sample county list

    val times = (0..23).map { hour -> if (hour < 10) "0$hour:00" else "$hour:00" }

    // Firestore collection reference
    val crimesCollection = Firebase.firestore.collection("crimes")

    // Function to save crime details to Firestore
    fun saveCrimeDetails() {
        val crimeDetails = hashMapOf(
            "location" to location,
            "time" to time,
            "isNight" to isNight,
            "description" to description.text
        )

        // Add crime details to Firestore
        crimesCollection.add(crimeDetails)
            .addOnSuccessListener { documentReference ->
                isReportSaved = true
            }
            .addOnFailureListener { e ->
                // Handle failure if needed
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report a Crime", color = Color.White) },
                backgroundColor = Color(0xff4A235A),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon", tint = Color.White)
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
                    .padding(paddingValues)
            ) {
                item {
                    Text("Location", color = Color.White, fontSize = 18.sp)
                    Box {
                        Column {
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, shape = MaterialTheme.shapes.small)
                                    .padding(16.dp),
                                textStyle = LocalTextStyle.current.copy(color = Color.Black)
                            ) { innerTextField ->
                                if (searchQuery.text.isEmpty()) {
                                    Text(
                                        text = "Search County",
                                        style = TextStyle(color = Color.Gray.copy(alpha = 0.3f))
                                    )
                                }
                                innerTextField()
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { dropdownExpanded = !dropdownExpanded },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(location.ifEmpty { "Select County" }, color = Color.White)
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", tint = Color.White)
                            }
                        }
                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            counties.filter { it.contains(searchQuery.text, ignoreCase = true) }
                                .forEach { county ->
                                    DropdownMenuItem(onClick = {
                                        location = county
                                        dropdownExpanded = false
                                    }) {
                                        Text(county)
                                    }
                                }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Time", color = Color.White, fontSize = 18.sp)
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { timeDropdownExpanded = !timeDropdownExpanded },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(time.ifEmpty { "Select Time" }, color = Color.White)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = timeDropdownExpanded,
                            onDismissRequest = { timeDropdownExpanded = false }
                        ) {
                            times.forEach { hour ->
                                DropdownMenuItem(onClick = {
                                    time = hour
                                    timeDropdownExpanded = false
                                }) {
                                    Text(hour)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Night", color = Color.White, fontSize = 18.sp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Switch(
                            checked = isNight,
                            onCheckedChange = { isNight = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.Cyan)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Transport method", color = Color.White, fontSize = 18.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CrimeIconButton(
                            icon = R.drawable.car,
                            onClick = { /* Save details for car in Firestore */ }
                        )
                        CrimeIconButton(
                            icon = R.drawable.bike,
                            onClick = { /* Save details for bicycle in Firestore */ }
                        )
                        CrimeIconButton(
                            icon = R.drawable.running,
                            onClick = { /* Save details for motorcycle in Firestore */ }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Weapons", color = Color.White, fontSize = 18.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CrimeIconButton(
                            icon = R.drawable.gun,
                            onClick = { /* Save details for gun in Firestore */ }
                        )
                        CrimeIconButton(
                            icon = R.drawable.knife,
                            onClick = { /* Save details for knife in Firestore */ }
                        )
                        CrimeIconButton(
                            icon = R.drawable.bomb,
                            onClick = { /* Save details for bat in Firestore */ }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Brief Description", color = Color.White, fontSize = 18.sp)
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(16.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Button(
                        onClick = {
                            saveCrimeDetails()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7b4397)),
                        modifier = Modifier
                            .padding(16.dp)
                            .size(200.dp, 56.dp)
                            .background(Color(0xFF7b4397), shape = MaterialTheme.shapes.small)
                    ) {
                        Text("REPORT", color = Color.White)
                    }
                }

                if (isReportSaved) {
                    item {
                        Text(
                            text = "Report has been saved",
                            color = Color.Green,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CrimeIconButton(icon: Int, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() }
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
    }
}
