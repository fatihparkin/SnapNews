package com.fatihparkin.snapnews.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fatihparkin.snapnews.data.model.Article
import com.fatihparkin.snapnews.databinding.ItemHeadlineBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HeadlineAdapter(
    private var headlines: List<Article>,
    private val onItemClick: (Article) -> Unit // EKLENDİ
) : RecyclerView.Adapter<HeadlineAdapter.HeadlineViewHolder>() {

    class HeadlineViewHolder(val binding: ItemHeadlineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val binding = ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return HeadlineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val article = headlines[position]
        holder.binding.headlineTitle.text = article.title

        if (article.description.isNullOrEmpty()) {
            holder.binding.headlineDescription.visibility = View.GONE
        } else {
            holder.binding.headlineDescription.text = article.description
            holder.binding.headlineDescription.visibility = View.VISIBLE
        }

        holder.binding.headlineDate.text = formatDate(article.publishedAt)

        Glide.with(holder.itemView.context)
            .load(article.imageUrl)
            .into(holder.binding.headlineImage)

        // BURASI TIKLANABİLİR YAPILDI
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }

    override fun getItemCount(): Int = headlines.size

    fun updateHeadlines(newHeadlines: List<Article>) {
        headlines = newHeadlines
        notifyDataSetChanged()
    }

    private fun formatDate(inputDate: String?): String {
        if (inputDate.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            ""
        }
    }
}
