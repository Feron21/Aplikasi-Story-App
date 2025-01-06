package com.example.storyapp.ui.addstory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.utils.FileUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Suppress("DEPRECATION")
class AddStoryActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        findViewById<ImageView>(R.id.iv_preview)
        val descriptionField = findViewById<EditText>(R.id.ed_add_description)
        val selectButton = findViewById<Button>(R.id.btn_select_image)
        val addButton = findViewById<Button>(R.id.button_add)

        // Ambil token dari Intent atau sumber lainnya
        token = intent.getStringExtra("TOKEN")

        selectButton.setOnClickListener {
            selectImage()
        }

        addButton.setOnClickListener {
            val description = descriptionField.text.toString().trim()

            if (selectedImageUri == null || description.isEmpty()) {
                Toast.makeText(this, "Foto dan deskripsi harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadStory(description, selectedImageUri!!)
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                val imageView = findViewById<ImageView>(R.id.iv_preview)
                Glide.with(this).load(uri).into(imageView)
            }
        }
    }

    private fun uploadStory(description: String, imageUri: Uri) {
        // Konversi URI menjadi File
        val file = FileUtil.getFileFromUri(this, imageUri)
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val imageBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        )

        token?.let { token ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiService = ApiConfig.getApiService(token)
                    val response = apiService.addStory(
                        description = descriptionBody,
                        photo = imageBody
                    )
                    withContext(Dispatchers.Main) {
                        if (!response.error) {
                            Toast.makeText(this@AddStoryActivity, "Cerita berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@AddStoryActivity, "Gagal menambahkan cerita: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddStoryActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(this, "Token tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE = 101
    }
}
