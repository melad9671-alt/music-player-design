package com.sonora.musicdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonora.musicdesign.ui.theme.SonoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SonoraApp() }
    }
}

private data class Track(val title: String, val artist: String, val duration: String, val art: Color)
private data class Album(val title: String, val artist: String, val art: Color)

private val tracks = listOf(
    Track("Midnight City", "M83", "4:03", Color(0xFF6554A4)),
    Track("The Less I Know The Better", "Tame Impala", "3:36", Color(0xFFB36C46)),
    Track("After Dark", "Mr.Kitty", "4:17", Color(0xFF315C78)),
    Track("Sweater Weather", "The Neighbourhood", "4:00", Color(0xFF65716C)),
    Track("Space Song", "Beach House", "5:20", Color(0xFF596B9C))
)
private val albums = listOf(
    Album("Hurry Up, We're Dreaming", "M83", Color(0xFF6956A5)),
    Album("Currents", "Tame Impala", Color(0xFFB46C47)),
    Album("Depression Cherry", "Beach House", Color(0xFF8E514B)),
    Album("AM", "Arctic Monkeys", Color(0xFF58636B))
)

@Composable
private fun SonoraApp() {
    var dark by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf("Home") }
    var playing by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf(tracks.first()) }
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }

    SonoraTheme(darkTheme = dark) {
        val cs = MaterialTheme.colorScheme
        Scaffold(
            containerColor = cs.background,
            bottomBar = {
                Column {
                    MiniPlayer(current, playing, onPlay = { playing = !playing }, onOpen = { selected = "Now Playing" })
                    NavigationBar(containerColor = cs.surface, tonalElevation = 0.dp) {
                        listOf(
                            "Home" to Icons.Filled.Home,
                            "Search" to Icons.Filled.Search,
                            "Albums" to Icons.Filled.Album,
                            "Artists" to Icons.Filled.People,
                            "Library" to Icons.Filled.LibraryMusic
                        ).forEach { (label, icon) ->
                            NavigationBarItem(
                                selected = selected == label,
                                onClick = { selected = label },
                                icon = { Icon(icon, contentDescription = label) },
                                label = { Text(label, maxLines = 1, fontSize = 10.sp) },
                                alwaysShowLabel = true,
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = cs.primary, indicatorColor = cs.surfaceVariant)
                            )
                        }
                    }
                }
            }
        ) { padding ->
            when (selected) {
                "Now Playing" -> PlayerScreen(current, playing, { playing = !playing }, { selected = "Home" })
                "Search" -> SearchScreen(query, { query = it }, tracks.filter { it.title.contains(query, true) || it.artist.contains(query, true) }, { current = it; selected = "Now Playing" })
                "Library" -> LibraryScreen(filter, { filter = it }, { selected = it }, albums, { current = it; selected = "Now Playing" })
                "Albums" -> AlbumsScreen(albums)
                "Artists" -> SimpleCollectionScreen("Artists", listOf("M83", "Tame Impala", "Beach House", "Arctic Monkeys", "The Neighbourhood"))
                "Playlists" -> SimpleCollectionScreen("Playlists", listOf("Late night drive", "Focus mode", "Indie discoveries", "Soft mornings"))
                else -> HomeScreen(
                    padding = padding,
                    dark = dark,
                    onTheme = { dark = !dark },
                    onNavigate = { selected = it },
                    onTrack = { current = it; selected = "Now Playing" },
                    onSeeAll = { selected = "Library" }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    padding: PaddingValues,
    dark: Boolean,
    onTheme: () -> Unit,
    onNavigate: (String) -> Unit,
    onTrack: (Track) -> Unit,
    onSeeAll: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding()),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.MusicNote, null, tint = cs.primary, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("DESIGN", color = cs.primary, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 3.sp)
                    }
                    Text("Music Player", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = cs.onBackground)
                }
                IconButton(onClick = onTheme, modifier = Modifier.size(46.dp).background(cs.surfaceVariant, CircleShape)) {
                    Icon(if (dark) Icons.Filled.LightMode else Icons.Filled.DarkMode, contentDescription = "Toggle theme")
                }
            }
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(28.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF344B25), Color(0xFF17221A), Color(0xFF29233B))))
                    .padding(22.dp)
            ) {
                Column(Modifier.align(Alignment.CenterStart)) {
                    Text("YOUR SOUND. YOUR WORLD.", color = Color(0xFFB9F36A), fontSize = 10.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Text("Find your\nfrequency.", color = Color.White, fontSize = 29.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Explore your mix", color = Color.White, fontSize = 12.sp)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.ArrowForward, null, tint = Color(0xFFB9F36A), modifier = Modifier.size(16.dp))
                    }
                }
                Icon(Icons.Filled.GraphicEq, null, tint = Color(0x557EF04A), modifier = Modifier.size(112.dp).align(Alignment.BottomEnd))
            }
        }
        item { SectionHeader("Made for you", "See all", onSeeAll) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(albums) { album ->
                    Column(Modifier.width(145.dp).clickable { onNavigate("Albums") }) {
                        AlbumArt(album.art, Modifier.size(145.dp))
                        Spacer(Modifier.height(9.dp))
                        Text(album.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(album.artist, color = cs.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
            }
        }
        item { SectionHeader("Recently played", "View library", onSeeAll) }
        items(tracks.take(4)) { track ->
            TrackRow(track, onClick = { onTrack(track) }, trailing = { Icon(Icons.Filled.MoreHoriz, null, tint = cs.onSurfaceVariant) })
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                listOf("Albums", "Artists", "Playlists").forEach { label ->
                    AssistChip(onClick = { onNavigate(label) }, label = { Text(label) }, leadingIcon = {
                        Icon(when (label) { "Albums" -> Icons.Filled.Album; "Artists" -> Icons.Filled.People; else -> Icons.Filled.QueueMusic }, null)
                    })
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text(action, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onClick() })
    }
}

@Composable
private fun AlbumArt(color: Color, modifier: Modifier = Modifier) {
    Box(modifier.clip(RoundedCornerShape(19.dp)).background(Brush.linearGradient(listOf(color, color.copy(alpha = .45f), Color(0xFF171A18))))) {
        Icon(Icons.Filled.GraphicEq, null, tint = Color.White.copy(alpha = .72f), modifier = Modifier.size(48.dp).align(Alignment.Center))
        Box(Modifier.align(Alignment.BottomStart).padding(11.dp).size(7.dp).background(Color(0xFFB9F36A), CircleShape))
    }
}

@Composable
private fun TrackRow(track: Track, onClick: () -> Unit, trailing: @Composable () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        AlbumArt(track.art, Modifier.size(52.dp))
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(track.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(track.artist, color = cs.onSurfaceVariant, fontSize = 12.sp)
        }
        Text(track.duration, color = cs.onSurfaceVariant, fontSize = 11.sp)
        Spacer(Modifier.width(8.dp))
        trailing()
    }
}

@Composable
private fun MiniPlayer(track: Track, playing: Boolean, onPlay: () -> Unit, onOpen: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp).clip(RoundedCornerShape(16.dp))
            .background(cs.surfaceVariant).clickable { onOpen() }.padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumArt(track.art, Modifier.size(42.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(track.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(track.artist, fontSize = 10.sp, color = cs.onSurfaceVariant)
        }
        IconButton(onClick = onPlay) { Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, null) }
        IconButton(onClick = onOpen) { Icon(Icons.Filled.OpenInFull, null) }
    }
}

