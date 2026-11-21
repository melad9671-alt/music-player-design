package com.sonora.musicdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.sonora.musicdesign.ui.theme.SonoraTheme

private val Bg=Color(0xFF05040D)
private val Panel=Color(0xFF171426)
private val Panel2=Color(0xFF211934)
private val Purple=Color(0xFF9B31FF)
private val Pink=Color(0xFFE45BFF)
private val Lilac=Color(0xFFCDA4FF)
private val Muted=Color(0xFFAAA1BD)
private val Gold=Color(0xFFFFD66B)
private data class Song(val title:String,val artist:String,val time:String,val art:Int)
private val songs=listOf(Song("Pink Venom","BLACKPINK","03:13",0),Song("Dream Big","BLACKPINK","03:42",1),Song("Forever","BTS","03:55",2),Song("Shut Down","BLACKPINK","02:55",3),Song("The Astronaut","Jin","04:25",4))
private val albums=listOf("BORN PINK","Proof","Afterglow","Blue Hour")
private val artists=listOf("BLACKPINK","BTS","IU","NewJeans")

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
  window.statusBarColor=android.graphics.Color.rgb(5,4,13);window.navigationBarColor=android.graphics.Color.rgb(5,4,13)
  @Suppress("DEPRECATION") window.decorView.systemUiVisibility=0
  setContent { DesignMusicApp() }
 }
}

@Composable private fun DesignMusicApp(){
 var page by remember{mutableStateOf("Home")};var song by remember{mutableStateOf(songs[0])};var playing by remember{mutableStateOf(false)}
 var query by remember{mutableStateOf("")};var progress by remember{mutableFloatStateOf(.39f)}
 SonoraTheme(darkTheme=true){
  Scaffold(containerColor=Bg,bottomBar={if(page!="Now Playing") Column(Modifier.background(Bg)){
   if(page!="Settings") MiniPlayer(song,playing,{page="Now Playing"},{playing=!playing})
   NavigationBar(containerColor=Bg,contentColor=Color.White,tonalElevation=0.dp,modifier=Modifier.height(68.dp)){
    listOf("Home","Library","Search","Settings").forEach{p->
     val ic=when(p){"Home"->Icons.Filled.Home;"Library"->Icons.Filled.LibraryMusic;"Search"->Icons.Filled.Search;else->Icons.Filled.Settings}
     NavigationBarItem(selected=page==p,onClick={page=p},icon={Icon(ic,null,Modifier.size(23.dp))},label={Text(p,fontSize=10.sp,fontWeight=FontWeight.Medium)},colors=NavigationBarItemDefaults.colors(selectedIconColor=Pink,selectedTextColor=Pink,unselectedIconColor=Muted,unselectedTextColor=Muted,indicatorColor=Color.Transparent))
    }
   }
  }}){pad->
   when(page){
    "Home"->HomeScreen(pad,{page=it},{song=it;page="Now Playing"})
    "Library"->LibraryScreen({page=it},{song=it;page="Now Playing"})
    "Search"->SearchScreen(query,{query=it},{song=it;page="Now Playing"})
    "Settings"->SettingsScreen()
    "Now Playing"->PlayerScreen(song,playing,progress,{progress=it},{playing=!playing},{page="Home"},{song=songs[(songs.indexOf(song)+1)%songs.size]})
    "Albums"->GridCollection("Albums",albums,{page="Now Playing";song=songs[0]})
    "Artists"->ArtistScreen()
    "Playlists"->GridCollection("Playlists",listOf("My favorites","Late night","Pop energy","Chill mix"),{})
    "Songs"->SongsScreen({song=it;page="Now Playing"})
   }
  }
 }
}

