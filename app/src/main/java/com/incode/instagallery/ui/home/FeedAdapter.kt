package com.incode.instagallery.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.incode.instagallery.databinding.ItemFeedBinding
import com.incode.instagallery.domain.data.Feed

class FeedAdapter : ListAdapter<Feed, FeedAdapter.FeedViewHolder>(FeedDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClicked(feed: Feed)
    }

    var onItemClickListener: OnItemClickListener? = null

    inner class FeedViewHolder(val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.textViewTitle.text = item.title
        holder.binding.textViewDescription.text = item.comment

        holder.binding.textViewTitle.visibility =
            if (item.title.isNullOrEmpty()) View.GONE else View.VISIBLE
        holder.binding.textViewDescription.visibility =
            if (item.comment.isNullOrEmpty()) View.GONE else View.VISIBLE

        val uriToLoad = if (item.isFromNetwork) {
            Uri.parse(item.pictureUri)
        } else {
            Uri.parse(item.pictureUri).path
        }

        Glide.with(holder.itemView.context)
            .load(uriToLoad)
            .centerCrop()
            .apply(RequestOptions.signatureOf(ObjectKey(item.id)))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
            .into(holder.binding.imageViewPhoto)


        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(getItem(holder.adapterPosition))
        }
    }

}

class FeedDiffCallback : DiffUtil.ItemCallback<Feed>() {
    override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem == newItem
    }
}
