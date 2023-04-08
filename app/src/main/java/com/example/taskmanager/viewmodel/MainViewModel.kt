package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.repository.MainRepository
import com.example.taskmanager.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel()  {

    private val _saveTask = MutableLiveData<String>()
    val saveTask: LiveData<String>
        get() = _saveTask

  fun saveTask(taskTittle:String){
         repository.saveTask(taskTittle)
  }

}