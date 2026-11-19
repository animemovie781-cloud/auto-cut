package com.mediaghor.autocut

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var etInterval: EditText
    private lateinit var tvValue: TextView
    private lateinit var tvPreviewInterval: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        etInterval = findViewById(R.id.etInterval)
        tvValue = findViewById(R.id.tvCpsValue)
        tvPreviewInterval = findViewById(R.id.tvPreviewInterval)

        val main = findViewById<View>(R.id.main)
        val statusBarScrim = findViewById<View>(R.id.statusBarScrim)
        val scroll = findViewById<ScrollView>(R.id.scroll)
        val bottomNav = findViewById<View>(R.id.bottomNav)
        ViewCompat.setOnApplyWindowInsetsListener(main) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, 0)
            statusBarScrim.layoutParams = statusBarScrim.layoutParams.apply { height = bars.top }
            scroll.setPadding(bars.left, 0, bars.right, 0)
            bottomNav.setPadding(0, 0, 0, bars.bottom)
            insets
        }

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<View>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            ))
        }
        findViewById<View>(R.id.navEdit).setOnClickListener {
            if (EditActivity.activeInstance?.get() != null) {
                startActivity(Intent(this, EditActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                ))
            } else {
                startActivity(Intent(this, MainActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                ))
                startActivity(Intent(this, VideoPickerActivity::class.java))
            }
        }

        etInterval.setText(AppSettings.cutIntervalSeconds(this).toString())
        findViewById<View>(R.id.btnApplyInterval).setOnClickListener { saveInterval() }
        listOf(
            R.id.btnPreset10 to 10,
            R.id.btnPreset12 to 12,
            R.id.btnPreset14 to 14,
            R.id.btnPreset15 to 15
        ).forEach { (id, seconds) ->
            findViewById<View>(id).setOnClickListener {
                etInterval.setText(seconds.toString())
                saveInterval()
            }
        }
        render()
    }

    private fun saveInterval() {
        val value = etInterval.text.toString().trim().toIntOrNull()
        if (value == null || value !in AppSettings.MIN_INTERVAL_SECONDS..AppSettings.MAX_INTERVAL_SECONDS) {
            etInterval.error = getString(R.string.interval_error, AppSettings.MAX_INTERVAL_SECONDS)
            return
        }
        AppSettings.setCutIntervalSeconds(this, value)
        etInterval.setText(value.toString())
        render()
        Toast.makeText(this, getString(R.string.interval_saved, value), Toast.LENGTH_SHORT).show()
    }

    private fun render() {
        val seconds = AppSettings.cutIntervalSeconds(this)
        tvValue.text = getString(R.string.interval_value, seconds)
        tvPreviewInterval.text = getString(R.string.interval_preview, seconds)
    }
}
