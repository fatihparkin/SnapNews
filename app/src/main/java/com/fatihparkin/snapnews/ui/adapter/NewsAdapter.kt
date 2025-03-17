package com.fatihparkin.snapnews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fatihparkin.snapnews.data.model.Article
import com.fatihparkin.snapnews.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.binding.newsTitle.text = article.title
        holder.binding.newsSource.text = article.source.name

        val formattedDate = formatDate(article.publishedAt)
        holder.binding.newsDate.text = formattedDate

        Glide.with(holder.itemView.context)
            .load(article.imageUrl)
            .into(holder.binding.newsImage)

        // BURASI: Tıklama işlemi
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }

    override fun getItemCount(): Int = articles.size

    private fun formatDate(dateString: String?): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

            val date = inputFormat.parse(dateString ?: "")
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            "Bilinmeyen Tarih"
        }
    }
}
