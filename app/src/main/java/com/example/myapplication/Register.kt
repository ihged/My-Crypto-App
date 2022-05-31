package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class Register : AppCompatActivity() {
    var mName: EditText? = null
    var mSurname: EditText? = null
    var mUsername: EditText? = null
    var mEmail: EditText? = null
    var mPassword: EditText? = null
    var mRegisterBtn: Button? = null
    var fAuth: FirebaseAuth? = null
    var progressBar: ProgressBar? = null
    var userID: String? = null
    var database: FirebaseFirestore? = null
    var ref = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mName = findViewById(R.id.name)
        mSurname = findViewById(R.id.surname)
        mUsername = findViewById(R.id.username)
        mEmail = findViewById(R.id.rEmail)
        mPassword = findViewById(R.id.rPassword)
        mRegisterBtn = findViewById(R.id.registerBtn)

        fAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        if (fAuth!!.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        mRegisterBtn?.setOnClickListener(View.OnClickListener {
            val email = mEmail?.text.toString().trim { it <= ' ' }
            val password = mPassword?.text.toString().trim { it <= ' ' }
            val name = mName?.text.toString().trim { it <= ' ' }
            val surname = mSurname?.text.toString().trim { it <= ' ' }
            val username = mUsername?.text.toString().trim { it <= ' ' }
            Infos.kName = name
            Infos.kSurname = surname

            if (TextUtils.isEmpty(email)) {
                mEmail?.error = "Email is Required."
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                mPassword?.error = "Password is Required."
                return@OnClickListener
            }
            if (password.length < 6) {
                mPassword?.error = "Password Must be >= 6 Characters"
                return@OnClickListener
            }

            ref.child("users").child(username).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mUsername?.error = "Username already taken"
                        return
                    } else {

                        fAuth!!.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this@Register,
                                        "User Created.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        println("chiamo metodo")
                        uploadinfo()
                        startActivity(Intent(applicationContext, index::class.java))
                        finish()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        })
    }


    fun uploadinfo() {
        var subRef = ref.child("users").child(mUsername?.text.toString())
        val userMap: HashMap<String, String> = HashMap()
        userMap["userId"] = userID.toString()
        userMap["name"] = mName?.text.toString()
        userMap["surname"] = mSurname?.text.toString()
        userMap["username"] = mUsername?.text.toString()

println("pusho")
        subRef.push().setValue(userMap).addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(
                    this,
                    "${it.result}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Data Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

            fun intentChange(view: View) {
                startActivity(Intent(applicationContext, Login::class.java))
                finish()
            }
}



