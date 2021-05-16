package come.hasan.foraty.note.screens

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import come.hasan.foraty.note.R
import come.hasan.foraty.note.viewmodel.MainViewModel
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.navigate


@Composable
fun NoteMain(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.Home.route
    ){
        composable(Destinations.Home.route){navBackStack ->
            val viewModel = hiltNavGraphViewModel<MainViewModel>(backStackEntry = navBackStack)
            MainNoteList(
                viewModel=viewModel,
                onMenuItemSelected = { navController.navigate(it) }
            )
        }
        composable(Destinations.NewNote.route){

        }
    }
}

sealed class Destinations(
    val title:String,
    val route:String,
    val vector: ImageVector? = null,
    @DrawableRes val drawableRes:Int?=null
){
    object Home:Destinations("home","home", Icons.Default.Home)
    object NewNote:Destinations(
        "New Note",
        "NewNote",
        drawableRes= R.drawable.ic_baseline_open_in_new_24)
}

