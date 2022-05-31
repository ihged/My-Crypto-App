package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class index : AppCompatActivity() {
    var mLogout: Button? = null
    var mAddCard: Button? = null
    var fAuth: FirebaseAuth? = null
    var mTvName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        mTvName = findViewById(R.id.nameSurname)
        mLogout = findViewById(R.id.logout)
        //mAddCard=findViewById(R.id.addCard)
        mTvName?.text = "${Infos.kName} ${Infos.kSurname}"
        fAuth = FirebaseAuth.getInstance()
        mLogout?.setOnClickListener {
            fAuth?.signOut()
            Toast.makeText(this, "Logged out+ ${fAuth?.currentUser == null}", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(applicationContext, Login::class.java))
            finish()
        }

    }
    fun uploadinfo() {
        val uid: String? = FirebaseAuth.getInstance().uid
        var ref = uid?.let { FirebaseDatabase.getInstance().getReference(it) }
        uid?.let {
            ref?.child(it)?.get()?.addOnSuccessListener {
                if (it.exists()) {
                    Infos.kName = it.child("name").value as String
                    Infos.kSurname = it.child("surname").value as String
                    println("name saved, ${Infos.kName}\nsurname saved ${Infos.kSurname}")
                    //Infos.kemai = it.child("email").value
                }
            }
        }
    }
}
