package cn.vce.noteapp.feature_note.presentation.notes

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import cn.vce.noteapp.feature_note.base.ObservableViewModel
import cn.vce.noteapp.feature_note.domain.model.Note
import cn.vce.noteapp.feature_note.domain.use_case.NoteUseCases
import cn.vce.noteapp.feature_note.domain.util.NoteOrder
import cn.vce.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ObservableViewModel() {

    private val _state = MutableLiveData<NotesState>().apply {
        value = NotesState()
    }

    val state = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {

                if ((state.value?.noteOrder ?: NoteOrder.Date(OrderType.Descending))::class == event.noteOrder::class &&
                    (state.value?.noteOrder?.orderType ?: NoteOrder.Title(OrderType.Descending))  == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value?.copy(
                    isOrderSectionVisible = !(state.value?.isOrderSectionVisible?: false)
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                state.value = NotesState(notes, noteOrder)
                _state.value = state.value?.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}
