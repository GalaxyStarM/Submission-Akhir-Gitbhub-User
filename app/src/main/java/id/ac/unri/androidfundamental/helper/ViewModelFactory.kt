package id.ac.unri.androidfundamental.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ac.unri.androidfundamental.data.database.SettingPreferences
import id.ac.unri.androidfundamental.ui.DetailViewModel
import id.ac.unri.androidfundamental.ui.FavoriteViewModel
import id.ac.unri.androidfundamental.ui.MainViewModel
import id.ac.unri.androidfundamental.ui.SettingThemesViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val pref: SettingPreferences? = null
): ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
        @JvmStatic
        fun getInstanceWithPref(application: Application, pref: SettingPreferences): ViewModelFactory {
            return ViewModelFactory(application, pref)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(SettingThemesViewModel::class.java)){
            if (pref!= null){
                return SettingThemesViewModel(pref) as T
            }else{
                throw IllegalArgumentException("SettingPreferences is required for SettingThemesViewModel.")
            }
        }
        throw IllegalArgumentException("Unknowm ViewModel class: ${modelClass.name}")
    }

    private fun <T : ViewModel> createViewModel(modelClass: Class<T>, application: Application): T {
        return modelClass.getConstructor(Application::class.java).newInstance(application)
    }
}