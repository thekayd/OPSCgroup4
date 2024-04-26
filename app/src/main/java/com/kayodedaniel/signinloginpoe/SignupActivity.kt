package com.kayodedaniel.signinloginpoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
//import androidx.activity.EdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var signupName: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // EdgeToEdge.enable(this)
        setContentView(R.layout.activity_signup)

        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        signupButton.setOnClickListener {
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("users")

            val name = signupName.text.toString()
            val email = signupEmail.text.toString()
            val username = signupUsername.text.toString()
            val password = signupPassword.text.toString()

            val helperClass = HelperClass(name, email, username, password)
            reference.child(name).setValue(helperClass)

            Toast.makeText(this, "You have signed up successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        loginRedirectText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
