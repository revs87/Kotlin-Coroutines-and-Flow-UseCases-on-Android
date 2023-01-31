package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val versionsResponse = mockApi.getRecentAndroidVersions()
                val versionNum = versionsResponse.maxOf { it.apiLevel }
                val featuresResponse = mockApi.getAndroidVersionFeatures(versionNum)
                uiState.value = UiState.Success(featuresResponse)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network error.")
            }
        }
    }
}