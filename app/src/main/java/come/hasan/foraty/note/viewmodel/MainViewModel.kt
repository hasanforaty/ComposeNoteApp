package come.hasan.foraty.note.viewmodel

import androidx.lifecycle.ViewModel
import come.hasan.foraty.note.model.Note
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@AndroidEntryPoint
class MainViewModel @Inject constructor():ViewModel(){
    @Inject lateinit var note:Note
}