package id.ac.unri.androidfundamental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.unri.androidfundamental.data.response.ItemsItem
import id.ac.unri.androidfundamental.databinding.SingleItemViewBinding

class UserAdapter(private val itemClickListener: (ItemsItem) -> Unit): ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SingleItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
        holder.itemView.setOnClickListener {
            itemClickListener(list)
        }
    }

    class MyViewHolder(val binding: SingleItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(listUser : ItemsItem) {
            with(binding) {
                Glide.with(itemView.context).load(listUser.avatarUrl)
                    .into(civPicture)
                tvUser.text= listUser.login
            }
        }
    }

    companion object {
        val DIFF_CALL = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}