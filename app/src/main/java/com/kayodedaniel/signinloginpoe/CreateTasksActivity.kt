package com.kayodedaniel.signinloginpoe

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateTasksActivity : AppCompatActivity() {

    private lateinit var editTextTaskName: EditText
    private lateinit var editTextCategory: Spinner
    private lateinit var editTextDate: EditText
    private lateinit var editTextStartTime: EditText
    private lateinit var editTextEndTime: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextMinGoal: EditText
    private lateinit var editTextMaxGoal: EditText
    private lateinit var buttonSave: Button
    private lateinit var imageViewPhoto: ImageView
    private lateinit var buttonAddPhoto: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private val REQUEST_PICK_IMAGE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tasks)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("tasks")

        // Initialize views
        editTextTaskName = findViewById(R.id.edit_task_name)
        editTextCategory = findViewById(R.id.category_dropdown)
        editTextDate = findViewById(R.id.edit_date)
        editTextStartTime = findViewById(R.id.edit_start_time)
        editTextEndTime = findViewById(R.id.edit_end_time)
        editTextDescription = findViewById(R.id.edit_description)
        editTextMinGoal = findViewById(R.id.edit_min_goal)
        editTextMaxGoal = findViewById(R.id.edit_max_goal)
        buttonSave = findViewById(R.id.button_save)
        imageViewPhoto = findViewById(R.id.image_view_photo)
        buttonAddPhoto = findViewById(R.id.button_add_photo)

        // Set up click listener for the "Add Photo" button
        buttonAddPhoto.setOnClickListener {
            dispatchPickImageIntent()
        }

        // Set up click listener for the "Save" button
        buttonSave.setOnClickListener {
            saveTask()
        }

        // Set up click listener for the date EditText
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up click listener for the start time EditText
        editTextStartTime.setOnClickListener {
            showTimePickerDialog(editTextStartTime)
        }

        // Set up click listener for the end time EditText
        editTextEndTime.setOnClickListener {
            showTimePickerDialog(editTextEndTime)
        }
    }

    private fun dispatchPickImageIntent() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (pickImageIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            imageViewPhoto.setImageURI(selectedImageUri)
            imageViewPhoto.visibility = ImageView.VISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                editTextDate.setText("$year-${month + 1}-$dayOfMonth")
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                editText.setText(String.format("%02d:%02d", hourOfDay, minute))
            },
            hourOfDay,
            minute,
            true
        )
        timePickerDialog.show()
    }


    private fun saveTask() {
        val taskName = editTextTaskName.text.toString().trim()
        val category = editTextCategory.selectedItem.toString().trim()
        val date = editTextDate.text.toString().trim()
        val startTime = editTextStartTime.text.toString().trim()
        val endTime = editTextEndTime.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val minGoal = editTextMinGoal.text.toString().trim().toDoubleOrNull() ?: 0.0
        val maxGoal = editTextMaxGoal.text.toString().trim().toDoubleOrNull() ?: 0.0

        val currentUser = mAuth.currentUser
        val username = currentUser?.displayName ?: ""

        if (taskName.isEmpty()) {
            showToast("Task name cannot be empty")
            return
        }

        // Create unique ID for the task
        val taskId = databaseReference.push().key

        // Construct Task object
        val task = Task(taskId, taskName, category, date, startTime, endTime, description, minGoal, maxGoal, username)

        // Save task to Firebase
        if (taskId != null) {
            databaseReference.child(username).child(taskId).setValue(task)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Task saved successfully!")
                        finish()
                    } else {
                        showToast("Failed to save task. Please try again.")
                    }
                }
        }
    }


}
data class Task(
    val id: String? = null,
    val name: String,
    val category: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val minGoal: Double,
    val maxGoal: Double,
    val username: String
)