package come.hasan.foraty.note.model

import android.graphics.Color

data class Note(
    var title:String? = null,
    var content:String,
    val tag:List<Tag> = mutableListOf(),
    var pictureURl:String? = null
){
    companion object{
        fun mock()=Note(
            "Test Title ",
            "Hello its content of first Test",
            listOf(Tag("Sport")),
        )
    }
}

data class Tag(
    var name:String,
    var color: Int = Color.WHITE
)