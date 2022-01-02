package com.example.notesappviewmodel

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappviewmodel.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var rvNotes: RecyclerView
    lateinit var adapter: NoteAdapter
    private val noteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRV()
        noteViewModel.getNote().observe(this, {
            notes -> adapter.updateRV(notes)
        })


        binding.apply {
            btnSubmit.setOnClickListener {
                val noteContent = etName.text.toString()
                etName.text.clear()
                noteViewModel.addNote(noteContent)
            }
        }
    }



    private fun setupRV() {
        rvNotes = binding.rvNotes
        adapter = NoteAdapter(this)
        rvNotes.adapter = adapter
        rvNotes.layoutManager = LinearLayoutManager(this)
    }

    fun showAlert(pk: Int, content: String, type: String){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.setHint(content)
        if (type == "update") {
            dialogBuilder.setMessage("Update note")
                .setPositiveButton("Save") { _, _ ->
                    noteViewModel.editNote(pk, updatedNote.text.toString())
                }
        } else {
            dialogBuilder.setMessage("Are you sure to delete note?")
                .setPositiveButton("Yes") { _, _ ->
                    noteViewModel.deleteNote(pk)
                }
        }

        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, _ -> dialog.cancel()
        })

        val alert = dialogBuilder.create()
        if (type == "update") {
            alert.setTitle("Update")
            alert.setView(updatedNote)
        } else {
            alert.setTitle("delete")
        }
        alert.show()
    }



}