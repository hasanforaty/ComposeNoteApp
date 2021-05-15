package come.hasan.foraty.note.screens

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import come.hasan.foraty.note.R

val localNavController = compositionLocalOf <NavHostController> {
    error("nav controller isn't init")
}

@Composable
fun Navigation(startDestination: String) {
    NavHost(
        navController = localNavController.current,
        startDestination = startDestination
    ){
        composable(Destinations.Home.route){
            MainNoteList()
      }
        composable(Destinations.NewNote.route){

        }
    }
}

open class Destinations(
    val title:String,
    val route:String,
    val vector: ImageVector? = null,
    @DrawableRes val drawableRes:Int?=null
){
    object Home:Destinations("home","home", Icons.Default.Home)
    object NewNote:Destinations(
        "New Note",
        "NewNote",
        drawableRes=R.drawable.ic_baseline_open_in_new_24)
}