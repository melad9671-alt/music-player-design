package com.design.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Purple = Color(0xFF9147FF)
private val Pink = Color(0xFFE05BB7)
private val Gold = Color(0xFFFFC857)

private data class Track(
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val art: String
)

private data class Artist(val name: String, val subtitle: String, val image: String)

private val tracks = listOf(
    Track("Pink Venom", "BLACKPINK", "BORN PINK", "3:06", "https://images.unsplash.com/photo-1516280440614-37939bbacd81?w=900"),
    Track("Dream Big", "NewJeans", "Dream Big", "3:24", "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=900"),
    Track("Forever", "IU", "The Collection", "3:41", "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=900"),
    Track("Midnight", "Jisoo", "ME", "2:58", "https://images.unsplash.com/photo-1506157786151-b8491531f063?w=900"),
    Track("Stay", "The Weekend", "After Hours", "3:52", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=900"),
    Track("Blue Hour", "TXT", "minisode", "3:18", "https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=900")
)

private val artists = listOf(
    Artist("BLACKPINK", "4 songs", "https://images.unsplash.com/photo-1524368638413-6e6b9e4f8f1b?w=700"),
    Artist("Jisoo", "3 songs", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=700"),
    Artist("NewJeans", "8 songs", "https://images.unsplash.com/photo-1529139574466-a303027c1d8b?w=700"),
    Artist("IU", "5 songs", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=700")
)

private enum class MusicTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("Home", Icons.Filled.Home),
    SEARCH("Search", Icons.Filled.Search),
    ALBUMS("Albums", Icons.Filled.Album),
    ARTISTS("Artists", Icons.Filled.Person),
    LIBRARY("Library", Icons.Filled.LibraryMusic)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MusicPlayerApp() }
    }
}

