package com.example.renthouse

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun showKeyboard(editText: EditText) {
    editText.requestFocus()  // Đảm bảo EditText nhận được focus
    val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}
