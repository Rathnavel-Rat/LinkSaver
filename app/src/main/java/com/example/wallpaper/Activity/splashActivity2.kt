package com.example.wallpaper.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.wallpaper.R


class splashActivity2 : AppCompatActivity() {
      private var handler: Handler=Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
        val pref: SharedPreferences = this.getSharedPreferences("Link-Pref", 0)
        val editor = pref.edit()
        val firstRun = pref.getBoolean("firstRun", true)


        handler.postDelayed({
            if(firstRun){
                editor.putBoolean("firstRun",false)
                editor.commit()
                val intent= Intent(this, IntroSlider::class.java)
                startActivity(intent)

                }
            else{
                editor.putBoolean("firstRun",false)
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        },500)
    }

    override fun onStop() {
        finish()
        super.onStop()
    }




}