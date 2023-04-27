package cn.vce.noteapp.feature_note.presentation.add_edit_note

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cn.vce.noteapp.R
import cn.vce.noteapp.databinding.FragmentAddEditNoteBinding
import cn.vce.noteapp.feature_note.base.BaseFragment
import cn.vce.noteapp.feature_note.domain.model.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddEditNoteFragment : BaseFragment(R.layout.fragment_add_edit_note) {


    val viewModel: AddEditNoteViewModel by viewModels()

    lateinit var binding: FragmentAddEditNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddEditNoteBinding.inflate(layoutInflater)
        binding.apply {
            title.apply {
                addTextChangedListener {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it.toString()))
                }
                /*setOnFocusChangeListener { view, b ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(b))
                }*/

            }
            content.apply {
                addTextChangedListener {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it.toString()))
                }
                /*setOnFocusChangeListener { view, b ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(b))
                }*/
            }
            saveFab.setOnClickListener {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }
            colorRecyclerView.layoutManager = GridLayoutManager(activity, Note.noteColors.size)


            viewModel.apply {
                noteTitle.observe(viewLifecycleOwner, Observer {
                    title.setText(it)
                    title.setSelection(it.length)
                })
                noteContent.observe(viewLifecycleOwner, Observer {
                    content.setText(it)
                    content.setSelection(it.length)
                })
                val searchView: SearchView
                noteColor.observe(viewLifecycleOwner, Observer {
                    binding.root.setBackgroundColor(it)
                    colorRecyclerView.adapter = ColorAdapter(this@AddEditNoteFragment, object:
                        ColorAdapter.ItemClickListener{
                        override fun itemClick(view: View, color: Int) {
                            viewModel.onEvent(AddEditNoteEvent.ChangeColor(color))
                            (view as RadioButton).apply {
                                text = ""
                                val drawable = GradientDrawable().apply {
                                    shape = GradientDrawable.OVAL
                                    setSize(48, 48)
                                    setColor(color) // 要设置的内部填充颜色
                                    cornerRadius = 24f // 圆角半径
                                    setStroke(8, Color.BLACK)
                                }
                                background = drawable
                                isChecked = true

                            }
                        }
                    })
                })
            }
        }

        viewModel.eventFlow.onEach { event ->
            when(event){
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    findNavController().navigateUp()
                }
                is AddEditNoteViewModel.UiEvent.ShwoSnackbar -> {
                    Toast.makeText(activity, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }.launchIn(lifecycleScope)

        return binding.root
    }

}