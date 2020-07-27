package com.judekwashie.noter.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.GestureDetector.OnDoubleTapListener
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.judekwashie.noter.R
import com.judekwashie.noter.entity.Note
import com.judekwashie.noter.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note_edit_read.*
import java.text.SimpleDateFormat
import java.util.*


class NoteEditReadFragment : Fragment(), OnTouchListener,
    GestureDetector.OnGestureListener,
    OnDoubleTapListener,
    View.OnClickListener, TextWatcher {

    val args: NoteEditReadFragmentArgs by navArgs()

    private lateinit var navController: NavController
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var detector: GestureDetectorCompat


    private var noteId: Int = 0
    private var isInEditMode: Boolean = false
    private var saveMenuItem: MenuItem? = null
    private var isNoteModified: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        noteId = args.noteId


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_edit_read, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.notes_toolbar))
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        saveMenuItem = menu.findItem(R.id.save_note)

    }

    private fun enableEditMode() {
        toolbar_check_button.visibility = View.VISIBLE
        note_edit_text.isCursorVisible = true
        note_edit_text.isFocusableInTouchMode = true
        note_title_editText.visibility = View.VISIBLE
        note_title_editText.isCursorVisible = true
        isInEditMode = true
        saveMenuItem?.isVisible = false
        note_title_editText.setSelection(note_title_editText.text.length)
        note_edit_text.setSelection(note_edit_text.text.length)

    }


    private fun disableEditMode() {
        toolbar_check_button.visibility = View.GONE
        note_edit_text.isCursorVisible = false
        note_edit_text.isFocusableInTouchMode = false
        note_edit_text.clearFocus()
        note_title_editText.clearFocus()
        setNoteTitleOnToolbar()
        note_title_editText.visibility = View.GONE
        isInEditMode = false
        saveMenuItem?.isVisible = true

    }

    private fun setNoteTitleOnToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title = note_title_editText.text
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        noteViewModel.getNote(noteId).observe(viewLifecycleOwner, Observer {
            if (it != null) { //if already existing note is selected

                disableEditMode()

                //set views with note properties
                note_edit_text.setText(it.noteText)
                note_title_editText.setText(it.noteTitle)
                note_date_textView.text = it.noteDate
                (activity as AppCompatActivity).supportActionBar?.title = it.noteTitle
                isNoteModified = false

            } else { //if new Note
                enableEditMode()
                note_date_textView.text = getCurrentDate()
            }


        })


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            if (isNoteModified) {
                showDialog()
            } else {
                isEnabled = false
                navigateToNoteListFragment()
            }
        }

        note_edit_text.addTextChangedListener(this)
        note_title_editText.addTextChangedListener(this)
        detector = GestureDetectorCompat(activity, this)
        note_edit_text.setOnTouchListener(this)
        toolbar_check_button.setOnClickListener(this)
        notes_toolbar.setOnClickListener(this)


    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().time)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
        saveMenuItem = menu.findItem(R.id.save_note)
        saveMenuItem?.isVisible = !isInEditMode
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save_note -> {
                Toast.makeText(activity, "Note Saved", Toast.LENGTH_LONG).show()
                val note = Note(
                    note_title_editText.text.toString(),
                    note_edit_text.text.toString(),
                    getCurrentDate()
                )
                if (noteId == 0) {
                    noteViewModel.insertNote(note)
                } else {
                    note.noteId = noteId
                    noteViewModel.updateNote(note)
                }
                hideKeyboard()
                navigateToNoteListFragment()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }

        }

    }


    private fun showDialog() {
        lateinit var dialog: AlertDialog


        val builder = AlertDialog.Builder(context as Context)


        builder.setMessage("Exit without saving note?")


        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    navigateToNoteListFragment()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
            }
        }

        builder.setPositiveButton("YES", dialogClickListener)

        builder.setNegativeButton("NO", dialogClickListener)

        dialog = builder.create()

        dialog.show()
    }

    private fun navigateToNoteListFragment() {
        navController.navigate(R.id.action_noteEditReadFragment_to_noteListFragment)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return detector.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d("onShowPress", "onShowPress")
    }


    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        note_edit_text.requestFocus()
        enableEditMode()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_check_button -> {
                disableEditMode()
            }
            R.id.notes_toolbar -> {
                note_title_editText.requestFocus()
                enableEditMode()
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        isNoteModified = true
    }

    private fun hideKeyboard() {
        super.onDestroyView()
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = activity?.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }
}