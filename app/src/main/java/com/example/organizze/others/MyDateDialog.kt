package com.example.organizze.others

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import com.example.organizze.R
import kotlinx.android.synthetic.main.content_income_expenses.*
import java.util.*
import java.util.Calendar.*


class MyDateDialog(private val context: Context, editText: Int) : View.OnClickListener, DatePickerDialog.OnDateSetListener {

    var calendar: Calendar = getInstance(Locale.getDefault())
        private set
    private var myDay = calendar[DAY_OF_MONTH]
    private var myMonth = calendar[MONTH]
    private var myYear = calendar[YEAR]
    private val activity = context as Activity
    private val realEditText = activity.findViewById<EditText>(editText)
    private var dialog: DatePickerDialog

    init {
        dialog = DatePickerDialog(context,this , myYear, myMonth, myDay)
        realEditText.setOnClickListener(this)
        realEditText.showSoftInputOnFocus = false
        realEditText.isCursorVisible = false
        realEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                choseDialogTheme()
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(realEditText.windowToken, 0)
                dialog.show()
            }

        }
    }

    private fun choseDialogTheme() {
        dialog = when {
            activity.checkBoxIncome.isChecked -> {
                DatePickerDialog(context, R.style.DateDialogIncome, this, myYear, myMonth, myDay)
            }
            activity.checkBoxExpense.isChecked -> {
                DatePickerDialog(context, R.style.DateDialogExpense, this, myYear, myMonth, myDay)
            }
            else -> {
                DatePickerDialog(context, R.style.DateDialogNothing, this, myYear, myMonth, myDay)
            }
        }
    }


    override fun onClick(v: View) {
        choseDialogTheme()
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        myDay = day
        calendar[DAY_OF_MONTH] = myDay

        myMonth = month+1
        calendar[MONTH] = myMonth

        myYear = year
        calendar[YEAR] = myYear
        updateViewText()
    }

    private fun updateViewText() {
        val dateString = "${calendar[DAY_OF_MONTH]}/${calendar[MONTH]}/${calendar[YEAR]}"
        realEditText.setText(dateString)
    }
}




/*public class MyDateDialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText _editText;
    private int _day;
    private int _month;
    private int _birthYear;
    private Context _context;

    public MyDateDialog(Context context, int editTextViewID)
    {
        Activity act = (Activity)context;
        this._editText = (EditText)act.findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        _editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
    }
}*/