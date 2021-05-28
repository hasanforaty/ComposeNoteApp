package come.hasan.foraty.note.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import come.hasan.foraty.note.R
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.ui.theme.PapayaWhip
import come.hasan.foraty.note.viewmodel.MainViewModel
import java.util.*

@ExperimentalAnimationApi
@Composable
fun MainNoteList(
    viewModel: MainViewModel,
    onMenuItemSelected: (route: String) -> Unit,
    onNoteSelected: (noteId: UUID) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val title = stringResource(id = R.string.title)
    var moreIcon = Icons.Default.Menu
    val notes = viewModel.notes.observeAsState(initial = emptyList())
    LaunchedEffect(key1 = viewModel){
        viewModel.getAllNotes()
    }
    val noteMode:MutableState<NoteMode> = remember {
        mutableStateOf(NoteMode.SelectingMode)
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = title, menus = menus, expended = expanded.value,
                onDismissRequest = {
                    moreIcon = Icons.Default.Menu
                    expanded.value = false
                    if (noteMode.value == NoteMode.SelectingMode){
                        noteMode.value = NoteMode.DefaultMode
                    }else{
                        noteMode.value = NoteMode.SelectingMode
                    }
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
        NoteList(notes.value, onNoteSelected = onNoteSelected,mode = noteMode.value)
    }
}

@ExperimentalAnimationApi
@Composable
fun NoteList(notes: List<Note>, onNoteSelected: ((noteId: UUID) -> Unit)?, mode: NoteMode) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
        ) {
            items(items = notes) { note ->
                NoteViewList(note = note, onNoteSelected = onNoteSelected,mode = mode)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NoteViewList(note: Note, onNoteSelected: ((noteId: UUID) -> Unit)?,mode:NoteMode) {
    val checked = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .clickable {
                if (onNoteSelected != null) {
                    onNoteSelected(note.id)
                }
            }
            .shadow(5.dp)
            .padding(5.dp)
            .fillMaxWidth()
        ,
        backgroundColor = PapayaWhip,
        shape = RoundedCornerShape(2.dp)
    ) {
        Column {
            Row{
                Text(
                    text = note.title,
                    modifier = Modifier.fillMaxWidth(0.90f),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                AnimatedVisibility(visible = mode == NoteMode.SelectingMode) {
                    Checkbox(checked = checked.value,
                        modifier = Modifier
                            .border(BorderStroke(1.dp,color = PapayaWhip))
                        ,
                        onCheckedChange = {
                            checked.value = !checked.value
                        })
                }
            }
            Row {
                Text(
                    text = note.content,
                    maxLines = 1,
                    modifier = Modifier.padding(2.dp),
                    color = MaterialTheme.colors.onBackground.copy(0.4f)
                )
                note.pictureURl?.let { url ->
                    Image(
                        painter = rememberGlidePainter(request = url), contentDescription = ""
                    )
                }
            }
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
            Menus(
                menus = menus,
                expended = expended,
                onDismissRequest = onDismissRequest,
                onMenuItemSelected = onMenuItemSelected
            )
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
        DropdownMenu(
            expanded = expended,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.size(125.dp)
        ) {
            for (menu in menus) {
                MenuItem(menu = menu, onMenuItemSelected = onMenuItemSelected)
            }
        }
    }

}

@Composable
fun MenuItem(menu: Destinations, onMenuItemSelected: (route: String) -> Unit) {
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
    MenuItem(menu = menus[0]) {}
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevNoteList() {
    NoteList(notes = MainViewModel.notesMock(), onNoteSelected = null,NoteMode.SelectingMode)
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PreNoteViewList() {
    NoteViewList(note = Note.mock(), null,NoteMode.SelectingMode)
}
sealed class NoteMode{
    object SelectingMode: NoteMode()
    object DefaultMode:NoteMode()
}
