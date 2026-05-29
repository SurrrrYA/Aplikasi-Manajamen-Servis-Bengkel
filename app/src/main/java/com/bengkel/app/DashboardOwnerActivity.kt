package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.google.android.material.bottomnavigation.BottomNavigationView

import java.text.NumberFormat
import java.util.Locale

class DashboardOwnerActivity : AppCompatActivity() {

    lateinit var txtTotalServis: TextView
    lateinit var txtPendapatanHariIni: TextView
    lateinit var txtServisBelumSelesai: TextView
    lateinit var txtServisMenginap: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard_owner)

        txtTotalServis =
            findViewById(R.id.txtTotalServis)

        txtPendapatanHariIni =
            findViewById(R.id.txtPendapatanHariIni)

        txtServisBelumSelesai =
            findViewById(R.id.txtServisBelumSelesai)

        txtServisMenginap =
            findViewById(R.id.txtServisMenginap)

        val btnInventori =
            findViewById<Button>(R.id.btnInventori)

        val btnLaporan =
            findViewById<Button>(R.id.btnLaporan)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        val btnJasa =
            findViewById<Button>(R.id.btnJasa)

        val bottomNav =
            findViewById<BottomNavigationView>(R.id.bottomNavigation)

        btnJasa.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    JasaActivity::class.java
                )
            )
        }

        btnInventori.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    InventoriBarangActivity::class.java
                )
            )
        }

        btnLaporan.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LaporanActivity::class.java
                )
            )
        }

        btnLogout.setOnClickListener {

            val sharedPreferences =
                getSharedPreferences(
                    "LOGIN",
                    MODE_PRIVATE
                )

            val editor =
                sharedPreferences.edit()

            editor.clear()
            editor.apply()

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }

        bottomNav.selectedItemId =
            R.id.nav_dashboard

        bottomNav.setOnItemSelectedListener {

            when(it.itemId){

                R.id.nav_dashboard -> {
                    true
                }

                R.id.nav_servis -> {

                    startActivity(
                        Intent(
                            this,
                            MenuServisActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_laporan -> {

                    startActivity(
                        Intent(
                            this,
                            LaporanActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_profil -> {

                    startActivity(
                        Intent(
                            this,
                            ProfileOwnerActivity::class.java
                        )
                    )

                    true
                }

                else -> false
            }
        }

        getDashboardOwner()
    }

    override fun onResume() {
        super.onResume()
        getDashboardOwner()
    }

    private fun formatRupiah(angka: Int): String {

        return NumberFormat
            .getInstance(
                Locale("in", "ID")
            )
            .format(angka)
    }

    private fun getDashboardOwner(){

        val url =
            "http://192.168.18.7/Bengkel_API/dashboard/get_dashboard_owner.php"

        val request =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    val totalServis =
                        response.getInt(
                            "total_servis_hari_ini"
                        )

                    val pendapatan =
                        response.getInt(
                            "pendapatan_hari_ini"
                        )

                    val belumSelesai =
                        response.getInt(
                            "servis_belum_selesai"
                        )

                    val menginap =
                        response.getInt(
                            "servis_menginap"
                        )

                    txtTotalServis.text =
                        "Total Servis Hari Ini : $totalServis"

                    txtPendapatanHariIni.text =
                        "Pendapatan Hari Ini : Rp ${formatRupiah(pendapatan)}"

                    txtServisBelumSelesai.text =
                        "Servis Belum Selesai : $belumSelesai"

                    txtServisMenginap.text =
                        "Servis Menginap : $menginap"
                },

                { error ->

                    Toast.makeText(
                        this,
                        error.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

        Volley.newRequestQueue(this).add(request)
    }
}