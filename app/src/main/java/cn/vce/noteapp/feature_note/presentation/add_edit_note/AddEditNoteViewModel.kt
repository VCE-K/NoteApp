package cn.vce.noteapp.feature_note.presentation.add_edit_note

import android.app.UiAutomation
import android.graphics.Color.toArgb
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import cn.vce.noteapp.feature_note.base.ObservableViewModel
import cn.vce.noteapp.feature_note.domain.model.Note
import cn.vce.noteapp.feature_note.domain.use_case.NoteUseCases
import cn.vce.noteapp.feature_note.presentation.notes.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

import cn.vce.noteapp.BR

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val state: SavedStateHandle
): ViewModel() {

    private val _noteTitle = MutableLiveData<String>()
    val noteTitle = MediatorLiveData<String>().apply {
        addSource(_noteTitle) {newValue ->
            if (newValue != value){
                value = newValue
            }
        }
    }

    private val _noteContent = MutableLiveData<String>()
    val noteContent = MediatorLiveData<String>().apply {
        addSource(_noteContent) {newValue ->
            if (newValue != value){
                value = newValue
            }
        }
    }

    private val _noteColor = MutableLiveData<Int>().apply {
        //随机初始化
        value = Note.noteColors.random()
    }
    val noteColor = MediatorLiveData<Int>().apply {
        addSource(_noteColor) {newValue ->
            if (newValue != value){
                value = newValue
            }
        }
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init{
        state.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.apply {
                        //修改情况下-页面数据初始化
                        _noteTitle.value = title
                        _noteContent.value = content
                        _noteColor.value = color
                        currentNoteId = id
                    }
                }
                state.get<String>("type")?.let { type ->
                    if ("copy" == type) {
                        currentNoteId = null
                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = event.value
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {

            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = event.value
            }
            is AddEditNoteEvent.ChangeContentFocus -> {

            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                title = _noteTitle.value ?:"",
                                content = _noteContent.value ?:"",
                                timestamp =  System.currentTimeMillis(),
                                color = _noteColor.value ?:-1)
                        )
                        _eventFlow.emit(
                            UiEvent.SaveNote
                        )
                    }catch (e: java.lang.Exception){
                        _eventFlow.emit(
                            UiEvent.ShwoSnackbar(
                                e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShwoSnackbar(val message: String): UiEvent()

        object SaveNote: UiEvent()
    }
}


@HiltViewModel
class AddEditNoteObservableViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val state: SavedStateHandle
): ObservableViewModel() {

    val noteTitle = ObservableField<String>()


    val noteContent = ObservableField<String>()


    val noteColor = ObservableInt(Note.noteColors.random())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init{
        state.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.apply {
                        //修改情况下-页面数据初始化
                        noteTitle.set(title)
                        noteContent.set(content)
                        noteColor.set(color)
                        currentNoteId = id
                    }
                }
                state.get<String>("type")?.let { type ->
                    if ("copy" == type) {
                        currentNoteId = null
                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                noteTitle.set(event.value)
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {

            }
            is AddEditNoteEvent.EnteredContent -> {
                noteContent.set(event.value)
            }
            is AddEditNoteEvent.ChangeContentFocus -> {

            }
            is AddEditNoteEvent.ChangeColor -> {
                noteColor.set(event.color)
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                title = noteTitle.get() ?:"",
                                content = noteContent.get() ?:"",
                                timestamp =  System.currentTimeMillis(),
                                color = noteColor.get())
                        )
                        _eventFlow.emit(
                            UiEvent.SaveNote
                        )
                    }catch (e: java.lang.Exception){
                        _eventFlow.emit(
                            UiEvent.ShwoSnackbar(
                                e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShwoSnackbar(val message: String): UiEvent()

        object SaveNote: UiEvent()
    }
}