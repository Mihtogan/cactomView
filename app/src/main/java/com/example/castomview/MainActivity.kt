package com.example.castomview
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.castomview.databinding.ActivityMainBinding
import com.example.castomview.statusbar.StatusElement

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val defStatuses = listOf(
            StatusElement(Color.GREEN, 4),
            StatusElement(Color.YELLOW, 7),
            StatusElement(Color.RED, 2)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}