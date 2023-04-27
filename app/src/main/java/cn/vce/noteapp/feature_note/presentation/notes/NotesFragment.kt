package cn.vce.noteapp.feature_note.presentation.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cn.vce.noteapp.R
import cn.vce.noteapp.databinding.FragmentNotesBinding
import cn.vce.noteapp.databinding.NoteItemBinding
import cn.vce.noteapp.feature_note.base.BaseFragment
import cn.vce.noteapp.feature_note.domain.model.Note
import cn.vce.noteapp.feature_note.domain.util.NoteOrder
import cn.vce.noteapp.feature_note.domain.util.OrderType
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotesFragment : BaseFragment(R.layout.fragment_notes) {

    val viewModel: NotesViewModel by viewModels()

    private lateinit var binding: FragmentNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(layoutInflater)

        binding.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = "Your note"
            }
            val layoutManager = GridLayoutManager(context, 1)
            noteItems.layoutManager = layoutManager



            viewModel.state.observe(viewLifecycleOwner, Observer {
                noteItems.linear().setup {
                    addType<Note>(R.layout.note_item)
                    onCreate {

                    }
                    onBind {
                        getBinding<NoteItemBinding>().apply {
                            getModel<Note>().apply {
                                noteTitleTv.text = title
                                noteContentTv.text = content
                                noteItem.setCardBackgroundColor(color)
                            }
                        }
                    }
                    onClick(R.id.noteItem) {
                        // Item设置点击事件, 就要给Item的根布局设置一个id, 这里设置的是R.id.item
                        val bundle = Bundle().apply {
                            getModel<Note>().id?.let { it1 -> putInt("noteId", it1) }
                        }
                        findNavController().navigate(
                            R.id.action_notesFragment_to_addEditFragment,
                            bundle
                        )
                    }
                    onClick(R.id.copy_btn){
                        // Item设置点击事件, 就要给Item的根布局设置一个id, 这里设置的是R.id.item
                        val bundle = Bundle().apply {
                            getModel<Note>().id?.let { it1 -> putInt("noteId", it1) }
                            putString("type", "copy")
                        }
                        findNavController().navigate(
                            R.id.action_notesFragment_to_addEditFragment,
                            bundle
                        )
                    }
                    onClick(R.id.delete_btn){
                        viewModel.onEvent(NotesEvent.DeleteNote(getModel<Note>()))
                        lifecycleScope.launch {
                            Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_SHORT)
                                .setAction("Undo") {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                    Toast.makeText(activity, "Note restored", Toast.LENGTH_SHORT).show()
                                }
                                .show()
                        }
                    }
                }.models = it.notes


                when(it.noteOrder){
                    is NoteOrder.Title -> {
                        titleRadioButton.isChecked = true
                        radioGroupOrder.check(titleRadioButton.id)
                    }
                    is NoteOrder.Date -> {
                        dateRadioButton.isChecked = true
                        radioGroupOrder.check(dateRadioButton.id)
                    }
                    is NoteOrder.Color -> {
                        colorRadioButton.isChecked = true
                        radioGroupOrder.check(colorRadioButton.id)
                    }
                }

                when(it.noteOrder.orderType){
                    is OrderType.Ascending -> {
                        ascendingRadioButton.isChecked = true
                        radioGroupOrderType.check(ascendingRadioButton.id)
                    }
                    is OrderType.Descending -> {
                        descendingRadioButton.isChecked = true
                        radioGroupOrderType.check(descendingRadioButton.id)
                    }
                }
            })

            titleRadioButton.setOnClickListener {
                viewModel.state.value?.noteOrder?.orderType?.let {
                    viewModel.onEvent(NotesEvent.Order(NoteOrder.Title(it)))
                }
            }
            dateRadioButton.setOnClickListener {
                viewModel.state.value?.noteOrder?.orderType?.let {
                    viewModel.onEvent(NotesEvent.Order(NoteOrder.Date(it)))
                }
            }
            colorRadioButton.setOnClickListener {
                viewModel.state.value?.noteOrder?.orderType?.let {
                    viewModel.onEvent(NotesEvent.Order(NoteOrder.Color(it)))
                }
            }

            ascendingRadioButton.setOnClickListener {
                viewModel.state.value?.noteOrder?.let {
                    viewModel.onEvent(NotesEvent.Order(it.copy(OrderType.Ascending)))
                }
            }
            descendingRadioButton.setOnClickListener {
                viewModel.state.value?.noteOrder?.let {
                    viewModel.onEvent(NotesEvent.Order(it.copy(OrderType.Descending)))
                }
            }

            addFab.setOnClickListener {
                findNavController().navigate(
                    R.id.action_notesFragment_to_addEditFragment,
                    Bundle().apply {
                        putSerializable("noteId", -1)
                    }
                )
            }
        }
        return binding.root
    }



}