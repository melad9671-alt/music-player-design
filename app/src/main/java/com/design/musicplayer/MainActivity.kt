package com.design.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

private val Ink = Color(0xFF090811)
private val Panel = Color(0xFF171421)
private val Violet = Color(0xFF9B6BFF)
private val Lavender = Color(0xFFD5C0FF)
private val Rose = Color(0xFFFF79C8)
private val Soft = Color(0xFFA8A1B7)

private data class Song(val title: String, val artist: String, val album: String, val length: String, val colors: List<Color>, val mark: String)
private val songs = listOf(
    Song("Pink Venom", "BLACKPINK", "BORN PINK", "3:06", listOf(Color(0xFF32134C), Color(0xFFB23D87)), "P"),
    Song("Flower", "JISOO", "ME", "2:54", listOf(Color(0xFF542C66), Color(0xFFE9A2B7)), "F"),
    Song("Super Shy", "NewJeans", "Get Up", "2:34", listOf(Color(0xFF173C52), Color(0xFF66B8B0)), "S"),
    Song("Love wins all", "IU", "The Winning", "4:31", listOf(Color(0xFF4B315F), Color(0xFFB28ACB)), "L"),
    Song("How You Like That", "BLACKPINK", "THE ALBUM", "3:01", listOf(Color(0xFF211A43), Color(0xFF9C5FC5)), "H"),
    Song("Ditto", "NewJeans", "OMG", "3:05", listOf(Color(0xFF1C3D4A), Color(0xFF9BC8C0)), "D")
)
private enum class Page(val label: String) { HOME("Home"), SEARCH("Search"), ALBUMS("Albums"), ARTISTS("Artists"), LIBRARY("Library") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(9, 8, 17)
        window.navigationBarColor = android.graphics.Color.rgb(9, 8, 17)
        setContent { NoireMusicApp() }
    }
}

