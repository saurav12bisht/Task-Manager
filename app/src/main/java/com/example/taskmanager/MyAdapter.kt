package com.example.taskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val data: ArrayList<String>, private val onclickItem: OnclickItem) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox: CheckBox = itemView.findViewById(R.id.checkBox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.checkbox.text = currentItem

        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->

                onclickItem.onClickItem(position)
        }
    }

    override fun getItemCount() = data.size
}

interface OnclickItem {
    fun onClickItem(position: Int)
}