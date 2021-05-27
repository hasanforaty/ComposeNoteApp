package come.hasan.foraty.note.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.model.Tag
import come.hasan.foraty.note.viewmodel.MainViewModel

@Composable
fun MainNote(
    viewModel: MainViewModel,
    isNewNote: Boolean = false
) {
    val note: Note = remember {
        if (isNewNote) {
            Note()
        } else {
            viewModel.currentNote.value!!
        }
    }
    val content = remember {
        mutableStateOf(note.content)
    }
    val title = remember {
        mutableStateOf(note.title)
    }
    val date = note.date
    val tags = remember {
        mutableStateOf(note.tag)
    }

    NoteViewContent(
        title = title.value,
        onTitleChange = { title.value = it },
        tags = tags.value
    )
}

@Composable
fun NoteViewContent(title: String, onTitleChange: (String) -> Unit, tags: List<Tag>) {
    Column {
        NoteTitle(title = title, onTitleChange = onTitleChange)
        TagList(tags = tags)

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
        }
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
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
            tags = tags
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