@Composable private fun Brand(){
 Row(verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(66.dp),contentAlignment=Alignment.Center){
   Box(Modifier.size(60.dp).clip(RoundedCornerShape(19.dp)).background(Brush.linearGradient(listOf(Color(0xFF31106D),Color(0xFF10091F),Color(0xFF35105F)))).border(1.dp,Purple.copy(.45f),RoundedCornerShape(19.dp)))
   Icon(Icons.Filled.MusicNote,null,tint=Pink,modifier=Modifier.size(53.dp))
   Text("♛",color=Gold,fontSize=22.sp,modifier=Modifier.align(Alignment.TopCenter).offset(y=(-2).dp))
  }
  Spacer(Modifier.width(7.dp))
  Column {Text("Design",fontSize=27.sp,fontWeight=FontWeight.ExtraBold,color=Color.White,letterSpacing=(-.8).sp);Text("M u s i c  P l a y e r",fontSize=10.sp,color=Muted,letterSpacing=1.sp)}
 }
}
@Composable private fun HomeScreen(pad:PaddingValues,go:(String)->Unit,open:(Song)->Unit){
 LazyColumn(Modifier.fillMaxSize().padding(bottom=pad.calculateBottomPadding()),contentPadding=PaddingValues(start=18.dp,end=18.dp,top=10.dp,bottom=20.dp),verticalArrangement=Arrangement.spacedBy(22.dp)){
  item{Row(verticalAlignment=Alignment.CenterVertically){Box(Modifier.weight(1f)){Brand()};IconButton(onClick={go("Search")}){Icon(Icons.Filled.Search,null,tint=Color.White,modifier=Modifier.size(29.dp))};IconButton(onClick={go("Settings")}){Icon(Icons.Filled.Settings,null,tint=Color.White,modifier=Modifier.size(28.dp))}}}
  item{Box(Modifier.fillMaxWidth().height(188.dp).clip(RoundedCornerShape(29.dp)).background(Brush.linearGradient(listOf(Color(0xFF6A16C9),Color(0xFF2C0A68),Color(0xFF090617))))){
   Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Purple.copy(.28f),Color.Transparent),center=androidx.compose.ui.geometry.Offset(700f,250f),radius=430f)))
   Column(Modifier.align(Alignment.CenterStart).padding(start=22.dp)){Text("Enjoy Your Music",fontSize=21.sp,fontWeight=FontWeight.Bold,color=Color.White);Spacer(Modifier.height(6.dp));Row(verticalAlignment=Alignment.CenterVertically){Text("With Design",fontSize=14.sp,color=Color(0xFFDCC8F8));Icon(Icons.Filled.Favorite,null,tint=Pink,modifier=Modifier.padding(start=5.dp).size(17.dp))}}
   Vinyl(Modifier.size(183.dp).align(Alignment.CenterEnd).offset(x=27.dp))
   Box(Modifier.align(Alignment.BottomEnd).padding(end=14.dp,bottom=13.dp).size(48.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Pink,Purple))),contentAlignment=Alignment.Center){Icon(Icons.Filled.PlayArrow,null,tint=Color.White,modifier=Modifier.size(32.dp))}
  }}
  item{Section("Quick Access","See All",{go("Library")})}
  item{Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){listOf("Songs" to Icons.Filled.MusicNote,"Albums" to Icons.Filled.Album,"Artists" to Icons.Filled.Person,"Playlists" to Icons.Filled.QueueMusic).forEach{(name,ic)->Column(Modifier.weight(1f).height(101.dp).clip(RoundedCornerShape(21.dp)).background(Brush.linearGradient(listOf(Color(0xFF211A35),Color(0xFF11101D)))).clickable{go(if(name=="Songs")"Songs" else name)}.padding(vertical=13.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){Icon(ic,null,tint=Brush.linearGradient(listOf(Pink,Purple)),modifier=Modifier.size(34.dp));Spacer(Modifier.height(7.dp));Text(name,color=Color.White,fontSize=11.sp)}}}}
  item{Section("Recently Played","See All",{go("Songs")})}
  item{LazyRow(horizontalArrangement=Arrangement.spacedBy(13.dp)){items(songs.take(4)){s->Column(Modifier.width(126.dp).clickable{open(s)}){Artwork(s.art,Modifier.fillMaxWidth().aspectRatio(1f));Spacer(Modifier.height(7.dp));Text(s.title,color=Color.White,fontWeight=FontWeight.SemiBold,fontSize=13.sp,maxLines=1);Text(s.artist,color=Muted,fontSize=11.sp)}}}}
 }
}
@Composable private fun Section(title:String,action:String,click:()->Unit){Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Text(title,color=Color.White,fontSize=18.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text(action,color=Lilac,fontSize=12.sp,modifier=Modifier.clickable{click()});Icon(Icons.Filled.ChevronRight,null,tint=Lilac,modifier=Modifier.size(18.dp))}}