@Composable
private fun NoireMusicApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var current by remember { mutableStateOf(songs.first()) }
    var player by remember { mutableStateOf(false) }
    var playing by remember { mutableStateOf(false) }
    var liked by remember { mutableStateOf(false) }
    var dark by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    val bg = if (dark) Ink else Color(0xFFF7F4FC)
    val panel = if (dark) Panel else Color.White
    val fg = if (dark) Color.White else Color(0xFF211A2D)
    val muted = if (dark) Soft else Color(0xFF777084)
    val scheme = if (dark) darkColorScheme(primary = Violet, background = bg, surface = panel, onSurface = fg) else lightColorScheme(primary = Violet, background = bg, surface = panel, onSurface = fg)

    MaterialTheme(colorScheme = scheme) {
        Surface(Modifier.fillMaxSize(), color = bg) {
            if (player) {
                PlayerPage(current, playing, liked, fg, muted, dark,
                    onBack = { player = false }, onPlay = { playing = !playing }, onLike = { liked = !liked },
                    onNext = { current = songs[(songs.indexOf(current) + 1) % songs.size] },
                    onPrevious = { current = songs[(songs.indexOf(current) - 1 + songs.size) % songs.size] })
            } else {
                Scaffold(
                    containerColor = bg,
                    bottomBar = {
                        Column(Modifier.background(bg)) {
                            MiniNowPlaying(current, playing, panel, fg, muted, { player = true }, { playing = !playing })
                            NavigationBar(containerColor = panel, tonalElevation = 0.dp) {
                                Page.values().forEach { item ->
                                    val icon = when (item) {
                                        Page.HOME -> Icons.Filled.Home
                                        Page.SEARCH -> Icons.Filled.Search
                                        Page.ALBUMS -> Icons.Filled.Album
                                        Page.ARTISTS -> Icons.Filled.People
                                        Page.LIBRARY -> Icons.Filled.LibraryMusic
                                    }
                                    NavigationBarItem(
                                        selected = page == item, onClick = { page = item },
                                        icon = { Icon(icon, contentDescription = item.label) },
                                        label = { Text(item.label, fontSize = 10.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Lavender, selectedTextColor = Lavender,
                                            indicatorColor = Violet.copy(alpha = 0.24f),
                                            unselectedIconColor = muted, unselectedTextColor = muted
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { inset ->
                    when (page) {
                        Page.HOME -> HomePage(inset, fg, muted, dark, { dark = !dark }, { current = it; player = true }, { page = it })
                        Page.SEARCH -> SearchPage(inset, query, { query = it }, fg, muted, { current = it; player = true })
                        Page.ALBUMS -> AlbumsPage(inset, fg, muted, { current = it; player = true })
                        Page.ARTISTS -> ArtistsPage(inset, fg, muted)
                        Page.LIBRARY -> LibraryPage(inset, fg, muted, { page = it }, { current = it; player = true })
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePage(inset: PaddingValues, fg: Color, muted: Color, dark: Boolean, onTheme: () -> Unit, onSong: (Song) -> Unit, onPage: (Page) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(inset), contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 22.dp, bottom = 26.dp), verticalArrangement = Arrangement.spacedBy(27.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Eyebrow("SATURDAY, YOUR WAY")
                    Spacer(Modifier.height(5.dp))
                    Text("Good evening.", color = fg, fontSize = 29.sp, fontWeight = FontWeight.ExtraBold)
                }
                IconButton(onClick = onTheme, modifier = Modifier.size(46.dp).clip(CircleShape).background(Panel)) {
                    Icon(if (dark) Icons.Filled.LightMode else Icons.Filled.DarkMode, contentDescription = "Change theme", tint = Lavender)
                }
                Spacer(Modifier.width(9.dp))
                Box(Modifier.size(46.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Violet, Rose))), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.GraphicEq, contentDescription = "Noire Music", tint = Color.White, modifier = Modifier.size(27.dp))
                }
            }
        }
        item { FeaturedCard { onSong(songs.first()) } }
        item {
            Row(verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Eyebrow("YOUR SPACE")
                    Text("Jump back in", color = fg, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                }
                Text("SEE ALL", color = Violet, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.clickable { onPage(Page.LIBRARY) }.padding(bottom = 4.dp))
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickCard("Songs", Icons.Filled.MusicNote, listOf(Color(0xFF39205A), Color(0xFF21172F)), Modifier.weight(1f)) { onPage(Page.LIBRARY) }
                QuickCard("Albums", Icons.Filled.Album, listOf(Color(0xFF512447), Color(0xFF29182C)), Modifier.weight(1f)) { onPage(Page.ALBUMS) }
                QuickCard("Artists", Icons.Filled.People, listOf(Color(0xFF263B61), Color(0xFF191D34)), Modifier.weight(1f)) { onPage(Page.ARTISTS) }
                QuickCard("Mixes", Icons.Filled.QueueMusic, listOf(Color(0xFF49305D), Color(0xFF241A35)), Modifier.weight(1f)) { onPage(Page.LIBRARY) }
            }
        }
        item {
            SectionHeader("Made for your mood", "Curated picks", fg, muted)
            Spacer(Modifier.height(14.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(songs.take(4)) { song ->
                    Column(Modifier.width(142.dp).clickable { onSong(song) }) {
                        AlbumArtwork(song, Modifier.fillMaxWidth().height(142.dp))
                        Spacer(Modifier.height(9.dp))
                        Text(song.album, color = fg, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                        Text(song.artist, color = muted, fontSize = 11.sp)
                    }
                }
            }
        }
        item {
            SectionHeader("Recently played", "Pick up where you left off", fg, muted)
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                songs.take(3).forEachIndexed { index, song -> SongRow(song, index + 1, fg, muted) { onSong(song) } }
            }
        }
    }
}

@Composable
private fun FeaturedCard(onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(232.dp).clip(RoundedCornerShape(30.dp)).background(Brush.linearGradient(listOf(Color(0xFF3C1E62), Color(0xFF171124), Color(0xFF7D326D))))) {
        Box(Modifier.size(220.dp).align(Alignment.CenterEnd).offset(x = 48.dp, y = (-16).dp).clip(CircleShape).background(Color.White.copy(alpha = 0.055f)))
        Box(Modifier.size(155.dp).align(Alignment.CenterEnd).offset(x = 10.dp, y = 20.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.055f)))
        Box(Modifier.size(100.dp).align(Alignment.CenterEnd).offset(x = (-22).dp, y = 20.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.09f)))
        Column(Modifier.align(Alignment.CenterStart).padding(start = 23.dp, end = 18.dp)) {
            Text("NOIRE  /  EXCLUSIVE", color = Lavender, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
            Spacer(Modifier.height(12.dp))
            Text("Your music.\nYour universe.", color = Color.White, fontSize = 29.sp, lineHeight = 33.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(17.dp))
            Row(Modifier.clip(RoundedCornerShape(50)).background(Color.White).clickable(onClick = onClick).padding(horizontal = 17.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFF31194F))
                Spacer(Modifier.width(5.dp))
                Text("PLAY FEATURED", color = Color(0xFF31194F), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }
        }
        Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.align(Alignment.CenterEnd).padding(end = 28.dp).size(64.dp))
        Text("01", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomEnd).padding(19.dp))
    }
}

@Composable
private fun QuickCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, colors: List<Color>, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(colors)).clickable(onClick = onClick).padding(vertical = 15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Lavender, modifier = Modifier.size(23.dp))
        Spacer(Modifier.height(8.dp))
        Text(label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SearchPage(inset: PaddingValues, query: String, onQuery: (String) -> Unit, fg: Color, muted: Color, onSong: (Song) -> Unit) {
    val filtered = songs.filter { it.title.contains(query, true) || it.artist.contains(query, true) || it.album.contains(query, true) }
    Column(Modifier.fillMaxSize().padding(inset).padding(horizontal = 22.dp)) {
        Spacer(Modifier.height(25.dp))
        Eyebrow("DISCOVER SOMETHING NEW")
        Text("Search", color = fg, fontSize = 31.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = query, onValueChange = onQuery, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Songs, artists, albums…", color = muted) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Violet) },
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { onQuery("") }) { Icon(Icons.Filled.Close, contentDescription = "Clear") } },
            shape = RoundedCornerShape(18.dp), singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Violet, unfocusedBorderColor = muted.copy(alpha = 0.25f), focusedTextColor = fg, unfocusedTextColor = fg, cursorColor = Violet)
        )
        Spacer(Modifier.height(25.dp))
        SectionHeader(if (query.isBlank()) "Trending now" else "Results", filtered.size.toString() + " tracks", fg, muted)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { song -> SongRow(song, null, fg, muted) { onSong(song) } }
        }
    }
}

