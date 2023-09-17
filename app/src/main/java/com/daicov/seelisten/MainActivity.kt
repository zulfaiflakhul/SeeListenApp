package com.daicov.seelisten

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.daicov.seelisten.databinding.ActivityMainBinding
import com.daicov.seelisten.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.home -> replaceFragment(HomeFragment())
                R.id.akun -> replaceFragment(AkunFragment())
                R.id.informasi -> replaceFragment(InformasiFragment())
                R.id.bantuan -> replaceFragment(BantuanFragment())

                else -> {

                }
            }
            true
        }

        binding.floatingActionButtonCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        when (intent?.extras?.getString("fragmentToLoad")) {
            "akunFragment" ->  {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, AkunFragment()).commit()
            }
            "gameFragmentAE" -> {
                val bundle = Bundle()
                bundle.putInt("progressGame", 5)
                bundle.putInt("GAME_MENU", 1)
                val fragment = GameTebakHurufFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            }
            "gameFragmentFJ" -> {
                val bundle = Bundle()
                bundle.putInt("progressGame", 5)
                bundle.putInt("GAME_MENU", 2)
                val fragment = GameTebakHurufFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            }
            "gameFragmentKO" -> {
                val bundle = Bundle()
                bundle.putInt("progressGame", 5)
                bundle.putInt("GAME_MENU", 3)
                val fragment = GameTebakHurufFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            }
            "gameFragmentPT" -> {
                val bundle = Bundle()
                bundle.putInt("progressGame", 5)
                bundle.putInt("GAME_MENU", 4)
                val fragment = GameTebakHurufFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            }
            "gameFragmentUZ" -> {
                val bundle = Bundle()
                bundle.putInt("progressGame", 5)
                bundle.putInt("GAME_MENU", 5)
                val fragment = GameTebakHurufFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            }
            "gameFragment" -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, GameFragment()).commit()
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}