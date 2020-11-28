package com.example.organizze.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import android.content.Intent
import android.view.View
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.activity.LoginActivity as LoginActivity


class MainActivity : IntroActivity(), View.OnClickListener{

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        isButtonBackVisible = false
        isButtonNextVisible = false

        /*addSlide(SimpleSlide.Builder()
            .title("Organize suas contas")
            .description("Você irá se surpreender")
            .background(android.R.color.holo_green_light)
            .image(R.drawable.um)
            .canGoBackward(false)
            .scrollable(true)
            .build()
        )

        addSlide(SimpleSlide.Builder()
            .title("Tenha o controle")
            .description("Tudo na palma da mão")
            .background(android.R.color.holo_green_light)
            .image(R.drawable.dois)
            .scrollable(true)
            .build()
        )

        addSlide(SimpleSlide.Builder()
            .title("Viva despreocupado")
            .description("Seus gastos bem gerenciados")
            .background(android.R.color.holo_green_light)
            .image(R.drawable.tres)
            .scrollable(true)
            .build()
        )*/

        addSlide(FragmentSlide.Builder()
            .fragment(R.layout.intro_cadastro)
            .canGoForward(false)
            .background(android.R.color.white)
            .canGoForward(false)
            .canGoBackward(false)
            .build()
        )
    }

    override fun onStart() {
        super.onStart()
        startFirstActivityIfHasALoggedUser()
    }


    private fun startFirstActivityIfHasALoggedUser() {
        auth = FirebaseConfiguration.getAuthentication()
        if(auth.currentUser != null) {
            openFirstActivity()
        }
    }

    private fun openFirstActivity() {
        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }

    override fun onClick(p0: View?) {
        val tvAlreadyHasAccount = findViewById<TextView>(R.id.tvAlreadyHasAccount)
        val btnCadastro = findViewById<Button>(R.id.btnCadastro)

        when (p0) {
            btnCadastro -> {
                startActivity(Intent(this, CadastroActivity::class.java))
                finish()
            }

            tvAlreadyHasAccount -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}