@Composable
private fun AlbumsPage(inset: PaddingValues, fg: Color, muted: Color, onSong: (Song) -> Unit) {
    Column(Modifier.fillMaxSize().padding(inset).padding(horizontal = 22.dp)) {
        Spacer(Modifier.height(25.dp))
        Eyebrow("THE COLLECTION")
        Text("Albums", color = fg, fontSize = 31.sp, fontWeight = FontWeight.ExtraBold)
        Text("Records worth coming back to.", color = muted, fontSize = 13.sp)
        Spacer(Modifier.height(20.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(bottom = 24.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(19.dp)) {
            items(songs) { song ->
                Column(Modifier.clickable { onSong(song) }) {
                    AlbumArtwork(song, Modifier.fillMaxWidth().aspectRatio(1f))
                    Spacer(Modifier.height(9.dp))
                    Text(song.album, color = fg, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(song.artist, color = muted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun ArtistsPage(inset: PaddingValues, fg: Color, muted: Color) {
    val names = listOf("BLACKPINK" to "4 tracks in your library", "JISOO" to "3 tracks in your library", "NewJeans" to "8 tracks in your library", "IU" to "5 tracks in your library")
    LazyColumn(Modifier.fillMaxSize().padding(inset).padding(horizontal = 22.dp), contentPadding = PaddingValues(top = 25.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Eyebrow("ARTIST SPOTLIGHT")
            Text("Artists", color = fg, fontSize = 31.sp, fontWeight = FontWeight.ExtraBold)
            Text("The people behind the music.", color = muted, fontSize = 13.sp)
            Spacer(Modifier.height(13.dp))
        }
        items(names) { entry ->
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Panel).padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(58.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Violet, Rose))), contentAlignment = Alignment.Center) {
                    Text(entry.first.take(1), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(entry.first, color = fg, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(entry.second, color = muted, fontSize = 11.sp)
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = muted)
            }
        }
    }
}

@Composable
private fun LibraryPage(inset: PaddingValues, fg: Color, muted: Color, onPage: (Page) -> Unit, onSong: (Song) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(inset).padding(horizontal = 22.dp), contentPadding = PaddingValues(top = 25.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Eyebrow("YOUR PERSONAL SPACE")
            Text("Library", color = fg, fontSize = 31.sp, fontWeight = FontWeight.ExtraBold)
            Text("Everything you saved, all in one place.", color = muted, fontSize = 13.sp)
            Spacer(Modifier.height(14.dp))
            LibraryTile("Your songs", "Listen to your full collection", Icons.Filled.MusicNote) { onSong(songs.first()) }
            LibraryTile("Albums", "Browse all saved albums", Icons.Filled.Album) { onPage(Page.ALBUMS) }
            LibraryTile("Artists", "Explore your favorite artists", Icons.Filled.People) { onPage(Page.ARTISTS) }
            LibraryTile("Playlists", "Your mixes and playlists", Icons.Filled.QueueMusic) { onPage(Page.SEARCH) }
            Spacer(Modifier.height(14.dp))
            SectionHeader("Recently added", "LATEST IN YOUR LIBRARY", fg, muted)
        }
        items(songs) { song -> SongRow(song, null, fg, muted) { onSong(song) } }
    }
}

@Composable
private fun LibraryTile(title: String, sub: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 7.dp).clip(RoundedCornerShape(18.dp)).background(Panel).clickable(onClick = onClick).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(Violet, Rose))), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White)
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(sub, color = Soft, fontSize = 11.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Soft)
    }
}

