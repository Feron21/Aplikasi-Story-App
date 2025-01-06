package com.example.storyapp.ui.auth

import com.example.storyapp.ui.main.MainActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.utils.SharedPrefs

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val loginButton = findViewById<Button>(R.id.btn_login)
        val registerButton = findViewById<Button>(R.id.btn_register)
        val welcomeImage = findViewById<ImageView>(R.id.iv_profile) // Gambar selamat datang
        val welcomeText = findViewById<TextView>(R.id.tv_welcome) // Kalimat selamat datang

        // Memeriksa apakah pengguna sudah login
        val prefs = SharedPrefs(this)
        if (prefs.isLoggedIn()) {
            // Jika sudah login, langsung ke com.example.storyapp.ui.main.MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Finish this activity so that it can't be accessed by pressing back
            return
        }

        // Animasi gambar selamat datang
        val imageAnimator = ObjectAnimator.ofFloat(welcomeImage, "alpha", 0f, 1f)
        imageAnimator.duration = 1000 // Durasi 1 detik
        imageAnimator.start()

        // Animasi teks selamat datang
        val textAnimator = ObjectAnimator.ofFloat(welcomeText, "translationY", -100f, 0f)
        textAnimator.duration = 1000 // Durasi 1 detik
        textAnimator.interpolator = AccelerateDecelerateInterpolator() // Jenis interpolator
        textAnimator.start()

        // Animasi tombol login
        val loginButtonAnimator = ObjectAnimator.ofFloat(loginButton, "translationY", 100f, 0f)
        loginButtonAnimator.duration = 1000 // Durasi 1 detik
        loginButtonAnimator.interpolator = AccelerateDecelerateInterpolator() // Jenis interpolator
        loginButtonAnimator.start()

        // Animasi tombol register
        val registerButtonAnimator = ObjectAnimator.ofFloat(registerButton, "translationY", 100f, 0f)
        registerButtonAnimator.duration = 1000 // Durasi 1 detik
        registerButtonAnimator.interpolator = AccelerateDecelerateInterpolator() // Jenis interpolator
        registerButtonAnimator.start()

        loginButton.setOnClickListener {
            // Arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        registerButton.setOnClickListener {
            // Arahkan ke RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
