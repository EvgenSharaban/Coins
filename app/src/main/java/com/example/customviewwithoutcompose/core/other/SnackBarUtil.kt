package com.example.customviewwithoutcompose.core.other

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(view: View, message: String) {
    val snackBar = Snackbar.make(view.context, view, message, Snackbar.LENGTH_INDEFINITE)
    snackBar.setTextMaxLines(5)
    snackBar.setAction("Ok") {
        snackBar.dismiss()
    }
    snackBar.show()
}