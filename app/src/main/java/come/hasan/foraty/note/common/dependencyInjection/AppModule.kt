package come.hasan.foraty.note.common.dependencyInjection

import android.content.Context
import androidx.room.Room
import come.hasan.foraty.note.database.NOTES_DATABASE
import come.hasan.foraty.note.database.NoteDao
import come.hasan.foraty.note.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
        @Provides
        fun database(context: Context):NotesDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                NOTES_DATABASE
            ).build()
        @Provides
        fun databaseDao(database: NotesDatabase):NoteDao = database.noteDao()
}