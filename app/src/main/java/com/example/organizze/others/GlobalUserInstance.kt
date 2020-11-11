package com.example.organizze.others

import com.example.organizze.model.User

class GlobalUserInstance{
    // Este é quase um singleton. Criei uma classe que representa uma instância stática da classe Usuário
    companion object {
        private var runnable: Runnable? = null

        // Instância, que apesar de não ser uma constante, pode ter seu valor recuperado em todo o app
        // Sei que não é uma boa prática deixar ele possível a alteração por qualquer classe, estou tentando achar uma solução pra isso
        // Por hora me garanti que estou modificando este objeto em apenas um local no projeto, que é na função OnStart()
        @JvmStatic var instance = User()
        set(value) {
            field = value
            onChangeUser(runnable)
        }

        private fun onChangeUser(run: Runnable?) {
            run?.run()
        }

        fun setOnChangeUser(runnable: Runnable) {
            this.runnable = runnable
        }
    }
}