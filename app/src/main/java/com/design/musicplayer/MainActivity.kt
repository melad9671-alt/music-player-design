package com.design.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- لوحة الألوان المطابقة للصورة ---
val BgDark = Color(0xFF0A0812)
val CardDark = Color(0xFF151221)
val CardBorder = Color(0xFF28223D)
val NeonPurple = Color(0xFF8B42FF)
val NeonPink = Color(0xFFD65DB1)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF9E98B3)
val GoldCrown = Color(0xFFFFC837)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        setContent {
            DesignMusicPlayerApp()
        }
    }
}

enum class ScreenRoute {
    HOME, LIBRARY, ALBUMS, ARTISTS, NOW_PLAYING
}

@Composable
fun DesignMusicPlayerApp() {
    var currentRoute by remember { mutableStateOf(ScreenRoute.HOME) }
    var previousRoute by remember { mutableStateOf(ScreenRoute.HOME) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // إضاءة خلفية خفيفة جداً في أعلى الشاشة
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF231347).copy(alpha = 0.55f),
                            BgDark
                        )
                    )
                )
        )

        if (currentRoute == ScreenRoute.NOW_PLAYING) {
            NowPlayingScreen(
                onBack = { currentRoute = previousRoute }
            )
        } else {
            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, BgDark.copy(alpha = 0.95f), BgDark)
                                )
                            )
                            .navigationBarsPadding()
                    ) {
                        MiniPlayerBar(
                            onClick = {
                                previousRoute = currentRoute
                                currentRoute = ScreenRoute.NOW_PLAYING
                            }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        BottomNavBar(
                            currentRoute = currentRoute,
                            onSelectRoute = { currentRoute = it }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentRoute) {
                        ScreenRoute.HOME -> HomeScreen(
                            onNavigate = { currentRoute = it }
                        )
                        ScreenRoute.LIBRARY -> LibraryScreen(
                            onNavigate = { currentRoute = it }
                        )
                        ScreenRoute.ALBUMS -> AlbumsScreen()
                        ScreenRoute.ARTISTS -> ArtistsScreen()
                        else -> {}
                    }
                }
            }
        }
    }
}

// ==========================================
// 1. شاشة الرئيسية (HOME SCREEN)
// ==========================================
@Composable
fun HomeScreen(onNavigate: (ScreenRoute) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // الهيدر العلوي (اللوجو + البحث + الإعدادات)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppLogoIcon(size = 44.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                                    append("De")
                                }
                                withStyle(
                                    SpanStyle(
                                        brush = Brush.horizontalGradient(listOf(NeonPink, NeonPurple)),
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("sign")
                                }
                            },
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Music Player",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
        }

        // البانر البنفسجي مع أسطوانة الفينيل (Enjoy Your Music)
        item {
            HeroBannerCard()
            Spacer(modifier = Modifier.height(26.dp))
        }

        // قسم Quick Access
        item {
            SectionHeader(title = "Quick Access", onSeeAll = { onNavigate(ScreenRoute.LIBRARY) })
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessBox(
                    title = "Songs",
                    icon = Icons.Rounded.MusicNote,
                    gradientColors = listOf(Color(0xFFD65DB1), Color(0xFF8B42FF)),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(ScreenRoute.LIBRARY) }
                )
                QuickAccessBox(
                    title = "Albums",
                    icon = Icons.Rounded.Album,
                    gradientColors = listOf(Color(0xFF9D6BFF), Color(0xFF5A32E6)),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(ScreenRoute.ALBUMS) }
                )
                QuickAccessBox(
                    title = "Artists",
                    icon = Icons.Rounded.Person,
                    gradientColors = listOf(Color(0xFFB56BFF), Color(0xFF7A35F0)),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(ScreenRoute.ARTISTS) }
                )
                QuickAccessBox(
                    title = "Playlists",
                    icon = Icons.AutoMirrored.Filled.QueueMusic,
                    gradientColors = listOf(Color(0xFFC45BFF), Color(0xFF6B2CF5)),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(ScreenRoute.LIBRARY) }
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
        }

        // قسم Recently Played
        item {
            SectionHeader(title = "Recently Played", onSeeAll = {})
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(recentTracks) { track ->
                    SquareAlbumCard(
                        title = track.title,
                        subtitle = track.artist,
                        artType = track.artType,
                        width = 128.dp
                    )
                }
            }
        }
    }
}

