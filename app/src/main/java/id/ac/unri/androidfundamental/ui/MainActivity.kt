package id.ac.unri.androidfundamental.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.unri.androidfundamental.R
import id.ac.unri.androidfundamental.adapter.FollowAdapter
import id.ac.unri.androidfundamental.adapter.UserAdapter
import id.ac.unri.androidfundamental.data.database.SettingPreferences
import id.ac.unri.androidfundamental.data.response.ItemsItem
import id.ac.unri.androidfundamental.databinding.ActivityMainBinding
import id.ac.unri.androidfundamental.helper.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var settingThemesViewModel: SettingThemesViewModel? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val viewModelFactory = ViewModelFactory.getInstance(application)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val pref = SettingPreferences.getInstance(application.dataStore)
        val viewModelFactoryWithPref = ViewModelFactory.getInstanceWithPref(application, pref)
        settingThemesViewModel = ViewModelProvider(this, viewModelFactoryWithPref).get(SettingThemesViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.layoutManager = layoutManager
        binding?.rvUser?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvUser?.addItemDecoration(itemDecoration)

        val adapter = UserAdapter{ user ->
            showDetailActivity(user.login, user.avatarUrl)
        }

        mainViewModel?.listUser?.observe(this){item ->
            item?.let {
                setUserData(it, adapter)
            }
        }

        mainViewModel?.searchUser?.observe(this){item ->
            item?.let {
                setUserData(it, adapter)
            }
        }

        mainViewModel?.isLoading?.observe(this){
            showLoading(it)
        }

        settingThemesViewModel?.getThemeSettings()?.observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding?.searchBar?.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId) {
                R.id.fav_button -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.light_night_btn -> {
                    val isDarkModeActive = settingThemesViewModel?.getThemeSettings()?.value ?: false
                    settingThemesViewModel?.saveThemeSetting(!isDarkModeActive)
                    true
                }
                else -> false
            }
        }

        with(binding) {
            this?.searchView?.setupWithSearchBar(searchBar)
            this?.searchView?.editText?.setOnEditorActionListener { textView, actionId, event ->
                this.searchBar.text = searchView.text
                this.searchView.hide()
                mainViewModel?.searchUserGithub(searchView.text.toString())
                false
            }

        }
        binding?.rvUser?.adapter = adapter
    }

    private fun setUserData(users: List<ItemsItem>, adapter: UserAdapter) {
        adapter.submitList(users.toMutableList())
        binding?.rvUser?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDetailActivity(username: String, avatarUrl: String){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("avatarUrl", avatarUrl ?: "")
        startActivity(intent)
    }
}