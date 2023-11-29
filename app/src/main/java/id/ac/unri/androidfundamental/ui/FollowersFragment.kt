package id.ac.unri.androidfundamental.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.unri.androidfundamental.adapter.FollowAdapter
import id.ac.unri.androidfundamental.data.response.ItemsItem
import id.ac.unri.androidfundamental.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment(), FollowAdapter.ItemClickListener {

    private var binding: FragmentFollowersBinding? = null
    private lateinit var followViewModel: FollowViewModel
    private lateinit var adapter: FollowAdapter

    companion object{
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
        const val TAG = "FollowersFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater,container, false)
        binding?.rvFollowers?.layoutManager = LinearLayoutManager(requireActivity())

        followViewModel = ViewModelProvider(this).get(FollowViewModel::class.java)

        adapter = FollowAdapter(this)
        binding?.rvFollowers?.adapter = adapter
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME).toString()

            if(position == 1){
                followViewModel.getGithubFollowers(username)
                followViewModel.followers.observe(viewLifecycleOwner){ list->
                    getListFollower(list)
                }
                followViewModel.isLoading.observe(viewLifecycleOwner){
                    showLoading(it)
                }
            }else if (position ==2){
                followViewModel.getGithubFollowings(username)
                followViewModel.followings.observe(viewLifecycleOwner){list->
                    getListFollowing(list)
                }
                followViewModel.isLoading.observe(viewLifecycleOwner){
                    showLoading(it)
                }
            }else{
                Log.e(TAG, "error onResponse: ")
            }
        }
    }

    private fun getListFollowing(githubUser: List<ItemsItem>) {
        adapter.submitList(githubUser)
    }


    private fun getListFollower(githubUser: List<ItemsItem>) {
        adapter.submitList(githubUser)
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onItemClick(username: String) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

}