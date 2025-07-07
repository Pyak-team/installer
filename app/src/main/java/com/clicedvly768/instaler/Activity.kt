package com.clicedvly768.instaler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

private val MainActivity.buttonInstall: Any
    get() {
        TODO()
    }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ваш код...

        buttonInstall.setOnClickListener {
            try {
                val apkFile = File("/path/to/your/app.apk") // Укажите правильный путь
                val installer = ApkInstaller(this)
                installer.installApk(apkFile)
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

private fun Any.setOnClickListener(function: () -> Unit) {
    TODO("Not yet implemented")
}
