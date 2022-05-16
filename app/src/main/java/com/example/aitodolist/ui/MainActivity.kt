package com.example.aitodolist.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.events.EventCommand
import com.example.aitodolist.data.Task
import com.example.aitodolist.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var taskList: List<Task>

    private val SDK_KEY = "b82261d015ac5813397caafd095e18182e956eca572e1d8b807a3e2338fdd0dc/stage"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[TaskViewModel::class.java]


        // Define the project key
        val alanConfig = AlanConfig.builder()
            .setProjectId(SDK_KEY)
            .build()
        binding.alanButton.initWithConfig(alanConfig)

        val alanCallback: AlanCallback = object : AlanCallback() {
            /// Handle commands from Alan Studio
            override fun onCommand(eventCommand: EventCommand) {
                try {
                    val command = eventCommand.data
                    val data = command.getJSONObject("data")
                    val commandName = data.getString("commandName")
                    Log.d("AlanButton", "onCommand: commandName: $commandName")

                    setupCommands(commandName, data)
                } catch (e: JSONException) {
                    e.message?.let {
                        Log.e("AlanButton", it)
                    }
                }
            }
        }

        // Register callbacks
        binding.alanButton.registerCallback(alanCallback)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter {
            Toast.makeText(this, it.title, Toast.LENGTH_LONG).show()
        }

        viewModel.taskList.observe(this) { items ->
            items.let {
                adapter.taskList = it
                taskList = it
            }

            // if no tasks added, show no task layout
            if (taskList.isEmpty()) {
                binding.noTaskAdded.root.visibility = View.VISIBLE
            } else {
                binding.noTaskAdded.root.visibility = View.GONE
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupCommands(commandName: String, data: JSONObject) {
        when (commandName) {
            "add_task" -> {
                val title = data.getString("title")
                if (!checkTitle(title, taskList)) {
                    binding.alanButton.playText("(Adding|Inserting) the task $title")
                    viewModel.createTask(
                        Task(null, title, "")
                    )
                    binding.alanButton.playText("task $title (Added|Inserted)")
                } else {
                    binding.alanButton.playText("$title already exist!")
                }
            }

            "delete_task" -> {
                val title = data.getString("title")

                if (checkTitle(title, taskList)) {
                    binding.alanButton.playText("Deleting the task")
                    viewModel.deleteTask(title)
                    binding.alanButton.playText("$title removed")
                } else {
                    binding.alanButton.playText("$title does not exist!")
                }
            }
        }
    }

    private fun checkTitle(title: String, taskList: List<Task>): Boolean {
        taskList.let {
            it.map { task ->
                if (title == task.title) {
                    return true
                }
            }
        }
        return false
    }
}