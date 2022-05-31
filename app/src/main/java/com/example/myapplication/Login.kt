package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
    var fAuth = FirebaseAuth.getInstance()
    var mLoginEmail: EditText? = null
    var mLoginPassword: EditText? = null
    var mLoginButton: Button? = null
    var mSignUpButton: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mLoginEmail = findViewById(R.id.LoginEmail)
        mLoginPassword = findViewById(R.id.LoginPassword)
        mLoginButton = findViewById(R.id.btnLogin)
        mSignUpButton = findViewById(R.id.btnSignUp)
        mLoginButton?.setOnClickListener {
            fAuth.signInWithCredential(
                EmailAuthProvider.getCredential(
                    mLoginEmail?.text.toString(),
                    mLoginPassword?.text.toString()
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "Logged in+ ${fAuth.currentUser != null}", Toast.LENGTH_SHORT)
                    .show()
                if (fAuth.currentUser != null) {
                    startActivity(Intent(applicationContext, index::class.java))
                    finish()
                    downloadInfo()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Email or password is wrong", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


    fun downloadInfo() {

        var ref = mLoginEmail?.text.toString().let { FirebaseDatabase.getInstance().getReference(it) }

        ref?.child(mLoginEmail?.text.toString())?.get().addOnSuccessListener {
            if (it.exists()) {
                Infos.kName = it.child("name").value as String
                Infos.kSurname = it.child("surname").value as String
                println("name saved, ${Infos.kName}\nsurname saved ${Infos.kSurname}")
            }else {

            }
        }
    }

    fun intentChange(view: View) {
        startActivity(Intent(applicationContext, Register::class.java))
        finish()
    }
}