@Composable
private fun MusicPlayerApp() {
    var tab by remember { mutableStateOf(MusicTab.HOME) }
    var selectedTrack by remember { mutableStateOf(tracks.first()) }
    var playerOpen by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }

    val background = if (darkMode) Color(0xFF0A0812) else Color(0xFFF5F1FA)
    val surface = if (darkMode) Color(0xFF171323) else Color.White
    val primaryText = if (darkMode) Color.White else Color(0xFF211A2D)
    val secondaryText = if (darkMode) Color(0xFFAAA2BA) else Color(0xFF756B82)
    val scheme = if (darkMode) darkColorScheme(
        primary = Purple, background = background, surface = surface,
        onBackground = primaryText, onSurface = primaryText
    ) else lightColorScheme(
        primary = Purple, background = background, surface = surface,
        onBackground = primaryText, onSurface = primaryText
    )

    MaterialTheme(colorScheme = scheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = background) {
            if (playerOpen) {
                PlayerScreen(
                    track = selectedTrack,
                    darkMode = darkMode,
                    onBack = { playerOpen = false },
                    onNext = { selectedTrack = tracks[(tracks.indexOf(selectedTrack) + 1) % tracks.size] },
                    onPrevious = { selectedTrack = tracks[(tracks.indexOf(selectedTrack) - 1 + tracks.size) % tracks.size] },
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )
            } else {
                Scaffold(
                    containerColor = background,
                    bottomBar = {
                        Column {
                            MiniPlayer(selectedTrack, primaryText, secondaryText) { playerOpen = true }
                            NavigationBar(containerColor = surface, contentColor = primaryText) {
                                MusicTab.values().forEach { item ->
                                    NavigationBarItem(
                                        selected = tab == item,
                                        onClick = { tab = item },
                                        icon = { Icon(item.icon, contentDescription = item.label) },
                                        label = { Text(item.label, fontSize = 10.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.White,
                                            selectedTextColor = Purple,
                                            indicatorColor = Purple.copy(alpha = 0.28f),
                                            unselectedIconColor = secondaryText,
                                            unselectedTextColor = secondaryText
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { padding ->
                    when (tab) {
                        MusicTab.HOME -> HomeScreen(
                            padding, primaryText, secondaryText, surface,
                            onOpenPlayer = { selectedTrack = it; playerOpen = true },
                            onTab = { tab = it },
                            onSettings = { darkMode = !darkMode }
                        )
                        MusicTab.SEARCH -> SearchScreen(
                            padding, query, { query = it }, primaryText, secondaryText,
                            onTrack = { selectedTrack = it; playerOpen = true }
                        )
                        MusicTab.ALBUMS -> AlbumsScreen(padding, primaryText, secondaryText, onTrack = { selectedTrack = it; playerOpen = true })
                        MusicTab.ARTISTS -> ArtistsScreen(padding, primaryText, secondaryText)
                        MusicTab.LIBRARY -> LibraryScreen(padding, primaryText, secondaryText, surface, onTab = { tab = it }, onTrack = { selectedTrack = it; playerOpen = true })
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(
    padding: PaddingValues,
    text: Color,
    muted: Color,
    surface: Color,
    onOpenPlayer: (Track) -> Unit,
    onTab: (MusicTab) -> Unit,
    onSettings: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("GOOD EVENING", color = muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Enjoy your music", color = text, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
                }
                IconButton(onClick = onSettings) {
                    Icon(Icons.Filled.Settings, contentDescription = "Toggle theme", tint = text)
                }
                Box(
                    modifier = Modifier.size(42.dp).clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Purple, Pink))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.MusicNote, contentDescription = null, tint = Color.White)
                    Text("♛", color = Gold, fontSize = 11.sp, modifier = Modifier.align(Alignment.TopEnd))
                }
            }
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(26.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF422078), Color(0xFF171127), Color(0xFF9D3E91))))
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1516280440614-37939bbacd81?w=1200",
                    contentDescription = "Featured music artwork",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(26.dp))
                )
                Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xDD10091E), Color(0x3310091E), Color(0xAA24102E)))))
                Column(Modifier.align(Alignment.CenterStart).padding(22.dp)) {
                    Text("YOUR SOUNDTRACK", color = Color(0xFFE8CFFF), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Feel every
beat.", color = Color.White, fontSize = 30.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { onOpenPlayer(tracks.first()) }, colors = ButtonDefaults.buttonColors(containerColor = Purple), contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(5.dp))
                        Text("Listen now")
                    }
                }
                Icon(Icons.Filled.Album, contentDescription = null, tint = Color.White.copy(alpha = 0.75f), modifier = Modifier.align(Alignment.CenterEnd).padding(24.dp).size(66.dp))
            }
        }
        item {
            SectionTitle("Quick access", "Explore your music", text, muted)
            Spacer(Modifier.height(13.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickTile("Songs", Icons.Filled.MusicNote, Purple, Modifier.weight(1f)) { onTab(MusicTab.LIBRARY) }
                QuickTile("Albums", Icons.Filled.Album, Pink, Modifier.weight(1f)) { onTab(MusicTab.ALBUMS) }
                QuickTile("Artists", Icons.Filled.Person, Color(0xFF6D8BFF), Modifier.weight(1f)) { onTab(MusicTab.ARTISTS) }
                QuickTile("Playlists", Icons.Filled.QueueMusic, Color(0xFFB16AE8), Modifier.weight(1f)) { onTab(MusicTab.LIBRARY) }
            }
        }
        item {
            SectionTitle("Recently played", "Your latest favorites", text, muted)
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(tracks.take(5)) { track ->
                    Column(Modifier.width(132.dp).clickable { onOpenPlayer(track) }) {
                        Artwork(track.art, Modifier.fillMaxWidth().height(132.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(track.title, color = text, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(track.artist, color = muted, fontSize = 12.sp, maxLines = 1)
                    }
                }
            }
        }
        item {
            SectionTitle("Made for you", "Albums & collections", text, muted)
            Spacer(Modifier.height(12.dp))
            AlbumWideCard("BORN PINK", "BLACKPINK • Album", tracks[0].art, text, muted) { onOpenPlayer(tracks[0]) }
        }
    }
}

@Composable
private fun SearchScreen(padding: PaddingValues, query: String, onQuery: (String) -> Unit, text: Color, muted: Color, onTrack: (Track) -> Unit) {
    val filtered = tracks.filter { it.title.contains(query, true) || it.artist.contains(query, true) || it.album.contains(query, true) }
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(22.dp))
        Text("Search", color = text, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("Find songs, albums and artists", color = muted, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = query, onValueChange = onQuery, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("What do you want to play?") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { onQuery("") }) { Icon(Icons.Filled.Close, contentDescription = "Clear") } },
            shape = RoundedCornerShape(18.dp), singleLine = true
        )
        Spacer(Modifier.height(22.dp))
        Text(if (query.isBlank()) "Popular right now" else "Search results", color = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { track -> TrackRow(track, text, muted) { onTrack(track) } }
        }
    }
}

