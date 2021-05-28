package come.hasan.foraty.note.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.model.Tag
import come.hasan.foraty.note.viewmodel.MainViewModel

private const val TAG = "Note"

@Composable
fun MainNote(
    viewModel: MainViewModel,
    isNewNote: Boolean = false,
    navigateBack:()->Unit
) {

    val note = viewModel.currentNote.observeAsState(initial = Note())
    if (isNewNote){
        viewModel.getNote(note.value)
    }
    val content = viewModel.currentNoteContent.observeAsState(initial = "")
    val title = viewModel.currentNoteTitle.observeAsState(initial = "")
    val tags = remember {
        mutableStateOf(note.value.tag)
    }

    DisposableEffect(key1 = note) {
        onDispose {
            Log.d(TAG, "MainNote: reach add note")
            val currentNote = note.value
            viewModel.addNote(note =currentNote)
        }
    }
    Scaffold(
        topBar = { NoteTopAppBar(navigateBack = navigateBack) }
    ) {
        NoteViewContent(
            title = title.value,
            onTitleChange = { viewModel.changeTitle(it) },
            tags = tags.value,
            content = content.value,
            onContentChange = {viewModel.changeContent(it)}
        )
    }

}
@Composable
fun NoteTopAppBar(navigateBack: () -> Unit){
    TopAppBar(title = {},
        navigationIcon = {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = Modifier.clickable {
                navigateBack()
            }
        )
    })
}
@Composable
fun NoteViewContent(
    title: String,
    onTitleChange: (String) -> Unit,
    tags: List<Tag>,
    content: String = "",
    onContentChange: (String) -> Unit
) {
    Column {
        NoteTitle(title = title, onTitleChange = onTitleChange)
        TagList(tags = tags)
        NoteContent(content = content, onContentChanged = onContentChange)
    }
}
@Composable
fun NoteContent(
    content: String,
    onContentChanged: (String) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(10.dp, shape = RoundedCornerShape(7.dp))
            .padding(5.dp)
        ,
        shape = RoundedCornerShape(7.dp)
    ) {
        OutlinedTextField(value = content,onValueChange = {onContentChanged(it)})
    }
}
@Composable
fun NoteTitle(title: String, onTitleChange: (String) -> Unit) {
    OutlinedTextField(
        value = title,
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(0.dp, Color.LightGray)),
        onValueChange = { onTitleChange(it) },
        label = { Text(text = "Title") },
        placeholder = { Text(text = "Enter Title") },
    )
}

@Composable
fun TagView(tag: Tag) {
    val fillIcon = remember {
        mutableStateOf(false)
    }
    val icons = if (fillIcon.value) {
        Icons.Filled.Star
    } else {
        Icons.Outlined.Star
    }
    Card(
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        modifier = Modifier
            .wrapContentSize(align = Alignment.TopStart)
            .padding(5.dp)
            .shadow(50.dp)
            .clickable {
                fillIcon.value = !fillIcon.value
            },
        backgroundColor = Color(tag.color)
    ) {
        Row {
            Icon(icons, "")
            Text(
                text = tag.name,
                modifier = Modifier.padding(5.dp)
            )
        }

    }
}

@Composable
fun TagList(tags: List<Tag>) {
    Column {
        LazyRow(state = rememberLazyListState()) {
            items(items = tags) { tag ->
                TagView(tag = tag)
            }
            item {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "",
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevNoteTopAppBar(){
    NoteTopAppBar {}
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevNoteView() {
    val note = Note.mock()
    val tag = Tag("Sport", Color.LightGray.value)
    val tags = listOf(
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
    )
    Surface(Modifier.size(800.dp)) {
        NoteViewContent(
            title = note.title,
            onTitleChange = {
                note.title = it
            },
            tags = tags,
            content = note.content,
            onContentChange = {note.content = it}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevTagList() {
    val tag = Tag("Sport", Color.LightGray.value)
    val tags = listOf(
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
        tag,
    )
    TagList(tags = tags)
}

