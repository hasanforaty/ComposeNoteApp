package come.hasan.foraty.note.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import come.hasan.foraty.note.R
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.ui.theme.PapayaWhip
import come.hasan.foraty.note.viewmodel.MainViewModel
import java.util.*
import kotlin.math.roundToInt

@ExperimentalMaterialApi
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
    val numberOfItemSelected = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = viewModel) {
        viewModel.getAllNotes()
    }
    val noteMode: MutableState<NoteMode> = remember {
        mutableStateOf(NoteMode.DefaultMode)
    }
    val onNotePress: (Note) -> Unit = {
        noteMode.value = NoteMode.SelectingMode
    }

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
        },
        bottomBar = {
            MainBottomAppBar(appMode = noteMode.value, onCancelClicked = {
                viewModel.onCancelSelected()
                noteMode.value = NoteMode.DefaultMode
            }, onDeleteClicked = {
                noteMode.value = NoteMode.DefaultMode
                viewModel.deleteSelected()
            }, numberOfSelected = numberOfItemSelected.value
            )
        },
        drawerContent = {
            for (menu in menus) {
                MenuItem(menu = menu, onMenuItemSelected = onMenuItemSelected)
            }
        }
    ) {
        NoteList(
            notes.value, onNoteSelected = onNoteSelected, mode = noteMode.value,
            onCheckedChange = { isSelected: Boolean, note: Note ->
                if (isSelected) {
                    numberOfItemSelected.value++
                } else {
                    numberOfItemSelected.value--
                }
                viewModel.onNoteCheckedChange(note, isSelected = isSelected)
            },
            onNotePress = onNotePress,
            onNoteDelete = {note ->
                viewModel.deleteNote(note=note)
            },
            onSwiped = {note ->
                viewModel.deleteNote(note = note)
            }
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun NoteList(
    notes: List<Note>,
    onNoteSelected: ((noteId: UUID) -> Unit)?,
    mode: NoteMode,
    onCheckedChange: ((Boolean, Note) -> Unit)?,
    onNotePress: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit,
    onSwiped: (Note) -> Unit
) {
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
                SwipeableNoteViewList(
                    note = note, onNoteSelected = onNoteSelected, mode = mode,
                    onCheckedChange = onCheckedChange,
                    onNotePress = onNotePress,
                    onNoteDelete = onNoteDelete,
                    onSwiped = onSwiped
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@ExperimentalAnimationApi
fun SwipeableNoteViewList(
    note: Note,
    onNoteSelected: ((noteId: UUID) -> Unit)?,
    mode: NoteMode,
    onCheckedChange: ((Boolean, Note) -> Unit)?,
    onNotePress: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit,
    onSwiped:(Note)->Unit
) {
    val visible = remember {
        mutableStateOf(true)
    }
    val iconVisible = remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(visible = visible.value) {
        Box(modifier = Modifier) {
                ForeGroundNoteList(
                    onSwiped = {
                        visible.value = false
                        onSwiped.invoke(note)
                    },
                    onNotePress = onNotePress,
                    onCheckedChange = onCheckedChange,
                    mode = mode,
                    onNoteSelected = onNoteSelected,
                    note = note,
                    onMiddle = {
                        iconVisible.value = true
                    },
                    onVisibleState = {
                        iconVisible.value = false
                    }
                )
                BackGroundNoteList(
                    iconVisible = iconVisible.value ,
                    modifier = Modifier.align(Alignment.TopEnd),
                    onDeleteIconClick = {
                        visible.value = false
                        onNoteDelete.invoke(note)
                    })

            }
    }
}

enum class SwipeState {
    SWIPED, VISIBLE, MIDDLE
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ForeGroundNoteList(
    onSwiped: () -> Unit,
    onMiddle: () -> Unit,
    onVisibleState:()->Unit,
    onNotePress: (Note) -> Unit,
    onCheckedChange: ((Boolean, Note) -> Unit)?,
    mode: NoteMode,
    onNoteSelected: ((noteId: UUID) -> Unit)?,
    note: Note
) {
    val swipeState = rememberSwipeableState(
        initialValue = SwipeState.VISIBLE,
        confirmStateChange = {
            when(it){
                SwipeState.SWIPED -> onSwiped.invoke()
                SwipeState.MIDDLE ->onMiddle.invoke()
                SwipeState.VISIBLE -> onVisibleState.invoke()
            }
            true
        }
    )

    val swipeAnchor = mapOf(
        (0f to SwipeState.VISIBLE),
        (-1000f to SwipeState.SWIPED),
        (-500f to SwipeState.MIDDLE)
    )

    NoteViewList(
        note = note,
        onNoteSelected = onNoteSelected,
        mode = mode,
        onCheckedChange = onCheckedChange,
        onNotePress = onNotePress,
        modifier = Modifier
            .swipeable(
                state = swipeState,
                anchors = swipeAnchor,
                orientation = Orientation.Horizontal
            )
            .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
            .background(MaterialTheme.colors.background)

    )

}

@ExperimentalAnimationApi
@Composable
fun BackGroundNoteList(modifier: Modifier, onDeleteIconClick: () -> Unit,iconVisible:Boolean) {
    Row(horizontalArrangement = Arrangement.End, modifier = modifier) {
        AnimatedVisibility(visible = iconVisible) {
            IconButton(onClick = { onDeleteIconClick.invoke() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
            }
        }

    }
}


@ExperimentalAnimationApi
@Composable
fun NoteViewList(
    note: Note, onNoteSelected: ((noteId: UUID) -> Unit)?, mode: NoteMode,
    onCheckedChange: ((Boolean, Note) -> Unit)?,
    onNotePress: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val checked = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .shadow(5.dp)
            .padding(5.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onNotePress(note)
                    },
                    onTap = {
                        if (onNoteSelected != null) {
                            onNoteSelected(note.id)
                        }
                    }
                )
            },
        backgroundColor = PapayaWhip,
        shape = RoundedCornerShape(2.dp)
    ) {
        Column {
            Row {
                Text(
                    text = note.title,
                    modifier = Modifier.fillMaxWidth(0.90f),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                AnimatedVisibility(visible = mode == NoteMode.SelectingMode) {
                    Checkbox(checked = checked.value,
                        modifier = Modifier
                            .border(BorderStroke(1.dp, color = PapayaWhip)),
                        onCheckedChange = {
                            checked.value = !checked.value
                            if (onCheckedChange != null) {
                                onCheckedChange(it, note)
                            }
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

@ExperimentalAnimationApi
@Composable
fun MainBottomAppBar(
    appMode: NoteMode,
    numberOfSelected: Int,
    onCancelClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AnimatedVisibility(visible = appMode == NoteMode.SelectingMode) {
        BottomAppBar {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .shadow(3.dp, shape = RoundedCornerShape(3.dp)),
                shape = RoundedCornerShape(7.dp),
            ) {
                Row {
                    TextButton(onClick = { onCancelClicked() }) {
                        Text(text = LocalContext.current.getString(R.string.Cancel))
                    }
                    TextButton(onClick = { onDeleteClicked() }) {
                        Text(text = "${LocalContext.current.getString(R.string.Delete)}($numberOfSelected)")
                    }
                }
            }
        }
    }

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

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevNoteList() {
    NoteList(
        notes = MainViewModel.notesMock(),
        onNoteSelected = null,
        mode = NoteMode.SelectingMode,
        onCheckedChange = null,
        onNotePress = {},
        onSwiped = {},
        onNoteDelete = {}
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevBottomAppBar() {
    MainBottomAppBar(
        appMode = NoteMode.SelectingMode,
        numberOfSelected = 10,
        onDeleteClicked = {},
        onCancelClicked = {})
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PreNoteViewList() {
    NoteViewList(note = Note.mock(), null, NoteMode.SelectingMode, null, onNotePress = {})
}

sealed class NoteMode {
    object SelectingMode : NoteMode()
    object DefaultMode : NoteMode()
}