@Composable
private fun AlbumsScreen(padding: PaddingValues, text: Color, muted: Color, onTrack: (Track) -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(22.dp))
        Text("Albums", color = text, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("Your collection, in one place", color = muted, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(bottom = 20.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            items(tracks) { track ->
                Column(Modifier.clickable { onTrack(track) }) {
                    Artwork(track.art, Modifier.fillMaxWidth().aspectRatio(1f))
                    Spacer(Modifier.height(8.dp))
                    Text(track.album, color = text, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(track.artist, color = muted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ArtistsScreen(padding: PaddingValues, text: Color, muted: Color) {
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 22.dp, bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        item {
            Text("Artists", color = text, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("The voices behind your favorites", color = muted, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
        }
        items(artists) { artist ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).padding(12.dp)) {
                AsyncImage(model = artist.image, contentDescription = artist.name, contentScale = ContentScale.Crop, modifier = Modifier.size(66.dp).clip(CircleShape))
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(artist.name, color = text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(artist.subtitle, color = muted, fontSize = 12.sp)
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = muted)
            }
        }
    }
}

@Composable
private fun LibraryScreen(padding: PaddingValues, text: Color, muted: Color, surface: Color, onTab: (MusicTab) -> Unit, onTrack: (Track) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 22.dp, bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Your Library", color = text, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("Everything you love, together", color = muted, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            LibraryShortcut("Songs", "All your tracks", Icons.Filled.MusicNote, surface, text, muted) { onTrack(tracks.first()) }
            LibraryShortcut("Albums", "Browse your albums", Icons.Filled.Album, surface, text, muted) { onTab(MusicTab.ALBUMS) }
            LibraryShortcut("Artists", "Your favorite artists", Icons.Filled.Person, surface, text, muted) { onTab(MusicTab.ARTISTS) }
            LibraryShortcut("Playlists", "Your personal mixes", Icons.Filled.QueueMusic, surface, text, muted) { onTab(MusicTab.SEARCH) }
            Spacer(Modifier.height(12.dp))
            SectionTitle("Recently added", "Pick up where you left off", text, muted)
        }
        items(tracks) { track -> TrackRow(track, text, muted) { onTrack(track) } }
    }
}

@Composable
private fun PlayerScreen(track: Track, darkMode: Boolean, onBack: () -> Unit, onNext: () -> Unit, onPrevious: () -> Unit, primaryText: Color, secondaryText: Color) {
    var playing by remember { mutableStateOf(true) }
    var liked by remember { mutableStateOf(false) }
    var progress by remember(track) { mutableFloatStateOf(0.34f) }
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Back", tint = primaryText) }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("NOW PLAYING", color = secondaryText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("From your library", color = primaryText, fontSize = 13.sp)
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { liked = !liked }) { Icon(if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = "Favorite", tint = if (liked) Pink else primaryText) }
        }
        Spacer(Modifier.weight(1f))
        Artwork(track.art, Modifier.fillMaxWidth().aspectRatio(1f).padding(6.dp))
        Spacer(Modifier.height(30.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(track.title, color = primaryText, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                Text(track.artist, color = secondaryText, fontSize = 14.sp)
            }
            IconButton(onClick = { liked = !liked }) { Icon(if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = null, tint = if (liked) Pink else secondaryText) }
        }
        Slider(value = progress, onValueChange = { progress = it }, colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = secondaryText.copy(alpha = 0.3f)))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("1:12", color = secondaryText, fontSize = 11.sp)
            Text(track.duration, color = secondaryText, fontSize = 11.sp)
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) { Icon(Icons.Filled.Shuffle, contentDescription = "Shuffle", tint = secondaryText) }
            IconButton(onClick = onPrevious) { Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous", tint = primaryText, modifier = Modifier.size(34.dp)) }
            FilledIconButton(onClick = { playing = !playing }, modifier = Modifier.size(66.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = Purple)) {
                Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = "Play or pause", modifier = Modifier.size(34.dp))
            }
            IconButton(onClick = onNext) { Icon(Icons.Filled.SkipNext, contentDescription = "Next", tint = primaryText, modifier = Modifier.size(34.dp)) }
            IconButton(onClick = {}) { Icon(Icons.Filled.Repeat, contentDescription = "Repeat", tint = secondaryText) }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun MiniPlayer(track: Track, text: Color, muted: Color, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp).clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface).clickable(onClick = onClick).padding(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = track.art, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(43.dp).clip(RoundedCornerShape(10.dp)))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(track.title, color = text, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(track.artist, color = muted, fontSize = 11.sp, maxLines = 1)
        }
        Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Purple)
        IconButton(onClick = onClick) { Icon(Icons.Filled.PlayArrow, contentDescription = "Open player", tint = text) }
    }
}

