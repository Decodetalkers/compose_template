package com.stein.mahoyinkuima.nhk

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stein.mahoyinkuima.common.Resource
import java.io.IOException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response

private val client = OkHttpClient()

class NhkHtmlModel : ViewModel() {
    val htmlData = mutableStateOf<String>(EMPTY_HTML)

    fun setData(data: String) {
        htmlData.value = data
    }
}

class NhKViewModel : ViewModel() {
    val state = mutableStateOf<Resource<List<NhkNews>>>(Resource.Begin)

    fun clearStatue() {
        state.value = Resource.Begin
    }

    fun syncNews() {
        val thestate by state
        if (thestate is Resource.Loading) return
        if (thestate is Resource.Success) return
        viewModelScope.launch { syncNewsInner().collect { response -> state.value = response } }
    }

    private fun syncNewsInner() = flow {
        emit(Resource.Loading)
        val request = requestTodayNews()
        client.newCall(request)
                .enqueue(
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                state.value = Resource.Failure("Unexpected code $e")
                            }

                            override fun onResponse(call: Call, response: Response) {
                                if (response.isSuccessful) {
                                    val bodys = response.body!!.string()

                                    val objs = Json.decodeFromString<List<NhkNews>>(bodys)
                                    state.value = Resource.Success(objs)
                                }
                            }
                        }
                )
    }
}
