package com.example.taskmanager.repository

interface Repository {
    fun saveTask(task:String)
    fun removeTask(id:String)
}