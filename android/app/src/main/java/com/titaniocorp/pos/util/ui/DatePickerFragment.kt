package com.titaniocorp.pos.util.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(val listener: DatePickerDialog.OnDateSetListener) : DialogFragment(){


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        activity?.let {
            return DatePickerDialog(it, listener, year, month, day)
        } ?: throw NullPointerException("Activity es null")
    }
}