package de.appsfactory.lecture.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.appsfactory.lecture.network.MetApiService
import de.appsfactory.lecture.network.ObjectDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val apiService: MetApiService
) : ViewModel() {

    private val _results = MutableStateFlow<List<ObjectDetails>>(emptyList())
    val results: StateFlow<List<ObjectDetails>> = _results.asStateFlow()

    private val _lastQuery = MutableStateFlow("")
    val lastQuery: StateFlow<String> = _lastQuery.asStateFlow()

    private val _rawCount = MutableStateFlow(0)
    val rawCount: StateFlow<Int> = _rawCount.asStateFlow()

    private val _apiTotal = MutableStateFlow(0)
    val apiTotal: StateFlow<Int> = _apiTotal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _lastQuery.value = query
            _isLoading.value = true
            try {
                val response = apiService.searchObjects(query)
                _apiTotal.value = response.total

                val ids = response.objectIDs?.take(50)

                if (response.total == 0 || ids.isNullOrEmpty()) {
                    _results.value = emptyList()
                    _rawCount.value = 0
                    return@launch
                }

                val detailsList = ids.mapNotNull { id ->
                    try {
                        apiService.getObjectDetails(id)
                    } catch (e: Exception) {
                        null
                    }
                }

                _rawCount.value = detailsList.size

                val filtered = detailsList.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.objectName.contains(query, ignoreCase = true) ||
                            it.department.contains(query, ignoreCase = true)
                }

                _results.value = filtered

            } catch (e: Exception) {
                _results.value = emptyList()
                _rawCount.value = 0
                _apiTotal.value = 0
            } finally {
                _isLoading.value = false
            }
        }
    }
}
