package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var versionsCall: Call<List<AndroidVersion>>? = null
    private var featuresCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        versionsCall = mockApi.getRecentAndroidVersions()
        versionsCall?.enqueue(object : Callback<List<AndroidVersion>> {
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if (response.isSuccessful) {
                    val versionsResponse = response.body()
                    versionsResponse.orEmpty()
                        .ifEmpty { uiState.value = UiState.Error("Versions failure"); return }
                    val versionsNum = versionsResponse.orEmpty().maxOf { it.apiLevel }

                    featuresCall = mockApi.getAndroidVersionFeatures(versionsNum)
                    featuresCall?.enqueue(object : Callback<VersionFeatures> {
                        override fun onResponse(
                            call: Call<VersionFeatures>,
                            response: Response<VersionFeatures>
                        ) {
                            if (response.isSuccessful) {
                                val featuresResponse = response.body()
                                featuresResponse ?: run { uiState.value = UiState.Error("Features failure") }
                                featuresResponse?.let { uiState.value = UiState.Success(featuresResponse) }
                            } else {
                                uiState.value = UiState.Error("Features network failure")
                            }
                        }

                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error("Features failure")
                        }
                    })
                } else {
                    uiState.value = UiState.Error("Versions network failure")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("Versions failure")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        versionsCall?.cancel()
        featuresCall?.cancel()
    }
}