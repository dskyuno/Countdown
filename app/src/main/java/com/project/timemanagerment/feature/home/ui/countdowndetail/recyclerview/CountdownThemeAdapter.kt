package com.project.timemanagerment.feature.home.ui.countdowndetail.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.timemanagerment.base.util.CountdownTheme
import com.project.timemanagerment.databinding.ListCountdownThemeBinding

class CountdownThemeAdapter :
    ListAdapter<CountdownTheme, RecyclerView.ViewHolder>(CountdownThemeDiffCallback()) {
    var currentSelectThemeIndex = 0
    lateinit var itemThemeClickCallBack: (Int) -> Unit

    fun setCurrentSelectThemeIndexAndUpdate(int: Int) {
        currentSelectThemeIndex = int
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ListCountdownThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountdownThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        (holder as CountdownThemeViewHolder).bind(
            theme,
            currentSelectThemeIndex,
            itemThemeClickCallBack, this
        )
    }

    class CountdownThemeViewHolder(val binding: ListCountdownThemeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            theme: CountdownTheme,
            currentSelectThemeIndex: Int,
            itemThemeClickCallBack: (Int) -> Unit,
            countdownThemeAdapter: CountdownThemeAdapter
        ) {
            binding.ivCheckItem.visibility =
                if (currentSelectThemeIndex == theme.themeIndex) View.VISIBLE else View.GONE
            binding.tvTitle.setBackgroundResource(theme.topColor)
            binding.tvDate.setBackgroundResource(theme.centerColor)
            binding.tvTargetTime.setBackgroundResource(theme.bottomColor)
            binding.clCountdown.setOnClickListener {
                itemThemeClickCallBack(theme.themeIndex)
                countdownThemeAdapter.currentSelectThemeIndex = theme.themeIndex
                countdownThemeAdapter.notifyDataSetChanged()
            }
            binding.tvIntroduction.text = theme.introduction
        }

    }

    class CountdownThemeDiffCallback : DiffUtil.ItemCallback<CountdownTheme>() {
        override fun areItemsTheSame(oldItem: CountdownTheme, newItem: CountdownTheme): Boolean {
            return oldItem.themeIndex == newItem.themeIndex
        }

        override fun areContentsTheSame(oldItem: CountdownTheme, newItem: CountdownTheme): Boolean {
            return oldItem == newItem
        }

    }

}