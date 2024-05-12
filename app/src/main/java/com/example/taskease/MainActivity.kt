package com.example.taskease

import android.content.DialogInterface
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskease.Adapter.ToDoAdapter
import com.example.taskease.Model.ToDoModel
import com.example.taskease.Utils.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private var tasksRecyclerView: RecyclerView? = null
    private var taskAdapter: ToDoAdapter? = null
    private var taskList: List<ToDoModel>? = null
    private var db: DatabaseHandler? = null
    private var fab: FloatingActionButton? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        db = DatabaseHandler(this)
        db!!.openDatabase()

        tasksRecyclerView = findViewById(R.id.recycleView)
        tasksRecyclerView?.layoutManager = LinearLayoutManager(this)
        taskAdapter = ToDoAdapter(db!!, this@MainActivity)
        tasksRecyclerView?.adapter = taskAdapter
        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(taskAdapter!!))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        fab = findViewById(R.id.fab)
        taskList = db?.allTasks
        taskList?.let {
            Collections.reverse(it)
            taskAdapter?.setTasks(it)
        }
        fab?.setOnClickListener {
            vibrator?.vibrate(25) // Vibrate for 25 milliseconds
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
    }

    override fun handleDialogClose(dialog: DialogInterface?) {
        taskList = db?.allTasks
        taskList?.let {
            Collections.reverse(it)
            taskAdapter?.setTasks(it)
            taskAdapter?.notifyDataSetChanged()
        }
    }
}
