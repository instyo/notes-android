package com.example.mynotes.ui.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.AdapterNoteItemBinding
import com.example.mynotes.source.note.NoteModel
import com.example.mynotes.util.ColorUtil
import com.example.mynotes.util.NoteDiffUtil

class NoteAdapter(
    val listener: OnAdapterListener,
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var notes = emptyList<NoteModel>()


    class NoteViewHolder(val binding: AdapterNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteModel) {
            binding.note = note
            binding.ivPinnedNote.visibility = if (note.pinned) View.VISIBLE else View.GONE
            val color = Color.parseColor(note.color)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.noteItemCard.setCardBackgroundColor(color)
            }
        }
    }

    interface OnAdapterListener {
        fun onTap(note: NoteModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            AdapterNoteItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)

        holder.itemView.setOnClickListener {
            listener.onTap(note)
        }
    }


    override fun getItemCount() = notes.size

    fun setData(toDoData: List<NoteModel>) {
        val toDoDiffUtil = NoteDiffUtil(notes, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.notes = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}