package com.example.organizze.activity


import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.organizze.R
import com.example.organizze.config.FirebaseConfiguration
import com.example.organizze.dao.SQLiteDAO
import com.example.organizze.database.DataBase
import com.example.organizze.model.FinancialMovement
import com.example.organizze.others.MyDateDialog
import com.example.organizze.others.getLocale
import com.example.organizze.others.showSnackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat

class IncomeExpensesActivity : AppCompatActivity() {

    private lateinit var secondConstraintLayout: ConstraintLayout
    private lateinit var viewTextValue: TextView
    private lateinit var checkBoxExpense: CheckBox
    private lateinit var checkBoxIncome: CheckBox
    private lateinit var viewValue: EditText
    private lateinit var viewDescription: EditText
    private lateinit var viewCategory: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var financialMovement: FinancialMovement
    private lateinit var sqLiteDataBase: SQLiteDAO
    private lateinit var dateDialog: MyDateDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_expenses)

        secondConstraintLayout = findViewById(R.id.secondConstraintLayout)
        viewTextValue = findViewById(R.id.textViewShowValue)
        checkBoxExpense = findViewById(R.id.checkBoxExpense)
        checkBoxIncome = findViewById(R.id.checkBoxIncome)
        viewValue = findViewById(R.id.editTextValue)
        viewCategory = findViewById(R.id.editTextCategory)
        viewDescription = findViewById(R.id.editTextDescription)
        fab = findViewById(R.id.FAB_Ok)
        financialMovement = FinancialMovement()
        financialMovement.userId = FirebaseConfiguration.getAuthentication().currentUser!!.uid
        sqLiteDataBase = SQLiteDAO(this)
        dateDialog = MyDateDialog(this, R.id.editTextDate)


        initViewValueFeatures(viewValue, viewTextValue)

        // Defines this financial movement as a expense
        // Define essa movimentação financeira como sendo uma despesa
        checkBoxExpense.setOnClickListener {
            financialMovement.incomeOrExpense = "E"
            checkBoxIncome.isChecked = false
            checkBoxIncome.isClickable = true
            checkBoxExpense.isClickable = false

            // Applies the Expenses theme to this Activity
            // Aplica o tema de Despesas na Activity
            fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.expenseAccent))
            secondConstraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.expensePrimaryDark))
            window.statusBarColor = ContextCompat.getColor(this, R.color.expensePrimaryDark)
        }


        // Defines this financial movement as a income
        // Define essa movimentação financeira como sendo uma receita
        checkBoxIncome.setOnClickListener {
            financialMovement.incomeOrExpense = "I"
            checkBoxExpense.isChecked = false
            checkBoxIncome.isClickable = false
            checkBoxExpense.isClickable = true


            // Applies the Incomes theme to this Activity
            // Aplica o tema de receitas na Activity
            fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.incomeAccent))
            secondConstraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.incomePrimaryDark))
            window.statusBarColor = ContextCompat.getColor(this, R.color.incomePrimaryDark)
        }


        // Verifica se os campos obrigatórios foram preenchidos e salva no banco de dados
        // Checks whether mandatory fields have been completed and saves on database
        fab.setOnClickListener {
            if(!checkBoxExpense.isChecked && !checkBoxIncome.isChecked) {
                showSnackbar(fab, getString(R.string.select_financial_movement_type))

            } else if(financialMovement.value == 0.0){
                showSnackbar(fab, getString(R.string.type_value))

            } else if(this.findViewById<EditText>(R.id.editTextDate).text.toString().isBlank()) {
                showSnackbar(fab, getString(R.string.type_date))

            } else {
                financialMovement.category = viewCategory.text.toString()
                financialMovement.description = viewDescription.text.toString()
                financialMovement.calendar = dateDialog.calendar
                // Save on SQLite database
                sqLiteDataBase.saveMovement(financialMovement)
                // Save on Firebase database
                DataBase.saveInDataBase(financialMovement)
                DataBase.updateUserInformation(FirebaseConfiguration.getAuthentication().currentUser!!.uid, financialMovement)
                finish()
            }
        }
    }

    /**
     * Resgata e formata os valores iniciais de um editText de valor
     * Inicia um listener para quando o foco na View for perdida executa a formatação e
     * a exibição do valor em uma TextView que também será passada por parâmetro
     * @param myViewValue EditText que no qual o valor será editado
     * @param viewToShowValue TextView onde o valor formatado será mostrado
     */
    private fun initViewValueFeatures(myViewValue: EditText, viewToShowValue: TextView) {
        val valueReceiver  = NumberFormat.getInstance(getLocale(resources))

        showValue(viewToShowValue, 0.00)
        myViewValue.setOnFocusChangeListener { _, b ->
            if (b) {
                val text = myViewValue.text.toString()
                if(text.isNotEmpty()) {
                    val textWithoutPrefix = text.replace(".", "")
                    val number = valueReceiver.parse(textWithoutPrefix) as Double
                    showValue(viewToShowValue, number)
                }

            } else {
                val value = myViewValue.text.toString()
                if(value.isNotEmpty()) {
                    val number = value.toDouble()
                    financialMovement.value = number
                    showValue(viewToShowValue, number)
                }
                myViewValue.setText("")
            }
        }
    }


    /**
     * Formata um valor de entrada no formato da moeda local
     * @param myViewValue a view que mostrará o valor
     * @param value o valor que será formatado e exbido
     */
    private fun showValue(myViewValue: TextView, value: Double) {
        val currentLocale = getLocale(resources)
        val toFormatValue = NumberFormat.getCurrencyInstance(currentLocale)
        myViewValue.text  = toFormatValue.format(value)
    }


}