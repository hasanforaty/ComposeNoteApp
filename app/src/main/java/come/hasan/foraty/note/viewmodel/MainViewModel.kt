package come.hasan.foraty.note.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.model.Tag
import come.hasan.foraty.note.reposetory.RoomNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteRepository: RoomNoteRepository
):ViewModel() {

    private val noteList:MutableLiveData<MutableList<Note>> = MutableLiveData<MutableList<Note>>()
    val  notes:LiveData<List<Note>> = Transformations.map(noteList){ notes ->
        Log.d(TAG, "notes LiveData: updated $notes")
        notes
    }
    private val note:MutableLiveData<Note> = MutableLiveData<Note>()
    val currentNote:LiveData<Note> = Transformations.map(note){
        note->
        note
    }

    val currentNoteTitle:MutableLiveData<String> = Transformations.map(note){
        Log.d(TAG, "current note title : $it")
        it.title
    } as MutableLiveData<String>
    fun changeTitle(title:String){
        currentNote.value?.title = title
        currentNoteTitle.value = title
    }
    val currentNoteContent:MutableLiveData<String> = Transformations.map(note){
        it.content
    } as MutableLiveData<String>
    fun changeContent(content:String){
        currentNote.value?.content  = content
        currentNoteContent.value = content
    }
    val currentTags:MutableLiveData<List<Tag>> = Transformations.map(note){
        it.tag
    }as MutableLiveData<List<Tag>>
    fun changeTag(tag: Tag){
        val tags = currentTags.value
        if (tags?.contains(tag) == true){
            return
        }
        val currentList = mutableListOf<Tag>()
        if (tags != null) {
            currentList.addAll(tags)
        }
        currentList.add(tag)
        currentTags.value = currentList
    }
    fun deleteTag(tag: Tag){
        val tags = currentTags.value
        if (tags?.contains(tag)==true){
            val currentList = mutableListOf<Tag>()
            currentList.addAll(tags)
            currentList.remove(tag)
            currentTags.value = currentList
        }
    }


    fun getAllNotes(){
        CoroutineScope(IO).launch {
            val allNotes=noteRepository.getAllNotes()
            Log.d(TAG, "getAllNotes: notes : $allNotes")
            allNotes.sortedBy { note ->
                note.date
            }
            withContext(Main){
                noteList.value = allNotes.toMutableList()
            }
        }
    }
    fun getNoteById(id:UUID){
        CoroutineScope(IO).launch {
            val receivedNote = noteRepository.getNoteById(id)
            withContext(Main){
                note.value = receivedNote
            }
        }
    }
    fun getNote(receivedNote:Note){
        CoroutineScope(IO).launch {
            withContext(Main){
                note.value = receivedNote
            }
        }
    }
    fun queryContent(query:String){
        CoroutineScope(IO).launch {
            val wantedNotes = noteRepository.searchNoteContent(query)
            noteList.value = wantedNotes.toMutableList()
        }
    }

    fun addNote(note:Note){
        CoroutineScope(IO).launch {
            noteRepository.addNewNote(note)
        }
    }

    fun deleteNote(note: Note){
        CoroutineScope(IO).launch {
            noteRepository.deleteNote(note)
        }
    }

    private val noteSelected:MutableSet<Note> = mutableSetOf()
    fun onNoteCheckedChange(note: Note, isSelected:Boolean){
        if (isSelected){
            noteSelected.add(note)
        }else{
            noteSelected.remove(note)
        }
    }
    fun onCancelSelected(){
        noteSelected.clear()
    }

    fun deleteSelected(){
        CoroutineScope(IO).launch {
            val listOfSelected = noteSelected.toList()
            noteSelected.clear()
            listOfSelected.forEach { note ->
                deleteNote(note)
            }
            val currentNote = noteList.value
            currentNote?.removeAll(listOfSelected)
            withContext(Main){
                noteList.value = currentNote
            }
        }
    }


    companion object{
        fun notesMock():List<Note> = listOf(
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
        )
    }

}