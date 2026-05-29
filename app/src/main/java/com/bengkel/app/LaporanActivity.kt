package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LaporanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_laporan)

        val btnLaporanServis =
            findViewById<Button>(R.id.btnLaporanServis)

        val btnLaporanPendapatan =
            findViewById<Button>(R.id.btnLaporanPendapatan)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        btnLaporanServis.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LaporanServisActivity::class.java
                )
            )

        }

        btnLaporanPendapatan.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LaporanPendapatanActivity::class.java
                )
            )
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }
}