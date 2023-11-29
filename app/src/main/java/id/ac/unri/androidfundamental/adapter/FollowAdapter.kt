package id.ac.unri.androidfundamental.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.unri.androidfundamental.data.response.ItemsItem
import id.ac.unri.androidfundamental.databinding.SingleItemViewBinding
import id.ac.unri.androidfundamental.ui.FollowersFragment

class FollowAdapter(private val clickListener: FollowersFragment): ListAdapter<ItemsItem, FollowAdapter.MyViewHolder>(DIFF_CALL) {

    inner class MyViewHolder(val binding: SingleItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.civPicture)
            binding.tvUser.text = user.login
            itemView.setOnClickListener {
                clickListener.onItemClick(user.login)
            }
        }
    }

    companion object{
        val DIFF_CALL = object : DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SingleItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    interface ItemClickListener{
        fun onItemClick(username: String)
    }
}
