package com.example.organizze.config

import com.google.firebase.auth.FirebaseAuth

class FirebaseConfiguration {

    companion object {
        @JvmStatic private var authentication: FirebaseAuth? = null

        fun getAuthentication() : FirebaseAuth {
            if(authentication == null) {
                authentication = FirebaseAuth.getInstance()
            }
            return this.authentication!!
        }
    }
}