// ==========================================
// 2. شاشة المكتبة (LIBRARY SCREEN)
// ==========================================
@Composable
fun LibraryScreen(onNavigate: (ScreenRoute) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp)
    ) {
        item {
            TopTitleBar(title = "Library")
            Spacer(modifier = Modifier.height(18.dp))
        }

        val itemsList = listOf(
            Triple("Songs", "All your songs", Icons.Rounded.MusicNote) to ScreenRoute.LIBRARY,
            Triple("Albums", "All albums", Icons.Rounded.Album) to ScreenRoute.ALBUMS,
            Triple("Artists", "All artists", Icons.Rounded.Person) to ScreenRoute.ARTISTS,
            Triple("Playlists", "Your playlists", Icons.AutoMirrored.Filled.QueueMusic) to ScreenRoute.LIBRARY
        )

        items(itemsList) { (info, route) ->
            LibraryCategoryRow(
                title = info.first,
                subtitle = info.second,
                icon = info.third,
                onClick = { onNavigate(route) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
            SectionHeader(title = "Recently Added", onSeeAll = {})
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(recentlyAddedTracks) { track ->
                    ArtworkBox(artType = track.artType, modifier = Modifier.size(112.dp))
                }
            }
        }
    }
}

// ==========================================
// 3. شاشة الألبومات (ALBUMS SCREEN)
// ==========================================
@Composable
fun AlbumsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 14.dp)
    ) {
        TopTitleBar(title = "Albums")
        Spacer(modifier = Modifier.height(18.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(albumsList) { album ->
                Column {
                    ArtworkBox(
                        artType = album.artType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = album.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = album.artist,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// ==========================================
// 4. شاشة الفنانين (ARTISTS SCREEN)
// ==========================================
@Composable
fun ArtistsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp)
    ) {
        item {
            TopTitleBar(title = "Artists")
            Spacer(modifier = Modifier.height(18.dp))
        }

        items(artistsList) { artist ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                color = CardDark,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArtistAvatar(colors = artist.colors, initials = artist.initials)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = artist.name,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = artist.songsCount,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}

// ==========================================
// 5. شاشة التشغيل الكاملة (NOW PLAYING)
// ==========================================
@Composable
fun NowPlayingScreen(onBack: () -> Unit) {
    var isPlaying by remember { mutableStateOf(true) }
    var sliderProgress by remember { mutableFloatStateOf(0.43f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // إضاءة بنفسجية متوهجة في منتصف الشاشة خلف الأغنية
        Box(
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.Center)
                .offset(y = (-40).dp)
                .blur(90.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.45f),
                            NeonPink.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // الشريط العلوي (سهم للأسفل + 3 نقاط)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Minimize",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // غلاف الألبوم الكبير (التاج النيون المضيء)
            ArtworkBox(
                artType = ArtType.CROWN_NEON,
                modifier = Modifier
                    .fillMaxWidth(0.84f)
                    .aspectRatio(1f),
                cornerRadius = 26.dp
            )

            // اسم الأغنية والفنان
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Pink Venom",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "BLACKPINK",
                    color = Color(0xFFB57BFF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.8.sp
                )
            }

            // شريط التقدم والوقت
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = sliderProgress,
                    onValueChange = { sliderProgress = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFD08BFF),
                        activeTrackColor = NeonPurple,
                        inactiveTrackColor = Color(0xFF2B2440)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "01:24", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "03:13", color = TextSecondary, fontSize = 12.sp)
                }
            }

            // أزرار التحكم (Shuffle, Previous, Play/Pause, Next, Repeat)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = "Shuffle",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFA259FF), Color(0xFF6E2CF2))
                            )
                        )
                        .clickable { isPlaying = !isPlaying },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.Repeat,
                    contentDescription = "Repeat",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // الأيقونات السفلية الصغيرة
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Outlined.SpeakerGroup,
                    contentDescription = "Devices",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                    contentDescription = "Queue",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==========================================
// مكونات الواجهة المشتركة (UI COMPONENTS)
// ==========================================

@Composable
fun HeroBannerCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(165.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3A1C71),
                        Color(0xFF5D26C1),
                        Color(0xFF1F1135)
                    )
                )
            )
            .border(1.dp, Color(0xFF6B42C2).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
    ) {
        // رسم أسطوانة الفينيل والموجات الضوئية بالـ Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val vinylCenter = Offset(size.width * 0.78f, size.height * 0.52f)
            val maxRadius = size.height * 0.65f

            // هالة مضيئة خلف الأسطوانة
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFA855F7).copy(alpha = 0.6f), Color.Transparent),
                    center = vinylCenter,
                    radius = maxRadius * 1.25f
                ),
                center = vinylCenter,
                radius = maxRadius * 1.25f
            )

            // جسم الأسطوانة الأسود اللامع
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF120C1C),
                        Color(0xFF4C2889),
                        Color(0xFF0E0916),
                        Color(0xFF6E3CBC),
                        Color(0xFF120C1C)
                    ),
                    center = vinylCenter
                ),
                center = vinylCenter,
                radius = maxRadius
            )

            // حلقات الأسطوانة الدقيقة
            for (i in 1..4) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    center = vinylCenter,
                    radius = maxRadius * (0.45f + i * 0.11f),
                    style = Stroke(width = 1.5f)
                )
            }

            // مركز الأسطوانة البنفسجي
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFD65DB1), Color(0xFF7B2CBF)),
                    center = vinylCenter,
                    radius = maxRadius * 0.34f
                ),
                center = vinylCenter,
                radius = maxRadius * 0.34f
            )
            drawCircle(
                color = BgDark,
                center = vinylCenter,
                radius = maxRadius * 0.09f
            )
        }

        // النصوص على يسار البانر
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = "Enjoy Your Music",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "With Design ",
                    color = Color(0xFFD6C9F0),
                    fontSize = 13.sp
                )
                Text(text = "💜", fontSize = 12.sp)
            }
        }

        // زر التشغيل الدائري أسفل يمين البانر
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFFA259FF), Color(0xFF6E2CF2)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun QuickAccessBox(
    title: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(92.dp)
            .clickable { onClick() },
        color = CardDark,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
fun LibraryCategoryRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = CardDark,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF211A36)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFFB56BFF),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}

