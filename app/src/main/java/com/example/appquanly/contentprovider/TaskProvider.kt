package com.example.appquanly.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import com.example.appquanly.db.TaskDatabase

class TaskProvider : ContentProvider() {
    private lateinit var dbHelper: TaskDatabase

    companion object {
        private const val AUTHORITY = "com.example.appquanly.contentprovider"
        private const val TASKS_PATH = "tasks"
        private const val TASKS = 1
        private const val TASK_ID = 2
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TASKS_PATH")

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TASKS_PATH, TASKS)
            addURI(AUTHORITY, "$TASKS_PATH/#", TASK_ID)
        }
    }

    override fun onCreate(): Boolean {
        dbHelper = TaskDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        val match = uriMatcher.match(uri)

        return when (match) {
            TASKS -> {
                val cursor = db.query(
                    TaskDatabase.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        val match = uriMatcher.match(uri)

        return when (match) {
            TASKS -> "vnd.android.cursor.dir/vnd.com.example.appquanly.contentprovider.tasks"
            TASK_ID -> "vnd.android.cursor.item/vnd.com.example.appquanly.contentprovider.task"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val match = uriMatcher.match(uri)

        return when (match) {
            TASKS -> {
                val id = db.insert(TaskDatabase.TABLE_NAME, null, values)
                if (id > 0) {
                    val newUri = ContentUris.withAppendedId(TaskProvider.CONTENT_URI, id)
                    context?.contentResolver?.notifyChange(uri, null)
                    newUri
                } else {
                    throw SQLException("Failed to insert row into $uri")
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val match = uriMatcher.match(uri)

        return when (match) {
            TASKS -> {
                val rowsDeleted = db.delete(TaskDatabase.TABLE_NAME, selection, selectionArgs)
                if (rowsDeleted > 0) {
                    context?.contentResolver?.notifyChange(uri, null)
                }
                rowsDeleted
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val match = uriMatcher.match(uri)

        return when (match) {
            TASKS -> {
                val rowsUpdated = db.update(TaskDatabase.TABLE_NAME, values, selection, selectionArgs)
                if (rowsUpdated > 0) {
                    context?.contentResolver?.notifyChange(uri, null)
                }
                rowsUpdated
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
