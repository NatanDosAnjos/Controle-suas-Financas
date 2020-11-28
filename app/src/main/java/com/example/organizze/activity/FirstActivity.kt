package com.example.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizze.adapter.MyAdapter
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.dao.SQLiteDAO
import com.example.organizze.database.DataBase
import com.example.organizze.helper.SQLiteConnection
import com.example.organizze.model.FinancialMovement
import com.example.organizze.others.GlobalUserInstance
import com.example.organizze.others.getLocale
import com.example.organizze.others.showToast
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.NumberFormat

class FirstActivity : AppCompatActivity() {
    // Lista que será manipulada pelo banco de dados e exibida na RecyclerView
    companion object {
        @JvmStatic val listOfFinancialMovement = mutableListOf<FinancialMovement>()
    }

    // Decalaração de atributos
    lateinit var viewSalutation: TextView
    private lateinit var viewInformation: TextView
    var uidFromFirebase: String? = null
    lateinit var sqlDataBase: SQLiteDAO
    private val myAdapter = MyAdapter(listOfFinancialMovement)
    private lateinit var yearMonth: String
    private lateinit var calendar: MaterialCalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setSupportActionBar(findViewById(R.id.toolbar))
        sqlDataBase = SQLiteDAO(this)


        // O Método estático getAuthentication() é um singleton da referência do Firebase Auth
        // Captura o UID do usuário logado no momento e o coloco em uma variável que seja visível por toda a classe
        // Essa variável será passada como parâmetro para buscar os dados do usuário correto no banco de dados na função OnStart()
        uidFromFirebase = FirebaseConfiguration.getAuthentication().currentUser?.uid

        // Lẽ os dados do usuário especificado pelo uid e atribui a instancia da classe User referente na variável "instance" da classe GlobalUserInstance
        DataBase.readUserInformationInDataBase(uidFromFirebase!!)

        //Define o listener listener para a propriedade instace
        GlobalUserInstance.setOnChangeUser(kotlinx.coroutines.Runnable {
            val salutation = "${getString(R.string.salutation)}, ${GlobalUserInstance.instance.name}"
            viewSalutation.text = salutation
            //Mostrar valores vindos do firebase
            //showValue(viewInformation, GlobalUserInstance.instance.getBalance())
            findViewById<TextView>(R.id.textViewBalance).text = getString(R.string.total_balance)
        })

        calendar = findViewById(R.id.calendarView)
        viewSalutation = findViewById(R.id.textViewSalutation)
        viewInformation = findViewById(R.id.textViewInformation)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, IncomeExpensesActivity::class.java))
        }

        calendar.setOnMonthChangedListener { _, date ->
            yearMonth = "${date.year}/${date.month+1}"
            sqlDataBase.getMovements(yearMonth, Runnable { myAdapter.notifyDataSetChanged() })
            changeTotalBalance()
        }


        val myLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
           layoutManager = myLayoutManager
           adapter = myAdapter
        }

    }

    private fun changeTotalBalance() {
        var total = 0.0
        for(fm in listOfFinancialMovement) {
            if (fm.isExpense()) {
                total -= fm.value
            } else {
                total += fm.value
            }
        }
        showValue(viewInformation, total)
    }

    override fun onStart() {
        super.onStart()
        val currentDate = calendar.currentDate
        yearMonth = "${currentDate.year}/${ currentDate.month+1 }"
        sqlDataBase.getMovements(yearMonth, Runnable { myAdapter.notifyDataSetChanged() })
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
