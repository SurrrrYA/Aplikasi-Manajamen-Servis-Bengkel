package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONObject

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var auth: FirebaseAuth

    val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val isLogin =
            sharedPreferences.getBoolean("isLogin", false)

        if(isLogin){

            val role =
                sharedPreferences.getString("role","")

            val intent =
                if(role == "owner"){
                    Intent(this, DashboardOwnerActivity::class.java)
                }else if(role == "kasir"){
                    Intent(this, DashboardKasirActivity::class.java)
                }else{
                    Intent(this, DashboardCustomerActivity::class.java)
                }

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        auth =
            FirebaseAuth.getInstance()

        val gso =
            GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestIdToken(
                    getString(R.string.default_web_client_id)
                )
                .requestEmail()
                .build()

        googleSignInClient =
            GoogleSignIn.getClient(this, gso)

        val etUsername =
            findViewById<EditText>(R.id.etUsername)

        val etPassword =
            findViewById<EditText>(R.id.etPassword)

        val btnLogin =
            findViewById<Button>(R.id.btnLogin)

        val btnGoogle =
            findViewById<Button>(R.id.btnGoogle)

        val txtDaftar =
            findViewById<TextView>(R.id.txtDaftar)

        txtDaftar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterCustomerActivity::class.java
                )
            )
        }

        btnLogin.setOnClickListener {

            val username =
                etUsername.text.toString()

            val password =
                etPassword.text.toString()

            login(username, password)
        }

        btnGoogle.setOnClickListener {

            val signInIntent =
                googleSignInClient.signInIntent

            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if(requestCode == RC_SIGN_IN){

            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account =
                    task.getResult(ApiException::class.java)

                firebaseAuth(
                    account.idToken!!
                )

            }catch (e: Exception){

                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun firebaseAuth(idToken: String){

        val credential =
            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->

                if(task.isSuccessful){

                    val user =
                        auth.currentUser

                    val email =
                        user?.email ?: ""

                    val nama =
                        user?.displayName ?: ""

                    loginGoogleKeMysql(email, nama)

                }else{

                    Toast.makeText(
                        this,
                        "Login Google Gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun simpanLogin(
        username: String,
        role: String,
        nama: String,
        email: String,
        noHp: String
    ){

        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val editor =
            sharedPreferences.edit()

        editor.putString("username", username)
        editor.putString("role", role)
        editor.putString("nama", nama)
        editor.putString("email", email)
        editor.putString("no_hp", noHp)
        editor.putBoolean("isLogin", true)

        editor.apply()
    }

    private fun pindahDashboard(role: String){

        val intent =
            if(role == "owner"){
                Intent(this, DashboardOwnerActivity::class.java)
            }else if(role == "kasir"){
                Intent(this, DashboardKasirActivity::class.java)
            }else if(role == "customer"){
                Intent(this, DashboardCustomerActivity::class.java)
            }else{
                Intent(this, DashboardActivity::class.java)
            }

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun loginGoogleKeMysql(email: String, nama: String){

        val url =
            "http://192.168.18.7/Bengkel_API/auth/google_login.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener { response ->

                val jsonObject =
                    JSONObject(response)

                val status =
                    jsonObject.getString("status")

                if(status == "success"){

                    val username =
                        jsonObject.getString("username")

                    val role =
                        jsonObject.getString("role")

                    val namaUser =
                        jsonObject.optString("nama", nama)

                    val emailUser =
                        jsonObject.optString("email", email)

                    val noHpUser =
                        jsonObject.optString("no_hp", "-")

                    simpanLogin(
                        username,
                        role,
                        namaUser,
                        emailUser,
                        noHpUser
                    )

                    Toast.makeText(
                        this,
                        "Login Google Berhasil",
                        Toast.LENGTH_SHORT
                    ).show()

                    pindahDashboard(role)

                }else{

                    Toast.makeText(
                        this,
                        jsonObject.optString(
                            "message",
                            "Gagal simpan akun Google"
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            },

            Response.ErrorListener { error ->

                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ){

            override fun getParams(): MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["email"] = email
                params["nama"] = nama

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun login(username: String, password: String) {

        val url =
            "http://192.168.18.7/Bengkel_API/auth/login.php"

        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener { response ->

                val jsonObject =
                    JSONObject(response)

                val status =
                    jsonObject.getString("status")

                if(status == "success"){

                    val role =
                        jsonObject.getString("role")

                    val nama =
                        jsonObject.optString("nama", "-")

                    val email =
                        jsonObject.optString("email", "-")

                    val noHp =
                        jsonObject.optString("no_hp", "-")

                    simpanLogin(
                        username,
                        role,
                        nama,
                        email,
                        noHp
                    )

                    Toast.makeText(
                        this,
                        "Login Berhasil",
                        Toast.LENGTH_SHORT
                    ).show()

                    pindahDashboard(role)

                }else{

                    Toast.makeText(
                        this,
                        jsonObject.optString(
                            "message",
                            "Login Gagal"
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },

            Response.ErrorListener { error ->

                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }

        ) {

            override fun getParams():
                    MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["username"] = username
                params["password"] = password

                return params
            }
        }

        queue.add(request)
    }
}