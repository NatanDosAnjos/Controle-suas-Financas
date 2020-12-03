package com.example.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizze.R
import com.example.organizze.recycler.adapter.MyAdapter
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.database.dao.FirebaseDAO
import com.example.organizze.database.dao.SQLiteDAO
import com.example.organizze.helper.SharedPrefsUserId
import com.example.organizze.model.FinancialMovement
import com.example.organizze.model.User
import com.example.organizze.others.GlobalUserInstance
import com.example.organizze.others.getLocale
import com.example.organizze.recycler.listener.RecyclerItemClickListener
import com.example.organizze.recycler.listener.RecyclerItemClickListener.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.NumberFormat

class FirstActivity : AppCompatActivity() {
    // Lista que será manipulada pelo banco de dados e exibida na RecyclerView
    companion object {
        @JvmStatic val listOfFinancialMovement = mutableListOf<FinancialMovement>()
    }

    // Decalaração de atributos
    private lateinit var viewSalutation: TextView
    private lateinit var viewInformation: TextView
    private lateinit var sqlDataBase: SQLiteDAO
    private val myAdapter = MyAdapter(listOfFinancialMovement)
    private lateinit var yearMonth: String
    private lateinit var calendar: MaterialCalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setSupportActionBar(findViewById(R.id.toolbar))

        connectOnSqLiteDb()

        // O Método estático getAuthentication() é um singleton da referência do Firebase Auth
        // Captura o UID do usuário logado no momento e o coloca em uma variável que seja visível por toda a classe
        // Essa variável será passada como parâmetro para buscar os dados do usuário correto no banco de dados na função OnStart()
        val uidFromFirebase = FirebaseConfiguration.getAuthentication().currentUser?.uid
        // Lẽ os dados do usuário especificado pelo uid e atribui a instancia da classe User referente na variável "instance" da classe GlobalUserInstance
        val firebaseDao = FirebaseDAO()
        firebaseDao.readUserInformationInDataBase(uidFromFirebase!!)

        //Define o listener listener para a propriedade instance
        GlobalUserInstance.setOnChangeUserInstance(kotlinx.coroutines.Runnable {
            val salutation = "${getString(R.string.salutation)}, ${GlobalUserInstance.instance.name}"
            viewSalutation.text = salutation
        })

        findViewById<TextView>(R.id.textViewBalance).text = getString(R.string.total_balance)
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
            addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext, this, object : OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        println("Clicado na posição: $position")
                    }

                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        if(sqlDataBase.deleteMovement(listOfFinancialMovement[position])) {
                            listOfFinancialMovement.removeAt(position)
                            changeTotalBalance()
                            adapter?.notifyItemRemoved(position)
                        }

                    }
                })
            )

        }
    }

    private fun connectOnSqLiteDb() {
        val userIdByPrefs =  SharedPrefsUserId(this).getUserId()
        println("$userIdByPrefs pra conectar no db FirstActivity")
        sqlDataBase = SQLiteDAO(this, userIdByPrefs!!)
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
        changeTotalBalance()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout_item -> {
                FirebaseConfiguration.getAuthentication().signOut()
                startActivity( Intent(this, LoginActivity::class.java) )
                GlobalUserInstance.instance = User()
                SQLiteDAO.disconnect()
                myAdapter.notifyDataSetChanged()
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
