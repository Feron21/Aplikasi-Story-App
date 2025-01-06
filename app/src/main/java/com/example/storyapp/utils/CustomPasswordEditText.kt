package com.example.storyapp.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        // Pastikan EditText dapat difokuskan dan diklik
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true

        // Tambahkan TextWatcher untuk memeriksa panjang password
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8) {
                    error = context.getString(R.string.error_password_too_short)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}