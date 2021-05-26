package come.hasan.foraty.note.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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

@Composable
fun MainNote(
    viewModel: MainViewModel,
    isNewNote: Boolean = false
) {
    val note: Note = viewModel.currentNote.value ?: Note()
    val content = remember {
        mutableStateOf(note.content)
    }
    val title = remember {
        mutableStateOf(note.title)
    }
    val date = remember {
        mutableStateOf(note.date)
    }
    val tags = remember {
        mutableStateListOf(note.tag)
    }

    NoteViewContent(title = title.value, onTitleChange = { title.value = it })
}

@Composable
fun NoteViewContent(title: String, onTitleChange: (String) -> Unit) {
    Column {
        Row {
            OutlinedTextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onTitleChange(it) },
                label = {Text(text = "Title") },
                placeholder = { Text(text = "Enter Title")}
            )
        }

    }
}

@Composable
fun Tag(tag:Tag){
    Card(
        shape = RoundedCornerShape(topStart = 10.dp),
        modifier = Modifier
            .wrapContentSize(align = Alignment.TopStart)
            .padding(10.dp)
            .shadow(50.dp)

        ,
        backgroundColor = Color(tag.color)
    ) {
        Text(
            text = tag.name,
            modifier = Modifier
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevNoteView() {
    val note = Note.mock()
    Surface(Modifier.size(800.dp)) {
        NoteViewContent(title = note.title) {
            note.title = it
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PrevTag(){
    val tag = Tag("Sport",Color.LightGray.value)
    Surface(Modifier.size(400.dp)) {
        Row {
            Tag(tag =tag)
            Tag(tag =tag)
        }
    }
}
