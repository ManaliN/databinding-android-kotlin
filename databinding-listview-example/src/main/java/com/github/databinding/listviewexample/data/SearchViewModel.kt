package com.github.databinding.listviewexample.data

import android.text.Editable
import android.text.TextWatcher

/**
 * UiModel for Github Search
 * @author: manali
 */

data class SearchViewModel (
        val searchLabel: String? = null,
        val searchText: String? = null,
        val textWatcher: TextWatcher? = null,
        val searchButton: UiButtonModel? = null
)

class TextWatcherWrapper(
        val onTextChanged: (String) -> Unit
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let { value ->
            onTextChanged(value.toString())
        }
    }
}

data class UiButtonModel(
        val label: String,
        private val click: () -> Unit = {}
) {
    fun doClick() {
        click()
    }
}


