package com.sonora.musicdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
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
        window.statusBarColor = android.graphics.Color.rgb(7, 5, 17)
        window.navigationBarColor = android.graphics.Color.rgb(7, 5, 17)
        setContent { DesignMusicApp() }
    }
}

private val Violet = Color(0xFF9B35FF)
private val Pink = Color(0xFFE35BFF)
private val Gold = Color(0xFFFFD46A)
private val Ink = Color(0xFF080611)
private val Panel = Color(0xFF171326)
private val Muted = Color(0xFFAAA2C0)
private data class Song(val title: String, val artist: String, val duration: String, val art: Int)
private val songs = listOf(
    Song("Pink Venom", "BLACKPINK", "03:13", 0),
    Song("Dream Big", "BLACKPINK", "03:42", 1),
    Song("Forever", "BTS", "03:55", 2),
    Song("Shut Down", "BLACKPINK", "02:55", 3),
    Song("The Astronaut", "Jin", "04:25", 4)
)
private val albums = listOf("BORN PINK", "Proof", "Afterglow", "Blue Hour")

@Composable
private fun DesignMusicApp() {
    var page by remember { mutableStateOf("Home") }
    var current by remember { mutableStateOf(songs[0]) }
    var playing by remember { mutableStateOf(false) }
    var dark by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    var progress by remember { mutableFloatStateOf(.39f) }
    SonoraTheme(darkTheme = dark) {
        val bg = if (dark) Ink else Color(0xFFF7F2FF)
        val surface = if (dark) Panel else Color.White
        val fg = if (dark) Color.White else Color(0xFF21152F)
        Scaffold(containerColor = bg, bottomBar = {
            Column(Modifier.background(bg)) {
                if (page != "Now Playing") MiniBar(current, playing, surface, { page = "Now Playing" }, { playing = !playing })
                NavigationBar(containerColor = surface, tonalElevation = 0.dp, modifier = Modifier.height(70.dp)) {
                    listOf("Home", "Library", "Search", "Settings").forEach { item ->
                        val icon = when (item) {
                            "Home" -> Icons.Filled.Home
                            "Library" -> Icons.Filled.LibraryMusic
                            "Search" -> Icons.Filled.Search
                            else -> Icons.Filled.Settings
                        }
                        NavigationBarItem(selected = page == item, onClick = { page = item },
                            icon = { Icon(icon, null, modifier = Modifier.size(22.dp)) },
                            label = { Text(item, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Pink, selectedTextColor = Pink, unselectedIconColor = Muted, unselectedTextColor = Muted, indicatorColor = Violet.copy(alpha = .18f)))
                    }
                }
            }
        }) { padding ->
            when (page) {
                "Now Playing" -> PlayerPage(current, playing, progress, { progress = it }, { playing = !playing }, { page = "Home" }, fg)
                "Library" -> LibraryPage(surface, fg, { page = it }, { current = it; page = "Now Playing" })
                "Search" -> SearchPage(query, { query = it }, fg, { current = it; page = "Now Playing" })
                "Settings" -> SettingsPage(dark, { dark = it }, fg, surface)
                "Albums" -> CollectionPage("Albums", albums, surface, fg)
                "Artists" -> CollectionPage("Artists", listOf("BLACKPINK", "BTS", "IU", "NewJeans"), surface, fg)
                "Playlists" -> CollectionPage("Playlists", listOf("My favorites", "Late night", "Pop energy", "Chill mix"), surface, fg)
                else -> HomePage(padding, surface, fg, { page = it }, { current = it; page = "Now Playing" })
            }
        }
    }
}

@Composable
private fun Brand() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(58.dp).clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(Color(0xFF34106E), Color(0xFF100A25)))), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.MusicNote, null, tint = Violet, modifier = Modifier.size(43.dp))
            Text("♛", color = Gold, fontSize = 19.sp, modifier = Modifier.align(Alignment.TopCenter).offset(y = (-4).dp))
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text("Design", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("M U S I C   P L A Y E R", fontSize = 9.sp, color = Muted)
        }
    }
}

