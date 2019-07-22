package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    val r = Rect()
    val rootview = this.window.decorView
    rootview.getWindowVisibleDisplayFrame(r)
    val screenHeight = rootview.height
    val keypadHeight = screenHeight - r.bottom

    return keypadHeight > screenHeight * 0.15
}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardOpen()
}