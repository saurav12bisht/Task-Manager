package com.example.taskmanager.repository

import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RepoImpl :Repository {
    val database = Firebase.database
    val myRef = database.getReference("tasks")

    override fun saveTask(task: String) {

        myRef.child(myRef.push().key.toString()).setValue(task)

    }

    override fun removeTask(id: String) {

    }
}