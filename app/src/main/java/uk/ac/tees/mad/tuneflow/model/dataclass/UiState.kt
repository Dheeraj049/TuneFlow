package uk.ac.tees.mad.tuneflow.model.dataclass

sealed class UiState {
    object Loading : UiState()
    data class Success(val response: ApiPlaylistResponse) : UiState()
    data class Error(val errorState: ErrorState, val message: String) : UiState()
}