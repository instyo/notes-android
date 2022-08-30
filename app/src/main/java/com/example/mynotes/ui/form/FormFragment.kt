package com.example.mynotes.ui.form

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.databinding.CustomToolbarBinding
import com.example.mynotes.databinding.FragmentNoteFormBinding
import com.example.mynotes.source.note.NoteModel
import com.example.mynotes.util.ColorUtil
import com.example.mynotes.util.Constants.Companion.NOTE_ITEM_ARGS
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val formModule = module {
    factory { FormFragment() }
}

class FormFragment : Fragment() {

    private lateinit var binding: FragmentNoteFormBinding
    private lateinit var bindingToolbar: CustomToolbarBinding

    private val viewModel: FormViewModel by viewModel()
    private lateinit var mMenu: Menu
    private var noteArgs: NoteModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteFormBinding.inflate(inflater, container, false)

        // Get passed arguments
        arguments?.let {
            noteArgs = it.getSerializable(NOTE_ITEM_ARGS) as NoteModel
        }

        // Set action on color picker
        binding.fabColorPicker.setOnClickListener {
            pickColor()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupFormFragment()
        setPinnedIcon()
    }

    private fun setupToolbar() {
        bindingToolbar = binding.toolbar
        bindingToolbar.container.inflateMenu(R.menu.menu_note_form)
        bindingToolbar.textTitle.text = if (noteArgs == null) "Add Note" else "Edit Note"

        val menu = bindingToolbar.container.menu
        mMenu = menu
        val saveNoteBtn = menu.findItem(R.id.action_save)
        val pinNoteBtn = menu.findItem(R.id.action_pin)

        saveNoteBtn.setOnMenuItemClickListener {
            saveNote()
            true
        }

        pinNoteBtn.setOnMenuItemClickListener {
            viewModel.pinned = !viewModel.pinned
            setPinnedIcon()
            true
        }

        bindingToolbar.container.setNavigationIcon(R.drawable.ic_arrow_back)
        bindingToolbar.container.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupFormFragment() {

        noteArgs?.let { note ->
            binding.etNoteTitle.setText(note.title)
            binding.etNoteDescription.setText(note.description)
            viewModel.backgroundColor = note.color
            viewModel.pinned = note.pinned
            setBackgroundColor(viewModel.backgroundColor)
        }
    }


    private fun setPinnedIcon() {
        val icon = if (viewModel.pinned) R.drawable.ic_unpin else R.drawable.ic_pin

        mMenu.let {
            it.getItem(0)
                .setIcon(ContextCompat.getDrawable(requireContext(), icon))
        }

    }

    private fun pickColor() {
        MaterialColorPickerDialog
            .Builder(requireContext())
            .setTitle("Pick Theme")
            .setColorRes(ColorUtil().listColor(requireContext()))
            .setColorShape(ColorShape.CIRCLE)
            .setColorSwatch(ColorSwatch._200)
            .setDefaultColor(viewModel.backgroundColor)
            .setColorListener { _, colorHex ->
                viewModel.backgroundColor = colorHex
                setBackgroundColor(colorHex)
            }
            .show()
    }

    private fun setBackgroundColor(color: String) {
        binding.noteFormContainer.setBackgroundColor(Color.parseColor(color))
    }

    private fun saveNote() {
        if (validateFields()) {
            val noteTitle = binding.etNoteTitle.text.toString()
            val noteDescription = binding.etNoteDescription.text.toString()

            if (noteArgs == null) {
                viewModel.saveNote(noteTitle, noteDescription)
            } else {
                val note = noteArgs!!.copy(
                    title = noteTitle,
                    description = noteDescription,
                    pinned = viewModel.pinned,
                    color = viewModel.backgroundColor,
                    updatedAt = System.currentTimeMillis(),
                )
                viewModel.updateNote(note)
            }

            findNavController().popBackStack()
        }
    }

    private fun validateFields(): Boolean {
        val title = binding.etNoteTitle
        val description = binding.etNoteDescription

        if (title.length() < 1) {
            title.error = "Title can't be empty"
            return false
        }

        if (description.length() < 10) {
            description.error = "Description should be at least 10 characters long"
            return false
        }

        return true
    }

}