package com.example.aitodolist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitodolist.data.Task
import com.example.aitodolist.data.database.TaskDB
import com.example.aitodolist.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val taskList: LiveData<List<Task>>

    init {
        val contestDB = TaskDB.getInstance(application).dao
        repository = Repository(contestDB)
        taskList = repository.taskList.asLiveData()
    }

    fun createTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(taskTitle)
        }
    }
}