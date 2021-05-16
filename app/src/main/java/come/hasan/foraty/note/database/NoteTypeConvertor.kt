package come.hasan.foraty.note.database

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import come.hasan.foraty.note.model.Tag
import java.util.*

class NoteTypeConvertor {
    @TypeConverter
    fun fromTagList(tags: List<Tag>): List<String> {
        return tags.map { tag ->
            "${tag.name} ${tag.color}"
        }
    }
    @TypeConverter
    fun toTagList(stringList: List<String>):List<Tag>{
        return stringList.map { string ->
            val name = string.substringBefore("/")
            val color = string.substringAfter("/").toInt()
            return@map Tag(name,color = color)
        }
    }
    @TypeConverter
    fun fromUUID(id:UUID):String= id.toString()
    @TypeConverter
    fun toUUID(id:String):UUID = UUID.fromString(id)
}