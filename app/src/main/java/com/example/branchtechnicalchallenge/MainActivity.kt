package com.example.branchtechnicalchallenge

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.example.branchtechnicalchallenge.db.Database


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window: Window = this@MainActivity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.primary)

        db = Room.databaseBuilder(this.applicationContext , Database::
        class.java, "DB").allowMainThreadQueries().build()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (db != null) {
            if (db.isOpen()) {
                db.close()
            }
        }
    }
    
    
    companion object{
        lateinit var db: Database
    }
}