@Composable
private fun TrackRow(track: Track, text: Color, muted: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).clickable(onClick = onClick).padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = track.art, contentDescription = track.album, contentScale = ContentScale.Crop, modifier = Modifier.size(54.dp).clip(RoundedCornerShape(11.dp)))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(track.title, color = text, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(track.artist, color = muted, fontSize = 12.sp)
        }
        Text(track.duration, color = muted, fontSize = 11.sp)
        IconButton(onClick = onClick) { Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = muted) }
    }
}

@Composable
private fun Artwork(url: String, modifier: Modifier = Modifier) {
    AsyncImage(model = url, contentDescription = "Music artwork", contentScale = ContentScale.Crop, modifier = modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFF29213B)))
}

@Composable
private fun SectionTitle(title: String, subtitle: String, text: Color, muted: Color) {
    Column {
        Text(title, color = text, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Text(subtitle, color = muted, fontSize = 11.sp)
    }
}

@Composable
private fun QuickTile(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(tint.copy(alpha = 0.14f)).clickable(onClick = onClick).padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(7.dp))
        Text(title, color = MaterialTheme.colorScheme.onSurface, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

@Composable
private fun LibraryShortcut(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, surface: Color, text: Color, muted: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 8.dp).clip(RoundedCornerShape(16.dp)).background(surface).clickable(onClick = onClick).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(46.dp).clip(RoundedCornerShape(13.dp)).background(Brush.linearGradient(listOf(Purple, Pink))), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White)
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = text, fontWeight = FontWeight.Bold)
            Text(subtitle, color = muted, fontSize = 11.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = muted)
    }
}

@Composable
private fun AlbumWideCard(title: String, subtitle: String, image: String, text: Color, muted: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).clickable(onClick = onClick).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Artwork(image, Modifier.size(82.dp))
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = text, fontWeight = FontWeight.Bold)
            Text(subtitle, color = muted, fontSize = 12.sp)
        }
        Icon(Icons.Filled.PlayCircleFilled, contentDescription = "Play album", tint = Purple, modifier = Modifier.size(34.dp))
    }
}
