package com.daicov.seelisten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daicov.seelisten.view.AkunFragment
import com.daicov.seelisten.view.CameraSecondFragment
import com.daicov.seelisten.view.GameTebakHurufFragment

class CameraSecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_second)

        val bundle = Bundle()
        when (intent?.extras?.getString("GAME")) {
            "gameAE" -> {
                bundle.putInt("game", 1)
                val fragment = CameraSecondFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_second, fragment).commit()
            }
            "gameFJ" -> {
                bundle.putInt("game", 2)
                val fragment = CameraSecondFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_second, fragment).commit()
            }
            "gameKO" -> {
                bundle.putInt("game", 3)
                val fragment = CameraSecondFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_second, fragment).commit()
            }
            "gamePT" -> {
                bundle.putInt("game", 4)
                val fragment = CameraSecondFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_second, fragment).commit()
            }
            "gameUZ" -> {
                bundle.putInt("game", 5)
                val fragment = CameraSecondFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_second, fragment).commit()
            }
        }
    }
}