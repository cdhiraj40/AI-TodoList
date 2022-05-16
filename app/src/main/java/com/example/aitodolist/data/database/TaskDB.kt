package com.example.aitodolist.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aitodolist.data.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDB : RoomDatabase() {
    abstract val dao: TaskDao

    companion object {
        private var INSTANCE: TaskDB? = null
        fun getInstance(application: Application): TaskDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    application,
                    TaskDB::class.java,
                    "my_todo_list.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}