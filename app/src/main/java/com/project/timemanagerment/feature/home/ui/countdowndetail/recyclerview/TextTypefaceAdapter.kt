package com.project.timemanagerment.feature.home.ui.countdowndetail.recyclerview

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.timemanagerment.base.util.TextTypeface
import com.project.timemanagerment.databinding.ListTextTypefaceBinding


class TextTypefaceAdapter :
    ListAdapter<TextTypeface, RecyclerView.ViewHolder>(TextTypefaceDiffCallback()) {
    var currentSelectTextIndex = 0
    lateinit var itemTypefaceClickCallback: (Int) -> Unit
    fun setCurrentSelectTextIndexAndUpdate(int: Int) {
        currentSelectTextIndex = int
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ListTextTypefaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TextTypefaceViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val typeface = getItem(position)
        (holder as TextTypefaceViewHolder).bind(
            typeface,
            currentSelectTextIndex,
            itemTypefaceClickCallback,
            this
        )
    }

    class TextTypefaceViewHolder(private val binding: ListTextTypefaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            typeface: TextTypeface?,
            currentSelectTextIndex: Int,
            itemTypefaceClickCallback: (Int) -> Unit,
            textTypefaceAdapter: TextTypefaceAdapter
        ) {

            typeface?.let {
                binding.tvDate.isSelected = currentSelectTextIndex == it.typefaceIndex
                binding.clParent.setOnClickListener {
                    itemTypefaceClickCallback(typeface.typefaceIndex)
                    textTypefaceAdapter.currentSelectTextIndex = typeface.typefaceIndex
                    textTypefaceAdapter.notifyDataSetChanged()
                }
                val face: Typeface = binding.root.context.resources.getFont(it.fontId)
                binding.tvDate.typeface = face
            }
            binding.apply {
                //executePendingBindings()
                executePendingBindings()
            }
        }
    }

    class TextTypefaceDiffCallback : DiffUtil.ItemCallback<TextTypeface>() {
        override fun areItemsTheSame(oldItem: TextTypeface, newItem: TextTypeface): Boolean {
            return oldItem.typefaceIndex == newItem.typefaceIndex
        }

        override fun areContentsTheSame(oldItem: TextTypeface, newItem: TextTypeface): Boolean {
            return oldItem == newItem
        }

    }
}