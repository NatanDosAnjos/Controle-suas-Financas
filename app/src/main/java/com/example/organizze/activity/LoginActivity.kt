package com.example.organizze.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.others.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.editTextEmail_LoginActivity)
        passwordField = findViewById(R.id.editTexLogin_LoginActivity)


        findViewById<Button>(R.id.btnLoginActivity).setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if(email.isNotEmpty()) {
                if(password.isNotEmpty()) {
                    btnLoginActivity.text = getString(R.string.logging_in)
                    btnLoginActivity.isEnabled = false
                    auth = FirebaseConfiguration.getAuthentication()

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {


                        if (it.isSuccessful) {
                            openFirstActivity()

                        } else {
                            try {
                                throw it.exception!!

                            } catch (e: FirebaseAuthInvalidUserException) {
                                showToast(this, getString(R.string.unregistered_user))

                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                showToast(this, getString(R.string.wrong_email_or_password))

                            } catch (e: Exception) {
                                showToast(this, getString(R.string.login_error))
                            }
                            btnLoginActivity.isEnabled = true
                            btnLoginActivity.text = getString(R.string.login)
                        }
                    }


                } else {
                    showToast(this, getString(R.string.type_password))
                }

            } else {
                showToast(this, getString(R.string.type_email))
            }
        }

        findViewById<TextView>(R.id.TV_DontHaveAccount).setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
            finish()
        }

    }

    private fun openFirstActivity() {
        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }
}