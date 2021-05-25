package come.hasan.foraty.note.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Note(
    var title:String? = null,
    var content:String,
    var date: Date = Date(),
    val tag:List<Tag> = mutableListOf(),
    var pictureURl:String? = null,
    @PrimaryKey val id:UUID = UUID.randomUUID()
){
    companion object{
        fun mock()=Note(
            title ="Test Title ",
            content = "Hello its content of first Test",
            tag = listOf(Tag("Sport")),
        )
    }
}

data class Tag(
    var name:String,
    var color: Int = Color.WHITE
)