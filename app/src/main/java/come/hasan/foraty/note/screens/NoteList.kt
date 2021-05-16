package come.hasan.foraty.note.screens

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import come.hasan.foraty.note.R
import come.hasan.foraty.note.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "NoteList"
@Composable
fun MainNoteList() {
    val expanded = remember {
        mutableStateOf(false)
    }
    val title = stringResource(id = R.string.title)
    var moreIcon = Icons.Default.Menu
    val viewModel = remember {
        MainViewModel()
    }
    Scaffold(
        topBar = {
            MainTopAppBar(
                title = title, menus = menus, expended = expanded.value
            ) {
                moreIcon = Icons.Default.Menu
                expanded.value = false
            }
            IconButton(onClick = {
                moreIcon = Icons.Default.MoreVert
                expanded.value = true
            }) {
                Icon(moreIcon, contentDescription = "more")
            }
        }
    ) {
        NoteList(viewModel)
    }
}

@Composable
fun NoteList(viewModel: MainViewModel){
    LazyColumn() {
        item{
            Log.d(TAG, "NoteList: ${viewModel.note}")
        }
    }

}

@Composable
fun MainTopAppBar(
    title: String,
    menus: List<Destinations>,
    expended: Boolean,
    onDismissRequest: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            Menus(menus = menus,expended = expended,onDismissRequest = onDismissRequest)
        }
    )
}

@Composable
fun Menus(menus: List<Destinations>, expended: Boolean, onDismissRequest: () -> Unit) {
    Card(
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier.padding(2.dp)
    ) {
        DropdownMenu(expanded = expended,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.size(125.dp)
        ) {
            for (menu in menus) {
                MenuItem(menu = menu)
            }
        }
    }

}

@Composable
fun MenuItem(menu:Destinations){
    val navController = localNavController.current
    DropdownMenuItem(
        onClick = { navController.navigate(menu.route) },
    ) {
        Card {
            Row {
                menu.vector?.let {
                    Icon(imageVector = it, contentDescription = menu.title)
                }
                menu.drawableRes?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = menu.title
                    )
                }
                Text(
                    text = menu.title,
                )
            }
        }
    }
}



val menus = listOf(
    Destinations.Home,
    Destinations.NewNote
)

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PreviewMenu() {
    val navController = rememberNavController()
    CompositionLocalProvider(localNavController provides navController) {
                MenuItem(menu = menus[0])
    }
}

@Preview(showBackground = true,backgroundColor = 0xffffff)
@Composable
fun PrevNoteList(){
    MainNoteList()
}