@Composable private fun Vinyl(modifier:Modifier=Modifier){
 Box(modifier.clip(CircleShape).background(Brush.radialGradient(listOf(Color(0xFF8A39FF),Color(0xFF160B29),Color(0xFF030208)))) .border(1.dp,Pink.copy(.35f),CircleShape),contentAlignment=Alignment.Center){
  repeat(9){i->Box(Modifier.fillMaxSize(1f-i*.075f).border(1.dp,Color.White.copy(.09f),CircleShape))}
  Box(Modifier.fillMaxSize(.36f).clip(CircleShape).background(Brush.radialGradient(listOf(Pink,Purple,Color(0xFF16072D)))),contentAlignment=Alignment.Center){Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF0A0612)))}
 }
}
@Composable private fun Artwork(index:Int,modifier:Modifier=Modifier){
 val bg=when(index%5){0->listOf(Color(0xFF4B0878),Color(0xFF09030F),Color(0xFF19082E));1->listOf(Color(0xFF7435FF),Color(0xFF140A40),Color(0xFF05040D));2->listOf(Color(0xFFEE77CE),Color(0xFF432078),Color(0xFF100B26));3->listOf(Color(0xFF6025E8),Color(0xFF180B46),Color(0xFF05040D));else->listOf(Color(0xFF1378C8),Color(0xFF101C50),Color(0xFF05040D))}
 Box(modifier.clip(RoundedCornerShape(17.dp)).background(Brush.linearGradient(bg)).border(1.dp,Color.White.copy(.12f),RoundedCornerShape(17.dp)),contentAlignment=Alignment.Center){
  when(index%5){
   0->Column(horizontalAlignment=Alignment.CenterHorizontally){Text("✧",color=Pink,fontSize=26.sp);Text("♕",color=Color(0xFFFF9BEF),fontSize=68.sp);Text("✦  ✧  ✦",color=Pink,fontSize=14.sp)}
   1->Box(Modifier.fillMaxSize(.53f).clip(CircleShape).background(Brush.radialGradient(listOf(Color(0xFF160B50),Color(0xFF05040D)))).border(2.dp,Purple,CircleShape),contentAlignment=Alignment.Center){Icon(Icons.Filled.Person,null,tint=Color(0xFF080619),modifier=Modifier.size(70.dp))}
   2->Text("♡",color=Pink,fontSize=95.sp)
   3->Text("◈",color=Color.White,fontSize=75.sp)
   else->Icon(Icons.Filled.Person,null,tint=Color(0xFF80BFFF),modifier=Modifier.size(82.dp))
  }
 }
}
@Composable private fun MiniPlayer(s:Song,playing:Boolean,open:()->Unit,toggle:()->Unit){
 Row(Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=4.dp).clip(RoundedCornerShape(18.dp)).background(Brush.horizontalGradient(listOf(Color(0xFF21182F),Color(0xFF151121)))).clickable{open()}.padding(horizontal=10.dp,vertical=8.dp),verticalAlignment=Alignment.CenterVertically){
  Artwork(s.art,Modifier.size(48.dp));Spacer(Modifier.width(11.dp));Column(Modifier.weight(1f)){Text(s.title,color=Color.White,fontSize=13.sp,fontWeight=FontWeight.SemiBold);Text(s.artist,color=Muted,fontSize=11.sp)}
  IconButton(onClick=toggle){Icon(if(playing)Icons.Filled.Pause else Icons.Filled.PlayArrow,null,tint=Pink,modifier=Modifier.size(27.dp))}
  IconButton(onClick=open){Icon(Icons.Filled.SkipNext,null,tint=Color.White,modifier=Modifier.size(25.dp))}
 }
}
@Composable private fun LibraryScreen(go:(String)->Unit,open:(Song)->Unit){
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{Row(verticalAlignment=Alignment.CenterVertically){Text("Library",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Icon(Icons.Filled.Search,null,tint=Color.White,modifier=Modifier.size(26.dp))}}
  items(listOf("Songs" to "All your songs","Albums" to "All albums","Artists" to "All artists","Playlists" to "Your playlists")){(name,desc)->
   Row(Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(19.dp)).background(Brush.horizontalGradient(listOf(Color(0xFF201A30),Color(0xFF141221)))).clickable{go(name)}.padding(horizontal=14.dp),verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.size(40.dp).clip(RoundedCornerShape(13.dp)).background(Brush.linearGradient(listOf(Color(0xFF401A87),Color(0xFF160D2D)))),contentAlignment=Alignment.Center){when(name){"Songs"->Icon(Icons.Filled.MusicNote,null,tint=Pink,modifier=Modifier.size(30.dp));"Albums"->Text("●",color=Purple,fontSize=28.sp);"Artists"->Icon(Icons.Filled.Person,null,tint=Purple,modifier=Modifier.size(30.dp));else->Icon(Icons.Filled.QueueMusic,null,tint=Pink,modifier=Modifier.size(30.dp))}}
    Spacer(Modifier.width(14.dp));Column(Modifier.weight(1f)){Text(name,color=Color.White,fontWeight=FontWeight.SemiBold,fontSize=14.sp);Text(desc,color=Muted,fontSize=11.sp)};Icon(Icons.Filled.ChevronRight,null,tint=Muted)
   }
  }
  item{Section("Recently Added","See All",{go("Songs")})}
  item{Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){songs.take(3).forEach{s->Artwork(s.art,Modifier.weight(1f).aspectRatio(1f).clickable{open(s)})}}}
  items(songs){s->Row(Modifier.fillMaxWidth().clickable{open(s)}.padding(vertical=4.dp),verticalAlignment=Alignment.CenterVertically){Artwork(s.art,Modifier.size(48.dp));Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f)){Text(s.title,color=Color.White,fontSize=13.sp,fontWeight=FontWeight.SemiBold);Text(s.artist,color=Muted,fontSize=11.sp)};Icon(Icons.Filled.MoreHoriz,null,tint=Muted)}}
 }
}
@Composable private fun SearchScreen(q:String,onQ:(String)->Unit,open:(Song)->Unit){
 Column(Modifier.fillMaxSize().padding(18.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text("Search",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold)}
 Spacer(Modifier.height(16.dp));OutlinedTextField(q,onQ,Modifier.fillMaxWidth(),placeholder={Text("Songs, artists, albums...",color=Muted)},leadingIcon={Icon(Icons.Filled.Search,null,tint=Muted)},singleLine=true,shape=RoundedCornerShape(18.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Pink,unfocusedBorderColor=Panel2,focusedTextColor=Color.White,unfocusedTextColor=Color.White))
 Spacer(Modifier.height(18.dp));Text(if(q.isBlank())"Popular right now" else "Results",color=Color.White,fontSize=18.sp,fontWeight=FontWeight.Bold)
 songs.filter{it.title.contains(q,true)||it.artist.contains(q,true)}.forEach{s->Row(Modifier.fillMaxWidth().clickable{open(s)}.padding(vertical=8.dp),verticalAlignment=Alignment.CenterVertically){Artwork(s.art,Modifier.size(52.dp));Spacer(Modifier.width(12.dp));Column{Text(s.title,color=Color.White,fontWeight=FontWeight.SemiBold);Text(s.artist,color=Muted,fontSize=11.sp)}}}
 }
}
@Composable private fun SongsScreen(open:(Song)->Unit){LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(18.dp)){item{Text("Songs",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold)};items(songs){s->Row(Modifier.fillMaxWidth().clickable{open(s)}.padding(vertical=8.dp),verticalAlignment=Alignment.CenterVertically){Artwork(s.art,Modifier.size(55.dp));Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(s.title,color=Color.White,fontWeight=FontWeight.SemiBold);Text(s.artist,color=Muted,fontSize=11.sp)};Text(s.time,color=Muted,fontSize=11.sp)}}}}
@Composable private fun GridCollection(title:String,names:List<String>,open:()->Unit){
 Column(Modifier.fillMaxSize().padding(18.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(title,color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Icon(Icons.Filled.Search,null,tint=Color.White)}
 LazyColumn{items(names.chunked(2)){row->Row(horizontalArrangement=Arrangement.spacedBy(12.dp),modifier=Modifier.padding(top=14.dp)){row.forEachIndexed{idx,n->Column(Modifier.weight(1f).clickable{open()}){Artwork(idx,Modifier.fillMaxWidth().aspectRatio(1f));Spacer(Modifier.height(5.dp));Text(n,color=Color.White,fontSize=13.sp,fontWeight=FontWeight.SemiBold);Text(if(title=="Albums")if(idx==0)"BLACKPINK" else "BTS" else "Your collection",color=Muted,fontSize=11.sp)}};if(row.size==1)Spacer(Modifier.weight(1f))}}}
 }
}
@Composable private fun ArtistScreen(){Column(Modifier.fillMaxSize().padding(18.dp)){Text("Artists",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(15.dp));artists.forEachIndexed{i,a->Row(Modifier.fillMaxWidth().padding(bottom=10.dp).clip(RoundedCornerShape(19.dp)).background(Panel).padding(10.dp),verticalAlignment=Alignment.CenterVertically){Artwork(i,Modifier.size(64.dp).clip(CircleShape));Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(a,color=Color.White,fontWeight=FontWeight.SemiBold);Text(listOf("5 songs","8 songs","3 songs","4 songs")[i],color=Muted,fontSize=11.sp)};Icon(Icons.Filled.ChevronRight,null,tint=Muted)}}}}
@Composable private fun PlayerScreen(s:Song,playing:Boolean,progress:Float,onProgress:(Float)->Unit,toggle:()->Unit,back:()->Unit,next:()->Unit){
 Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF160825),Bg,Bg))).padding(horizontal=25.dp,vertical=10.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.SpaceBetween){
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){IconButton(onClick=back){Icon(Icons.Filled.KeyboardArrowDown,null,tint=Color.White)};Spacer(Modifier.weight(1f));Icon(Icons.Filled.MoreVert,null,tint=Color.White)}
  Artwork(s.art,Modifier.fillMaxWidth(.88f).aspectRatio(1f))
  Column(Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally){Text(s.title,color=Color.White,fontSize=22.sp,fontWeight=FontWeight.Bold);Text(s.artist,color=Pink,fontSize=14.sp);Spacer(Modifier.height(15.dp));Slider(progress,onProgress,colors=SliderDefaults.colors(thumbColor=Pink,activeTrackColor=Pink,inactiveTrackColor=Panel2));Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("01:24",color=Muted,fontSize=11.sp);Text(s.time,color=Muted,fontSize=11.sp)}
   Spacer(Modifier.height(14.dp));Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Icon(Icons.Filled.Shuffle,null,tint=Muted);Icon(Icons.Filled.SkipPrevious,null,tint=Color.White,modifier=Modifier.size(28.dp).clickable{back()});Box(Modifier.size(66.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Pink,Purple))).clickable{toggle()},contentAlignment=Alignment.Center){Icon(if(playing)Icons.Filled.Pause else Icons.Filled.PlayArrow,null,tint=Color.White,modifier=Modifier.size(34.dp))};Icon(Icons.Filled.SkipNext,null,tint=Color.White,modifier=Modifier.size(28.dp).clickable{next()});Icon(Icons.Filled.Repeat,null,tint=Muted)}
  }
  Row(Modifier.fillMaxWidth().padding(bottom=8.dp),horizontalArrangement=Arrangement.SpaceBetween){Icon(Icons.Filled.Devices,null,tint=Muted);Icon(Icons.Filled.QueueMusic,null,tint=Muted)}
 }
}
@Composable private fun SettingsScreen(){Column(Modifier.fillMaxSize().padding(20.dp)){Text("Settings",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(15.dp));Text("Design Music Player",color=Muted,fontSize=14.sp);Text("Purple glow • Dark experience",color=Muted,fontSize=12.sp)}}
