package com.example.geoquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    // widgets
    private lateinit var textName : EditText
    private lateinit var textEmail: EditText
    private lateinit var textPassword: EditText
    private lateinit var btnSignup: Button

    // firebase auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val currentUser : FirebaseUser? = mAuth.currentUser

        if (currentUser != null) {
            autoLogin(currentUser)
        }

        textName = findViewById(R.id.et_name)
        textEmail = findViewById(R.id.et_email)
        textPassword = findViewById(R.id.et_password)
        btnSignup = findViewById(R.id.btn_signup)

        btnSignup.setOnClickListener {
            mAuth.createUserWithEmailAndPassword(textEmail.text.toString(), textPassword.text.toString())
                .addOnCompleteListener{ task : Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser!!
                        autoLogin(user)
                    } else {
                        Toast.makeText(this, "Registration unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun autoLogin(user : FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("currentUser", user)
        startActivity(intent)
    }
}
