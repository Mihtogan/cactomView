package com.example.castomview

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.castomview.statusbar.MaterialUsageIndicator
import com.example.castomview.statusbar.StatusElement

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view: MaterialUsageIndicator = findViewById(R.id.statusBar)

        val defStatuses = listOf(
            StatusElement(Color.GREEN, 4),
            StatusElement(Color.YELLOW, 7),
            StatusElement(Color.RED, 2)
        )

        view.setStatuses(defStatuses)
    }
}