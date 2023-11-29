package id.ac.unri.androidfundamental.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.unri.androidfundamental.adapter.UserAdapter
import id.ac.unri.androidfundamental.data.response.ItemsItem
import id.ac.unri.androidfundamental.databinding.ActivityFavoriteBinding
import id.ac.unri.androidfundamental.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var binding: ActivityFavoriteBinding? = null
    private var adapter : UserAdapter? = null
    private var favoriteViewModel: FavoriteViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        adapter = UserAdapter{
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            intent.putExtra("username", it.login)
            intent.putExtra("avatarUrl", it.avatarUrl)
            startActivity(intent)
        }

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.layoutManager = layoutManager
        binding?.rvFavorite?.setHasFixedSize(true)
        binding?.rvFavorite?.adapter = adapter

        favoriteViewModel?.getAllFavoriteUser()?.observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            adapter?.submitList(items)
        }

        favoriteViewModel?.isLoading?.observe(this){
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

}