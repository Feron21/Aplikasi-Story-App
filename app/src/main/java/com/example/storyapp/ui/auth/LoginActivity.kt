package com.example.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.viewModel.LoginViewModel
import com.example.storyapp.utils.CustomPasswordEditText
import com.example.storyapp.utils.Injection
import com.example.storyapp.utils.SharedPrefs
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        val edEmail = findViewById<EditText>(R.id.ed_login_email)
        val edPassword = findViewById<CustomPasswordEditText>(R.id.ed_login_password) // Custom EditText
        val btnLogin = findViewById<Button>(R.id.btn_login)

        // Launch coroutine to initialize the ViewModel asynchronously
        lifecycleScope.launch {
            val repository = Injection.provideRepository(this@LoginActivity)
            viewModel = LoginViewModel(repository) // Initialize the ViewModel with the repository

            btnLogin.setOnClickListener {
                val email = edEmail.text.toString().trim()
                val password = edPassword.text.toString().trim()

                if (email.isNotEmpty() && password.length >= 8) {
                    loginUser(email, password)
                } else {
                    Toast.makeText(this@LoginActivity, "Periksa input Anda", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Perform login using ViewModel's suspend function
                val response = viewModel.login(email, password)
                val token = response.loginResult.token
                saveTokenAndNavigate(token)
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTokenAndNavigate(token: String) {
        val sharedPrefs = SharedPrefs(this)
        sharedPrefs.saveToken(token)

        // Navigate to com.example.storyapp.ui.main.MainActivity
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}
