package com.hindupanchang.calendar.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hindupanchang.calendar.R
import com.hindupanchang.calendar.utils.AppLanguage
import com.hindupanchang.calendar.utils.LanguageManager

class LanguageSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        val btnEnglish = findViewById<Button>(R.id.btn_english)
        val btnHindi = findViewById<Button>(R.id.btn_hindi)
        val btnOdia = findViewById<Button>(R.id.btn_odia)

        btnEnglish.setOnClickListener { selectLanguage(AppLanguage.ENGLISH) }
        btnHindi.setOnClickListener { selectLanguage(AppLanguage.HINDI) }
        btnOdia.setOnClickListener { selectLanguage(AppLanguage.ODIA) }
    }

    private fun selectLanguage(language: AppLanguage) {
        LanguageManager.saveLanguage(this, language)
        LanguageManager.setNotFirstLaunch(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
