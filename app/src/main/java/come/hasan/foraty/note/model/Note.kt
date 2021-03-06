package come.hasan.foraty.note.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    var title:String = "",
    var content:String = "",
    var date: Date = Date(),
    var tag:List<Tag> = mutableListOf(),
    var pictureURl:String? = null,
    @PrimaryKey val id:UUID = UUID.randomUUID()
){
    companion object{
        fun mock()=Note(
            title ="Long Test Title Test Title Test Title Test Title Test Title Test Title Test Title Test Title Test Title Test Title ",
            content = "Hello its content of first Test",
            tag = listOf(Tag("Sport")),
        )
    }
}

data class Tag(
    var name:String,
    var color: ULong = Color.White.value
)