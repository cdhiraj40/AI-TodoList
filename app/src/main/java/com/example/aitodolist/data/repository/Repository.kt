package com.example.aitodolist.data.repository

import com.example.aitodolist.data.Task
import com.example.aitodolist.data.database.TaskDao
import kotlinx.coroutines.flow.Flow

class Repository constructor(private val taskDao: TaskDao) {

    suspend fun createTask(task: Task) = taskDao.createTask(task)
    suspend fun deleteTask(taskTitle: String) = taskDao.deleteTask(taskTitle)

    val taskList: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
}