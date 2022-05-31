package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var fAuth=FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)
        var mLandingPageLayout: View = findViewById(R.id.clickableView)
        mLandingPageLayout.setOnClickListener{
            var intent:Intent?=null
            if (fAuth?.currentUser == null){
                intent=Intent(this,Login::class.java)
            }else{
                intent=Intent(this,index::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

}