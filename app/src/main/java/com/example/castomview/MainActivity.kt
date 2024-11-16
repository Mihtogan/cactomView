package com.example.castomview

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
            StatusElement(Color.GREEN, 7),
            StatusElement(Color.YELLOW, 10),
            StatusElement(Color.RED, 3)
        )

        binding?.apply {
            statusBar.setStatuses(defStatuses)

            firstStatus0.apply {
                setCount(7)
                setTotalCount(20)
            }
            firstStatus1.apply {
                setCount(10)
                setTotalCount(20)
            }
            firstStatus2.apply {
                setCount(3)
                setTotalCount(20)
            }

            buttonSetDrawMod0.setOnClickListener {
                binding?.statusBar?.apply {
                    setDrawMode(0)
                    reDraw()
                }
            }
            buttonSetDrawMod1.setOnClickListener {
                binding?.statusBar?.apply {
                    setDrawMode(1)
                    reDraw()
                }
            }
            buttonSetDrawMod2.setOnClickListener {
                binding?.statusBar?.apply {
                    setDrawMode(2)
                    reDraw()
                }
            }

            buttonRoundingStatusLine.setOnClickListener {
                binding?.statusBar?.apply {
                    isRoundingStatusLine(true)
                    reDraw()
                }
            }
            buttonNoRoundingStatusLine.setOnClickListener {
                binding?.statusBar?.apply {
                    isRoundingStatusLine(false)
                    reDraw()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}