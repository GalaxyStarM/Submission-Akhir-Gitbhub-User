package id.ac.unri.androidfundamental.repository

import android.app.Application
import androidx.lifecycle.LiveData
import id.ac.unri.androidfundamental.data.database.FavoriteDao
import id.ac.unri.androidfundamental.data.database.FavoriteRoomDatabase
import id.ac.unri.androidfundamental.data.database.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {

    private val mFavoriteDao : FavoriteDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favoriteUser) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mFavoriteDao.getFavoriteUserByUsername(username)
    }
}