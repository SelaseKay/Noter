package com.judekwashie.noter.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judekwashie.noter.R
import com.judekwashie.noter.adapters.NoteRecyclerAdapter
import com.judekwashie.noter.entity.Note
import com.judekwashie.noter.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note_list.*

class NoteListFragment : Fragment() {

    private lateinit var viewAdapter: NoteRecyclerAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var navController: NavController
    private lateinit var noteViewModel: NoteViewModel

    private var notes = ArrayList<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.notes_toolbar))
        (activity as AppCompatActivity).supportActionBar?.title = "Notes"
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        fab.setOnClickListener {
            navController.navigate(R.id.action_noteListFragment_to_noteEditReadFragment)
        }


        viewManager = LinearLayoutManager(activity)
        viewAdapter = NoteRecyclerAdapter(navController)

        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view)

        noteViewModel.notes.observe(viewLifecycleOwner, Observer {
            viewAdapter.submitList(it)
            if (it.isEmpty()){
                empty_view.visibility = View.VISIBLE
            }
            else{
                empty_view.visibility = View.GONE
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_all_notes -> {
                showDialog()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote(viewAdapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(activity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showDialog(){
        lateinit var dialog:AlertDialog


        val builder = AlertDialog.Builder(context as Context)

        builder.setTitle("Delete")

        builder.setMessage("Do you want to delete all notes?")


        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    noteViewModel.deleteAllNote()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
            }
        }

        builder.setPositiveButton("YES",dialogClickListener)

        builder.setNegativeButton("NO",dialogClickListener)

        dialog = builder.create()

        dialog.show()
    }
}