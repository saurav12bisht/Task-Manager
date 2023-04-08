package com.example.taskmanager

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: Dialog
    private val database = Firebase.database
    private var list = ArrayList<Model>()
    private var keys = ArrayList<String>()
    private val myRef = database.getReference("notes")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //init function for all initialisations
        init()

        //function for getting tasks from firebase
        getTaskData()

        //on floating action button click for adding new tasks
        onClickFab()

        //function for swipe functionality
        swipeFunctionality()


    }

    private fun swipeFunctionality() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myRef.child(keys[viewHolder.adapterPosition]).removeValue().addOnSuccessListener {
                    Toast.makeText(applicationContext, "Task Removed", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }).attachToRecyclerView(binding.taskRecyclerview)
    }

    private fun init() {
        FirebaseApp.initializeApp(this)

        dialog = Dialog(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickFab() {
        binding.addTaskBtn.setOnClickListener {
            dialog.setContentView(R.layout.fragment_save_task)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val tittle = dialog.findViewById<EditText>(R.id.etTittle)
            val description = dialog.findViewById<EditText>(R.id.description_et)
            val submit = dialog.findViewById<Button>(R.id.button2)
            val cancel = dialog.findViewById<Button>(R.id.button3)

            //on submit button click
            submit.setOnClickListener {
                if (tittle.text.toString().trim().isEmpty() || description.text.toString().trim()
                        .isEmpty()
                ) {
                    Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_LONG).show()
                } else {
                    val myRef = database.getReference("notes")

                    val map: HashMap<String, String> = HashMap()

                    map["tittle"] = tittle.text.toString().trim()
                    map["description"] = description.text.toString().trim()
                    map["time"] = LocalDateTime.now().toLocalTime().toString()

                    myRef.child(myRef.push().key.toString()).setValue(map)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_LONG)
                                .show()
                        }
                    dialog.dismiss()
                }
            }

            //on cancel click
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    private fun getTaskData() {


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()
                keys.clear()
                snapshot.children.forEach { data ->
                    list.add(
                        Model(
                            data.child("tittle").value.toString(),
                            data.child("description").value.toString(),
                            data.child("time").value.toString()
                        )
                    )
                    keys.add(data.key.toString())

                }.also {
                    val adapter = MyAdapter(list)

                    val layoutManager = LinearLayoutManager(applicationContext)
                    binding.taskRecyclerview.layoutManager = layoutManager
                    layoutManager.reverseLayout = true
                    layoutManager.stackFromEnd = true
                    binding.taskRecyclerview.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error $error")
            }

        })

    }


    private fun log(message: String) {
        Log.e("TAG", "Log is -> $message")
    }
}