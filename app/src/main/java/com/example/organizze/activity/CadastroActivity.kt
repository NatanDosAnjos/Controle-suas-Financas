package com.example.organizze.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.organizze.others.*
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.database.DataBase
import com.example.organizze.model.User
import com.google.firebase.auth.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        nameField = findViewById(R.id.editTextName)
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextPassword)

        findViewById<Button>(R.id.btnSignUp).setOnClickListener {

            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (name.isNotEmpty()) {
                if (email.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        user = User(name, email)
                        user.password = password
                        registerUser(user)

                    } else {
                        showToast(this, "Coloque a senha")
                    }

                } else {
                    showToast(this, "Informe o Email")
                }

            } else {
                showToast(this, "Preencha o seu nome")
            }
        }

        findViewById<TextView>(R.id.textViewAlreadyHaveAnAccount).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(user: User) {
        val auth = FirebaseConfiguration.getAuthentication()

            auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener{
            if (it.isSuccessful) {
                user.userId = FirebaseConfiguration.getAuthentication().currentUser!!.uid
                DataBase.saveInDataBase(user)

                startActivity(Intent(this, FirstActivity::class.java))
                finish()

            } else {

                try {
                   throw it.exception!!

                } catch (e: FirebaseAuthUserCollisionException) {
                    showToast(this, "Email já cadastrado")

                } catch (e: FirebaseAuthEmailException) {
                    showToast(this, "Formato de e-mail não suportado")

                } catch (e: FirebaseAuthWeakPasswordException) {
                    showToast(this, "Senha com menos de 6 caracteres")

                }catch (e: FirebaseAuthInvalidCredentialsException) {
                    showToast(this, "Formato de e-mail não válido")

                } catch (e: Exception) {
                    showToast(this, "Erro ao Cadastrar Usuário")
                    e.printStackTrace()
                }
            }
        }

    }
}
