package com.example.mynotes.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.R
import com.example.mynotes.databinding.CustomToolbarBinding
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.source.note.NoteModel
import com.example.mynotes.ui.adapter.NoteAdapter
import com.example.mynotes.ui.adapter.SwipeToDelete
import com.example.mynotes.util.Constants.Companion.NOTE_ITEM_ARGS
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val homeModule = module {
    factory { HomeFragment() }
}

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingToolbar: CustomToolbarBinding

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindingToolbar = binding.toolbar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar()
        setupRecyclerView()

        // First initialization
        getAllNotes()

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragmentToFormFragment
            )
        }
    }

    private fun setupToolbar() {
        bindingToolbar.container.inflateMenu(R.menu.menu_home)
        val menu = bindingToolbar.container.menu
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null) searchFromDb(query) else getAllNotes()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null) searchFromDb(newText) else getAllNotes()
                return true
            }
        })

        bindingToolbar.textTitle.text = "MyNotes"
        bindingToolbar.textTitle.gravity = Gravity.CENTER
    }

    private fun setupRecyclerView() {
        binding.rvNoteList.apply {
            adapter = noteAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }

            swipeToDelete(this)
        }
    }

    private fun searchFromDb(query: String) {
        val searchQuery = "%$query%"

        viewModel.repository.searchNotes(searchQuery).observe(viewLifecycleOwner) {
            noteAdapter.setData(it)
        }
    }

    private fun getAllNotes() {
        viewModel.notes.observe(viewLifecycleOwner) {
            binding.ivEmptyNotes.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.tvEmptyNotes.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.rvNoteList.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE

            noteAdapter.setData(it)
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = noteAdapter.notes[viewHolder.adapterPosition]

                // Delete Item
                viewModel.removeNote(deletedItem)
                noteAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                // Restore Deleted Item
                restoreDeletedNote(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedNote(view: View, deletedItem: NoteModel) {
        val snackBar = Snackbar.make(
            view, "'${deletedItem.title}' removed",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            viewModel.addNote(deletedItem)
            getAllNotes()
        }
        snackBar.show()
    }

    private val noteAdapter by lazy {
        NoteAdapter(object : NoteAdapter.OnAdapterListener {
            override fun onTap(note: NoteModel) {
                val bundle = Bundle().apply {
                    putSerializable(NOTE_ITEM_ARGS, note)
                }

                findNavController().navigate(
                    R.id.action_homeFragmentToFormFragment,
                    bundle
                )
            }
        })
    }

}