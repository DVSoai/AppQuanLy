package com.example.appquanly.screen

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.appquanly.R
import com.example.appquanly.contentprovider.TaskProvider
import com.example.appquanly.db.TaskDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondScreen : AppCompatActivity() {

    private lateinit var editTextTaskName: EditText
    private lateinit var datePickerTaskDate: DatePicker
    private lateinit var buttonSaveTask: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)

        editTextTaskName = findViewById(R.id.editTextTaskName)
        datePickerTaskDate = findViewById(R.id.datePickerTaskDate)
        buttonSaveTask = findViewById(R.id.buttonSaveTask)

        // Xử lý khi nhấn nút "Lưu công việc"
        buttonSaveTask.setOnClickListener {
            saveTaskToDatabase()
        }
    }


    private fun saveTaskToDatabase() {
        val taskName = editTextTaskName.text.toString()
        val selectedDate = getDateFromDatePicker(datePickerTaskDate)

        if (taskName.isNotEmpty()) {
            val values = ContentValues()
            values.put(TaskDatabase.COLUMN_TASK_NAME, taskName)
            values.put(TaskDatabase.COLUMN_TASK_DATE, selectedDate)

            val uri = contentResolver.insert(TaskProvider.CONTENT_URI, values)

            if (uri != null) {
                Toast.makeText(this, "Công việc đã được lưu.", Toast.LENGTH_SHORT).show()
                finish() // Quay lại Màn hình 1
            } else {
                Toast.makeText(this, "Lỗi khi lưu công việc.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập nội dung công việc.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}