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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import come.hasan.foraty.note.R
import come.hasan.foraty.note.viewmodel.MainViewModel

private const val TAG = "NoteList"
@Composable
fun MainNoteList(viewModel: MainViewModel,onMenuItemSelected:(route:String)->Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val title = stringResource(id = R.string.title)
    var moreIcon = Icons.Default.Menu
    Scaffold(
        topBar = {
            MainTopAppBar(
                title = title, menus = menus, expended = expanded.value,
                onDismissRequest = {
                    moreIcon = Icons.Default.Menu
                    expanded.value = false
                },
                onMenuItemSelected = onMenuItemSelected
            )
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

        }
    }

}

@Composable
fun MainTopAppBar(
    title: String,
    menus: List<Destinations>,
    expended: Boolean,
    onDismissRequest: () -> Unit,
    onMenuItemSelected: (route: String) -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            Menus(menus = menus, expended = expended, onDismissRequest = onDismissRequest,onMenuItemSelected = onMenuItemSelected)
        }
    )
}

@Composable
fun Menus(
    menus: List<Destinations>,
    expended: Boolean,
    onDismissRequest: () -> Unit,
    onMenuItemSelected: (route: String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier.padding(2.dp)
    ) {
        DropdownMenu(expanded = expended,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.size(125.dp)
        ) {
            for (menu in menus) {
                MenuItem(menu = menu,onMenuItemSelected = onMenuItemSelected)
            }
        }
    }

}

@Composable
fun MenuItem(menu:Destinations,onMenuItemSelected: (route: String) -> Unit){
    DropdownMenuItem(
        onClick = { onMenuItemSelected(menu.route) },
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
                MenuItem(menu = menus[0],{})
}

@Preview(showBackground = true,backgroundColor = 0xffffff)
@Composable
fun PrevNoteList(){
    val viewModel:MainViewModel =  viewModel()
    MainNoteList(viewModel = viewModel,{})
}
