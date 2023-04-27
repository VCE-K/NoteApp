package cn.vce.noteapp.feature_note.presentation.notes

import androidx.lifecycle.ViewModelProvider
import cn.vce.noteapp.feature_note.domain.model.Note
import cn.vce.noteapp.feature_note.domain.util.NoteOrder
import cn.vce.noteapp.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    var isOrderSectionVisible: Boolean = false
)