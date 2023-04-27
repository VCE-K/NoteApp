package cn.vce.noteapp.feature_note.presentation.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.vce.noteapp.R
import cn.vce.noteapp.databinding.NoteItemBinding
import cn.vce.noteapp.feature_note.domain.model.Note

class NotesAdapter(
    private val fragment: NotesFragment,
    private val noteList: List<Note>,
    private val onItemClickListener: ItemClickListener
):
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding:NoteItemBinding) : RecyclerView.ViewHolder(binding.root){
        val title: TextView = binding.noteTitleTv
        val content: TextView = binding.noteContentTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false

        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = noteList[position].title
        val content = noteList[position].content
        val color = noteList[position].color
        holder.title.text = title
        holder.content.text = content
        holder.itemView.setBackgroundColor(color)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(noteList[position])
        }
    }


    interface ItemClickListener {
        fun onItemClick(note: Note)
    }
}