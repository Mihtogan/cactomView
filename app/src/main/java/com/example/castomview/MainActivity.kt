package com.example.castomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.castomview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.firstStatus0?.apply {
            setCount(4)
            setTotalCount(36)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}