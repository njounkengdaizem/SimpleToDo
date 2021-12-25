package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import org.apache.commons.io.FileUtils
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //Remove Item from list
                listOfTasks.removeAt(position)
                //Notify the adapter that our data set changed
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        //Detect when user clicks on add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Caren", "User Clicked on button")
//        }
        loadItems()
        //look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //set up the button and input field, so that the user can add it to the list
        findViewById<Button>(R.id.button).setOnClickListener{
            //Grab user's text into edit text field
            val userInputtedTask = inputTextField.text.toString()
            //Add the string to listOfTasks
            listOfTasks.add(userInputtedTask)
            //Notify the adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)
            //Reset text field inputTextField.setText("")
            inputTextField.setText("")
            //Save data that user has input
            saveItems()
        }
    }

    //Save data by writing and reading from a file

    //Get file that we need
    fun getDataFile(): File{
        //Every line represent a specific task in our lists of tasks
        return File(filesDir, "data.txt")
    }
    //Create a method to get the data file we need
    //Load the items by reading every line in the data files
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into our data file
    fun saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}