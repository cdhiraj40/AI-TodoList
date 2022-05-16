package com.example.aitodolist.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.aitodolist.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun createTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM tasks WHERE title=:taskTitle")
    suspend fun deleteTask(taskTitle: String)

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * from tasks WHERE id = :id")
    fun getTask(id: Int): Flow<Task>
}