package come.hasan.foraty.note.common.dependencyInjection

import come.hasan.foraty.note.model.Note
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {
    @Provides
    fun test():Note = Note("|","")
}