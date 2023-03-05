package com.example.audiorecorderfinalchoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.audiorecorderfinalchoice.databinding.FileItemBinding
import java.io.File


class AudioListAdapter(private val listOfFiles: List<File>, private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {
    private lateinit var timeCreated: TimeCreated

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        timeCreated = TimeCreated()
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        with(holder) {
            with(listOfFiles[position]) {
                binding.tvTitleNote.text = name
                println("${listOfFiles[position].lastModified()} last modified date ")
                binding.date.text = timeCreated.getTimeCreated(listOfFiles[position].lastModified())
            }
            itemView.setOnClickListener{
                onItemClick.onItemClickListener(listOfFiles[adapterPosition],adapterPosition)
            }
        }
    }
    inner class AudioViewHolder(val binding: FileItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return listOfFiles.size
    }

}