@Composable
fun MiniPlayerBar(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable { onClick() },
        color = Color(0xFF1B162B).copy(alpha = 0.96f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color(0xFF332952))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArtworkBox(
                artType = ArtType.CROWN_NEON,
                modifier = Modifier.size(42.dp),
                cornerRadius = 10.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pink Venom",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "BLACKPINK",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Prev",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(NeonPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentRoute: ScreenRoute,
    onSelectRoute: (ScreenRoute) -> Unit
) {
    val navItems = listOf(
        Triple("Home", Icons.Rounded.Home, ScreenRoute.HOME),
        Triple("Library", Icons.Rounded.MusicNote, ScreenRoute.LIBRARY),
        Triple("Search", Icons.Outlined.Search, ScreenRoute.ALBUMS),
        Triple("Settings", Icons.Outlined.Settings, ScreenRoute.ARTISTS)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEach { (label, icon, route) ->
            val isSelected = currentRoute == route ||
                    (route == ScreenRoute.LIBRARY && (currentRoute == ScreenRoute.ALBUMS || currentRoute == ScreenRoute.ARTISTS))
            val activeColor = if (isSelected) Color(0xFFB56BFF) else TextSecondary

            Column(
                modifier = Modifier
                    .clickable { onSelectRoute(route) }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = activeColor,
                    modifier = Modifier.size(23.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = label,
                    color = activeColor,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.clickable { onSeeAll() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "See All",
                color = Color(0xFFB56BFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFB56BFF),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun TopTitleBar(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun AppLogoIcon(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF28174D), Color(0xFF130B24))
                )
            )
            .border(1.dp, Color(0xFF6B42C2).copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.MusicNote,
            contentDescription = null,
            tint = Color(0xFFC77DFF),
            modifier = Modifier.size(size * 0.62f)
        )
        // تاج ذهبي صغير أعلى النوتة الموسيقية
        Canvas(
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.TopCenter)
                .offset(y = 3.dp, x = (-2).dp)
        ) {
            val path = Path().apply {
                moveTo(0f, size.toPx())
                lineTo(0f, size.toPx() * 0.3f)
                lineTo(size.toPx() * 0.25f, size.toPx() * 0.6f)
                lineTo(size.toPx() * 0.5f, size.toPx() * 0.1f)
                lineTo(size.toPx() * 0.75f, size.toPx() * 0.6f)
                lineTo(size.toPx(), size.toPx() * 0.3f)
                lineTo(size.toPx(), size.toPx())
                close()
            }
            drawPath(path, color = GoldCrown)
        }
    }
}

// ==========================================
// رسومات الأغلفة الفنية (ARTWORKS & DATA)
// ==========================================

enum class ArtType {
    CROWN_NEON, SILHOUETTE_PURPLE, HEART_NEON, PROOF_SHIELD, SUNSET_MOUNTAIN, BLUE_SKY
}

@Composable
fun SquareAlbumCard(
    title: String,
    subtitle: String,
    artType: ArtType,
    width: Dp
) {
    Column(modifier = Modifier.width(width)) {
        ArtworkBox(
            artType = artType,
            modifier = Modifier.size(width)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            color = TextSecondary,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

@Composable
fun ArtworkBox(
    artType: ArtType,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp
) {
    val bgBrush = when (artType) {
        ArtType.CROWN_NEON -> Brush.radialGradient(listOf(Color(0xFF4A154B), Color(0xFF0B0612)))
        ArtType.SILHOUETTE_PURPLE -> Brush.verticalGradient(listOf(Color(0xFF7B2CBF), Color(0xFF1A0B2E)))
        ArtType.HEART_NEON -> Brush.radialGradient(listOf(Color(0xFF3C096C), Color(0xFF090510)))
        ArtType.PROOF_SHIELD -> Brush.verticalGradient(listOf(Color(0xFF2B2B36), Color(0xFF08080A)))
        ArtType.SUNSET_MOUNTAIN -> Brush.verticalGradient(listOf(Color(0xFFFF758F), Color(0xFF5A189A)))
        ArtType.BLUE_SKY -> Brush.verticalGradient(listOf(Color(0xFF48CAE4), Color(0xFF1D3557)))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(bgBrush)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        when (artType) {
            ArtType.CROWN_NEON -> {
                // رسم التاج النيون الوردي المشع
                Canvas(modifier = Modifier.fillMaxSize(0.65f)) {
                    val w = size.width
                    val h = size.height
                    val crownPath = Path().apply {
                        moveTo(w * 0.15f, h * 0.78f)
                        lineTo(w * 0.08f, h * 0.35f)
                        lineTo(w * 0.32f, h * 0.55f)
                        lineTo(w * 0.5f, h * 0.22f)
                        lineTo(w * 0.68f, h * 0.55f)
                        lineTo(w * 0.92f, h * 0.35f)
                        lineTo(w * 0.85f, h * 0.78f)
                        close()
                    }
                    drawPath(
                        path = crownPath,
                        color = Color(0xFFFF70D9),
                        style = Stroke(width = 5f, join = StrokeJoin.Round)
                    )
                    drawCircle(
                        color = Color(0xFFFFB3EC),
                        radius = 8f,
                        center = Offset(w * 0.5f, h * 0.52f)
                    )
                }
            }
            ArtType.HEART_NEON -> {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = Color(0xFFD65DB1),
                    modifier = Modifier.fillMaxSize(0.55f)
                )
            }
            ArtType.SILHOUETTE_PURPLE -> {
                Icon(
                    imageVector = Icons.Rounded.Headphones,
                    contentDescription = null,
                    tint = Color(0xFFE0AAFF),
                    modifier = Modifier.fillMaxSize(0.5f)
                )
            }
            ArtType.PROOF_SHIELD -> {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(Modifier.width(12.dp).height(46.dp).background(Color.White, RoundedCornerShape(2.dp)))
                    Box(Modifier.width(12.dp).height(46.dp).background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(2.dp)))
                }
            }
            else -> {}
        }
    }
}

@Composable
fun ArtistAvatar(colors: List<Color>, initials: String) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Brush.linearGradient(colors))
            .border(1.5.dp, Color.White.copy(alpha = 0.25f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

// --- البيانات المعروضة في الشاشات ---
data class TrackItem(val title: String, val artist: String, val artType: ArtType)
data class ArtistItem(val name: String, val songsCount: String, val initials: String, val colors: List<Color>)

val recentTracks = listOf(
    TrackItem("Dream Big", "BLACKPINK", ArtType.SILHOUETTE_PURPLE),
    TrackItem("Pink Venom", "BLACKPINK", ArtType.CROWN_NEON),
    TrackItem("Forever", "BTS", ArtType.HEART_NEON)
)

val recentlyAddedTracks = listOf(
    TrackItem("Pink Venom", "BLACKPINK", ArtType.CROWN_NEON),
    TrackItem("Dream Big", "BLACKPINK", ArtType.SILHOUETTE_PURPLE),
    TrackItem("Sunset", "IU", ArtType.SUNSET_MOUNTAIN)
)

val albumsList = listOf(
    TrackItem("BORN PINK", "BLACKPINK", ArtType.CROWN_NEON),
    TrackItem("Proof", "BTS", ArtType.PROOF_SHIELD),
    TrackItem("Lilac", "IU", ArtType.SUNSET_MOUNTAIN),
    TrackItem("Get Up", "NewJeans", ArtType.BLUE_SKY)
)

val artistsList = listOf(
    ArtistItem("BLACKPINK", "5 songs", "BP", listOf(Color(0xFFFF5E9C), Color(0xFF2B102F))),
    ArtistItem("BTS", "8 songs", "BTS", listOf(Color(0xFF9D4EDD), Color(0xFF240046))),
    ArtistItem("IU", "3 songs", "IU", listOf(Color(0xFF7209B7), Color(0xFF3A0CA3))),
    ArtistItem("NewJeans", "4 songs", "NJ", listOf(Color(0xFF4CC9F0), Color(0xFF4361EE)))
)
