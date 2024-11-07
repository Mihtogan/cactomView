package com.example.castomview

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.castomview.statusbar.StatusBarView
import com.example.castomview.statusbar.StatusElement

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view: StatusBarView = findViewById(R.id.statusBar)

        val defStatuses = listOf(
            StatusElement(Color.BLUE, 0.3f),
            StatusElement(Color.CYAN, 0.2f),
            StatusElement(Color.BLACK, 0.3f),
            StatusElement(Color.MAGENTA, 0.2f)
        )

        view.setStatuses(defStatuses)
    }
}