@Composable
private fun HomePage(padding: PaddingValues, surface: Color, fg: Color, go: (String) -> Unit, song: (Song) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding()),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) { Brand() }
                IconButton(onClick = { go("Search") }) { Icon(Icons.Filled.Search, null, tint = Color.White, modifier = Modifier.size(27.dp)) }
                IconButton(onClick = { go("Settings") }) { Icon(Icons.Filled.Settings, null, tint = Color.White, modifier = Modifier.size(27.dp)) }
            }
        }
        item {
            Box(Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(27.dp)).background(Brush.linearGradient(listOf(Color(0xFF5C13B8), Color(0xFF210A53), Color(0xFF090617)))).padding(22.dp)) {
                Column(Modifier.align(Alignment.CenterStart)) {
                    Text("Enjoy Your Music", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("With Design", color = Color(0xFFD9C8F6), fontSize = 14.sp)
                        Spacer(Modifier.width(5.dp))
                        Icon(Icons.Filled.Favorite, null, tint = Pink, modifier = Modifier.size(17.dp))
                    }
                }
                VinylRecord(Modifier.size(180.dp).align(Alignment.CenterEnd).offset(x = 28.dp))
                Box(Modifier.align(Alignment.BottomEnd).size(48.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Pink, Violet))), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        }
        item { SectionTitle("Quick Access", "See All", { go("Library") }, fg) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("Songs" to Icons.Filled.MusicNote, "Albums" to Icons.Filled.Album, "Artists" to Icons.Filled.Person, "Playlists" to Icons.Filled.QueueMusic).forEach { pair ->
                    Column(Modifier.weight(1f).clip(RoundedCornerShape(19.dp)).background(surface).clickable { go(if (pair.first == "Songs") "Library" else pair.first) }.padding(vertical = 15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(pair.second, null, tint = Pink, modifier = Modifier.size(34.dp))
                        Spacer(Modifier.height(7.dp))
                        Text(pair.first, color = fg, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        item { SectionTitle("Recently Played", "See All", { go("Library") }, fg) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(songs.take(4)) { s ->
                    Column(Modifier.width(126.dp).clickable { song(s) }) {
                        AlbumArtwork(s.art, Modifier.fillMaxWidth().aspectRatio(1f))
                        Spacer(Modifier.height(7.dp))
                        Text(s.title, color = fg, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp)
                        Text(s.artist, color = Muted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, action: String, click: () -> Unit, fg: Color) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = fg, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(action, color = Color(0xFFC98BFF), fontSize = 12.sp, modifier = Modifier.clickable { click() })
        Icon(Icons.Filled.ChevronRight, null, tint = Color(0xFFC98BFF), modifier = Modifier.size(17.dp))
    }
}

@Composable
private fun VinylRecord(modifier: Modifier = Modifier) {
    Box(modifier.clip(CircleShape).background(Brush.radialGradient(listOf(Color(0xFF5B26A8), Color(0xFF080611), Color(0xFF211334)))).border(1.dp, Violet.copy(alpha = .5f), CircleShape), contentAlignment = Alignment.Center) {
        repeat(5) { i -> Box(Modifier.fillMaxSize(.88f - i * .13f).border(1.dp, Color.White.copy(alpha = .09f), CircleShape)) }
        Box(Modifier.size(57.dp).clip(CircleShape).background(Brush.radialGradient(listOf(Pink, Violet, Color(0xFF35106D)))), contentAlignment = Alignment.Center) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF18072B)))
        }
    }
}

@Composable
private fun AlbumArtwork(index: Int, modifier: Modifier = Modifier) {
    val gradients = listOf(
        listOf(Color(0xFF5A0D8D), Color(0xFF16072D), Color(0xFF030208)),
        listOf(Color(0xFF2A1C78), Color(0xFF0C123A), Color(0xFF050612)),
        listOf(Color(0xFFED63C9), Color(0xFF6B2DA5), Color(0xFF130A2B)),
        listOf(Color(0xFF6D39F2), Color(0xFF220B60), Color(0xFF050611)),
        listOf(Color(0xFF1367B6), Color(0xFF101A50), Color(0xFF030611))
    )
    Box(modifier.clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(gradients[index % gradients.size])).border(1.dp, Color.White.copy(alpha = .12f), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
        if (index == 0) Text("♕", color = Color(0xFFFF8DEB), fontSize = 66.sp)
        else if (index == 1 || index == 4) Icon(Icons.Filled.Person, null, tint = Color.White.copy(alpha = .55f), modifier = Modifier.size(62.dp))
        else Icon(Icons.Filled.Favorite, null, tint = Pink.copy(alpha = .8f), modifier = Modifier.size(58.dp))
    }
}

@Composable
private fun MiniBar(song: Song, playing: Boolean, surface: Color, open: () -> Unit, play: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp).clip(RoundedCornerShape(17.dp)).background(surface).clickable { open() }.padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
        AlbumArtwork(song.art, Modifier.size(45.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(song.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(song.artist, color = Muted, fontSize = 10.sp)
        }
        IconButton(onClick = play) { Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, tint = Pink) }
        IconButton(onClick = open) { Icon(Icons.Filled.SkipNext, null, tint = Color.White) }
    }
}

@Composable
private fun PlayerPage(song: Song, playing: Boolean, progress: Float, onProgress: (Float) -> Unit, play: () -> Unit, back: () -> Unit, fg: Color) {
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF16082C), Ink, Ink))).padding(horizontal = 25.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = back) { Icon(Icons.Filled.KeyboardArrowDown, null, tint = fg) }
            Spacer(Modifier.weight(1f))
            Text("NOW PLAYING", color = Muted, fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {}) { Icon(Icons.Filled.MoreVert, null, tint = fg) }
        }
        AlbumArtwork(song.art, Modifier.fillMaxWidth().aspectRatio(1f).padding(4.dp))
        Column(Modifier.fillMaxWidth()) {
            Text(song.title, color = fg, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text(song.artist, color = Pink, fontSize = 15.sp)
            Spacer(Modifier.height(18.dp))
            Slider(value = progress, onValueChange = onProgress, colors = SliderDefaults.colors(thumbColor = Pink, activeTrackColor = Pink, inactiveTrackColor = Color(0xFF392B4C)))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("01:24", color = Muted, fontSize = 11.sp)
                Text(song.duration, color = Muted, fontSize = 11.sp)
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}) { Icon(Icons.Filled.Shuffle, null, tint = Muted) }
                IconButton(onClick = {}) { Icon(Icons.Filled.SkipPrevious, null, tint = fg, modifier = Modifier.size(30.dp)) }
                Box(Modifier.size(66.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Pink, Violet))).clickable { play() }, contentAlignment = Alignment.Center) {
                    Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(34.dp))
                }
                IconButton(onClick = {}) { Icon(Icons.Filled.SkipNext, null, tint = fg, modifier = Modifier.size(30.dp)) }
                IconButton(onClick = {}) { Icon(Icons.Filled.Repeat, null, tint = Muted) }
            }
        }
        Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Filled.Devices, null, tint = Muted)
            Icon(Icons.Filled.QueueMusic, null, tint = Muted)
        }
    }
}