@Composable
private fun PlayerPage(song: Song, playing: Boolean, liked: Boolean, fg: Color, muted: Color, dark: Boolean, onBack: () -> Unit, onPlay: () -> Unit, onLike: () -> Unit, onNext: () -> Unit, onPrevious: () -> Unit) {
    var progress by remember(song) { mutableFloatStateOf(0.28f) }
    Column(Modifier.fillMaxSize().background(if (dark) Ink else Color(0xFFF7F4FC)).padding(horizontal = 25.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Close player", tint = fg) }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("NOW PLAYING", color = Violet, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                Text("From your collection", color = muted, fontSize = 11.sp)
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onLike) { Icon(if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = "Favorite", tint = if (liked) Rose else fg) }
        }
        Spacer(Modifier.weight(0.7f))
        AlbumArtwork(song, Modifier.fillMaxWidth().aspectRatio(1f).padding(4.dp))
        Spacer(Modifier.height(34.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(song.title, color = fg, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                Text(song.artist, color = muted, fontSize = 14.sp)
            }
            IconButton(onClick = onLike) { Icon(if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = "Like song", tint = if (liked) Rose else muted) }
        }
        Slider(value = progress, onValueChange = { progress = it }, colors = SliderDefaults.colors(thumbColor = Violet, activeTrackColor = Violet, inactiveTrackColor = muted.copy(alpha = 0.25f)))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("0:52", color = muted, fontSize = 10.sp)
            Text(song.length, color = muted, fontSize = 10.sp)
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) { Icon(Icons.Filled.Shuffle, contentDescription = "Shuffle", tint = muted) }
            IconButton(onClick = onPrevious) { Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous", tint = fg, modifier = Modifier.size(34.dp)) }
            FilledIconButton(onClick = onPlay, modifier = Modifier.size(70.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = Violet)) {
                Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = "Play pause", tint = Color.White, modifier = Modifier.size(35.dp))
            }
            IconButton(onClick = onNext) { Icon(Icons.Filled.SkipNext, contentDescription = "Next", tint = fg, modifier = Modifier.size(34.dp)) }
            IconButton(onClick = {}) { Icon(Icons.Filled.Repeat, contentDescription = "Repeat", tint = muted) }
        }
        Spacer(Modifier.weight(1f))
        Text("NOIRE  •  MUSIC WITHOUT LIMITS", color = muted.copy(alpha = 0.7f), fontSize = 9.sp, letterSpacing = 1.5.sp)
    }
}

@Composable
private fun MiniNowPlaying(song: Song, playing: Boolean, panel: Color, fg: Color, muted: Color, onClick: () -> Unit, onPlay: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 7.dp).clip(RoundedCornerShape(18.dp)).background(panel).clickable(onClick = onClick).padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
        AlbumArtwork(song, Modifier.size(46.dp))
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(song.title, color = fg, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(song.artist, color = muted, fontSize = 10.sp)
        }
        Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Violet, modifier = Modifier.padding(horizontal = 8.dp))
        IconButton(onClick = onPlay) { Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = "Play pause", tint = fg) }
    }
}

@Composable
private fun SongRow(song: Song, number: Int?, fg: Color, muted: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable(onClick = onClick).padding(vertical = 6.dp, horizontal = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        if (number != null) Text(number.toString().padStart(2, '0'), color = muted, fontSize = 10.sp, modifier = Modifier.width(27.dp))
        AlbumArtwork(song, Modifier.size(52.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(song.title, color = fg, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(song.artist + "  •  " + song.album, color = muted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(song.length, color = muted, fontSize = 10.sp)
        Icon(Icons.Filled.MoreHoriz, contentDescription = "More", tint = muted, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun AlbumArtwork(song: Song, modifier: Modifier = Modifier) {
    Box(modifier.clip(RoundedCornerShape(19.dp)).background(Brush.linearGradient(song.colors))) {
        Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent))))
        Box(Modifier.size(90.dp).align(Alignment.TopEnd).offset(x = 30.dp, y = (-24).dp).clip(CircleShape).background(Color.White.copy(alpha = 0.10f)))
        Box(Modifier.size(62.dp).align(Alignment.BottomStart).offset(x = (-17).dp, y = 18.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.12f)))
        Text(song.mark, color = Color.White.copy(alpha = 0.92f), fontSize = 54.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.Center))
        Text("NOIRE", color = Color.White.copy(alpha = 0.75f), fontSize = 7.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp))
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String, fg: Color, muted: Color) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = fg, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text(subtitle, color = muted, fontSize = 11.sp)
        }
        Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Violet, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun Eyebrow(text: String) {
    Text(text, color = Violet, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
}
