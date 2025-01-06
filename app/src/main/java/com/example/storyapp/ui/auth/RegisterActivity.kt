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
import com.example.storyapp.ui.viewModel.RegisterViewModel
import com.example.storyapp.utils.Injection
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI components
        val edName = findViewById<EditText>(R.id.ed_register_name)
        val edEmail = findViewById<EditText>(R.id.ed_register_email)
        val edPassword = findViewById<EditText>(R.id.ed_register_password)
        val btnRegister = findViewById<Button>(R.id.btn_register)

        // Launch coroutine to initialize the ViewModel asynchronously
        lifecycleScope.launch {
            val repository = Injection.provideRepository(this@RegisterActivity)
            viewModel = RegisterViewModel(repository) // Initialize the ViewModel with the repository

            // Set up button click listener
            btnRegister.setOnClickListener {
                val name = edName.text.toString().trim()
                val email = edEmail.text.toString().trim()
                val password = edPassword.text.toString().trim()

                // Validate input
                if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 6) {
                    registerUser(name, email, password)
                } else {
                    Toast.makeText(this@RegisterActivity, "Periksa input Anda", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        // Launch a coroutine to handle registration
        lifecycleScope.launch {
            try {
                // Call the register function in ViewModel
                val response = viewModel.register(name, email, password)
                Toast.makeText(this@RegisterActivity, "Registrasi berhasil: ${response.message}", Toast.LENGTH_SHORT).show()
                navigateToMain()
            } catch (exception: Exception) {
                // Handle errors
                Toast.makeText(this@RegisterActivity, "Registrasi gagal: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        // Navigate to MainActivity after successful registration
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