@Composable
private fun LibraryPage(surface: Color, fg: Color, go: (String) -> Unit, song: (Song) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        item { Text("Library", color = fg, fontSize = 27.sp, fontWeight = FontWeight.Bold) }
        items(listOf("Songs" to "All your songs", "Albums" to "All albums", "Artists" to "All artists", "Playlists" to "Your playlists")) { pair ->
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(surface).clickable { go(pair.first) }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(Violet, Pink))), contentAlignment = Alignment.Center) {
                    Icon(when (pair.first) { "Songs" -> Icons.Filled.MusicNote; "Albums" -> Icons.Filled.Album; "Artists" -> Icons.Filled.Person; else -> Icons.Filled.QueueMusic }, null, tint = Color.White)
                }
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text(pair.first, color = fg, fontWeight = FontWeight.SemiBold)
                    Text(pair.second, color = Muted, fontSize = 11.sp)
                }
                Icon(Icons.Filled.ChevronRight, null, tint = Muted)
            }
        }
        item { SectionTitle("Recently Added", "See All", { go("Songs") }, fg) }
        items(songs) { s ->
            Row(Modifier.fillMaxWidth().clickable { song(s) }.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                AlbumArtwork(s.art, Modifier.size(55.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) { Text(s.title, color = fg, fontWeight = FontWeight.SemiBold); Text(s.artist, color = Muted, fontSize = 11.sp) }
                Icon(Icons.Filled.MoreHoriz, null, tint = Muted)
            }
        }
    }
}

@Composable
private fun SearchPage(query: String, onQuery: (String) -> Unit, fg: Color, song: (Song) -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Search", color = fg, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(17.dp))
        OutlinedTextField(value = query, onValueChange = onQuery, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Songs, artists, albums...") }, leadingIcon = { Icon(Icons.Filled.Search, null) }, singleLine = true, shape = RoundedCornerShape(18.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Pink, unfocusedBorderColor = Color(0xFF44335C), focusedTextColor = fg, unfocusedTextColor = fg))
        Spacer(Modifier.height(20.dp))
        Text(if (query.isBlank()) "Popular right now" else "Results", color = fg, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        songs.filter { it.title.contains(query, true) || it.artist.contains(query, true) }.forEach { s ->
            Row(Modifier.fillMaxWidth().clickable { song(s) }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                AlbumArtwork(s.art, Modifier.size(52.dp))
                Spacer(Modifier.width(12.dp))
                Column { Text(s.title, color = fg, fontWeight = FontWeight.SemiBold); Text(s.artist, color = Muted, fontSize = 11.sp) }
            }
        }
    }
}

@Composable
private fun CollectionPage(title: String, names: List<String>, surface: Color, fg: Color) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text(title, color = fg, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        names.forEachIndexed { index, name ->
            Row(Modifier.fillMaxWidth().padding(bottom = 12.dp).clip(RoundedCornerShape(18.dp)).background(surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                AlbumArtwork(index, Modifier.size(62.dp))
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text(name, color = fg, fontWeight = FontWeight.SemiBold)
                    Text(if (title == "Artists") "5 songs" else "Your collection", color = Muted, fontSize = 11.sp)
                }
                Icon(Icons.Filled.ChevronRight, null, tint = Muted)
            }
        }
    }
}

@Composable
private fun SettingsPage(dark: Boolean, onDark: (Boolean) -> Unit, fg: Color, surface: Color) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Settings", color = fg, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(surface).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.DarkMode, null, tint = Pink)
            Spacer(Modifier.width(12.dp))
            Text("Dark mode", color = fg, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            Switch(checked = dark, onCheckedChange = onDark, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Violet))
        }
    }
}
