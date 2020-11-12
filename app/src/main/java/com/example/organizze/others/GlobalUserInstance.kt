package com.example.organizze.others

import com.example.organizze.model.User

class GlobalUserInstance{
    // Este é quase um singleton. Criei uma classe que representa uma instância stática da classe Usuário
    companion object {
        private var runnable: Runnable? = null

        @JvmStatic var instance = User()
        set(value) {
            field = value
            onChangeUser(runnable)
        }

        private fun onChangeUser(run: Runnable?) {
            run?.run()
        }
    }
}