package com.therxmv.base.ui.context

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.startIntentChooser(
    action: String,
    title: String? = null,
    onError: () -> Unit = { this.onError() },
    intentBuilder: Intent.() -> Intent,
) {
    try {
        val intent = Intent(action).intentBuilder()
        startActivity(Intent.createChooser(intent, title))
    } catch (e: Exception) {
        e.printStackTrace()
        onError()
    }
}

private val onError: Context.() -> Unit = {
    Toast.makeText(this, "Oops", Toast.LENGTH_SHORT).show()
}