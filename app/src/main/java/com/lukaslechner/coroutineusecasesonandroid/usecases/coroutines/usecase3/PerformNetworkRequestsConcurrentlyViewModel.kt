package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val versions = mockApi.getRecentAndroidVersions()
                val features = versions.map { mockApi.getAndroidVersionFeatures(it.apiLevel) }
                uiState.value = UiState.Success(features)
            }
        } catch (e: Exception) {
            Timber.e(e)
            uiState.value = UiState.Error("Network request failed")
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val versions = mockApi.getRecentAndroidVersions()
                val features: List<VersionFeatures> = versions
                    .map { async { mockApi.getAndroidVersionFeatures(it.apiLevel) } }
                    .map { it.await() }
                uiState.value = UiState.Success(features)
            }
        } catch (e: Exception) {
            Timber.e(e)
            uiState.value = UiState.Error("Network request failed")
        }
    }
}