package id.ac.unri.androidfundamental.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.unri.androidfundamental.data.database.FavoriteUser
import id.ac.unri.androidfundamental.repository.FavoriteUserRepository


class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "FavoriteViewModel"
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> {
        return mFavoriteUserRepository.getAllFavoriteUser()
    }
}