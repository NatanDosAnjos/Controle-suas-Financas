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
            val psswd = passwordField.text.toString()

            if(email.isNotEmpty()) {
                if(psswd.isNotEmpty()) {
                   auth = FirebaseConfiguration.getAuthentication()

                    auth.signInWithEmailAndPassword(email, psswd).addOnCompleteListener {
                        if (it.isSuccessful) {
                            openFirstActivity()

                        } else {
                            try {
                                throw it.exception!!

                            } catch (e: FirebaseAuthInvalidUserException) {
                                showToast(this, "Usuário não cadastrado")

                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                showToast(this, "Email ou Senha incorretas")
                                e.printStackTrace()

                            } catch (e: Exception) {
                                showToast(this, "Erro ao tentar logar")
                                e.printStackTrace()
                            }
                        }
                    }


                } else {
                    showToast(this, "Digite a senha")
                }

            } else {
                showToast(this, "Digite o email")
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