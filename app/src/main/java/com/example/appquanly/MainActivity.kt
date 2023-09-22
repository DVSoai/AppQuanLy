package com.example.appquanly

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appquanly.adapter.Task
import com.example.appquanly.adapter.TaskAdapter
import com.example.appquanly.contentprovider.TaskProvider
import com.example.appquanly.db.TaskDatabase
import com.example.appquanly.screen.SecondScreen

class MainActivity : AppCompatActivity() {

     private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var buttonAddTask: Button
    private lateinit var taskAdapter: TaskAdapter

    // Khai báo biến toàn cục để lưu danh sách công việc
    private val taskList = mutableListOf<Task>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)
        buttonAddTask = findViewById(R.id.buttonAddTask)

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList)
        recyclerViewTasks.adapter = taskAdapter

        // Hiển thị danh sách công việc từ Content Provider
        loadTasksFromContentProvider()

        // Xử lý khi nhấn nút "Thêm công việc"
        buttonAddTask.setOnClickListener {
            val intent = Intent(this, SecondScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Cập nhật danh sách công việc khi quay lại Màn hình 1
        loadTasksFromContentProvider()
    }

    private fun loadTasksFromContentProvider() {
        val projection = arrayOf(
            TaskDatabase.COLUMN_ID,
            TaskDatabase.COLUMN_TASK_NAME,
            TaskDatabase.COLUMN_TASK_DATE
        )

        val cursor: Cursor? = contentResolver.query(
            TaskProvider.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.apply {
            taskList.clear()
            while (moveToNext()) {
                val taskId = getInt(getColumnIndex(TaskDatabase.COLUMN_ID))
                val taskName = getString(getColumnIndex(TaskDatabase.COLUMN_TASK_NAME))
                val taskDate = getString(getColumnIndex(TaskDatabase.COLUMN_TASK_DATE))

                val task = Task(taskId, taskName, taskDate)
                taskList.add(task)
            }
            taskAdapter.notifyDataSetChanged()
            close()
        }
    }
}
