package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardKasirActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard_kasir)

        val btnBuatAkunCustomer =
            findViewById<Button>(R.id.btnBuatAkunCustomer)

        val btnTambahProgress =
            findViewById<Button>(R.id.btnTambahProgress)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        val btnMenuServis =
            findViewById<Button>(R.id.btnMenuServis)

        btnMenuServis.setOnClickListener {

            startActivity(
                Intent(this, MenuServisActivity::class.java)
            )
        }

        btnBuatAkunCustomer.setOnClickListener {

            startActivity(
                Intent(this, BuatAkunCustomerActivity::class.java)
            )
        }

        btnTambahProgress.setOnClickListener {

            startActivity(
                Intent(this, TambahProgressActivity::class.java)
            )
        }

        btnLogout.setOnClickListener {

            val sharedPreferences =
                getSharedPreferences("LOGIN", MODE_PRIVATE)

            val editor =
                sharedPreferences.edit()

            editor.clear()
            editor.apply()

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
        }
    }
}