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
                        showToast(this, getString(R.string.type_password))
                    }

                } else {
                    showToast(this, getString(R.string.type_email))
                }

            } else {
                showToast(this, getString(R.string.type_your_name))
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
                DataBase.saveInDataBase(user, User.FIRST_CHILD)

                startActivity(Intent(this, FirstActivity::class.java))
                finish()

            } else {

                try {
                   throw it.exception!!

                } catch (e: FirebaseAuthUserCollisionException) {
                    showToast(this, getString(R.string.email_already_registered))

                } catch (e: FirebaseAuthEmailException) {
                    showToast(this, getString(R.string.invalid_email_format))

                } catch (e: FirebaseAuthWeakPasswordException) {
                    showToast(this, getString(R.string.at_least_six_characters))

                }catch (e: FirebaseAuthInvalidCredentialsException) {
                    showToast(this, getString(R.string.invalid_email_format))

                } catch (e: Exception) {
                    showToast(this, getString(R.string.error_to_register_user))
                    e.printStackTrace()
                }
            }
        }

    }
}
