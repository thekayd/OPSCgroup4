package com.kayodedaniel.signinloginpoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
//import androidx.activity.EdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signUpRedirectText: TextView
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // EdgeToEdge.enable(this)
        setContentView(R.layout.activity_login)

        loginUsername = findViewById(R.id.login_username)
        loginPassword = findViewById(R.id.login_password)
        signUpRedirectText = findViewById(R.id.signupRedirectText)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            if (validateUsername() && validatePassword()) {
                checkUser()
            }
        }

        signUpRedirectText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateUsername(): Boolean {
        val username = loginUsername.text.toString().trim()
        return if (username.isEmpty()) {
            loginUsername.error = "Username cannot be empty"
            false
        } else {
            loginUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = loginPassword.text.toString().trim()
        return if (password.isEmpty()) {
            loginPassword.error = "Password cannot be empty"
            false
        } else {
            loginPassword.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = loginUsername.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase: Query = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val passwordFromDB = userSnapshot.child("password").getValue(String::class.java)

                        if (passwordFromDB == userPassword) {
                            // Passwords match, login successful
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            return
                        }
                    }
                    // Password doesn't match any record
                    loginPassword.error = "Invalid Credentials"
                    loginPassword.requestFocus()
                } else {
                    // User not found
                    loginUsername.error = "User does not exist"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        })
    }
}
