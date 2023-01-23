package com.bikeshare.vhome.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.bikeshare.vhome.R
import java.util.Calendar

class MyCalendar: DialogFragment() {
    private val calendar = Calendar.getInstance()

    var onCalendarOkClick: ((year: Int,month:Int,day: Int) -> Unit)? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireActivity(), R.style.DialogTheme, { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            onCalendarOkClick?.invoke(year, month, dayOfMonth)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    fun setDate(year: Int,month: Int,day: Int){
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.DAY_OF_MONTH,day )
    }

    fun getDate(): String{
        return DateFormat.format("dd/MM/yyyy",calendar).toString()
    }
}