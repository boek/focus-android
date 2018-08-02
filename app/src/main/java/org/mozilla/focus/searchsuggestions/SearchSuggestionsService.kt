package org.mozilla.focus.searchsuggestions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import mozilla.components.browser.search.suggestions.SearchSuggestionClient
import org.mozilla.focus.Components
import org.mozilla.focus.utils.Settings
import kotlinx.coroutines.experimental.launch
import mozilla.components.browser.search.SearchEngine
import okhttp3.OkHttpClient
import okhttp3.Request

class SearchSuggestionsService(searchEngine: SearchEngine) {
    private var client: SearchSuggestionClient = SearchSuggestionClient(searchEngine, { fetch(it) })
    private var httpClient = OkHttpClient()

    private val _canProvideSearchSuggestions = MutableLiveData<Boolean>()
    val canProvideSearchSuggestions: LiveData<Boolean>
        get() = _canProvideSearchSuggestions

    private fun fetch(url: String): String? {
        httpClient.dispatcher().queuedCalls()
                .filter { it.request().tag() == REQUEST_TAG }
                .forEach { it.cancel() }

        val request = Request.Builder()
                .tag(REQUEST_TAG)
                .url(url)
                .build()

        return httpClient.newCall(request).execute().body()?.string() ?: ""
    }

    fun getSuggestions(query: String): LiveData<Pair<String, List<String>>> {
        val result = MutableLiveData<Pair<String, List<String>>>()

        if (query.isBlank()) { result.value = Pair(query, listOf()) }

        launch(CommonPool) {
            val suggestions = client.getSuggestions(query) ?: listOf()

            launch(UI) {
                result.value = Pair(query, suggestions)
            }
        }

        return result
    }

    companion object {
        private const val REQUEST_TAG = "searchSuggestionFetch"
    }
}