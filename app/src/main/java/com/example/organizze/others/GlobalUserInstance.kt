package com.example.organizze.others

import com.example.organizze.model.User

class GlobalUserInstance{
    // Cria uma classe que representa uma instância stática da classe Usuário
    companion object {
        private var runnable: Runnable? = null

        @JvmStatic var instance = User()
            set(value) {
            field = value
                if (instance.name.isNotBlank()) {
                    runnable?.run()
                }
        }

        fun setOnChangeUserInstance(runnable: Runnable) {
            this.runnable = runnable
        }
    }
}