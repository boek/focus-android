package org.mozilla.focus.searchsuggestions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.preference.PreferenceManager
import org.mozilla.focus.Components
import org.mozilla.focus.utils.Settings

class SearchSuggestionsPreferences(private val context: Context) {
    private val settings = Settings.getInstance(context)
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val _searchEngineSupportsSuggestions = MutableLiveData<Boolean>()
    val searchEngineSupportsSuggestions: LiveData<Boolean>
        get() = _searchEngineSupportsSuggestions

    private val _userHasRespondedToSearchSuggestionPrompt = MutableLiveData<Boolean>()
    val userHasRespondedToSearchSuggestionPrompt: LiveData<Boolean>
        get() = _userHasRespondedToSearchSuggestionPrompt

    private val _shouldShowSearchSuggestions = MutableLiveData<Boolean>()
    val shouldShowSearchSuggestions: LiveData<Boolean>
        get() = _shouldShowSearchSuggestions

    init {
        _shouldShowSearchSuggestions.value = settings.shouldShowSearchSuggestions()
        _userHasRespondedToSearchSuggestionPrompt.value = false
        _searchEngineSupportsSuggestions.value = currentSearchEngineSupportsSearchSuggestions()
    }

    private fun currentSearchEngineSupportsSearchSuggestions(): Boolean {
        val searchEngine = Components.searchEngineManager.getDefaultSearchEngine(
                context, settings.defaultSearchEngineName)

        return searchEngine.canProvideSearchSuggestions
    }
}
