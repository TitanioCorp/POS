package com.titaniocorp.pos.util.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Maneja los diferentes Snackbar
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class SnackbarUtil {
    companion object{
        @JvmStatic
        fun default(view: View, message: String){
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            /*val snackbarView = snackbar.view
            snackbarView.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.LTGRAY)
            snackbarView.setBackgroundColor(Color.WHITE)*/

            snackbar.show()
        }

        @JvmStatic
        fun action(view: View, message: String, action: String, listener: (View) -> Unit){
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(action,  listener)
            snackbar.show()
        }
    }
}