@Composable
private fun SearchScreen(query: String, onQuery: (String) -> Unit, results: List<Track>, onTrack: (Track) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Search", fontSize = 29.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(value = query, onValueChange = onQuery, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Songs, artists, albums...") }, leadingIcon = { Icon(Icons.Filled.Search, null) }, shape = RoundedCornerShape(18.dp), singleLine = true)
        Spacer(Modifier.height(22.dp))
        Text(if (query.isBlank()) "Popular right now" else "Results", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(10.dp))
        if (results.isEmpty()) Text("No matches yet. Try another search.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        results.forEach { TrackRow(it, { onTrack(it) }, { Icon(Icons.Filled.MoreHoriz, null) }) }
    }
}

@Composable
private fun LibraryScreen(filter: String, onFilter: (String) -> Unit, onNavigate: (String) -> Unit, albums: List<Album>, onTrack: (Track) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Your Library", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Playlists", "Albums", "Artists").forEach {
                FilterChip(selected = filter == it, onClick = { onFilter(it); if (it != "All") onNavigate(it) }, label = { Text(it) })
            }
        }
        Spacer(Modifier.height(18.dp))
        if (filter == "All" || filter == "Playlists") LibraryLink("Your playlists", "Made for every mood", Icons.Filled.QueueMusic) { onNavigate("Playlists") }
        if (filter == "All" || filter == "Albums") LibraryLink("Albums", "Your saved records", Icons.Filled.Album) { onNavigate("Albums") }
        if (filter == "All" || filter == "Artists") LibraryLink("Artists", "The voices you follow", Icons.Filled.People) { onNavigate("Artists") }
        Spacer(Modifier.height(18.dp))
        Text("Recently added", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        tracks.forEach { TrackRow(it, { onTrack(it) }, { Icon(Icons.Filled.MoreHoriz, null) }) }
    }
}

