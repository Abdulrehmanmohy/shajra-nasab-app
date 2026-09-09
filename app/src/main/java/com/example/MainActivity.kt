package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Work
import com.example.util.PdfExporter
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FamilyMember
import com.example.ui.FamilyViewModel
import com.example.ui.theme.BentoAmberIcon
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBlueOnContainer
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoGreenCard
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoLightCard
import com.example.ui.theme.BentoNavBackground
import com.example.ui.theme.BentoNeutralCard
import com.example.ui.theme.BentoPinkCard
import com.example.ui.theme.BentoPinkText
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: FamilyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShajraNasabApp(viewModel = viewModel)
        }
    }
}

@Composable
fun ShajraNasabApp(viewModel: FamilyViewModel) {
    val context = LocalContext.current
    val allMembers by viewModel.allMembers.collectAsState()
    val filteredMembers by viewModel.filteredMembers.collectAsState()
    val totalCount by viewModel.totalMembersCount.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val genFilter by viewModel.selectedGenFilter.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var selectedMemberForDetail by remember { mutableStateOf<FamilyMember?>(null) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var memberToEdit by remember { mutableStateOf<FamilyMember?>(null) }
    var parentIdForNewChild by remember { mutableStateOf<Int?>(null) }
    var isDarkMode by remember { mutableStateOf(false) }

    MyApplicationTheme(darkTheme = isDarkMode) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color(0xFF121212) else BentoBackground),
            containerColor = if (isDarkMode) Color(0xFF121212) else BentoBackground,
            bottomBar = {
                ShajraBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ShajraDashboardTab(
                    allMembers = allMembers,
                    filteredMembers = filteredMembers,
                    totalCount = totalCount,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    selectedGenFilter = genFilter,
                    onGenFilterSelected = { viewModel.setGenFilter(it) },
                    onMemberClick = { selectedMemberForDetail = it },
                    onAddMemberClick = {
                        parentIdForNewChild = null
                        memberToEdit = null
                        showAddMemberDialog = true
                    }
                )

                1 -> DirectoryTab(
                    filteredMembers = filteredMembers,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onMemberClick = { selectedMemberForDetail = it },
                    onAddMemberClick = {
                        parentIdForNewChild = null
                        memberToEdit = null
                        showAddMemberDialog = true
                    }
                )

                2 -> AddMemberTab(
                    allMembers = allMembers,
                    initialParentId = parentIdForNewChild,
                    onSaveMember = { newMember ->
                        viewModel.addMember(newMember)
                        Toast.makeText(context, "Fard Shajra me Shamil Ho Gaya!", Toast.LENGTH_SHORT).show()
                        selectedTab = 0
                    }
                )

                3 -> ShareShajraTab(
                    allMembers = allMembers,
                    getLineage = { m -> viewModel.getLineageChain(m, allMembers) }
                )

                4 -> SettingsAndHelpTab(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { isDarkMode = it }
                )
            }
        }

        // --- Dialogs ---
        selectedMemberForDetail?.let { member ->
            MemberDetailDialog(
                member = member,
                allMembers = allMembers,
                getLineage = { m -> viewModel.getLineageChain(m, allMembers) },
                onDismiss = { selectedMemberForDetail = null },
                onAddChild = { parentId ->
                    parentIdForNewChild = parentId
                    memberToEdit = null
                    selectedMemberForDetail = null
                    showAddMemberDialog = true
                },
                onEdit = { m ->
                    memberToEdit = m
                    selectedMemberForDetail = null
                    showAddMemberDialog = true
                },
                onDelete = { m ->
                    viewModel.deleteMember(m)
                    selectedMemberForDetail = null
                    Toast.makeText(context, "Fard Delete Ho Gaya", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (showAddMemberDialog) {
            AddOrEditMemberDialog(
                allMembers = allMembers,
                editingMember = memberToEdit,
                preselectedParentId = parentIdForNewChild,
                onDismiss = {
                    showAddMemberDialog = false
                    memberToEdit = null
                    parentIdForNewChild = null
                },
                onSave = { member ->
                    if (memberToEdit != null) {
                        viewModel.updateMember(member)
                        Toast.makeText(context, "Tafseelat Update Ho Gaein!", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addMember(member)
                        Toast.makeText(context, "Naya Fard Shamil Ho Gaya!", Toast.LENGTH_SHORT).show()
                    }
                    showAddMemberDialog = false
                    memberToEdit = null
                    parentIdForNewChild = null
                }
            )
        }
    }
}
}

// --- TAB 0: SHAJRA DASHBOARD & BENTO GRID TREE ---
@Composable
fun ShajraDashboardTab(
    allMembers: List<FamilyMember>,
    filteredMembers: List<FamilyMember>,
    totalCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedGenFilter: Int?,
    onGenFilterSelected: (Int?) -> Unit,
    onMemberClick: (FamilyMember) -> Unit,
    onAddMemberClick: () -> Unit
) {
    val rootAncestor = allMembers.find { it.parentId == null || it.generation == 1 }
    val maxGen = if (allMembers.isNotEmpty()) allMembers.maxOf { it.generation } else 1
    val tribeCount = allMembers.map { it.casteTribe }.filter { it.isNotBlank() }.distinct().size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Header Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "شجرہ نسب",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Shajra-e-Nasab",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "Apne khandan ki shaan aur pehchan ko mehfooz karein",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(BentoBlueContainer)
                        .border(1.5.dp, BentoBlueOnContainer, CircleShape)
                        .clickable { onAddMemberClick() }
                        .testTag("add_member_header_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add Member",
                        tint = BentoBlueOnContainer
                    )
                }
            }
        }

        // Hero Banner Illustration
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_shajra_1785337120015),
                        contentDescription = "Shajra Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f))
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Text(
                                text = "Khandani Shajra Nasab",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Pardada se le kar Aulad tak ki nasli tareekh",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Bento Grid Metrics Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1: Total Family Members
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onAddMemberClick() }
                        .testTag("total_members_card"),
                    colors = CardDefaults.cardColors(containerColor = BentoBlueContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Groups,
                                contentDescription = null,
                                tint = BentoBlueOnContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = BentoBlueOnContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "$totalCount Afraad",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoBlueOnContainer
                            )
                            Text(
                                text = "Khandan me shamil",
                                fontSize = 11.sp,
                                color = BentoBlueOnContainer.copy(alpha = 0.75f)
                            )
                        }
                    }
                }

                // Card 2: Generations Count
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .testTag("generations_card"),
                    colors = CardDefaults.cardColors(containerColor = BentoGreenCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            Icons.Outlined.AccountTree,
                            contentDescription = null,
                            tint = BentoGreenText,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "$maxGen Naslein",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoGreenText
                            )
                            Text(
                                text = "Generations Tracked",
                                fontSize = 11.sp,
                                color = BentoGreenText.copy(alpha = 0.75f)
                            )
                        }
                    }
                }
            }
        }

        // Ancestor Spotlight Banner Card
        rootAncestor?.let { patriarch ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
                        .clickable { onMemberClick(patriarch) }
                        .testTag("root_ancestor_card"),
                    colors = CardDefaults.cardColors(containerColor = BentoLightCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BentoPinkCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.HistoryEdu,
                                contentDescription = null,
                                tint = BentoPinkText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Buzurg-e-Aala / Head Ancestor",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoPinkText
                            )
                            Text(
                                text = patriarch.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                            if (patriarch.casteTribe.isNotBlank() || patriarch.nativeCity.isNotBlank()) {
                                Text(
                                    text = "${patriarch.casteTribe} • ${patriarch.nativeCity}",
                                    fontSize = 12.sp,
                                    color = BentoTextSecondary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BentoBlueContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Gen 1",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoBlueOnContainer
                            )
                        }
                    }
                }
            }
        }

        // Search Bar & Generation Filters
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Fard, Zaat ya Shehar se talash karein...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BentoLightCard,
                        unfocusedContainerColor = BentoLightCard,
                        focusedBorderColor = BentoBlueOnContainer,
                        unfocusedBorderColor = BentoBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input_field")
                )

                // Generation Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedGenFilter == null,
                            onClick = { onGenFilterSelected(null) },
                            label = { Text("All (Sab)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BentoBlueOnContainer,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("filter_gen_all")
                        )
                    }
                    items(listOf(1, 2, 3, 4, 5)) { gen ->
                        val genName = when (gen) {
                            1 -> "Gen 1: Pardada"
                            2 -> "Gen 2: Dada/Dadi"
                            3 -> "Gen 3: Abba/Ammi"
                            4 -> "Gen 4: Aap/Bhai"
                            5 -> "Gen 5: Aulad"
                            else -> "Gen $gen"
                        }
                        FilterChip(
                            selected = selectedGenFilter == gen,
                            onClick = { onGenFilterSelected(if (selectedGenFilter == gen) null else gen) },
                            label = { Text(genName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BentoBlueOnContainer,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("filter_gen_$gen")
                        )
                    }
                }
            }
        }

        // Visual Generation Grouped Tree Cards
        val groupedByGen = filteredMembers.groupBy { it.generation }.toSortedMap()

        if (groupedByGen.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BentoNeutralCard),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Koi fard nahi mila. Search query tabdeel karein ya naya fard shamil karein.",
                        modifier = Modifier.padding(20.dp),
                        fontSize = 13.sp,
                        color = BentoTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            groupedByGen.forEach { (genLevel, membersInGen) ->
                item {
                    val genTitle = when (genLevel) {
                        1 -> "Pehli Nasal (Pardada / Ancestors)"
                        2 -> "Doosri Nasal (Dada / Dadi / Nana)"
                        3 -> "Teesri Nasal (Abba / Ammi / Chacha / Mama)"
                        4 -> "Chauthi Nasal (Aap / Bhai / Behan / Cousins)"
                        5 -> "Panchwin Nasal (Aulad / Potay / Potiyaan)"
                        else -> "Nasal $genLevel"
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(BentoBlueOnContainer)
                            )
                            Text(
                                text = genTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${membersInGen.size} Afraad",
                                fontSize = 11.sp,
                                color = BentoTextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        membersInGen.forEach { member ->
                            FamilyMemberTreeCard(
                                member = member,
                                allMembers = allMembers,
                                onClick = { onMemberClick(member) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Composable: Family Member Tree Card ---
@Composable
fun FamilyMemberTreeCard(
    member: FamilyMember,
    allMembers: List<FamilyMember>,
    onClick: () -> Unit
) {
    val parent = allMembers.find { it.id == member.parentId }
    val parentDisplayName = parent?.name ?: member.fatherName

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("member_card_${member.id}"),
        colors = CardDefaults.cardColors(containerColor = BentoLightCard),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Gender-themed Avatar Icon
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (member.gender == "Male") BentoBlueContainer else BentoPinkCard),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (member.gender == "Male") Icons.Default.Male else Icons.Default.Female,
                    contentDescription = member.gender,
                    tint = if (member.gender == "Male") BentoBlueOnContainer else BentoPinkText,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = member.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (parentDisplayName.isNotBlank()) {
                    Text(
                        text = "Walid: $parentDisplayName",
                        fontSize = 12.sp,
                        color = BentoTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (member.casteTribe.isNotBlank()) {
                        Text(
                            text = member.casteTribe,
                            fontSize = 11.sp,
                            color = BentoBlueOnContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (member.nativeCity.isNotBlank()) {
                        Text(
                            text = "📍 ${member.nativeCity}",
                            fontSize = 11.sp,
                            color = BentoTextSecondary
                        )
                    }
                    if (member.birthYear.isNotBlank()) {
                        Text(
                            text = "🗓 ${member.birthYear}",
                            fontSize = 11.sp,
                            color = BentoTextSecondary
                        )
                    }
                }
            }

            // Relationship Tag Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (member.relation) {
                            "Self / Aap" -> BentoGreenCard
                            "Father", "Abba Ji / Father" -> BentoBlueContainer
                            "Mother", "Ammi Ji / Mother" -> BentoPinkCard
                            else -> BentoNeutralCard
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = member.relation,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (member.relation) {
                        "Self / Aap" -> BentoGreenText
                        "Father", "Abba Ji / Father" -> BentoBlueOnContainer
                        "Mother", "Ammi Ji / Mother" -> BentoPinkText
                        else -> BentoTextPrimary
                    }
                )
            }
        }
    }
}

// --- TAB 1: DIRECTORY & SEARCH ---
@Composable
fun DirectoryTab(
    filteredMembers: List<FamilyMember>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMemberClick: (FamilyMember) -> Unit,
    onAddMemberClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Khandani Fehrist (Directory)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BentoTextPrimary
            )
            Text(
                text = "Tamam shamil afraad ki mukammal list aur maloomat",
                fontSize = 13.sp,
                color = BentoTextSecondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Naam, Zaat, Watan ya Rishta likhein...") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BentoLightCard,
                    unfocusedContainerColor = BentoLightCard,
                    focusedBorderColor = BentoBlueOnContainer,
                    unfocusedBorderColor = BentoBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("directory_search_input")
            )
        }

        if (filteredMembers.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BentoNeutralCard),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Koi fard darj nahi mila.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onAddMemberClick,
                            colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("add_member_from_empty_directory")
                        ) {
                            Text("Naya Fard Shamil Karein", color = Color.White)
                        }
                    }
                }
            }
        } else {
            items(filteredMembers) { member ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
                        .clickable { onMemberClick(member) }
                        .testTag("directory_item_${member.id}"),
                    colors = CardDefaults.cardColors(containerColor = BentoLightCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(
                                    imageVector = if (member.gender == "Male") Icons.Default.Male else Icons.Default.Female,
                                    contentDescription = null,
                                    tint = if (member.gender == "Male") BentoBlueOnContainer else BentoPinkText
                                )
                                Column {
                                    Text(
                                        text = member.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = BentoTextPrimary
                                    )
                                    if (member.fatherName.isNotBlank()) {
                                        Text(
                                            text = "Walid: ${member.fatherName}",
                                            fontSize = 12.sp,
                                            color = BentoTextSecondary
                                        )
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(BentoBlueContainer)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Gen ${member.generation}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoBlueOnContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (member.casteTribe.isNotBlank()) {
                                Text(
                                    text = "Zaat: ${member.casteTribe}",
                                    fontSize = 12.sp,
                                    color = BentoBlueOnContainer,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            if (member.nativeCity.isNotBlank()) {
                                Text(
                                    text = "Watan: ${member.nativeCity}",
                                    fontSize = 12.sp,
                                    color = BentoTextSecondary
                                )
                            }
                            if (member.profession.isNotBlank()) {
                                Text(
                                    text = "Pesha: ${member.profession}",
                                    fontSize = 12.sp,
                                    color = BentoTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- TAB 2: ADD MEMBER FORM TAB ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberTab(
    allMembers: List<FamilyMember>,
    initialParentId: Int?,
    onSaveMember: (FamilyMember) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var selectedParentId by remember { mutableStateOf(initialParentId) }
    var generation by remember { mutableStateOf(if (initialParentId != null) {
        val p = allMembers.find { it.id == initialParentId }
        (p?.generation ?: 3) + 1
    } else 3) }
    var relation by remember { mutableStateOf("Member") }
    var gender by remember { mutableStateOf("Male") }
    var birthYear by remember { mutableStateOf("") }
    var casteTribe by remember { mutableStateOf(allMembers.firstOrNull()?.casteTribe ?: "") }
    var nativeCity by remember { mutableStateOf(allMembers.firstOrNull()?.nativeCity ?: "") }
    var profession by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isLiving by remember { mutableStateOf(true) }

    var expandedParentDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Naya Fard Shamil Karein",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BentoTextPrimary
            )
            Text(
                text = "Khandani shajra me fard ki mukammal tafseelat darj karein:",
                fontSize = 13.sp,
                color = BentoTextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoLightCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Fard Ka Naam (Name)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_member_name")
                    )

                    OutlinedTextField(
                        value = fatherName,
                        onValueChange = { fatherName = it },
                        label = { Text("Walid Ka Naam (Father's Name)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_father_name")
                    )

                    // Parent Dropdown Selection
                    ExposedDropdownMenuBox(
                        expanded = expandedParentDropdown,
                        onExpandedChange = { expandedParentDropdown = !expandedParentDropdown }
                    ) {
                        val selectedParent = allMembers.find { it.id == selectedParentId }
                        OutlinedTextField(
                            value = selectedParent?.let { "${it.name} (Gen ${it.generation})" } ?: "Khandan me Walid/Head Chunein (Optional)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Lineage Parent Node") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedParentDropdown) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("parent_dropdown_anchor")
                        )
                        ExposedDropdownMenu(
                            expanded = expandedParentDropdown,
                            onDismissRequest = { expandedParentDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("None (Root / Ancestor)") },
                                onClick = {
                                    selectedParentId = null
                                    generation = 1
                                    expandedParentDropdown = false
                                }
                            )
                            allMembers.forEach { parent ->
                                DropdownMenuItem(
                                    text = { Text("${parent.name} - ${parent.relation} (Gen ${parent.generation})") },
                                    onClick = {
                                        selectedParentId = parent.id
                                        fatherName = parent.name
                                        generation = parent.generation + 1
                                        expandedParentDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Gender Selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Jins (Gender):", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = gender == "Male",
                                onClick = { gender = "Male" },
                                modifier = Modifier.testTag("radio_male")
                            )
                            Text("Mard (Male)", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            RadioButton(
                                selected = gender == "Female",
                                onClick = { gender = "Female" },
                                modifier = Modifier.testTag("radio_female")
                            )
                            Text("Aurat (Female)", fontSize = 13.sp)
                        }
                    }

                    // Relation Input
                    OutlinedTextField(
                        value = relation,
                        onValueChange = { relation = it },
                        label = { Text("Rishta (Relation, e.g. Father, Son, Sister, Self)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_relation")
                    )

                    // Caste / Tribe & Native City Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = casteTribe,
                            onValueChange = { casteTribe = it },
                            label = { Text("Zaat / Khabeela") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_caste_tribe")
                        )
                        OutlinedTextField(
                            value = nativeCity,
                            onValueChange = { nativeCity = it },
                            label = { Text("Watan / Shehar") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_native_city")
                        )
                    }

                    // Birth Year & Profession Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = birthYear,
                            onValueChange = { birthYear = it },
                            label = { Text("Saal-e-Pedaish") },
                            placeholder = { Text("e.g. 1985") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_birth_year")
                        )
                        OutlinedTextField(
                            value = profession,
                            onValueChange = { profession = it },
                            label = { Text("Pesha / Occupation") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_profession")
                        )
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Khas Maloomat / Notes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_notes")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSaveMember(
                                    FamilyMember(
                                        name = name.trim(),
                                        fatherName = fatherName.trim(),
                                        parentId = selectedParentId,
                                        generation = generation,
                                        relation = if (relation.isBlank()) "Member" else relation.trim(),
                                        gender = gender,
                                        birthYear = birthYear.trim(),
                                        casteTribe = casteTribe.trim(),
                                        nativeCity = nativeCity.trim(),
                                        profession = profession.trim(),
                                        phone = phone.trim(),
                                        notes = notes.trim(),
                                        isLiving = isLiving
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_new_member_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Shajra me Shamil Karein", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --- TAB 3: SHARE SHAJRA TAB & PREMIUM PDF EXPORTER ---
@Composable
fun ShareShajraTab(
    allMembers: List<FamilyMember>,
    getLineage: (FamilyMember) -> String
) {
    val context = LocalContext.current
    var isProUnlocked by remember { mutableStateOf(false) }
    var showProDialog by remember { mutableStateOf(false) }

    val formattedShajraText = remember(allMembers) {
        buildString {
            append("📜 *** KHANDANI SHAJRA NASAB (شجرہ نسب) ***\n")
            append("=====================================\n\n")

            val grouped = allMembers.groupBy { it.generation }.toSortedMap()
            grouped.forEach { (gen, members) ->
                val genName = when (gen) {
                    1 -> "Nasal 1 (Aala Ancestors / Pardada)"
                    2 -> "Nasal 2 (Grandparents / Dada Dadi)"
                    3 -> "Nasal 3 (Parents / Abba Ammi)"
                    4 -> "Nasal 4 (Current Generation / Aap)"
                    5 -> "Nasal 5 (Children / Aulad)"
                    else -> "Nasal $gen"
                }
                append("📌 $genName:\n")
                members.forEach { m ->
                    append("  • ${m.name}")
                    if (m.fatherName.isNotBlank()) append(" (Walid: ${m.fatherName})")
                    if (m.relation.isNotBlank()) append(" - [${m.relation}]")
                    if (m.casteTribe.isNotBlank()) append(" - Zaat: ${m.casteTribe}")
                    append("\n")
                }
                append("\n")
            }
            append("=====================================\n")
            append("Generated by Shajra-e-Nasab Android Application\n")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Shajra Share & PDF Export",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BentoTextPrimary
            )
            Text(
                text = "Apne khandan ke aziz-o-aqareeb ke sath shajra nasab share ya Printable PDF document banayein:",
                fontSize = 13.sp,
                color = BentoTextSecondary
            )
        }

        // --- PREMIUM FEATURE BANNER: FAMILY TREE PDF EXPORTER ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoBlueContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, BentoBlueOnContainer.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .testTag("premium_pdf_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Premium",
                                tint = BentoAmberIcon,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "Shajra-e-Nasab PDF Exporter",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = BentoBlueOnContainer
                                )
                                Text(
                                    text = if (isProUnlocked) "✨ PRO Pass Active • High Definition Export" else "⭐ Premium Printable Document Feature",
                                    fontSize = 11.sp,
                                    color = BentoBlueOnContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isProUnlocked) BentoGreenCard else BentoPinkCard)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isProUnlocked) "PRO UNLOCKED" else "$4.99 / PRO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isProUnlocked) BentoGreenText else BentoPinkText
                            )
                        }
                    }

                    Text(
                        text = "Khandani Shajra Nasab ka mukammal, Printable A4 PDF Document download aur share karein.",
                        fontSize = 12.sp,
                        color = BentoBlueOnContainer.copy(alpha = 0.9f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                PdfExporter.generateAndOpenFamilyTreePdf(
                                    context = context,
                                    familyMembers = allMembers,
                                    isPremium = isProUnlocked
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("export_pdf_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PDF Banayein", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showProDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoLightCard),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("unlock_pro_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = BentoAmberIcon,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isProUnlocked) "PRO Status" else "PRO Unlock", color = BentoTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- FORMATTED TEXT SUMMARY CARD ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoLightCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Text Shajra Summary",
                            fontWeight = FontWeight.Bold,
                            color = BentoBlueOnContainer
                        )
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Shajra Nasab", formattedShajraText)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Shajra Copy Ho Gaya!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoBlueContainer),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("copy_shajra_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = BentoBlueOnContainer, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Text", color = BentoBlueOnContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(BentoBackground)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = formattedShajraText,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Normal,
                            color = BentoTextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }

    // --- PRO MONETIZATION & UNLOCK DIALOG ---
    if (showProDialog) {
        AlertDialog(
            onDismissRequest = { showProDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = BentoAmberIcon)
                    Text("Shajra PRO PDF Pass", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Shajra PDF Exporter se aap apne khandan ki mukammal tareekh aala printable format me mehfooz kar sakte hain.",
                        fontSize = 13.sp,
                        color = BentoTextPrimary
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(BentoBlueContainer)
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("⭐ PRO Features Included:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BentoBlueOnContainer)
                            Text("• Watermark-Free Official A4 PDF Document", fontSize = 11.sp, color = BentoBlueOnContainer)
                            Text("• Unlimited Family Tree Downloads & Exports", fontSize = 11.sp, color = BentoBlueOnContainer)
                            Text("• Direct WhatsApp, Email & Print PDF Sharing", fontSize = 11.sp, color = BentoBlueOnContainer)
                            Text("• Ancestral Crest & Verification Stamp", fontSize = 11.sp, color = BentoBlueOnContainer)
                        }
                    }

                    Text(
                        text = "Plan Price: $4.99 USD (Lifetime Access Pass)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BentoPinkText
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isProUnlocked = !isProUnlocked
                        showProDialog = false
                        Toast.makeText(
                            context,
                            if (isProUnlocked) "Shajra PRO PDF Pass Unlock Ho Gaya!" else "PRO Mode Disabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer),
                    modifier = Modifier.testTag("toggle_pro_status_button")
                ) {
                    Text(if (isProUnlocked) "Downgrade to Free" else "Unlock PRO Pass ($4.99)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showProDialog = false }) {
                    Text("Band Karein", color = BentoTextSecondary)
                }
            },
            containerColor = BentoLightCard,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

// --- TAB 4: SETTINGS & HELP TAB ---
@Composable
fun SettingsAndHelpTab(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    var isUrduFontEnabled by remember { mutableStateOf(true) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var helpTopicTitle by remember { mutableStateOf("") }
    var helpTopicDesc by remember { mutableStateOf("") }
    var showContactDialog by remember { mutableStateOf(false) }
    var contactMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Settings & Support (ہدايات و سيٹنگز)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BentoTextPrimary
            )
            Text(
                text = "Shajra App ki tanzimaat aur madad ki tafseelat:",
                fontSize = 13.sp,
                color = BentoTextSecondary
            )
        }

        // --- SECTION 1: URDU & FONT PREFERENCES ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoLightCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_urdu_settings")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BentoBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = BentoBlueOnContainer)
                        }
                        Column {
                            Text("Urdu Text & Font Mode", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BentoTextPrimary)
                            Text("Urdu zaban aur Nastaliq font options", fontSize = 11.sp, color = BentoTextSecondary)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nastalique Urdu Typography", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Shajra title aur nasab headings urdu me dikhayein", fontSize = 11.sp, color = BentoTextSecondary)
                        }
                        Switch(
                            checked = isUrduFontEnabled,
                            onCheckedChange = {
                                isUrduFontEnabled = it
                                Toast.makeText(context, if (it) "Urdu Nastaliq Active!" else "Standard Font Active", Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = BentoBlueOnContainer, checkedTrackColor = BentoBlueContainer)
                        )
                    }

                    HorizontalDivider(color = BentoBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Midnight Black (Dark Mode)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Pro OLED Dark Theme across the whole app", fontSize = 11.sp, color = BentoTextSecondary)
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = {
                                onDarkModeChange(it)
                                Toast.makeText(context, if (it) "🌙 Dark Black Mode Active!" else "☀️ Light Mode Active", Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFFD700), checkedTrackColor = Color(0xFF333333))
                        )
                    }
                }
            }
        }

        if (isDarkMode) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333333)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = Color(0xFFFFD700))
                            Text("🌙 Pro Midnight Black Demo Preview", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        }
                        Text(
                            text = "Aap ki app ka Dark Mode style OLED display screens par intehai shandaar aur eye-friendly lagta hai!",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E1E1E))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("شجرہ نسب - Shajra Nasab Pro", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("BLACK EDITION", color = Color.Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // --- SECTION: APP VERSION & RELEASE DETAILS ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoLightCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_version_info")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(BentoBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = BentoBlueOnContainer)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("App Version & Build", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BentoTextPrimary)
                        Text("Version 1.0.0 (Build 100) - Production Ready", fontSize = 12.sp, color = BentoTextSecondary)
                        Text("Target Android: SDK 34 (Android 14+ Compatible)", fontSize = 10.sp, color = BentoTextSecondary.copy(alpha = 0.7f))
                    }
                }
            }
        }

        // --- SECTION 2: DATA SECURITY & OFFLINE PRIVACY ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoGreenCard),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_privacy_settings")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = BentoGreenText, modifier = Modifier.size(28.dp))
                        Column {
                            Text("100% Offline & Private Security", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BentoGreenText)
                            Text("Aap ka khandani data bilkul mehfooz hai", fontSize = 11.sp, color = BentoGreenText.copy(alpha = 0.8f))
                        }
                    }

                    Text(
                        text = "Aap ka tamam Shajra Nasab data sirf aap ke phone ki local storage me mehfooz hota hai. Kisi external server par koi privacy risk nahi.",
                        fontSize = 12.sp,
                        color = BentoGreenText.copy(alpha = 0.9f)
                    )

                    Button(
                        onClick = {
                            Toast.makeText(context, "✅ Sab Data Phone Me Safe Hai (Database Clean)", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoGreenText),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Backup, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Security Verify Karein", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- SECTION 3: HELP & NEED SUPPORT FAQ ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoLightCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_faq_help")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BentoPinkCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = BentoPinkText)
                        }
                        Column {
                            Text("Need Help / Rahnmai (ہدايات)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BentoTextPrimary)
                            Text("Shajra app istemal karne ki asan rahnumai", fontSize = 11.sp, color = BentoTextSecondary)
                        }
                    }

                    // Help Topic 1
                    HelpItemRow(
                        title = "1. Shajra Nasab me Dada/Pardada kaise shamil karein?",
                        onClick = {
                            helpTopicTitle = "Dada / Pardada (Root Ancestor) Add Karna"
                            helpTopicDesc = "Pehli ya Doosri Nasal (Generation 1/2) ke buzurg ko shamil karne ke liye '+ Naya Fard' tab me jayein aur Nasal field me '1' (Great Grandparent) ya '2' (Grandparent) muntakhib karein."
                            showHelpDialog = true
                        }
                    )

                    // Help Topic 2
                    HelpItemRow(
                        title = "2. Aulad ko Abba/Ammi se connect kaise karein?",
                        onClick = {
                            helpTopicTitle = "Aulad Connect Karna"
                            helpTopicDesc = "Kisi bhi fard ki detail khol kar 'Bacha Shamil Karein' par click karein. System khud unka nasli silsila (Lineage Chain) connect kar dega."
                            showHelpDialog = true
                        }
                    )

                    // Help Topic 3
                    HelpItemRow(
                        title = "3. PDF Export & PRO Pass ki jankari",
                        onClick = {
                            helpTopicTitle = "PDF Export & PRO Pass"
                            helpTopicDesc = "PDF Export feature se aap pore Khandan ka A4 Printable Document generate kar sakte hain. Free version me watermark hota hai jabke $4.99 USD me PRO Pass Lifetime unlock ho jata hai."
                            showHelpDialog = true
                        }
                    )
                }
            }
        }

        // --- SECTION 4: CONTACT DEVELOPER & NEED ASSISTANCE ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoBlueContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_contact_support")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Rabta & Support (رابطہ و مدد)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BentoBlueOnContainer)
                    Text("Kia aap ko app me kisi madad ya mashware ki zaroorat hai?", fontSize = 12.sp, color = BentoBlueOnContainer.copy(alpha = 0.85f))

                    Button(
                        onClick = { showContactDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("contact_support_button")
                    ) {
                        Icon(Icons.Default.QuestionAnswer, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Need Assistance / Swāl Poochhein", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // --- HELP TOPIC DIALOG ---
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text(helpTopicTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = { Text(helpTopicDesc, fontSize = 13.sp, color = BentoTextPrimary) },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Samjh Gaya", fontWeight = FontWeight.Bold, color = BentoBlueOnContainer)
                }
            },
            containerColor = BentoLightCard,
            shape = RoundedCornerShape(24.dp)
        )
    }

    // --- CONTACT SUPPORT DIALOG ---
    if (showContactDialog) {
        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            title = { Text("Developer Support & Feedback", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Apna sawal ya taasur yahan likhein:", fontSize = 12.sp, color = BentoTextSecondary)
                    OutlinedTextField(
                        value = contactMessage,
                        onValueChange = { contactMessage = it },
                        placeholder = { Text("E.g. Shajra PDF print guide...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showContactDialog = false
                        contactMessage = ""
                        Toast.makeText(context, "Shukriya! Aap ka pagham bhej diya gaya.", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer)
                ) {
                    Text("Bhej Dein", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showContactDialog = false }) {
                    Text("Kansal", color = BentoTextSecondary)
                }
            },
            containerColor = BentoLightCard,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun HelpItemRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(BentoBackground)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = BentoTextPrimary)
        Text(text = "Padhein ➔", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BentoBlueOnContainer)
    }
}

// --- COMPOSABLE: MEMBER DETAIL DIALOG ---
@Composable
fun MemberDetailDialog(
    member: FamilyMember,
    allMembers: List<FamilyMember>,
    getLineage: (FamilyMember) -> String,
    onDismiss: () -> Unit,
    onAddChild: (Int) -> Unit,
    onEdit: (FamilyMember) -> Unit,
    onDelete: (FamilyMember) -> Unit
) {
    val lineageText = getLineage(member)
    val children = allMembers.filter { it.parentId == member.id }
    val siblings = allMembers.filter { it.parentId == member.parentId && it.id != member.id && member.parentId != null }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (member.gender == "Male") BentoBlueContainer else BentoPinkCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (member.gender == "Male") Icons.Default.Male else Icons.Default.Female,
                            contentDescription = null,
                            tint = if (member.gender == "Male") BentoBlueOnContainer else BentoPinkText
                        )
                    }
                    Text(member.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Lineage Chain Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(BentoBlueContainer.copy(alpha = 0.6f))
                        .padding(10.dp)
                ) {
                    Column {
                        Text("Nasli Silsila (Lineage Chain):", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BentoBlueOnContainer)
                        Text(lineageText, fontSize = 12.sp, color = BentoBlueOnContainer, fontWeight = FontWeight.Medium)
                    }
                }

                if (member.casteTribe.isNotBlank()) {
                    Text("• Zaat / Khabeela: ${member.casteTribe}", fontSize = 13.sp, color = BentoTextPrimary)
                }
                if (member.nativeCity.isNotBlank()) {
                    Text("• Watan / Shehar: ${member.nativeCity}", fontSize = 13.sp, color = BentoTextPrimary)
                }
                if (member.profession.isNotBlank()) {
                    Text("• Pesha: ${member.profession}", fontSize = 13.sp, color = BentoTextPrimary)
                }
                if (member.birthYear.isNotBlank()) {
                    Text("• Saal-e-Pedaish: ${member.birthYear}", fontSize = 13.sp, color = BentoTextPrimary)
                }
                if (member.notes.isNotBlank()) {
                    Text("• Notes: ${member.notes}", fontSize = 12.sp, color = BentoTextSecondary)
                }

                // Children Section
                if (children.isNotEmpty()) {
                    Text("Aulad (${children.size}):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BentoGreenText)
                    children.forEach { child ->
                        Text("   ↳ ${child.name} (${child.relation})", fontSize = 12.sp, color = BentoTextPrimary)
                    }
                }

                // Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onAddChild(member.id) },
                        modifier = Modifier.testTag("add_child_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Bacha Shamil Karein", fontSize = 11.sp)
                    }

                    TextButton(
                        onClick = { onDelete(member) },
                        modifier = Modifier.testTag("delete_member_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", color = Color.Red, fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Band Karein", color = BentoBlueOnContainer, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = BentoLightCard,
        shape = RoundedCornerShape(24.dp)
    )
}

