package com.example.aksacarma.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aksacarma.R
import com.example.aksacarma.databinding.ItemRowArticleBinding
import com.example.aksacarma.model.Article

class ArticleListAdapter(private val listHistory: List<Article>):RecyclerView.Adapter<ArticleListAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemRowArticleBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                textViewTitleResult.text = article.title
                textViewDesc.text = article.description
                Glide.with(itemView.context)
                    .load(article.photoUrl)
                    .error(R.drawable.logo_result)
                    .into(circleImage)
                textViewTitleResult.setOnClickListener {
                    val intent = Intent(textViewTitleResult.context, ArticleResultActivity::class.java)
                    intent.putExtra("url", article.url)
                    textViewTitleResult.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowArticleBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int = listHistory.size
}