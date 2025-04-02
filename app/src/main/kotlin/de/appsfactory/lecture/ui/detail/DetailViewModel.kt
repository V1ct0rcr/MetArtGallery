package de.appsfactory.lecture.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.appsfactory.lecture.network.MetApiService
import de.appsfactory.lecture.network.ObjectDetails
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val apiService: MetApiService
) : ViewModel() {

    private val _objectDetails = MutableStateFlow<ObjectDetails?>(null)
    val objectDetails: StateFlow<ObjectDetails?> = _objectDetails.asStateFlow()

    private val _favorites = MutableStateFlow<List<ObjectDetails>>(emptyList())
    val favorites: StateFlow<List<ObjectDetails>> = _favorites.asStateFlow()

    fun loadObjectDetails(objectId: Int) {
        viewModelScope.launch {
            try {
                val details = apiService.getObjectDetails(objectId)
                _objectDetails.value = details
            } catch (e: Exception) {
                println("❌ Error loading object: ${e.message}")
            }
        }
    }

    fun toggleFavorite(item: ObjectDetails) {
        _favorites.value = if (_favorites.value.contains(item)) {
            _favorites.value - item
        } else {
            _favorites.value + item
        }
    }

    fun isFavorite(item: ObjectDetails): Boolean {
        return _favorites.value.contains(item)
    }
}