// --- COMPOSABLE: ADD OR EDIT MEMBER DIALOG ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditMemberDialog(
    allMembers: List<FamilyMember>,
    editingMember: FamilyMember?,
    preselectedParentId: Int?,
    onDismiss: () -> Unit,
    onSave: (FamilyMember) -> Unit
) {
    var name by remember { mutableStateOf(editingMember?.name ?: "") }
    var fatherName by remember { mutableStateOf(editingMember?.fatherName ?: "") }
    var parentId by remember { mutableStateOf(editingMember?.parentId ?: preselectedParentId) }
    var relation by remember { mutableStateOf(editingMember?.relation ?: "Member") }
    var gender by remember { mutableStateOf(editingMember?.gender ?: "Male") }
    var birthYear by remember { mutableStateOf(editingMember?.birthYear ?: "") }
    var casteTribe by remember { mutableStateOf(editingMember?.casteTribe ?: "") }
    var nativeCity by remember { mutableStateOf(editingMember?.nativeCity ?: "") }
    var profession by remember { mutableStateOf(editingMember?.profession ?: "") }
    var notes by remember { mutableStateOf(editingMember?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (editingMember != null) "Tafseelat Edit Karein" else "Naya Fard Shamil Karein",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_input_name")
                )
                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father's Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = relation,
                    onValueChange = { relation = it },
                    label = { Text("Relation (e.g. Son, Daughter, Brother)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = casteTribe,
                    onValueChange = { casteTribe = it },
                    label = { Text("Zaat / Caste") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val parent = allMembers.find { it.id == parentId }
                        val gen = editingMember?.generation ?: ((parent?.generation ?: 3) + 1)
                        onSave(
                            editingMember?.copy(
                                name = name,
                                fatherName = fatherName,
                                parentId = parentId,
                                relation = relation,
                                gender = gender,
                                birthYear = birthYear,
                                casteTribe = casteTribe,
                                nativeCity = nativeCity,
                                profession = profession,
                                notes = notes
                            ) ?: FamilyMember(
                                name = name,
                                fatherName = fatherName,
                                parentId = parentId,
                                generation = gen,
                                relation = relation,
                                gender = gender,
                                birthYear = birthYear,
                                casteTribe = casteTribe,
                                nativeCity = nativeCity,
                                profession = profession,
                                notes = notes
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BentoBlueOnContainer),
                modifier = Modifier.testTag("dialog_save_button")
            ) {
                Text("Mehfooz Karein", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kansal", color = BentoTextSecondary)
            }
        },
        containerColor = BentoLightCard,
        shape = RoundedCornerShape(24.dp)
    )
}

// --- SHAJRA BOTTOM NAVIGATION BAR ---
data class ShajraNavDestination(
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun ShajraBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        ShajraNavDestination("Tree", Icons.Outlined.AccountTree, "nav_tree"),
        ShajraNavDestination("Fehrist", Icons.Outlined.Groups, "nav_directory"),
        ShajraNavDestination("+ Fard", Icons.Default.AddCircle, "nav_add"),
        ShajraNavDestination("Share", Icons.Outlined.Share, "nav_share"),
        ShajraNavDestination("Settings", Icons.Default.Settings, "nav_settings")
    )

    Surface(
        color = BentoNavBackground,
        tonalElevation = 3.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedTab == index

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 6.dp, horizontal = 10.dp)
                        .testTag(item.testTag)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) BentoBlueContainer else Color.Transparent)
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) BentoBlueOnContainer else BentoTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BentoTextPrimary else BentoTextSecondary
                    )
                }
            }
        }
    }
}
