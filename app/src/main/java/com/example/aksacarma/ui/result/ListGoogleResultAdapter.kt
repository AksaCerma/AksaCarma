package com.example.aksacarma.ui.result

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aksacarma.R
import com.example.aksacarma.data.remote.response.GoogleResultItem
import com.example.aksacarma.databinding.ItemRowGoogleResultBinding

class ListGoogleResultAdapter(private val listHistory: List<GoogleResultItem>): RecyclerView.Adapter<ListGoogleResultAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemRowGoogleResultBinding):RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SuspiciousIndentation")
        fun bind(result: GoogleResultItem) {
            binding.apply {
               textViewTitleResult.text = result.title
                textViewDesc.text = result.description
                Glide.with(itemView.context)
                    .load(result.imageUrl)
                    .error(R.drawable.logo_result)
                    .into(circleImage)
                textViewTitleResult.setOnClickListener {
                    val intent = Intent(textViewTitleResult.context, GoogleResultActivity::class.java)
                      intent.putExtra("url", result.url)
                      textViewTitleResult.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowGoogleResultBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int = listHistory.size
}