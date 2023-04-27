package cn.vce.noteapp.feature_note.presentation.notes

import cn.vce.noteapp.feature_note.domain.model.Note
import cn.vce.noteapp.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()//切换排序类型
    data class DeleteNote(val note: Note): NotesEvent()//删除
    object RestoreNote: NotesEvent()//还原
    object ToggleOrderSection: NotesEvent()//切换正序反序
}
