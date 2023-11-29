package id.ac.unri.androidfundamental.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.unri.androidfundamental.data.database.FavoriteUser
import id.ac.unri.androidfundamental.data.response.DetailUserResponse
import id.ac.unri.androidfundamental.data.retrofit.ApiConfig
import id.ac.unri.androidfundamental.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _listUser = MutableLiveData<DetailUserResponse>()
    val listUser: LiveData<DetailUserResponse> = _listUser

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    private val _isFavorite = MutableLiveData<Boolean>()
//    val isFavorite: LiveData<Boolean> = _isFavorite

    var isFavorite: Boolean = false

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun getDetailAcc(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object: Callback<DetailUserResponse>{
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listUser.value = response.body()
                } else{
                    Log.e(TAG, "error onResponse detail: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addFavorite(username: String, avatarUrl: String){
        isFavorite = false
        val user = FavoriteUser(username, avatarUrl)
        mFavoriteUserRepository.insert(user)
    }

    fun deleteFav(username: String, avatarUrl: String){
        val user = FavoriteUser(username, avatarUrl)
        isFavorite = true
        mFavoriteUserRepository.delete(user)
    }

    fun checkFavoriteUserLD(username: String) : LiveData<FavoriteUser>{
        return mFavoriteUserRepository.getFavoriteUserByUsername(username)
    }

}