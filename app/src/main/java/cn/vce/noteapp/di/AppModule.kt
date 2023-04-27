package cn.vce.noteapp.di

import android.app.Application
import androidx.room.Room
import cn.vce.noteapp.feature_note.data.data_source.NoteDatabase
import cn.vce.noteapp.feature_note.data.repository.NoteRepositoryImpl
import cn.vce.noteapp.feature_note.domain.repository.NoteRepository
import cn.vce.noteapp.feature_note.domain.use_case.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)//@InstallIn 注解可以与 Hilt 的 @Module 注解一起使用，用于声明要提供的依赖项，并指定模块要安装到的注入器组件.安装在@HiltAndroidApp上面
object AppModule {

    @Provides//@Provides 注解来声明提供对象的方法
    @Singleton//@Singleton 注解来定义用于提供单例对象的方法。
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}