package id.ac.unri.androidfundamental.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.unri.androidfundamental.R
import id.ac.unri.androidfundamental.adapter.TabAdapter
import id.ac.unri.androidfundamental.data.database.FavoriteUser
import id.ac.unri.androidfundamental.data.response.DetailUserResponse
import id.ac.unri.androidfundamental.databinding.ActivityDetailBinding
import id.ac.unri.androidfundamental.helper.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private var binding: ActivityDetailBinding? = null
    private var detailViewModel: DetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        detailViewModel = obtainViewModel(this@DetailActivity)

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val username = intent.getStringExtra("username")
        val avatarUrl = intent.getStringExtra("avatarUrl")

        if (username != null) {
            detailViewModel?.getDetailAcc(username)
        }

        detailViewModel?.listUser?.observe(this){user ->
            getListUser(user)
        }

        detailViewModel?.isLoading?.observe(this){
            showLoading(it)
        }

        detailViewModel?.checkFavoriteUserLD(username.toString())?.observe(this){ favoriteUser ->
            if (favoriteUser != null){
                binding?.fabAdd?.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.favfull))
            }else{
                binding?.fabAdd?.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.favborder))
            }
        }

        val tabAdapter = TabAdapter(this)
        tabAdapter.username = username.toString()
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = tabAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.tab_follower)
                1 -> tab.text = resources.getString(R.string.tab_following)
                else -> tab.text = ""
            }
        }.attach()

        binding?.fabAdd?.setOnClickListener{
            if (detailViewModel?.isFavorite == false){
                detailViewModel?.addFavorite(username.toString(), avatarUrl.toString())
                detailViewModel?.isFavorite = true
                showToast(getString(R.string.addFav))
            }else{
                detailViewModel?.deleteFav(username.toString(), avatarUrl.toString())
                showToast(getString(R.string.delFav))
                detailViewModel?.isFavorite = false
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun getListUser(detailUsn: DetailUserResponse?) {
        if (detailUsn != null){
            binding?.namaGh?.text = detailUsn.name
            binding?.usernameGh?.text = detailUsn.login
            binding?.dFollowers?.text = resources.getString(R.string.rfollower, detailUsn.followers)
            binding?.dFollowing?.text = resources.getString(R.string.rfollowing, detailUsn.following)
            binding?.let {
                Glide.with(this@DetailActivity)
                    .load(detailUsn.avatarUrl)
                    .into(it.civImgDetail)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}