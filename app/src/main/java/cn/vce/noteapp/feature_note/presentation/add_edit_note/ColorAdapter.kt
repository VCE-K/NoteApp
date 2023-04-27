package cn.vce.noteapp.feature_note.presentation.add_edit_note

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import cn.vce.noteapp.AppApplication
import cn.vce.noteapp.R
import cn.vce.noteapp.databinding.ColorItemBinding
import cn.vce.noteapp.feature_note.domain.model.Note
import kotlin.properties.Delegates

class ColorAdapter(
    private val fragment: AddEditNoteFragment,
    private val itemClickListener: ItemClickListener,
    private val colorList: List<Int> = Note.noteColors):
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder{
        val binding = ColorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colorList[position]

        holder.apply {
            radioButton.apply {
                text = ""
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setSize(48, 48)
                    setColor(color) // 要设置的内部填充颜色
                    cornerRadius = 24f // 圆角半径
                }
                background = drawable
            }
            radioButton.isChecked = false
            fragment.viewModel.noteColor.observe(fragment.viewLifecycleOwner, Observer {
                if (it == color){
                    (radioButton.background as GradientDrawable).apply {
                        setStroke(8, Color.BLACK)
                    }
                }
            })
            radioButton.setOnClickListener {
                itemClickListener.itemClick(it, color)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int  = colorList.size


    inner class ColorViewHolder(private val binding:ColorItemBinding) :
        RecyclerView.ViewHolder(binding.root){
            val radioButton = binding.radioButton
        }


    interface ItemClickListener{
        fun itemClick(view: View, color: Int)
    }
}