@Composable
private fun LibraryLink(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(52.dp).clip(RoundedCornerShape(15.dp)).background(cs.surfaceVariant), contentAlignment = Alignment.Center) { Icon(icon, null, tint = cs.primary) }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(subtitle, color = cs.onSurfaceVariant, fontSize = 12.sp) }
        Icon(Icons.Filled.ChevronRight, null, tint = cs.onSurfaceVariant)
    }
}

@Composable
private fun AlbumsScreen(albums: List<Album>) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Albums", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("A home for the records you love", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        albums.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                row.forEach { album ->
                    Column(Modifier.weight(1f).padding(bottom = 18.dp)) {
                        AlbumArt(album.art, Modifier.fillMaxWidth().aspectRatio(1f))
                        Spacer(Modifier.height(8.dp))
                        Text(album.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(album.artist, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SimpleCollectionScreen(title: String, entries: List<String>) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Explore your collection", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(18.dp))
        entries.forEachIndexed { index, name ->
            Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(54.dp).clip(RoundedCornerShape(16.dp)).background(listOf(Color(0xFF6554A4), Color(0xFFB36C46), Color(0xFF315C78))[index % 3]), contentAlignment = Alignment.Center) {
                    Icon(if (title == "Artists") Icons.Filled.Person else Icons.Filled.QueueMusic, null, tint = Color.White)
                }
                Spacer(Modifier.width(13.dp))
                Text(name, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PlayerScreen(track: Track, playing: Boolean, onPlay: () -> Unit, onBack: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Column(Modifier.fillMaxSize().padding(horizontal = 26.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.KeyboardArrowDown, "Back") }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("NOW PLAYING", fontSize = 10.sp, letterSpacing = 2.sp, color = cs.onSurfaceVariant, fontWeight = FontWeight.Bold)
                Text("Playing from your mix", fontSize = 12.sp)
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {}) { Icon(Icons.Filled.MoreHoriz, "More") }
        }
        AlbumArt(track.art, Modifier.fillMaxWidth().aspectRatio(1f).padding(8.dp))
        Column(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(track.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(track.artist, color = cs.onSurfaceVariant)
                }
                IconButton(onClick = {}) { Icon(Icons.Filled.FavoriteBorder, "Like", tint = cs.primary) }
            }
            Spacer(Modifier.height(18.dp))
            var progress by remember { mutableFloatStateOf(.34f) }
            Slider(value = progress, onValueChange = { progress = it }, colors = SliderDefaults.colors(thumbColor = cs.primary, activeTrackColor = cs.primary))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("1:24", fontSize = 11.sp, color = cs.onSurfaceVariant)
                Text(track.duration, fontSize = 11.sp, color = cs.onSurfaceVariant)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}) { Icon(Icons.Filled.Shuffle, "Shuffle") }
                IconButton(onClick = {}) { Icon(Icons.Filled.SkipPrevious, "Previous", modifier = Modifier.size(34.dp)) }
                FilledIconButton(onClick = onPlay, modifier = Modifier.size(66.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = cs.primary, contentColor = cs.onPrimary)) {
                    Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, "Play or pause", modifier = Modifier.size(34.dp))
                }
                IconButton(onClick = {}) { Icon(Icons.Filled.SkipNext, "Next", modifier = Modifier.size(34.dp)) }
                IconButton(onClick = {}) { Icon(Icons.Filled.Repeat, "Repeat") }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Filled.Devices, "Devices", tint = cs.onSurfaceVariant)
            Icon(Icons.Filled.QueueMusic, "Queue", tint = cs.onSurfaceVariant)
        }
    }
}
