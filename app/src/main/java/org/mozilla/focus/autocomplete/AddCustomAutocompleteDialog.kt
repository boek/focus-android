package org.mozilla.focus.autocomplete

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import org.mozilla.focus.R

object AddCustomAutocompleteDialog {
    @JvmStatic
    fun show(context: Context, url: String) {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        Log.d("Hello world", "Url=$url")
        val view = inflater.inflate(R.layout.add_custom_autiocomplete_dialog, null)

        builder.setView(view)
        val dialog = builder.create()
        dialog.window.attributes.gravity = Gravity.TOP
        dialog.show()
    }
}