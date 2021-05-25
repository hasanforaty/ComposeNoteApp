package come.hasan.foraty.note.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import come.hasan.foraty.note.model.Tag
import java.util.*

class NoteTypeConvertor {
    @TypeConverter
    fun fromTagList(tags: List<Tag>): String {
        return Gson().toJson(tags)
    }

    @TypeConverter
    fun toTagList(stringList: String):List<Tag>{
        val listTags = object :TypeToken<List<Tag>>(){}.type
        return Gson().fromJson(stringList,listTags)
    }
    @TypeConverter
    fun fromUUID(id:UUID):String= id.toString()
    @TypeConverter
    fun toUUID(id:String):UUID = UUID.fromString(id)
    @TypeConverter
    fun fromDate(date:Date):Long = date.time
    @TypeConverter
    fun toDate(time:Long):Date = Date(time)
}