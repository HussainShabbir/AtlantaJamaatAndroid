package com.atlantajamaat.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlantajamaat.app.network.ScreenAccessRepository
import com.atlantajamaat.app.ui.theme.AppTopBar
import com.example.app.ui.theme.LightGreyBg
import java.util.Locale

// Tab Model
enum class BottomTab(val title: String, val categoryFilter: String?, val icon: ImageVector) {
    HOME("Home", null, Icons.Default.Home),
    FMB("FMB", "FMB", Icons.Default.List),
    NIYAZ("Niyaz", "NIY", Icons.Default.AccountBox),
    DANA_COMMITTEE("Dana", "DNA", Icons.Default.Info),
}

@Composable
fun Home() {
    var screens by remember { mutableStateOf<List<ScreenAccessItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }

    val repository = remember { ScreenAccessRepository() }
    var retryTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(retryTrigger) {
        isLoading = true
        errorMessage = null
        try {
            val rawData = repository.getScreenAccessInfo()
            screens = rawData.map { raw ->
                mapToUIModel(
                    screenCode = raw.screenCode,
                    roleCode = raw.roleCode,
                    screenTypeCode = raw.screenTypeCode
                )
            }
        } catch (e: Exception) {
            errorMessage = e.message ?: "Failed to fetch data"
        } finally {
            isLoading = false
        }
    }

    // Tab ke basis par items filter karne ka logic
    val filteredScreens = remember(selectedTab, screens) {
        when (selectedTab) {
            BottomTab.HOME -> screens
            BottomTab.FMB -> screens.filter { it.category.equals("FMB", ignoreCase = true) }
            BottomTab.NIYAZ -> screens.filter { it.category.equals("NIY", ignoreCase = true) }
            BottomTab.DANA_COMMITTEE -> screens.filter { it.category.equals("DNA", ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Atlanta Jamaat"
            )
        },
        bottomBar = {
            NavigationBar {
                BottomTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab.title) },
                        icon = { Icon(tab.icon, contentDescription = tab.title) }
                    )
                }
            }
        },
        containerColor = LightGreyBg
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = errorMessage ?: "Something went wrong",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { retryTrigger++ }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                filteredScreens.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No screens found for ${selectedTab.title}")
                    }
                }

                else -> {
                    ScreenAccessList(screens = filteredScreens)
                }
            }
        }
    }
}

// 3. List Component
@Composable
fun ScreenAccessList(screens: List<ScreenAccessItem>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = screens,
            key = { item -> item.id }
        ) { item ->
            ScreenAccessCard(item = item)
        }
    }
}

// 4. Card Component
@Composable
fun ScreenAccessCard(item: ScreenAccessItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set card background to pure white
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp // Soft drop shadow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F) // Dark crisp text color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.rawCode,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Category Badge & Role
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = item.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.roleLabel,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// 5. Mapper Function
fun mapToUIModel(
    screenCode: String,
    roleCode: String,
    screenTypeCode: String
): ScreenAccessItem {

    val title = when (screenCode) {
        "FMB_SIZE_CONFIG" -> "Size Configuration"
        "FMB_FEEDBACK_REPORT" -> "Feedback Report"
        "FMB_SUPPORTROLE_CONFIG" -> "Support Role Setup"
        "FMB_ITEMTYPE_CONFIG" -> "Item Type Configuration"
        "FMB_ACTIVESTAFF_REPORT" -> "Active Staff Report"
        "FMB_ZONESUMMARY_REPORT" -> "Zone Summary Report"
        "FMB_STAFF_CONFIG" -> "Staff Management"
        "FMB_PRICE_VIEW" -> "Price Overview"
        "FMB_COOK_VIEW" -> "Cook Directory"
        "FMB_ITEMTYPE_VIEW" -> "Item Types"
        "FMB_COOK_BILL" -> "Cook Billing"
        "FMB_DRIVER_VIEW" -> "Driver Directory"
        "FMB_MENU_VIEW" -> "Menu Catalog"
        "FMB_ZONEDETAIL_REPORT" -> "Zone Detail Report"
        "FMB_ZONE_CONFIG" -> "Zone Settings"
        "FMB_MENU_CONFIG" -> "Menu Management"
        "FMB_COOK_REPORT" -> "Cook Performance Report"
        "FMB_THAALI_PREF" -> "Thaali Preferences"
        "FMB_SCHEDULE" -> "FMB Schedule"
        "FMB_DRIVER_REPORT" -> "Driver Activity Report"
        "FMB_SIGNUP_REPORT" -> "Signup Report"
        "FMB_THAALI_SIGN" -> "Thaali Registrations"
        "FMB_ZONE_VIEW" -> "Zone Information"
        "NIY_SIGNUP" -> "Niyaz Registration"
        "NIY_SCHEDULE" -> "Niyaz Schedule"
        "NIY_SIGNUP_REPORT" -> "Niyaz Signup Report"
        "NIY_SIGNUP_REPORT_KARNAR" -> "Niyaz Karnar Report"
        "DNA_REP" -> "DNA Analytics Report"
        "DNA_MNG" -> "DNA Management"
        else -> screenCode.replace("_", " ")
            .lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    val roleLabel = when (roleCode) {
        "FMB_ADM" -> "FMB Admin"
        "NIY_ADM" -> "Niyaz Admin"
        "DNA_ADM" -> "DNA Admin"
        "GEN_MEM" -> "General Member"
        else -> roleCode
    }

    return ScreenAccessItem(
        id = screenCode,
        title = title,
        category = screenTypeCode,
        roleLabel = roleLabel,
        rawCode = screenCode
    )
}
data class ScreenAccessItem(
    val id: String,
    val title: String,
    val category: String,
    val roleLabel: String,
    val rawCode: String
)
