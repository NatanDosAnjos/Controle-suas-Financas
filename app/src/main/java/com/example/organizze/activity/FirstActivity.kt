package com.example.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizze.adapter.MyAdapter
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.database.DataBase
import com.example.organizze.model.FinancialMovement
import com.example.organizze.others.GlobalUserInstance
import com.example.organizze.others.getLocale
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.NumberFormat

class FirstActivity : AppCompatActivity() {
    lateinit var viewSalutation: TextView
    lateinit var viewBalance: TextView
    lateinit var viewInformation: TextView
    var uidFromFirebase: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setSupportActionBar(findViewById(R.id.toolbar))

        // O Método estático getAuthentication() é um singleton da referência do Firebase Auth
        // Capturo o UID do usuário logado no momento e o coloco em uma variável que seja visível por toda a classe
        // Essa variável será passada como parâmetro para buscar os dados do usuário correto no banco de dados na função OnStart()
        uidFromFirebase = FirebaseConfiguration.getAuthentication().currentUser?.uid

        viewSalutation = findViewById(R.id.textViewSalutation)
        viewInformation = findViewById(R.id.textViewInformation)
        viewBalance = findViewById(R.id.textViewBalance)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, IncomeExpensesActivity::class.java))
        }

        findViewById<MaterialCalendarView>(R.id.calendarView).setOnMonthChangedListener { _, date ->
            println(date.month)
        }

        val myAdapter = MyAdapter(fakeList())
        val myLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
           layoutManager = myLayoutManager
           adapter = myAdapter
        }
    }


    override fun onStart() {
        super.onStart()
        // Esse método recebe uma instância da classe User que será atribuída a única propriedade do companion object
        // da classe GlobalUserInstance que está no pacote others
        // E é nesse companion object que estou tentando colocar um listener
        DataBase.readUserInformationInDataBase(uidFromFirebase!!)
        GlobalUserInstance.setOnChangeUser(kotlinx.coroutines.Runnable {

            // Quando a propriedade da classe GlobalUserInstance mudar seu valor, queria colocar esse bloco de código para executar
            val salutation =
                "${getString(R.string.salutation)}, ${GlobalUserInstance.instance.name}"

            viewSalutation.text = salutation
            showValue(viewInformation, GlobalUserInstance.instance.totalExpenses)
            showValue(viewBalance, GlobalUserInstance.instance.getBalance())
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout_item -> {
                FirebaseConfiguration.getAuthentication().signOut()
                startActivity( Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun showValue(myViewValue: TextView, value: Double) {
        val currentLocale = getLocale(resources)
        val toFormatValue = NumberFormat.getCurrencyInstance(currentLocale)
        myViewValue.text  = toFormatValue.format(value)
    }
}

/*---------------------------------------------------------------------------------------------*/

private fun fakeList(): Array<FinancialMovement> {
    val fin1 = FinancialMovement().apply {
        value = 16.50
        description = "Só um lanchinho"
        category = "Comida"
        incomeOrExpense = "E"
    }

    val fin2 = FinancialMovement().apply {
        value = 3547.0
        description = "Salário AIS Digital"
        category = "Salário"
        incomeOrExpense = "I"
    }

    val fin3 = FinancialMovement().apply {
        value = 123.90
        description = "Internet"
        category = "Gastos Fixos"
        incomeOrExpense = "E"
    }

    return arrayOf(fin1, fin2, fin3)
}