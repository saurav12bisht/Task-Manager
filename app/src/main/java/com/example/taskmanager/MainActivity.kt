package com.example.taskmanager

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), OnclickItem {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: Dialog
    val database = Firebase.database
    var list = ArrayList<String>()
    var keys = ArrayList<String>()
    private lateinit var onClickContext: OnclickItem
    val myRef = database.getReference("tasks")

    //    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        onClickContext = this
        init()
        getTaskData()
        onClickFab()


    }

    private fun init() {
        FirebaseApp.initializeApp(this)

        dialog = Dialog(this)
    }

    private fun onClickFab() {
        binding.addTaskBtn.setOnClickListener {
            dialog.setContentView(R.layout.fragment_save_task)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            var tittle = dialog.findViewById<EditText>(R.id.etTittle);
            var submit = dialog.findViewById<Button>(R.id.button2);
            var cancel = dialog.findViewById<Button>(R.id.button3);

            submit.setOnClickListener {
                val myRef = database.getReference("tasks")


                myRef.child(myRef.push().key.toString()).setValue(tittle.text.toString().trim())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_LONG).show()
                    }

                dialog.dismiss()

            }

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
                    list.add(data.value.toString())
                    keys.add(data.key.toString())

                }.also {
                    val adapter = MyAdapter(list, onClickContext)
                    var layoutManager = LinearLayoutManager(applicationContext)
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

    override fun onClickItem(position: Int) {


         Log(position.toString())
        myRef.child(keys[position]).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Task Completed", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
        }
    }

    private fun Log(message:String){
        Log.e("TAG","Log is -> $message")
    }
}