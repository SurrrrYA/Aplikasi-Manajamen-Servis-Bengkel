package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileOwnerActivity : AppCompatActivity() {

    lateinit var txtNama: TextView
    lateinit var txtUsername: TextView
    lateinit var txtRole: TextView
    lateinit var txtEmail: TextView
    lateinit var txtNoHp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_owner)

        txtNama = findViewById(R.id.txtNama)
        txtUsername = findViewById(R.id.txtUsername)
        txtRole = findViewById(R.id.txtRole)
        txtEmail = findViewById(R.id.txtEmail)
        txtNoHp = findViewById(R.id.txtNoHp)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val nama =
            sharedPreferences.getString("nama", "-")

        val username =
            sharedPreferences.getString("username", "-")

        val role =
            sharedPreferences.getString("role", "-")

        val email =
            sharedPreferences.getString("email", "-")

        val noHp =
            sharedPreferences.getString("no_hp", "-")

        txtNama.text = "Nama : $nama"
        txtUsername.text = "Username : $username"
        txtRole.text = "Role : $role"
        txtEmail.text = "Email : $email"
        txtNoHp.text = "No HP : $noHp"

        btnLogout.setOnClickListener {

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
    }
}