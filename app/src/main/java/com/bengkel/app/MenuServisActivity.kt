package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuServisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu_servis)

        findViewById<Button>(R.id.btnServisMasuk)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        ServisMasukActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.btnListServis)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        ListServisActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.btnServisMenginap)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        ServisMenginapActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.btnServisSelesai)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        ServisSelesaiActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.btnKembali)
            .setOnClickListener {

                finish()
            }
    }
}