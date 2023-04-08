package com.example.taskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val data: ArrayList<Model>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tittle: TextView = itemView.findViewById(R.id.note_title_text_view  )
        val time: TextView = itemView.findViewById(R.id.note_timestamp_text_view  )
        val  description: TextView = itemView.findViewById(R.id.note_content_text_view  )


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_note_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.tittle.text = currentItem.tittle
        holder.description.text = currentItem.description
        val time = currentItem.timeStamp.split(":")[0]+":"+currentItem.timeStamp.split(":")[1]
        holder.time.text =time


    }

    override fun getItemCount() = data.size
}

