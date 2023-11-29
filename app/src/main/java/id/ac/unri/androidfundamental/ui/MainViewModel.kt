package id.ac.unri.androidfundamental.ui

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.unri.androidfundamental.data.response.GithubResponse
import id.ac.unri.androidfundamental.data.response.ItemsItem
import id.ac.unri.androidfundamental.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: Application): ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchUser = MutableLiveData<List<ItemsItem>>()
    val searchUser: LiveData<List<ItemsItem>> = _searchUser

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        getListUser()
    }

    private fun getListUser(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUserGithub(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchForUser(query)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _searchUser.value = response.body()?.items
                } else{
                    Log.e(ContentValues.TAG, "error onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "error onFailure: ${t.message.toString()}")
            }
        })
    }
}