package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class DashboardCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard_customer)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        val btnLihatProgress =
            findViewById<Button>(R.id.btnLihatProgress)

        val btnProfil =
            findViewById<Button>(R.id.btnProfil)

        btnLihatProgress.setOnClickListener {
            startActivity(
                Intent(this, ProgressServisActivity::class.java)
            )
        }

        btnProfil.setOnClickListener {
            startActivity(
                Intent(this, ProfilCustomerActivity::class.java)
            )
        }

        btnLogout.setOnClickListener {

            val sharedPreferences =
                getSharedPreferences("LOGIN", MODE_PRIVATE)

            val editor =
                sharedPreferences.edit()

            editor.clear()
            editor.apply()

            FirebaseAuth.getInstance().signOut()

            val googleSignInClient =
                GoogleSignIn.getClient(
                    this,
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                )

            googleSignInClient.signOut()
                .addOnCompleteListener {

                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )

                    overridePendingTransition(0, 0)

                    finish()
                }
        }
    }
}